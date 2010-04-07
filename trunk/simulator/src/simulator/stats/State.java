package simulator.stats;

import java.util.List;

public class State {
  
  public enum Phase {
    PREFLOP, FLOP, TURN, RIVER
  };
  
  public enum Amount {
    LOW, MEDIUM, HIGH
  };
  
  public static final Phase[] PHASES = {Phase.PREFLOP, Phase.FLOP, Phase.TURN, Phase.RIVER};
  public static final Amount[] POT_AMOUNTS = {Amount.LOW, Amount.MEDIUM, Amount.HIGH};
  public static final Amount[] STACK_AMOUNTS = {Amount.LOW, Amount.MEDIUM, Amount.HIGH};
  
  private Amount stackPercent;
  private Amount potPercent;
  private Phase phase;
  
  public Amount getStackPercent() {
    return stackPercent;
  }
  
  public void setStackPercent(double stackPercent) {
    if (stackPercent < .15) {
      this.stackPercent = Amount.LOW;
    }
    else if (stackPercent < .3) {
      this.stackPercent = Amount.MEDIUM;
    }
    else {
      this.stackPercent = Amount.HIGH;
    }
  }
  
  public void setStackAmount(Amount stackPercent)
  {
	  this.stackPercent = stackPercent;
  }
  
  public Amount getPotPercent() {
    return potPercent;
  }
  
  public void setPotPercent(double potPercent) {
    if (potPercent > 1) {
      this.potPercent = Amount.HIGH;
    }
    else if (potPercent < .5) {
      this.potPercent = Amount.LOW;
    }
    else {
      this.potPercent = Amount.MEDIUM;
    }
  }
  
  public void setPotPercent(Amount potPercent)
  {
	  this.potPercent = potPercent;
  }
  
  public Phase getPhase() {
    return phase;
  }
  
  public void setPhase(Phase phase) {
    this.phase = phase;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj) {
    if (!(obj instanceof State)) {
      System.err.println("Invalid comparison");
      return false;
    }
    
    State s = (State) obj;
    
    return s.phase == phase && s.potPercent == potPercent 
                    && s.stackPercent == stackPercent;
  } 
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    return phase.hashCode() + potPercent.hashCode() + stackPercent.hashCode();
  }
  
  public int getClusterIndex() {
	int ix = 0;
	switch(phase)
	{
	case PREFLOP:
		break;
	case FLOP:
		ix += 9;
		break;
	case TURN:
		ix += 18;
		break;
	case RIVER:
		ix += 27;
		break;
	}
	switch (potPercent)
	{
	case LOW:
		break;
	case MEDIUM:
		ix += 3;
		break;
	case HIGH:
		ix += 6;
		break;
	}
	switch (stackPercent)
	{
	case LOW:
		break;
	case MEDIUM:
		ix += 1;
		break;
	case HIGH:
		ix += 2;
		break;
	}	
	return ix;
  }
}
