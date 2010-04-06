package simulator.stats;

import java.text.DecimalFormat;
import java.util.ArrayList;

import simulator.game.Action;

public class Stats {
  
  public enum PHASE {
    PREFLOP, FLOP, TURN, RIVER
  };
  
  private String name;
  private int cluster;
  private int game;
  private int numPlayers;
  private PHASE phase;
  private boolean pay;
  private double avgRaise;
  private double potSize;
  private double potPercentage;
  private double lastPhasePercentage;
  private double stackPercentage;
  private double maxOppStackPercentage;
  private double oppStackPercentage;
  private int bets;
  private int raises;
  private int maxOppBets;
  
  private Action.ACTION action;
  
  
  DecimalFormat four = new DecimalFormat("#0.0000");
  
  public void setName(String name) {
    this.name = name;
  }
  public void setCluster(int cluster) {
    this.cluster = cluster;
  }
  public void setGame(int game) {
    this.game = game;
  }
  public void setNumPlayers(int numPlayers) {
    this.numPlayers = numPlayers;
  }
  public void setPhase(PHASE phase) {
    this.phase = phase;
  }
  public void setPay(boolean pay) {
    this.pay = pay;
  }
  public void setAvgRaise(double avgRaise) {
    this.avgRaise = avgRaise;
  }
  public void setPotSize(double potSize) {
    this.potSize = potSize;
  }
  public void setPotPercentage(double potPercentage) {
    this.potPercentage = potPercentage;
  }
  public void setLastPhasePercentage(double lastPhasePercentage) {
    this.lastPhasePercentage = lastPhasePercentage;
  }
  public void setStackPercentage(double stackPercentage) {
    this.stackPercentage = stackPercentage;
  }
  public void setMaxOppStackPercentage(double maxOppStackPercentage) {
    this.maxOppStackPercentage = maxOppStackPercentage;
  }
  public void setOppStackPercentage(double oppStackPercentage) {
    this.oppStackPercentage = oppStackPercentage;
  }
  public void setBets(int bets) {
    this.bets = bets;
  }
  public void setRaises(int raises) {
    this.raises = raises;
  }
  public void setMaxOppBets(int maxOppBets) {
    this.maxOppBets = maxOppBets;
  }
  public void setAction(Action.ACTION action) {
    this.action = action;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append(name).append("\t").append(cluster).append("\t").append(game).append("\t");
    sb.append(numPlayers).append("\t").append(phase.toString()).append("\t").append(pay).append("\t");
    sb.append(four.format(avgRaise)).append("\t").append(four.format(potSize)).append("\t").append(four.format(potPercentage)).append("\t");
    sb.append(four.format(lastPhasePercentage)).append("\t").append(four.format(stackPercentage)).append("\t");
    sb.append(four.format(maxOppStackPercentage)).append("\t").append(four.format(oppStackPercentage)).append("\t");
    sb.append(bets).append("\t").append(raises).append("\t").append(maxOppBets).append("\t");
    if (action != null) {
      sb.append(action.toString());
    }
    sb.append("\n");
    
    return sb.toString();
    
  }

}
