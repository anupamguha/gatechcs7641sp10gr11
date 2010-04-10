package simulator.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import simulator.game.Player;
import simulator.stats.Stats.PHASE;

public class Tracker {
  
  private static Tracker instance = null;
  
  private ArrayList<Stats> stats;
  
  private Tracker() {
    stats = new ArrayList<Stats>();
  }
  
  public static Tracker getInstance() {
    if (instance == null) {
      instance = new Tracker();
    }
    return instance;
  }

  public void addStat(Stats t) {
    stats.add(t);
  }
  
  public void save(String filename) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
      
      StringBuilder sb = new StringBuilder();
      
      sb.append("Player").append("\t").append("Cluster").append("\t").append("GameId").append("\t");
      sb.append("NumPlayers").append("\t").append("Phase").append("\t").append("Pay?").append("\t");
      sb.append("AvgRaise").append("\t").append("PotSize").append("\t").append("Pot%").append("\t");
      sb.append("LastPhase%").append("\t").append("Stack%").append("\t").append("MaxOppStack%").append("\t");
      sb.append("OppsStack%").append("\t").append("Bets").append("\t").append("Raises").append("\t");
      sb.append("MaxOppBets").append("\t").append("FoldAmt").append("\t").append("Action").append("\n");
      
      bw.write(sb.toString());
      
      // clear builder
      sb.delete(0, sb.length() - 1);
      
      sb.append("d").append("\t").append("d").append("\t").append("d").append("\t");
      sb.append("d").append("\t").append("d").append("\t").append("d").append("\t");
      sb.append("c").append("\t").append("c").append("\t").append("c").append("\t");
      sb.append("c").append("\t").append("c").append("\t").append("c").append("\t");
      sb.append("c").append("\t").append("c").append("\t").append("c").append("\t");
      sb.append("c").append("\t").append("c").append("\t").append("d").append("\n");
      
      bw.write(sb.toString());
      
      sb.delete(0, sb.length() - 1);
      
      sb.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("\t");
      sb.append("\t").append("\t").append("\t").append("\t").append("\t").append("\t").append("\t");
      sb.append("\t").append("\t").append("\t").append("class").append("\n");
      
      // write stats to file
      
      for (Stats s : stats) {
    	  if (s.getNumPlayers() == 2)
    	  {
    	  bw.write(s.toString());
    	  }
    	  }

 /*     bw.write(sb.toString());
      
      for (Stats s : stats) {
        bw.write(s.toString());
      }
   */   
      bw.close();
      
    }
    catch(IOException ioe) {
      System.err.println("Error writing stats to file"); 
      System.err.println(ioe.getMessage());
    }
    
  }
  
  
  
  

}
