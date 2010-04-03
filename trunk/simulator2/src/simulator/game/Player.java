package simulator.game;

import simulator.deck.Card;

/**
 * @author christina
 *
 */
public class Player implements Comparable {
  
  /**
   * The cards dealt to the player
   */
  private Hand hand;
  
  /**
   * Actions taken by the player preflop
   */
  private Action[] preflop;
  
  /**
   * Actions taken by the player on the flop
   */
  private Action[] flop;
  
  /**
   * Actions taken by the player on the turn
   */
  private Action[] turn;
  
  /**
   * Actions taken by the player on the river
   */
  private Action[] river;
  
  /**
   * Amount of chips the player has before the hand starts
   */
  private int bankroll;
  
  /**
   * The player name
   */
  private String name;
  
  /**
   * Boolean indicating whether the player wins the hand
   */
  private boolean winner = false;
  
  /**
   * An integer representing the total number of chips a player has yet to bet over 
   * the course of a game
   */
  private int chipsPlayed;
  
  /**
   * The number of chips a player has bet over the course of a game 
   */
  private int chipsBet;
  
  /**
   * Integer indicating where in order player came
   */
  private int location;
  
  /**
   * Sets the player's hand
   * 
   * @param h
   *   a String array representing the cards in the hand
   */
  public void setHand(String[] h) {
    hand = new Hand();
    
    for (int i = 0; i < h.length; i++) {
      Card c = new Card(h[i]);
      hand.addCard(c);
    }
  }
  
  /**
   * Gets the player's hand
   * 
   * @return
   *   the player's hand
   */
  public Hand getHand() {
    return hand;
  }
  
  /**
   * Sets the player's bankroll
   * 
   * @param b
   *   the number of chips the player currently has
   */
  public void setBankroll(int b) {
    bankroll = b;
  }
  
  /**
   * 
   * @return
   */
  public int getBankroll() {
    return bankroll;
  }
  
  /**
   * Sets the player's name
   * 
   * @param name
   *   the name of the player
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Indicates that this player wins the current poker hand
   */
  public void setAsWinner() {
    winner = true;
  }
  
  /**
   * Returns a boolean indicating if the player is the winner
   * 
   * @return
   *   a boolean indicating if the player wins the hand
   */
  public boolean isWinner() {
    return winner;
  }
  
  /**
   * Sets the number of chips played to the given integer
   * 
   * @param chips
   *   the number of chips played
   */
  public void setChipsPlayed(int chips) {
    chipsPlayed = chips;
  }
  
  /**
   * Decrements the given amount from the chips played
   * 
   * @param amount
   *   the number of chips to remove
   */
  public void decrementChipsPlayed(int amount) {
    chipsPlayed -= amount;
    bankroll -= amount;
    chipsBet += amount;
  }
  
  /**
   * Gets the number of chips that have not yet been played
   * 
   * @return
   *   the number of chips yet to play
   */
  public int getChipsPlayed() {
    return chipsPlayed;
  }
  
  /**
   * Gets the number of chips that have been bet
   * 
   * @return
   *   the number of chips bet
   */
  public int getChipsBet() {
    return chipsBet;
  }
  
  /**
   * Adds the number to the number of chips bet
   * 
   * @param i
   *   the chips to add
   */
  public void addChipsBet(int i) {
    chipsBet += i;
    bankroll -= i;
  }
  
  /**
   * Sets the location of the player
   * 
   * @param loc
   *   player's location
   */
  public void setLocation(int loc) {
    location = loc;
  }
  
  /**
   * Sets the preflop actions
   * 
   * @param s
   *   a string of characters representing the actions
   */
  public void setPreflop(String s) {
    preflop = new Action[s.length()];
    
    for (int i = 0; i < s.length(); i++) {
      Action a = new Action();
      a.setAction(s.charAt(i));
      preflop[i] = a;
    }
    // TODO -- set betting amounts on the actions that require it
  }
  
  /**
   * Sets the flop actions
   * 
   * @param s
   *   a string of characters representing the actions
   */
  public void setFlop(String s) {
    flop = new Action[s.length()];
    
    for (int i = 0; i < s.length(); i++) {
      Action a = new Action();
      a.setAction(s.charAt(i));
      flop[i] = a;
    }
    // TODO -- set betting amounts on the actions that require it
  }
  
  /**
   * Sets the turn actions
   * 
   * @param s
   *   a string of characters representing the actions
   */
  public void setTurn(String s) {
    turn = new Action[s.length()];
    
    for (int i = 0; i < s.length(); i++) {
      Action a = new Action();
      a.setAction(s.charAt(i));
      turn[i] = a;
    }
    // TODO -- set betting amounts on the actions that require it
  }
  
  /**
   * Sets the river actions
   * 
   * @param s
   *   a string of characters representing the actions
   */
  public void setRiver(String s) {
    river = new Action[s.length()];
    
    for (int i = 0; i < s.length(); i++) {
      Action a = new Action();
      a.setAction(s.charAt(i));
      river[i] = a;
    }
    // TODO -- set betting amounts on the actions that require it
  }
  
  /**
   * Does the next preflop action
   */
  public Action doNextPreflopAction() {
    if (preflop == null) {
      return null;
    }
    Action a = preflop[0];
    
    System.out.println(name + " " + a.toString());
    
    if (preflop.length != 1) {
      Action[] temp = new Action[preflop.length - 1];
      for (int i = 1; i < preflop.length; i++) {
        temp[i - 1] = preflop[i];
      }
      preflop = temp;
    }
    else {
      preflop = null;
    }
    
    return a;
  }
  
  /**
   * Checks if there are more preflop actions to do
   * 
   * @return
   *   a boolean value indicating if there are more actions to do
   */
  public boolean hasNextPreflopAction() {
    return preflop != null;
  }
  
  /**
   * Does the next flop action 
   */
  public Action doNextFlopAction() {
    if (flop == null) {
      return null;
    }
    Action a = flop[0];
    
    System.out.println(name + " " + a.toString());
    
    if (flop.length != 1) {
      Action[] temp = new Action[flop.length - 1];
      for (int i = 1; i < flop.length; i++) {
        temp[i - 1] = flop[i];
      }
      flop = temp;
    }
    else {
      flop = null;
    }
    
    return a;
  }
  
  /**
   * Checks if there are more flop actions to do
   * 
   * @return
   *   a boolean value indicating if there are more actions to do
   */
  public boolean hasNextFlopAction() {
    return flop != null;
  }
  
  /**
   * Does the next turn action
   */
  public Action doNextTurnAction() {
    if (turn == null) {
      return null;
    }
    Action a = turn[0];
    
    System.out.println(name + " " + a.toString());
    
    if (turn.length != 1) {
      Action[] temp = new Action[turn.length - 1];
      for (int i = 1; i < turn.length; i++) {
        temp[i - 1] = turn[i];
      }
      turn = temp;
    }
    else {
      turn = null;
    }
    
    return a;
  }
  
  /**
   * Checks if there are more turn actions to do
   * 
   * @return
   *   a boolean value indicating if there are more actions to do
   */
  public boolean hasNextTurnAction() {
    return turn != null;
  }
  
  /**
   * Does the next river action
   */
  public Action doNextRiverAction() {
    if (river == null) {
      return null;
    }
    Action a = river[0];
    
    System.out.println(name + " " + a.toString());
    
    if (river.length != 1) {
      Action[] temp = new Action[river.length - 1];
      for (int i = 1; i < river.length; i++) {
        temp[i - 1] = river[i];
      }
      river = temp;
    }
    else {
      river = null;
    }
    
    return a;
  }
  
  /**
   * Checks if there are more river actions to do
   * 
   * @return
   *   a boolean value indicating if there are more actions to do
   */
  public boolean hasNextRiverAction() {
    return river != null;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    return name;
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Object obj) {
    if (!(obj instanceof Player)) {
      System.err.println("Cannot cast object to Player");
      return -2;
    }
    Player p = (Player) obj;
    
    if (p.location == location) {
      return 0;
    }
    else if (p.location < location) {
      return 1;
    }
    
    return -1;
  }
  
  public boolean equals(Object obj) {
    Player p = (Player) obj;
    
    return p.name.equals(this.name);
  }
  
  public int hashCode() {
    return name.hashCode();
  }
    
}
