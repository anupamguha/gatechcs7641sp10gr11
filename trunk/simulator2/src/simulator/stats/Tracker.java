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
  
  public void save(String directory) {
    BufferedWriter pll = null;
    BufferedWriter plm = null;
    BufferedWriter plh = null;
    BufferedWriter pml = null;
    BufferedWriter pmm = null;
    BufferedWriter pmh = null;
    BufferedWriter phl = null;
    BufferedWriter phm = null;
    BufferedWriter phh = null;
    
    BufferedWriter fll = null;
    BufferedWriter flm = null;
    BufferedWriter flh = null;
    BufferedWriter fml = null;
    BufferedWriter fmm = null;
    BufferedWriter fmh = null;
    BufferedWriter fhl = null;
    BufferedWriter fhm = null;
    BufferedWriter fhh = null;
    
    BufferedWriter tll = null;
    BufferedWriter tlm = null;
    BufferedWriter tlh = null;
    BufferedWriter tml = null;
    BufferedWriter tmm = null;
    BufferedWriter tmh = null;
    BufferedWriter thl = null;
    BufferedWriter thm = null;
    BufferedWriter thh = null;
    
    BufferedWriter rll = null;
    BufferedWriter rlm = null;
    BufferedWriter rlh = null;
    BufferedWriter rml = null;
    BufferedWriter rmm = null;
    BufferedWriter rmh = null;
    BufferedWriter rhl = null;
    BufferedWriter rhm = null;
    BufferedWriter rhh = null;
    
    try {
      pll = new BufferedWriter(new FileWriter(directory + "pll.csv"));
      plm = new BufferedWriter(new FileWriter(directory + "plm.csv"));
      plh = new BufferedWriter(new FileWriter(directory + "plh.csv"));
      pml = new BufferedWriter(new FileWriter(directory + "pml.csv"));
      pmm = new BufferedWriter(new FileWriter(directory + "pmm.csv"));
      pmh = new BufferedWriter(new FileWriter(directory + "pmh.csv"));
      phl = new BufferedWriter(new FileWriter(directory + "phl.csv"));
      phm = new BufferedWriter(new FileWriter(directory + "phm.csv"));
      phh = new BufferedWriter(new FileWriter(directory + "phh.csv"));
      
      fll = new BufferedWriter(new FileWriter(directory + "fll.csv"));
      flm = new BufferedWriter(new FileWriter(directory + "flm.csv"));
      flh = new BufferedWriter(new FileWriter(directory + "flh.csv"));
      fml = new BufferedWriter(new FileWriter(directory + "fml.csv"));
      fmm = new BufferedWriter(new FileWriter(directory + "fmm.csv"));
      fmh = new BufferedWriter(new FileWriter(directory + "fmh.csv"));
      fhl = new BufferedWriter(new FileWriter(directory + "fhl.csv"));
      fhm = new BufferedWriter(new FileWriter(directory + "fhm.csv"));
      fhh = new BufferedWriter(new FileWriter(directory + "fhh.csv"));
      
      tll = new BufferedWriter(new FileWriter(directory + "tll.csv"));
      tlm = new BufferedWriter(new FileWriter(directory + "tlm.csv"));
      tlh = new BufferedWriter(new FileWriter(directory + "tlh.csv"));
      tml = new BufferedWriter(new FileWriter(directory + "tml.csv"));
      tmm = new BufferedWriter(new FileWriter(directory + "tmm.csv"));
      tmh = new BufferedWriter(new FileWriter(directory + "tmh.csv"));
      thl = new BufferedWriter(new FileWriter(directory + "thl.csv"));
      thm = new BufferedWriter(new FileWriter(directory + "thm.csv"));
      thh = new BufferedWriter(new FileWriter(directory + "thh.csv"));
      
      rll = new BufferedWriter(new FileWriter(directory + "rll.csv"));
      rlm = new BufferedWriter(new FileWriter(directory + "rlm.csv"));
      rlh = new BufferedWriter(new FileWriter(directory + "rlh.csv"));
      rml = new BufferedWriter(new FileWriter(directory + "rml.csv"));
      rmm = new BufferedWriter(new FileWriter(directory + "rmm.csv"));
      rmh = new BufferedWriter(new FileWriter(directory + "rmh.csv"));
      rhl = new BufferedWriter(new FileWriter(directory + "rhl.csv"));
      rhm = new BufferedWriter(new FileWriter(directory + "rhm.csv"));
      rhh = new BufferedWriter(new FileWriter(directory + "rhh.csv"));
      
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

          if (s.getPhase().equals(State.Phase.PREFLOP)) {
            if (s.getPotPercent().equals(State.Amount.LOW)) {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                pll.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                plm.write(sb.toString());
              }
              else {
                plh.write(sb.toString());
              }
            }
            else if (s.getPotPercent().equals(State.Amount.MEDIUM)) {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                pml.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                pmm.write(sb.toString());
              }
              else {
                pmh.write(sb.toString());
              }
            }
            else {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                phl.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                phm.write(sb.toString());
              }
              else {
                phh.write(sb.toString());
              }
            }
          }
          
          else if (s.getPhase().equals(State.Phase.FLOP)) {
            if (s.getPotPercent().equals(State.Amount.LOW)) {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                fll.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                flm.write(sb.toString());
              }
              else {
                flh.write(sb.toString());
              }
            }
            else if (s.getPotPercent().equals(State.Amount.MEDIUM)) {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                fml.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                fmm.write(sb.toString());
              }
              else {
                fmh.write(sb.toString());
              }
            }
            else {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                fhl.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                fhm.write(sb.toString());
              }
              else {
                fhh.write(sb.toString());
              }
            }
          }
          
          else if (s.getPhase().equals(State.Phase.TURN)) {
            if (s.getPotPercent().equals(State.Amount.LOW)) {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                tll.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                tlm.write(sb.toString());
              }
              else {
                tlh.write(sb.toString());
              }
            }
            else if (s.getPotPercent().equals(State.Amount.MEDIUM)) {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                tml.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                tmm.write(sb.toString());
              }
              else {
                tmh.write(sb.toString());
              }
            }
            else {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                thl.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                thm.write(sb.toString());
              }
              else {
                thh.write(sb.toString());
              }
            }
          }
          
          else {
            if (s.getPotPercent().equals(State.Amount.LOW)) {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                rll.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                rlm.write(sb.toString());
              }
              else {
                rlh.write(sb.toString());
              }
            }
            else if (s.getPotPercent().equals(State.Amount.MEDIUM)) {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                rml.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                rmm.write(sb.toString());
              }
              else {
                rmh.write(sb.toString());
              }
            }
            else {
              if (s.getStackPercent().equals(State.Amount.LOW)) {
                rhl.write(sb.toString());
              }
              else if (s.getStackPercent().equals(State.Amount.MEDIUM)) {
                rhm.write(sb.toString());
              }
              else {
                rhh.write(sb.toString());
              }
            }
          }
        }
      }

      pll.close();
      plm.close();
      plh.close();
      pml.close();
      pmm.close();
      pmh.close();
      phl.close();
      phm.close();
      phh.close();
      
      fll.close();
      flm.close();
      flh.close();
      fml.close();
      fmm.close();
      fmh.close();
      fhl.close();
      fhm.close();
      fhh.close();
      
      tll.close();
      tlm.close();
      tlh.close();
      tml.close();
      tmm.close();
      tmh.close();
      thl.close();
      thm.close();
      thh.close();
      
      rll.close();
      rlm.close();
      rlh.close();
      rml.close();
      rmm.close();
      rmh.close();
      rhl.close();
      rhm.close();
      rhh.close();  
    }
    catch(IOException ioe) {
      System.err.println("Error writing stats to file"); 
      System.err.println(ioe.getMessage());
    }
    
  }
  
  
  
  

}
