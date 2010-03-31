package simulator.game;

/**
 * @author christina
 *
 */
/**
 * @author christina
 *
 */
public class Action {
  
  /**
   * The action taken
   */
  private ACTION action;
  
  /**
   * The amount the pot is increased by
   * 
   * Only valid for BLIND, BET, CALL, RAISE, ALL_IN events 
   */
  //private int amount;
  
  /**
   * Enumeration dictating possible actions a player can take
   */
  public enum ACTION {
    BLIND, CHECK, BET, CALL, RAISE, FOLD, ALL_IN, QUIT, BOOTED;
  }
  
  /**
   * Returns the action taken
   * 
   * @return
   *   the action
   */
  public ACTION getAction() {
    return action;
  }
  
  /**
   * Translates a character indicating the action taken to the ACTION enumeration
   * 
   * @param a
   *   a character indicating the action
   */
  public void setAction(char a) {
    switch (a) {
      case 'B':
        action = ACTION.BLIND;
        break;
      case 'k':
        action = ACTION.CHECK;
        //amount = -1;
        break;
      case 'b':
        action = ACTION.BET;
        break;
      case 'c':
        action = ACTION.CALL;
        break;
      case 'r':
        action = ACTION.RAISE;
        break;
      case 'f':
        action = ACTION.FOLD;
       // amount = -1;
        break;
      case 'A':
        action = ACTION.ALL_IN;
        break;
      case 'Q':
        action = ACTION.QUIT;
        break;
      case 'K':
        action = ACTION.BOOTED;
        break;
      default:
        action = null;
       // amount = -1;
    }
  }
  
  /**
   * Sets the amount bet on an action
   * 
   * Cannot be done before the action is set, or if the 
   * action is a CHECK or FOLD action
   * 
   * @param a
   *   the amount of the bet
   */
//  public void setAmount(int a) {
//    if (action == null) {
//      System.err.println("Action must be set before amount");
//      return;
//    }
//    
//    if (action == ACTION.CHECK || action == ACTION.FOLD) {
//      System.err.println("Setting the amount is not valid for check or fold actions");
//      return;
//    }
//    
//    amount = a;
//  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    if (action != null) {
      return action.toString();
    }
    return null;
  }

}
