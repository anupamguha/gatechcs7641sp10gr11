package simulator.game;

import java.util.ArrayList;
import java.util.Collections;

import simulator.deck.Card;
import simulator.game.Action.ACTION;
import simulator.stats.State;
import simulator.stats.Stats;
import simulator.stats.Tracker;

/**
 * @author christina
 *
 */
public class Game {

  /**
   * The list of people playing the game
   */
  private ArrayList<Player> players;
  
  /**
   * The list of people who have not folded
   */
  private ArrayList<Player> activePlayers;
  
  /**
   *  The currenty community 
   */
  private Community community;
  
  /**
   * The list of all cards that will eventually make up the community 
   */
  private Card[] cards;
 
  /**
   * Size of pot at flop 
   */
  private int flopPot;
  
  /**
   * Size of pot at turn 
   */
  private int turnPot;
  
  /**
   * Size of pot at river 
   */
  private int riverPot;
  
  /**
   * Size of pot at showdown 
   */
  private int showdownPot;
  
  /**
   * The timestamp identifying the game
   */
  private int timestamp;
  
  /**
   * No-arg constructor
   */
  public Game() {
    players = new ArrayList<Player>();
    activePlayers = new ArrayList<Player>();
    community = new Community();
  }
  
  /**
   * Adds a player to the game
   * 
   * @param p
   *   the player to add
   */
  public void addPlayer(Player p) {
    players.add(p);
    activePlayers.add(p);
  }
  
  /**
   * Sets the array of cards that will become the community over the 
   * course of the game
   * 
   * @param cards
   *   the array of cards
   */
  public void setCards(Card[] cards) {
    this.cards = cards;
  }
  
  /**
   * Sets pot at flop
   * 
   * @param flopPot
   *   size of pot at flop
   */
  public void setFlopPot(int flopPot) {
    this.flopPot = flopPot;
  }

  /**
   * Sets pot at turn
   * 
   * @param turnPot
   *   size of pot at turn
   */
  public void setTurnPot(int turnPot) {
    this.turnPot = turnPot;
  }

  /**
   * Sets pot at river
   * 
   * @param riverPot
   *   size of pot at river
   */
  public void setRiverPot(int riverPot) {
    this.riverPot = riverPot;
  }

  /**
   * Sets pot at showdown
   * 
   * @param showdownPot
   *   size of pot at showdown
   */
  public void setShowdownPot(int showdownPot) {
    this.showdownPot = showdownPot;
  }
  
  /**
   * Sets the game timestamp
   * 
   * @param ts
   *   the game timestamp
   */
  public void setTimestamp(int ts) {
    timestamp = ts;
  }

  /**
   * Plays the game 
   */
  public void play() {
    if (players.size() == 0) {
      System.out.println("No players playing the game!");
      return;
    }
    
    Collections.sort(players);
   // if (cards == null) {
   //   System.out.println("Please set the cards for use in the community");
   //   return;
   // }
    
    System.out.println("***********************\n");
    System.out.println("Game " + timestamp);
    
    boolean done = false;
    Action a = null;
    int potIncrease = 0;
    int numPlayers = 0;
    int bet = 0;
    int table = 0;
    
    // stat tracking stuffs
    boolean track = false;
    Tracker tracker = Tracker.getInstance();
    State state = null;
    Stats stats = null;
    
    // get total chips on table
    for (Player p : players) {
      table += p.getBankroll();
    }
    
    // lists to keep track of which players bet through to next card and which players
    // bet then folded
    ArrayList<Player> betList = new ArrayList<Player>();
    ArrayList<Player> betAndFoldList = new ArrayList<Player>();
    ArrayList<Player> allInList = new ArrayList<Player>();
    ArrayList<Player> foldList = new ArrayList<Player>();
    
    // all parameters set -- the game can be played
    System.out.println("----- Preflop -----");
    
    // iterate through the player array and do preflop actions
    while (!done) { // possibly more than one action here -- need to account for that
      for (Player p : players) {
        if (activePlayers.isEmpty()) {
          done = true;
          break;
        }
        
        if (activePlayers.size() == Tracker.PLAYERS) {
          track = true;
        }
        else {
          track = false;
        }
        
        if (activePlayers.contains(p)) {
          a = p.doNextPreflopAction();
          if (a == null) {
            done = true;
            break;
          }
          if (a.getAction() == ACTION.FOLD) {
            activePlayers.remove(p);
            if (betList.contains(p)) {
              betList.remove(p);
              betAndFoldList.add(p);
            }
            else {
              foldList.add(p);
            }
          }
          else if (a.getAction() == ACTION.QUIT || a.getAction() == ACTION.BOOTED) {
            activePlayers.remove(p);
          }
          else if (a.getAction() == ACTION.ALL_IN) {
            activePlayers.remove(p);
            if (betList.contains(p)) {
              betList.remove(p);
            }
            allInList.add(p);
          }
          else {
            if (!betList.contains(p)) {
              betList.add(p);
            }
          }
        }
      }
      //if (!activePlayers.get(0).hasNextPreflopAction()) {
      //  done = true;
     // }
    }
    
    // calculate bet sizes
    if (activePlayers.size() + allInList.size() == 1) {
      potIncrease = showdownPot;
    }
    else {
      potIncrease = flopPot;
    }

    if (!betAndFoldList.isEmpty()) {
      for (Player p : betAndFoldList) {
        if (track) {
          state = new State();
          state.setPhase(State.Phase.PREFLOP);
          state.setStackPercent((double) p.getBankroll() / (double) table);
          state.setPotPercent(0); // initially no chips bet, no pot

          stats = tracker.getStats(p, state);
          stats.addSituation();
        }

        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");

        if (track) {
          stats.addBet();
          stats.addBetAmt(bet);

          stats.addFold();
          
          tracker.addMapping(p, state, stats);
        }
      }
    }

    if (!allInList.isEmpty()) {
      for (Player p : allInList) {
        if (track) {
          state = new State();
          state.setPhase(State.Phase.PREFLOP);
          state.setStackPercent((double) p.getBankroll() / (double) table);
          state.setPotPercent(0); // initially no chips bet, no pot

          stats = tracker.getStats(p, state);
          stats.addSituation();

          stats.addAllIn();
          
          tracker.addMapping(p, state, stats);
        }

        bet = p.getChipsPlayed();
        if (bet != 0) {
          potIncrease -= bet;
          table -= bet;
          System.out.println(p + " went all in with " + bet);
          p.setChipsPlayed(0);
        }
      }
    }

    if (betList.size() != 0) {
      numPlayers = betList.size();
      bet = Math.abs(potIncrease / numPlayers);
    }

    for (Player p : betList) {
      if (track) {
        state = new State();
        state.setPhase(State.Phase.PREFLOP);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        state.setPotPercent(0); // initially no chips bet, no pot

        stats = tracker.getStats(p, state);
        stats.addSituation();
      }

      table -= bet;
      p.decrementChipsPlayed(bet);
      System.out.println(p + " bet " + bet + " this hand");

      if (track) {
        stats.addBet();
        stats.addBetAmt(bet);
        
        tracker.addMapping(p, state, stats);
      }
    }

    // for stat purposes, find out how much caused people to fold
    if (track) {
      for (Player p : foldList) {
        state = new State();
        state.setPhase(State.Phase.PREFLOP);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        state.setPotPercent(0); // initially no chips bet, no pot

        stats = tracker.getStats(p, state);
        stats.addSituation();

        stats.addFold();
        stats.addFoldAmt(bet);
        tracker.addMapping(p, state, stats);
      } 
      
      for (Player p : betAndFoldList) {
        state = new State();
        state.setPhase(State.Phase.PREFLOP);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        state.setPotPercent(0); // initially no chips bet, no pot
        
        stats = tracker.getStats(p, state);
        
        stats.addFoldAmt(bet - p.getChipsPlayed());
        
        tracker.addMapping(p, state, stats);
      }
    }

    // check if there are any players left
    if (activePlayers.size() + allInList.size() == 1) { // someone won
      if (!activePlayers.isEmpty()) {
        System.out.println("\nVictor:  " + activePlayers.get(0).toString() + " (" + showdownPot + ")");
        if (activePlayers.get(0).getHand() != null) {
          System.out.println("Winning hand:  " + activePlayers.get(0).getHand().toString());
        }
      }
      else { // else in all in list
        System.out.println("\nVictor:  " + allInList.get(0).toString() + " " + showdownPot + ")");
        if (allInList.get(0).getHand() != null) {
          System.out.println("Winning hand:  " + allInList.get(0).getHand().toString());
        }
      }
      System.out.println();
      return;
    }

    System.out.println("Total pot:  " + flopPot);

    // now need to show the flop and do flop actions
    community.addCard(cards[0]);
    community.addCard(cards[1]);
    community.addCard(cards[2]);
    betList.clear();
    betAndFoldList.clear();
    foldList.clear();

    System.out.println("\n----- Flop -----");
    
    System.out.println(community.toString());
    
    done = false;
    
    // iterate through the player array and do flop actions
    while (!done) { // possibly more than one action here -- need to account for that
      for (Player p : players) {
        if (activePlayers.isEmpty()) {
          done = true;
          break;
        }
        if (activePlayers.size() == Tracker.PLAYERS) {
          track = true;
        }
        else {
          track = false;
        }
        if (activePlayers.contains(p)) {
          a = p.doNextFlopAction();
          if (a == null) {
            done = true;
            break;
          }
          if (a.getAction() == ACTION.FOLD) {
            activePlayers.remove(p);
            if (betList.contains(p)) {
              betList.remove(p);
              betAndFoldList.add(p);
            }
            else {
              foldList.add(p);
            }
          }
          else if (a.getAction() == ACTION.QUIT || a.getAction() == ACTION.BOOTED) {
            activePlayers.remove(p);
          }
          else if (a.getAction() == ACTION.ALL_IN) {
            activePlayers.remove(p);
            if (betList.contains(p)) {
              betList.remove(p);
            }
            allInList.add(p);
          }
          else {
            if (!betList.contains(p)) {
              betList.add(p);
            }
          }
        }
      }
      //if (!activePlayers.get(0).hasNextFlopAction()) {
      //  done = true;
      //}
    }
    
    // calculate bet sizes
    if (activePlayers.size() + allInList.size() == 1) {
      potIncrease = showdownPot - flopPot;
    }
    else {
      potIncrease = turnPot - flopPot;
    }
    
    if (!betAndFoldList.isEmpty()) {
      for (Player p : betAndFoldList) {
        if (track) {
          state = new State();
          state.setPhase(State.Phase.FLOP);
          state.setStackPercent((double) p.getBankroll() / (double) table);
          if (p.getBankroll() != 0)
            state.setPotPercent(p.getChipsBet() / p.getBankroll());
          else 
            state.setPotPercent(1);

          stats = tracker.getStats(p, state);
          stats.addSituation();
        }

        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");

        if (track) {
          stats.addBet();
          stats.addBetAmt(bet);

          stats.addFold();
          
          tracker.addMapping(p, state, stats);
        }   
      }
    }
    
    if (!allInList.isEmpty()) {
      for (Player p : allInList) {
        if (track) {
          state = new State();
          state.setPhase(State.Phase.FLOP);
          state.setStackPercent((double) p.getBankroll() / (double) table);
          if (p.getBankroll() != 0)
            state.setPotPercent(p.getChipsBet() / p.getBankroll());
          else
            state.setPotPercent(1);

          stats = tracker.getStats(p, state);
          stats.addSituation();
          
          stats.addAllIn();
          
          tracker.addMapping(p, state, stats);
        }
        
        bet = p.getChipsPlayed();
        if (bet != 0) {
          potIncrease -= bet;
          table -= bet;
          System.out.println(p + " went all in with " + bet);
          p.setChipsPlayed(0);
        }
      }
    }
    
    
    if (betList.size() != 0) {
      numPlayers = betList.size();
      bet = Math.abs(potIncrease / numPlayers);
    }
    
    for (Player p : betList) {
      if (track) {
        state = new State();
        state.setPhase(State.Phase.FLOP);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        if (p.getBankroll() != 0)
          state.setPotPercent(p.getChipsBet() / p.getBankroll());
        else 
          state.setPotPercent(1);

        stats = tracker.getStats(p, state);
        stats.addSituation();
      }
      
      p.decrementChipsPlayed(bet);
      table -= bet;
      System.out.println(p + " bet " + bet + " this hand");
      
      if (track) {
        stats.addBet();
        stats.addBetAmt(bet);
        
        tracker.addMapping(p, state, stats);
      }
    }
    
    // for stat purposes, find out how much caused people to fold
    if (track) {
      for (Player p : foldList) {
        state = new State();
        state.setPhase(State.Phase.FLOP);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        if (p.getBankroll() != 0)
          state.setPotPercent(p.getChipsBet() / p.getBankroll());
        else 
          state.setPotPercent(1);

        stats = tracker.getStats(p, state);
        stats.addSituation();

        stats.addFold();
        stats.addFoldAmt(bet);
        
        tracker.addMapping(p, state, stats);
      } 
      
      for (Player p : betAndFoldList) {
        state = new State();
        state.setPhase(State.Phase.FLOP);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        if (p.getBankroll() != 0)
          state.setPotPercent(p.getChipsBet() / p.getBankroll());
        else 
          state.setPotPercent(1);
        
        stats = tracker.getStats(p, state);
        
        stats.addFoldAmt(bet - p.getChipsPlayed());
        
        tracker.addMapping(p, state, stats);
      }
    }
  
    // check if there are any players left
    if (activePlayers.size() + allInList.size() == 1) { // someone won
      if (!activePlayers.isEmpty()) {
        System.out.println("\nVictor:  " + activePlayers.get(0).toString() + " (" + showdownPot + ")");
        if (activePlayers.get(0).getHand() != null) {
          System.out.println("Winning hand:  " + activePlayers.get(0).getHand().toString());
        }
      }
      else { // else in all in list
        System.out.println("\nVictor:  " + allInList.get(0).toString() + " " + showdownPot + ")");
        if (allInList.get(0).getHand() != null) {
          System.out.println("Winning hand:  " + allInList.get(0).getHand().toString());
        }
      }
      System.out.println();
      return;
    }
    
    System.out.println("Total pot:  " + turnPot);
    
    // add turn to community and do turn actions
    community.addCard(cards[3]);
    betList.clear();
    betAndFoldList.clear();
    foldList.clear();
    
    System.out.println("\n----- Turn -----");
    
    System.out.println(community.toString());
    
    done = false;
    
    // iterate through the player array and do turn actions
    while (!done) { // possibly more than one action here -- need to account for that
      for (Player p : players) {
        if (activePlayers.isEmpty()) {
          done = true;
          break;
        }
        if (activePlayers.size() == Tracker.PLAYERS) {
          track = true;
        }
        else {
          track = false;
        }
        if (activePlayers.contains(p)) {
          a = p.doNextTurnAction();
          if (a == null) {
            done = true;
            break;
          }
          if (a.getAction() == ACTION.FOLD) {
            activePlayers.remove(p);
            if (betList.contains(p)) {
              betList.remove(p);
              betAndFoldList.add(p);
            }
            else {
              foldList.add(p);
            }
          }
          else if (a.getAction() == ACTION.QUIT || a.getAction() == ACTION.BOOTED) {
            activePlayers.remove(p);
          }
          else if (a.getAction() == ACTION.ALL_IN) {
            activePlayers.remove(p);
            if (betList.contains(p)) {
              betList.remove(p);
            }
            allInList.add(p);
          }
          else {
            if (!betList.contains(p)) {
              betList.add(p);
            }
          }
        }
      }
      //if (!activePlayers.get(0).hasNextTurnAction()) {
      //  done = true;
      //}
    }
    
    // calculate bet sizes
    if (activePlayers.size() + allInList.size() == 1) {
      potIncrease = showdownPot - turnPot;
    }
    else {
      potIncrease = riverPot - turnPot;
    }
    
    if (!betAndFoldList.isEmpty()) {
      for (Player p : betAndFoldList) {
        if (track) {
          state = new State();
          state.setPhase(State.Phase.TURN);
          state.setStackPercent((double) p.getBankroll() / (double) table);
          if (p.getBankroll() != 0)
            state.setPotPercent(p.getChipsBet() / p.getBankroll());
          else 
            state.setPotPercent(1);

          stats = tracker.getStats(p, state);
          stats.addSituation();
        }

        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");

        if (track) {
          stats.addBet();
          stats.addBetAmt(bet);

          stats.addFold();
          
          tracker.addMapping(p, state, stats);
        }   
      }
    }
    
    if (!allInList.isEmpty()) {
      for (Player p : allInList) {
        if (track) {
          state = new State();
          state.setPhase(State.Phase.TURN);
          state.setStackPercent((double) p.getBankroll() / (double) table);
          if (p.getBankroll() != 0)
            state.setPotPercent(p.getChipsBet() / p.getBankroll());
          else 
            state.setPotPercent(1);

          stats = tracker.getStats(p, state);
          stats.addSituation();
          stats.addAllIn();
          
          tracker.addMapping(p, state, stats);
        }
        bet = p.getChipsPlayed();
        if (bet != 0) {
          potIncrease -= bet;
          table -= bet;
          System.out.println(p + " went all in with " + bet);
          p.setChipsPlayed(0);
        }
      }
    }
    
    if (betList.size() != 0) {
      numPlayers = betList.size();
      bet = Math.abs(potIncrease / numPlayers);
    }
    
    for (Player p : betList) {
      if (track) {
        state = new State();
        state.setPhase(State.Phase.TURN);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        if (p.getBankroll() != 0)
          state.setPotPercent(p.getChipsBet() / p.getBankroll());
        else 
          state.setPotPercent(1);

        stats = tracker.getStats(p, state);
        stats.addSituation();
      }
      
      p.decrementChipsPlayed(bet);
      table -= bet;
      System.out.println(p + " bet " + bet + " this hand");
      
      if (track) {
        stats.addBet();
        stats.addBetAmt(bet);
        
        tracker.addMapping(p, state, stats);
      }
    }
    
    // for stat purposes, find out how much caused people to fold
    if (track) {
      for (Player p : foldList) {
        state = new State();
        state.setPhase(State.Phase.TURN);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        if (p.getBankroll() != 0)
          state.setPotPercent(p.getChipsBet() / p.getBankroll());
        else 
          state.setPotPercent(1);

        stats = tracker.getStats(p, state);
        stats.addSituation();

        stats.addFold();
        stats.addFoldAmt(bet);
        
        tracker.addMapping(p, state, stats);
      } 
      
      for (Player p : betAndFoldList) {
        state = new State();
        state.setPhase(State.Phase.TURN);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        if (p.getBankroll() != 0)
          state.setPotPercent(p.getChipsBet() / p.getBankroll());
        else 
          state.setPotPercent(1);
        
        stats = tracker.getStats(p, state);
        
        stats.addFoldAmt(bet - p.getChipsPlayed());
        
        tracker.addMapping(p, state, stats);
      }
    }
    
    // check if there are any players left
    if (activePlayers.size() + allInList.size() == 1) { // someone won
      if (!activePlayers.isEmpty()) {  
        System.out.println("\nVictor:  " + activePlayers.get(0).toString() + " (" + showdownPot + ")");
        if (activePlayers.get(0).getHand() != null) {
          System.out.println("Winning hand:  " + activePlayers.get(0).getHand().toString());
        }
      }
      else { // else in all in list
        System.out.println("\nVictor:  " + allInList.get(0).toString() + " " + showdownPot + ")");
        if (allInList.get(0).getHand() != null) {
          System.out.println("Winning hand:  " + allInList.get(0).getHand().toString());
        }
      }
      System.out.println();
      return;
    }
    
    System.out.println("Total pot:  " + riverPot);
    
    // add river to community and do river actions
    community.addCard(cards[4]);
    betList.clear();
    betAndFoldList.clear();
    foldList.clear();
    
    System.out.println("\n----- River -----");
    
    System.out.println(community.toString());
    
    done = false;
    
    // iterate through the player array and do flop actions
    while (!done) { // possibly more than one action here -- need to account for that
      for (Player p : players) {
        if (activePlayers.isEmpty()) {
          done = true;
          break;
        }
        if (activePlayers.size() == Tracker.PLAYERS) {
          track = true;
        }
        else {
          track = false;
        }
        if (activePlayers.contains(p)) {
          a = p.doNextRiverAction();
          if (a == null) {
            done = true;
            break;
          }
          if (a.getAction() == ACTION.FOLD) {
            activePlayers.remove(p);
            if (betList.contains(p)) {
              betList.remove(p);
              betAndFoldList.add(p);
            }
            else {
              foldList.add(p);
            }
          }
          else if (a.getAction() == ACTION.QUIT || a.getAction() == ACTION.BOOTED) {
            activePlayers.remove(p);
          }
          else if (a.getAction() == ACTION.ALL_IN) {
            activePlayers.remove(p);
            if (betList.contains(p)) {
              betList.remove(p);
            }
            allInList.add(p);
          }
          else {
            if (!betList.contains(p)) {
              betList.add(p);
            }
          }
        }
      }
      //if (!activePlayers.get(0).hasNextRiverAction()) {
      //  done = true;
      //}
    }
    
    // calculate bet sizes
    potIncrease = showdownPot - riverPot;
    
    if (!betAndFoldList.isEmpty()) {
      for (Player p : betAndFoldList) {
        if (track) {
          state = new State();
          state.setPhase(State.Phase.RIVER);
          state.setStackPercent((double) p.getBankroll() / (double) table);
          if (p.getBankroll() != 0)
            state.setPotPercent(p.getChipsBet() / p.getBankroll());
          else 
            state.setPotPercent(1);

          stats = tracker.getStats(p, state);
          stats.addSituation();
        }

        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");

        if (track) {
          stats.addBet();
          stats.addBetAmt(bet);

          stats.addFold();
          
          tracker.addMapping(p, state, stats);
        }        
      }
    }
    
    if (!allInList.isEmpty()) {
      for (Player p : allInList) {
        if (track) {
          state = new State();
          state.setPhase(State.Phase.RIVER);
          state.setStackPercent((double) p.getBankroll() / (double) table);
          if (p.getBankroll() != 0)
            state.setPotPercent(p.getChipsBet() / p.getBankroll());
          else 
            state.setPotPercent(1);

          stats = tracker.getStats(p, state);
          stats.addSituation();
          
          stats.addAllIn();
          
          tracker.addMapping(p, state, stats);
        }
        
        bet = p.getChipsPlayed();
        if (bet != 0) {
          potIncrease -= bet;
          table -= bet;
          System.out.println(p + " went all in with " + bet);
          p.setChipsPlayed(0);
        }
      }
    }
    
    if (betList.size() != 0) {
      numPlayers = betList.size();
      bet = Math.abs(potIncrease / numPlayers);
    }
    
    for (Player p : betList) {
      if (track) {
        state = new State();
        state.setPhase(State.Phase.RIVER);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        if (p.getBankroll() != 0)
          state.setPotPercent(p.getChipsBet() / p.getBankroll());
        else 
          state.setPotPercent(1);

        stats = tracker.getStats(p, state);
        stats.addSituation();
      }
      
      p.decrementChipsPlayed(bet);
      table -= bet;
      System.out.println(p + " bet " + bet + " this hand");
       
      if (track) {
        stats.addBet();
        stats.addBetAmt(bet);
        
        tracker.addMapping(p, state, stats);
      }
    }
    
    // for stat purposes, find out how much caused people to fold
    if (track) {
      for (Player p : foldList) {
        state = new State();
        state.setPhase(State.Phase.RIVER);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        if (p.getBankroll() != 0)
          state.setPotPercent(p.getChipsBet() / p.getBankroll());
        else 
          state.setPotPercent(1);

        stats = tracker.getStats(p, state);
        stats.addSituation();

        stats.addFold();
        stats.addFoldAmt(bet);
        
        tracker.addMapping(p, state, stats);
      } 
      
      for (Player p : betAndFoldList) {
        state = new State();
        state.setPhase(State.Phase.RIVER);
        state.setStackPercent((double) p.getBankroll() / (double) table);
        if (p.getBankroll() != 0)
          state.setPotPercent(p.getChipsBet() / p.getBankroll());
        else 
          state.setPotPercent(1);
        
        stats = tracker.getStats(p, state);
        
        stats.addFoldAmt(bet - p.getChipsPlayed());
        
        tracker.addMapping(p, state, stats);
      }
    }
  
    activePlayers.addAll(allInList);
    
    // by this point, someone has won
    // possibly more than one player though, so need to find out who
    for (Player p : activePlayers) {
      if (p.isWinner()) {
        System.out.println("\nVictor:  " + p.toString() + " (" + showdownPot + ")");
        if (p.getHand() != null) {
          System.out.println("Winning hand:  " + p.getHand().toString());
        }
        System.out.println();
        break;
      }
    }
  }  
}