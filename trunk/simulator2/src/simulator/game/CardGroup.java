package simulator.game;

import simulator.deck.Card;

public abstract class CardGroup {

  /**
   * The cards making up the hand
   */
  protected Card[] cards;
  
  /**
   * The number of cards in the hand
   */
  protected int numCards;

  public CardGroup() {
    super();
  }

  /**
   * Adds a card to the hand
   * 
   * @param c
   *   the card to add
   */
  public void addCard(Card c) {
    cards[numCards++] = c;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public abstract String toString(); 

}