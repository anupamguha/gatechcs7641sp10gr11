package simulator.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import simulator.game.Player;

public class Tracker {
  
  private static Tracker instance = null;
  
  public static final int PLAYERS = 4;
  private HashMap<Player, HashMap<State, Stats>> stats;
  
  private Tracker() {
    stats = new HashMap<Player, HashMap<State, Stats>>();
  }
  
  public static Tracker getInstance() {
    if (instance == null) {
      instance = new Tracker();
    }
    return instance;
  }
  
  public Stats getStats(Player p, State s) {
    Stats st = null;
    
    if (stats.containsKey(p)) {
      st = stats.get(p).get(s);
    }
    
    if (st == null) {
      st = new Stats();
    }
    return st;
  }
  
  public void addMapping(Player p, State s, Stats t) {
    HashMap<State, Stats> sts = stats.get(p);
    
    if (sts == null) {
      sts = new HashMap<State, Stats>();
    }
    
    sts.put(s, t);
    
    stats.put(p, sts);
  }
  
  public void save(String filename) {
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(filename));
      
      StringBuilder sb = new StringBuilder();
      for (Player p : stats.keySet()) {
        HashMap<State, Stats> sts = stats.get(p);
        for (State s : sts.keySet()) {
          sb.delete(0, sb.length());

          sb.append(s.getPhase()).append(",").append(s.getPotPercent());
          sb.append(",").append(s.getStackPercent());

          Stats st = sts.get(s);

          ArrayList<Double> results = st.calculate();

          for (Double d : results) {
            sb.append(",").append(d);
          }

          sb.append("\n");

          bw.write(sb.toString());
        }
      }

      bw.close();
      
    }
    catch(IOException ioe) {
      System.err.println("Error writing stats to file " + filename); 
      System.err.println(ioe.getMessage());
    }
    
  }
  
  
  
  

}
