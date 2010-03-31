package simulator.stats;

public class State {
  
  public enum Phase {
    PREFLOP, FLOP, TURN, RIVER
  };
  
  public enum Amount {
    LOW, MEDIUM, HIGH
  };
  
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
  

}
