package simulator.game;

import java.util.ArrayList;
import java.util.Collections;

import simulator.deck.Card;
import simulator.game.Action.ACTION;
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
    int playing = activePlayers.size();
    int bet = 0;
    int table = 0;
    int numRaises = 0;
    
    // stat tracking stuffs
    Tracker tracker = Tracker.getInstance();
    Stats stats = null;
    
    // get total chips on table
    for (Player p : players) {
      table += p.getBankroll();
    }
    
    // lists to keep track of which players bet through to next card and which players
    // bet then folded
    ArrayList<Player> betList = new ArrayList<Player>();
    ArrayList<Player> callList = new ArrayList<Player>();
    ArrayList<Player> raiseList = new ArrayList<Player>();
    ArrayList<Player> callAndFoldList = new ArrayList<Player>();
    ArrayList<Player> raiseAndFoldList = new ArrayList<Player>();
    ArrayList<Player> betAndFoldList = new ArrayList<Player>();
    ArrayList<Player> foldList = new ArrayList<Player>();
    ArrayList<Player> checkList = new ArrayList<Player>();
    
    ArrayList<Stats> phaseStats = new ArrayList<Stats>();
    
    // all parameters set -- the game can be played
    System.out.println("----- Preflop -----");
    
    // iterate through the player array and do preflop actions
    while (!done) { // possibly more than one action here -- need to account for that
      for (Player p : players) {
        if (activePlayers.isEmpty()) {
          done = true;
          break;
        }
        
        if (activePlayers.contains(p)) {
          // set up stats
          stats = new Stats();
          
          stats.setName(p.getName());
          stats.setGame(timestamp);
          stats.setNumPlayers(playing);
          stats.setPhase(Stats.PHASE.PREFLOP);
          stats.setPotSize(0);
          stats.setPotPercentage(0);
          stats.setLastPhasePercentage(p.getLastPhasePercent());
          stats.setStackPercentage((double) p.getBankroll() / (double) table);
          
          int maxOpp = -1;
          double oppStacks = 0;
          int maxBets = -1;
          
          for (Player pl : activePlayers) {
            if (pl.equals(p)) {
              continue;
            }
            
            if (pl.getBankroll() > maxOpp) {
              maxOpp = pl.getBankroll();
            }
            
            oppStacks += pl.getBankroll();
            
            if (pl.getBets() + pl.getRaises() > maxBets) {
              maxBets = pl.getBets() + pl.getRaises();
            }
          }
          
          stats.setMaxOppStackPercentage((double) maxOpp / (double) table);
          stats.setOppStackPercentage(oppStacks / (double) table);
          
          stats.setBets(p.getBets());
          stats.setRaises(p.getRaises());
          stats.setMaxOppBets(maxBets);
          
          // do action
          a = p.doNextPreflopAction();
          if (a == null) {
            done = true;
            break;
          }
          if (a.getAction() == ACTION.FOLD) {
            activePlayers.remove(p);
            stats.setAction(ACTION.FOLD);
            --playing;
            if (!union(betList, union(callList, raiseList)).contains(p)) {
              foldList.add(p);
            }
            else {
              if (callList.contains(p)) {
                callList.remove(p);
                callAndFoldList.add(p);
              }
              if (raiseList.contains(p)) {
                raiseList.remove(p);
                raiseAndFoldList.add(p);
              }
              if (betList.contains(p)) {
                betList.remove(p);
                betAndFoldList.add(p);
              }
            }
          }
          else if (a.getAction() == ACTION.QUIT || a.getAction() == ACTION.BOOTED) {
            activePlayers.remove(p);
          }
          else if (a.getAction() == ACTION.ALL_IN) {
            return;
          }
          else {
            if (a.getAction() == ACTION.RAISE && !raiseList.contains(p)) {
              stats.setAction(ACTION.RAISE);
              ++numRaises;
              p.addRaise();
              raiseList.add(p);
            }
            else if (a.getAction() == ACTION.CALL && !callList.contains(p)) {
              stats.setAction(ACTION.CALL);
              callList.add(p);
            }
            else if (a.getAction() == ACTION.CHECK) {
              stats.setAction(ACTION.CHECK);
              checkList.add(p);
            }
            else {
              stats.setAction(ACTION.BET);
              p.addBet();
              betList.add(p);
            }
          }
          phaseStats.add(stats);
        }
      }
    }
    
    // calculate bet sizes
    if (activePlayers.size() == 1) {
      potIncrease = showdownPot;
    }
    else {
      potIncrease = flopPot;
    }
    
    double avgRaise = 0;
    
    if (numRaises == 0) {
      avgRaise = 0;
    }
    else {
     avgRaise = (double) potIncrease / (double) numRaises;
    }
    
    for (Stats s : phaseStats) {
      s.setAvgRaise(avgRaise);
      tracker.addStat(s);
    }
    
    // reset stats stuff -- not needed until next phase
    phaseStats.clear();
    numRaises = 0;

    if (!betAndFoldList.isEmpty()) {
      for (Player p : betAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");
      }
    }
    
    if (!callAndFoldList.isEmpty()) {
      for (Player p : callAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " called " + bet + " this hand, then folded");
      }
    }
    
    if (!raiseAndFoldList.isEmpty()) {
      for (Player p : raiseAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " raised " + bet + " this hand, then folded");
      }
    }

    if (union(betList, union(callList, raiseList)).size() != 0) {
      numPlayers = union(betList, union(callList, raiseList)).size();
      bet = Math.abs(potIncrease / numPlayers);
    }

    for (Player p : union(betList, union(callList, raiseList))) {
      table -= bet;
      p.decrementChipsPlayed(bet);
      System.out.println(p + " bet " + bet + " this hand");
    }

    // check if there are any players left
    if (activePlayers.size() == 1) { // someone won
      System.out.println("\nVictor:  " + activePlayers.get(0).toString() + " (" + showdownPot + ")");
      if (activePlayers.get(0).getHand() != null) {
        System.out.println("Winning hand:  " + activePlayers.get(0).getHand().toString());
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
    callList.clear();
    callAndFoldList.clear();
    raiseList.clear();
    raiseAndFoldList.clear();
    foldList.clear();
    checkList.clear();

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
        // set up stats
        stats = new Stats();
        
        stats.setName(p.getName());
        stats.setGame(timestamp);
        stats.setNumPlayers(playing);
        stats.setPhase(Stats.PHASE.FLOP);
        stats.setPotSize((double) flopPot / (double) table);
        stats.setPotPercentage((double) p.getChipsBet() / (double) table);
        stats.setLastPhasePercentage(p.getLastPhasePercent());
        stats.setStackPercentage((double) p.getBankroll() / (double) table);
        
        int maxOpp = -1;
        double oppStacks = 0;
        int maxBets = -1;
        
        for (Player pl : activePlayers) {
          if (pl.equals(p)) {
            continue;
          }
          
          if (pl.getBankroll() > maxOpp) {
            maxOpp = pl.getBankroll();
          }
          
          oppStacks += pl.getBankroll();
          
          if (pl.getBets() + pl.getRaises() > maxBets) {
            maxBets = pl.getBets() + pl.getRaises();
          }
        }
        
        stats.setMaxOppStackPercentage((double) maxOpp / (double) table);
        stats.setOppStackPercentage(oppStacks / (double) table);
        
        stats.setBets(p.getBets());
        stats.setRaises(p.getRaises());
        stats.setMaxOppBets(maxBets);
        
        // do actions
        if (activePlayers.contains(p)) {
          a = p.doNextFlopAction();
          if (a == null) {
            done = true;
            break;
          }
          if (a.getAction() == ACTION.FOLD) {
            activePlayers.remove(p);
            stats.setAction(ACTION.FOLD);
            --playing;
            if (!union(betList, union(callList, raiseList)).contains(p)) {
              foldList.add(p);
            }
            else {
              if (callList.contains(p)) {
                callList.remove(p);
                callAndFoldList.add(p);
              }
              if (raiseList.contains(p)) {
                raiseList.remove(p);
                raiseAndFoldList.add(p);
              }
              if (betList.contains(p)) {
                betList.remove(p);
                betAndFoldList.add(p);
              }
            }
          }
          else if (a.getAction() == ACTION.QUIT || a.getAction() == ACTION.BOOTED) {
            activePlayers.remove(p);
          }
          else if (a.getAction() == ACTION.ALL_IN) {
            return;
          }
          else {
            if (a.getAction() == ACTION.RAISE && !raiseList.contains(p)) {
              ++numRaises;
              stats.setAction(ACTION.RAISE);
              p.addRaise();
              raiseList.add(p);
            }
            else if (a.getAction() == ACTION.CALL && !callList.contains(p)) {
              stats.setAction(ACTION.CALL);
              callList.add(p);
            }
            else if (a.getAction() == ACTION.CHECK) {
              stats.setAction(ACTION.CHECK);
              checkList.add(p);
            }
            else {
              stats.setAction(ACTION.BET);
              p.addBet();
              betList.add(p);
            }
          }
          phaseStats.add(stats);
        }
      }
      //if (!activePlayers.get(0).hasNextFlopAction()) {
      //  done = true;
      //}
    }
    
    // calculate bet sizes
    if (activePlayers.size() == 1) {
      potIncrease = showdownPot - flopPot;
    }
    else {
      potIncrease = turnPot - flopPot;
    }
    
    
    if (numRaises == 0) {
      avgRaise = 0;
    }
    else {
     avgRaise = (double) potIncrease / (double) numRaises;
    }
    
    
    for (Stats s : phaseStats) {
      s.setAvgRaise(avgRaise);
      tracker.addStat(s);
    }
    
    // reset stats stuff -- not needed until next phase
    phaseStats.clear();
    numRaises = 0;
    
    if (!betAndFoldList.isEmpty()) {
      for (Player p : betAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");   
      }
    }
    
    if (!callAndFoldList.isEmpty()) {
      for (Player p : callAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");
      }
    }
    
    if (!raiseAndFoldList.isEmpty()) {
      for (Player p : raiseAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded"); 
      }
    }
    
    if (union(betList, union(callList, raiseList)).size() != 0) {
      numPlayers = union(betList, union(callList, raiseList)).size();
      bet = Math.abs(potIncrease / numPlayers);
    }
    
    for (Player p : union(betList, union(callList, raiseList))) {      
      p.decrementChipsPlayed(bet);
      table -= bet;
      System.out.println(p + " bet " + bet + " this hand");
    }
     
    // check if there are any players left
    if (activePlayers.size() == 1) { // someone won
      System.out.println("\nVictor:  " + activePlayers.get(0).toString() + " (" + showdownPot + ")");
      if (activePlayers.get(0).getHand() != null) {
        System.out.println("Winning hand:  " + activePlayers.get(0).getHand().toString());
      }

      System.out.println();
      return;
    }
    
    System.out.println("Total pot:  " + turnPot);
    
    // add turn to community and do turn actions
    community.addCard(cards[3]);
    betList.clear();
    betAndFoldList.clear();
    callList.clear();
    callAndFoldList.clear();
    raiseList.clear();
    raiseAndFoldList.clear();
    foldList.clear();
    checkList.clear();
    
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
        
        // set up stats
        stats = new Stats();
        
        stats.setName(p.getName());
        stats.setGame(timestamp);
        stats.setNumPlayers(playing);
        stats.setPhase(Stats.PHASE.TURN);
        stats.setPotSize((double) turnPot / (double) table);
        stats.setPotPercentage((double) p.getChipsBet() / (double) table);
        stats.setLastPhasePercentage(p.getLastPhasePercent());
        stats.setStackPercentage((double) p.getBankroll() / (double) table);
        
        int maxOpp = -1;
        double oppStacks = 0;
        int maxBets = -1;
        
        for (Player pl : activePlayers) {
          if (pl.equals(p)) {
            continue;
          }
          
          if (pl.getBankroll() > maxOpp) {
            maxOpp = pl.getBankroll();
          }
          
          oppStacks += pl.getBankroll();
          
          if (pl.getBets() + pl.getRaises() > maxBets) {
            maxBets = pl.getBets() + pl.getRaises();
          }
        }
        
        stats.setMaxOppStackPercentage((double) maxOpp / (double) table);
        stats.setOppStackPercentage(oppStacks / (double) table);
        
        stats.setBets(p.getBets());
        stats.setRaises(p.getRaises());
        stats.setMaxOppBets(maxBets);
        
        if (activePlayers.contains(p)) {
          a = p.doNextTurnAction();
          if (a == null) {
            done = true;
            break;
          }
          if (a.getAction() == ACTION.FOLD) {
            activePlayers.remove(p);
            --playing;
            stats.setAction(ACTION.FOLD);
            if (!union(betList, union(callList, raiseList)).contains(p)) {
              foldList.add(p);
            }
            else {
              if (callList.contains(p)) {
                callList.remove(p);
                callAndFoldList.add(p);
              }
              if (raiseList.contains(p)) {
                raiseList.remove(p);
                raiseAndFoldList.add(p);
              }
              if (betList.contains(p)) {
                betList.remove(p);
                betAndFoldList.add(p);
              }
            }
          }
          else if (a.getAction() == ACTION.QUIT || a.getAction() == ACTION.BOOTED) {
            activePlayers.remove(p);
          }
          else if (a.getAction() == ACTION.ALL_IN) {
            return;
          }
          else {
            if (a.getAction() == ACTION.RAISE && !raiseList.contains(p)) {
              ++numRaises;
              p.addRaise();
              stats.setAction(ACTION.RAISE);
              raiseList.add(p);
            }
            else if (a.getAction() == ACTION.CALL && !callList.contains(p)) {
              stats.setAction(ACTION.CALL);
              callList.add(p);
            }
            else if (a.getAction() == ACTION.CHECK) {
              stats.setAction(ACTION.CHECK);
              checkList.add(p);
            }
            else {
              stats.setAction(ACTION.BET);
              p.addBet();
              betList.add(p);
            }
          }
          phaseStats.add(stats);
        }
      }
      //if (!activePlayers.get(0).hasNextTurnAction()) {
      //  done = true;
      //}
    }
    
    // calculate bet sizes
    if (activePlayers.size() == 1) {
      potIncrease = showdownPot - turnPot;
    }
    else {
      potIncrease = riverPot - turnPot;
    }
    
    if (numRaises == 0) {
      avgRaise = 0;
    }
    else {
     avgRaise = (double) potIncrease / (double) numRaises;
    }
    
    
    for (Stats s : phaseStats) {
      s.setAvgRaise(avgRaise);
      tracker.addStat(s);
    }
    
    // reset stats stuff -- not needed until next phase
    phaseStats.clear();
    numRaises = 0;
    
    if (!betAndFoldList.isEmpty()) {
      for (Player p : betAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");
      }
    }   
    
    if (!callAndFoldList.isEmpty()) {
      for (Player p : callAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");
      }
    }
    
    if (!raiseAndFoldList.isEmpty()) {
      for (Player p : raiseAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");
      }
    }
    
    if (union(betList, union(callList, raiseList)).size() != 0) {
      numPlayers = union(betList, union(callList, raiseList)).size();
      bet = Math.abs(potIncrease / numPlayers);
    }
    
    for (Player p : union(betList, union(callList, raiseList))) {   
      p.decrementChipsPlayed(bet);
      table -= bet;
      System.out.println(p + " bet " + bet + " this hand");
    }
      
    // check if there are any players left
    if (activePlayers.size() == 1) { // someone won
      System.out.println("\nVictor:  " + activePlayers.get(0).toString() + " (" + showdownPot + ")");
      if (activePlayers.get(0).getHand() != null) {
        System.out.println("Winning hand:  " + activePlayers.get(0).getHand().toString());
      }
      
      System.out.println();
      return;
    }
    
    System.out.println("Total pot:  " + riverPot);
    
    // add river to community and do river actions
    community.addCard(cards[4]);
    betList.clear();
    betAndFoldList.clear();
    callList.clear();
    callAndFoldList.clear();
    raiseList.clear();
    raiseAndFoldList.clear();
    foldList.clear();
    checkList.clear();
    
    System.out.println("\n----- River -----");
    
    System.out.println(community.toString());
    
    done = false;
    
    // can track all in on river/showdown, just not earlier
    ArrayList<Player> allInList = new ArrayList<Player>();
    
    // iterate through the player array and do flop actions
    while (!done) { // possibly more than one action here -- need to account for that
      for (Player p : players) {
        if (activePlayers.isEmpty()) {
          done = true;
          break;
        }
        
        // set up stats
        stats = new Stats();
        
        stats.setName(p.getName());
        stats.setGame(timestamp);
        stats.setNumPlayers(playing);
        stats.setPhase(Stats.PHASE.RIVER);
        stats.setPotSize((double) riverPot / (double) table);
        stats.setPotPercentage((double) p.getChipsBet() / (double) table);
        stats.setLastPhasePercentage(p.getLastPhasePercent());
        stats.setStackPercentage((double) p.getBankroll() / (double) table);
        
        int maxOpp = -1;
        double oppStacks = 0;
        int maxBets = -1;
        
        for (Player pl : activePlayers) {
          if (pl.equals(p)) {
            continue;
          }
          
          if (pl.getBankroll() > maxOpp) {
            maxOpp = pl.getBankroll();
          }
          
          oppStacks += pl.getBankroll();
          
          if (pl.getBets() + pl.getRaises() > maxBets) {
            maxBets = pl.getBets() + pl.getRaises();
          }
        }
        
        stats.setMaxOppStackPercentage((double) maxOpp / (double) table);
        stats.setOppStackPercentage(oppStacks / (double) table);
        
        stats.setBets(p.getBets());
        stats.setRaises(p.getRaises());
        stats.setMaxOppBets(maxBets);
        
        if (activePlayers.contains(p)) {
          a = p.doNextRiverAction();
          if (a == null) {
            done = true;
            break;
          }
          if (a.getAction() == ACTION.FOLD) {
            activePlayers.remove(p);
            --playing;
            stats.setAction(ACTION.FOLD);
            if (!union(betList, union(callList, raiseList)).contains(p)) {
              foldList.add(p);
            }
            else {
              if (callList.contains(p)) {
                callList.remove(p);
                callAndFoldList.add(p);
              }
              if (raiseList.contains(p)) {
                raiseList.remove(p);
                raiseAndFoldList.add(p);
              }
              if (betList.contains(p)) {
                betList.remove(p);
                betAndFoldList.add(p);
              }
            }
          }
          else if (a.getAction() == ACTION.QUIT || a.getAction() == ACTION.BOOTED) {
            activePlayers.remove(p);
          }
          else if (a.getAction() == ACTION.ALL_IN) {
            stats.setAction(ACTION.ALL_IN);
            activePlayers.remove(p);
            if (betList.contains(p)) {
              betList.remove(p);
            }
            if (raiseList.contains(p)) {
              raiseList.remove(p);
            }
            if (callList.contains(p)) {
              callList.remove(p);
            }
            if (checkList.contains(p)) {
              checkList.remove(p);
            }
            allInList.add(p);
          }
          else {
            if (a.getAction() == ACTION.RAISE && !raiseList.contains(p)) {
              raiseList.add(p);
              stats.setAction(ACTION.RAISE);
              ++numRaises;
              p.addRaise();
            }
            else if (a.getAction() == ACTION.CALL && !callList.contains(p)) {
              callList.add(p);
              stats.setAction(ACTION.CALL);
            }
            else if (a.getAction() == ACTION.CHECK) {
              stats.setAction(ACTION.CHECK);
              checkList.add(p);
            }
            else {
              stats.setAction(ACTION.BET);
              p.addBet();
              betList.add(p);
            }
          }
          phaseStats.add(stats);
        }
      }
      //if (!activePlayers.get(0).hasNextRiverAction()) {
      //  done = true;
      //}
    }
    
    // calculate bet sizes
    potIncrease = showdownPot - riverPot;
    
    if (numRaises == 0) {
      avgRaise = 0;
    }
    else {
     avgRaise = (double) potIncrease / (double) numRaises;
    }
    
    
    for (Stats s : phaseStats) {
      s.setAvgRaise(avgRaise);
      tracker.addStat(s);
    }
    
    if (!betAndFoldList.isEmpty()) {
      for (Player p : betAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");  
      }
    }
    
    if (!callAndFoldList.isEmpty()) {
      for (Player p : callAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");     
      }
    }
       
    if (!raiseAndFoldList.isEmpty()) {
      for (Player p : raiseAndFoldList) {
        bet = p.getChipsPlayed();
        potIncrease -= bet;
        table -= bet;
        System.out.println(p + " bet " + bet + " this hand, then folded");      
      }
    }
    
    if (!allInList.isEmpty()) {
      for (Player p : allInList) {        
        bet = p.getChipsPlayed();
        if (bet != 0) {
          potIncrease -= bet;
          table -= bet;
          System.out.println(p + " went all in with " + bet);
          p.setChipsPlayed(0);
        }
      }
    }
    
    if (union(betList, union(callList, raiseList)).size() != 0) {
      numPlayers = union(betList, union(callList, raiseList)).size();
      bet = Math.abs(potIncrease / numPlayers);
    }
    
    for (Player p : union(betList, union(callList, raiseList))) {     
      p.decrementChipsPlayed(bet);
      table -= bet;
      System.out.println(p + " bet " + bet + " this hand");
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
  
  
  /**
   * Private method -- returns the union of two lists
   * Values common between the two lists are only represented once
   * 
   * @param l1
   * @param l2
   * @return
   */
  private ArrayList<Player> union(ArrayList<Player> l1, ArrayList<Player> l2) {
    ArrayList<Player> temp = new ArrayList<Player>();
    
    temp.addAll(l1);
    
    for (Player p : l2) {
      if (!l1.contains(p)) {
        temp.add(p);
      }
    }
    
    return temp;
  }
}
