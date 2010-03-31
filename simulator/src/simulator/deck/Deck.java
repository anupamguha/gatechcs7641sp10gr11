/**
 * 
 */
package simulator.deck;

/**
 * This class defines the enumerations needed to define a deck of cards
 * 
 * @author christina
 */
public class Deck {
  
  /**
   * Enumeration for possible suits
   */
  public enum SUIT {
    HEARTS, DIAMONDS, SPADES, CLUBS;
  }

  /**
   * Enumeration for possible value
   */
  public enum VALUE {
    ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;
  }
  
  /**
   * Given a character h, d, s, or c indicating the suit of a card, return the
   * corresponding SUIT
   * 
   * @param c
   *   the character indicating the suit
   *   
   * @return
   *   the corresponding SUIT enum value
   */
  public static SUIT getSuit(char c) {
    switch (c) {
      case 'h':
        return SUIT.HEARTS;
      case 'd':
        return SUIT.DIAMONDS;
      case 's':
        return SUIT.SPADES;
      case 'c':
        return SUIT.CLUBS;
      default:
        return null;
    }
  }
  
  /**
   * Given a character representing the card's value, return the corresponding
   * VALUE enum value
   * 
   * @param c
   *   a character representing the card's value
   *   
   * @return
   *   the corresponding VALUE enum value
   */
  public static VALUE getValue(char c) {
    switch (c) {
      case 'A':
        return VALUE.ACE;
      case '2':
        return VALUE.TWO;
      case '3':
        return VALUE.THREE;
      case '4':
        return VALUE.FOUR;
      case '5':
        return VALUE.FIVE;
      case '6':
        return VALUE.SIX;
      case '7':
        return VALUE.SEVEN;
      case '8':
        return VALUE.EIGHT;
      case '9':
        return VALUE.NINE;
      case 'T':
        return VALUE.TEN;
      case 'J':
        return VALUE.JACK;
      case 'Q':
        return VALUE.QUEEN;
      case 'K':
        return VALUE.KING;
      default:
        return null;
    }
  }
}
