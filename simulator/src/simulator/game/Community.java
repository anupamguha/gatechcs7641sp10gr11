package simulator.game;

import simulator.deck.Card;

/**
 * This class implements the set of community cards that are currently
 * available to all players
 * 
 * @author christina
 */
public class Community extends CardGroup {
  
  /**
   * No-arg constructor
   */
  public Community() {
    cards = new Card[5];
    numCards = 0;
  }
  
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("Community is:");
    
    for (Card c : cards) {
      if (c != null) {
        sb.append(" ").append(c.toString());
      }
    }
    
    return sb.toString();
  }

}
