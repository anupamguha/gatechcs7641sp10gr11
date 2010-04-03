/**
 * 
 */
package simulator.deck;

/**
 * This class implements a single card as a suit and a value.
 * 
 * The suit is defined as a {@link Deck.SUIT} enumeration value, while
 * the value is a {@link Deck.VALUE} enumeration value
 * 
 * @author christina
 */
public class Card {
  
  /**
   * The card's suit
   */
  private Deck.SUIT suit;
 
  /**
   * The card's value 
   */
  private Deck.VALUE value;  
  
  /**
   * Constructs a card given a 2-character string indicating suit and value
   * 
   * @param s
   *   a 2-character string indicating suit and value
   */
  public Card(String s) {
    if (s.length() != 2) { // invalid string
      System.err.println("Invalid card value " + s);
      System.err.println("Cannot construct card");
      return;
    }
    
    else { // valid value
      value = Deck.getValue(s.charAt(0));
      suit = Deck.getSuit(s.charAt(1));

      
      if (suit == null || value == null) { // invalid values
        System.err.println("Invalid suit or value for card");
        System.err.println("Cannot construct card");
        return;
      }
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append(value.toString()).append(" of ").append(suit.toString());
    
    return sb.toString();
  }

}
