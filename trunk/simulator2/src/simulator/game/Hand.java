package simulator.game;

import simulator.deck.Card;

/**
 * This class implements the set of cards dealt to one player
 * 
 * @author christina
 */
public class Hand extends CardGroup {
  
  /**
   * No-arg constructor
   */
  public Hand() {
    cards = new Card[2];
    numCards = 0;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    for (Card c : cards) {
      sb.append(" ").append(c.toString());
    }
    
    sb.append("\n");
    
    return sb.toString();
  }

}
