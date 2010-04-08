package simulator.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import simulator.game.Player;

public class Tracker {
	
  private static final int MIN_NUM_SITUATIONS = 10;
  
  private static Tracker instance = null;
  
  public static final int PLAYERS = 2;
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
    BufferedWriter agg = null;
    
    try {
      agg = new BufferedWriter(new FileWriter(directory + "agg.tab"));
      
      StringBuilder sb = new StringBuilder();
      
      // set up headers
      sb.append("Player\tPhase\tPot%\tStack%\tCheck%\tBet%\tCall%\tRaise%\tAvgBetAmt\tAvgBetStdDev\tAllIn%\tFold%\tAvgFoldAmt\n");
      sb.append("d\td\td\td\tc\tc\tc\tc\tc\tc\tc\tc\tc\n");
      sb.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\n");
      
      agg.write(sb.toString());
      
      for (Player p : stats.keySet()) {
    	  
        HashMap<State, Stats> sts = stats.get(p);
        
        for (State s : sts.keySet()) {
        	
        	Stats st = sts.get(s);
        	
        	if (st.getNumSituations() < MIN_NUM_SITUATIONS)
        		continue;
        	
          sb.delete(0, sb.length());
          
          sb.delete(0, sb.length());
      	  sb.append(p.getName());
      	  sb.append("\t");

          sb.append(s.getPhase()).append("\t").append(s.getPotPercent());
          sb.append("\t").append(s.getStackPercent());

          ArrayList<Double> results = st.calculate();

          for (Double d : results) {
        	DecimalFormat fourDecs = new DecimalFormat("#0.0000");
            sb.append("\t").append(fourDecs.format(d));
          }

          sb.append("\n");

          agg.write(sb.toString());
        }
      }

      agg.close();
    }
    catch(IOException ioe) {
      System.err.println("Error writing stats to file"); 
      System.err.println(ioe.getMessage());
    }
    
  }
  
  public void saveSeparate(String directory) {
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
      pll = new BufferedWriter(new FileWriter(directory + "pll.tab"));
      plm = new BufferedWriter(new FileWriter(directory + "plm.tab"));
      plh = new BufferedWriter(new FileWriter(directory + "plh.tab"));
      pml = new BufferedWriter(new FileWriter(directory + "pml.tab"));
      pmm = new BufferedWriter(new FileWriter(directory + "pmm.tab"));
      pmh = new BufferedWriter(new FileWriter(directory + "pmh.tab"));
      phl = new BufferedWriter(new FileWriter(directory + "phl.tab"));
      phm = new BufferedWriter(new FileWriter(directory + "phm.tab"));
      phh = new BufferedWriter(new FileWriter(directory + "phh.tab"));
      
      fll = new BufferedWriter(new FileWriter(directory + "fll.tab"));
      flm = new BufferedWriter(new FileWriter(directory + "flm.tab"));
      flh = new BufferedWriter(new FileWriter(directory + "flh.tab"));
      fml = new BufferedWriter(new FileWriter(directory + "fml.tab"));
      fmm = new BufferedWriter(new FileWriter(directory + "fmm.tab"));
      fmh = new BufferedWriter(new FileWriter(directory + "fmh.tab"));
      fhl = new BufferedWriter(new FileWriter(directory + "fhl.tab"));
      fhm = new BufferedWriter(new FileWriter(directory + "fhm.tab"));
      fhh = new BufferedWriter(new FileWriter(directory + "fhh.tab"));
      
      tll = new BufferedWriter(new FileWriter(directory + "tll.tab"));
      tlm = new BufferedWriter(new FileWriter(directory + "tlm.tab"));
      tlh = new BufferedWriter(new FileWriter(directory + "tlh.tab"));
      tml = new BufferedWriter(new FileWriter(directory + "tml.tab"));
      tmm = new BufferedWriter(new FileWriter(directory + "tmm.tab"));
      tmh = new BufferedWriter(new FileWriter(directory + "tmh.tab"));
      thl = new BufferedWriter(new FileWriter(directory + "thl.tab"));
      thm = new BufferedWriter(new FileWriter(directory + "thm.tab"));
      thh = new BufferedWriter(new FileWriter(directory + "thh.tab"));
      
      rll = new BufferedWriter(new FileWriter(directory + "rll.tab"));
      rlm = new BufferedWriter(new FileWriter(directory + "rlm.tab"));
      rlh = new BufferedWriter(new FileWriter(directory + "rlh.tab"));
      rml = new BufferedWriter(new FileWriter(directory + "rml.tab"));
      rmm = new BufferedWriter(new FileWriter(directory + "rmm.tab"));
      rmh = new BufferedWriter(new FileWriter(directory + "rmh.tab"));
      rhl = new BufferedWriter(new FileWriter(directory + "rhl.tab"));
      rhm = new BufferedWriter(new FileWriter(directory + "rhm.tab"));
      rhh = new BufferedWriter(new FileWriter(directory + "rhh.tab"));
      
      StringBuilder sb = new StringBuilder();
      
      // set up headers
      sb.append("Player\tPhase\tPot%\tStack%\tCheck%\tBet%\tCall%\tRaise%\tAvgBetAmt\tAvgBetStdDev\tAllIn%\tFold%\tAvgFoldAmt\n");
      sb.append("d\td\td\td\tc\tc\tc\tc\tc\tc\tc\tc\tc\n");
      sb.append("i\ti\ti\ti\t\t\t\t\t\t\t\t\t\t\n");
      
      pll.write(sb.toString());
      plm.write(sb.toString());
      plh.write(sb.toString());
      pml.write(sb.toString());
      pmm.write(sb.toString());
      pmh.write(sb.toString());
      phl.write(sb.toString());
      phm.write(sb.toString());
      phh.write(sb.toString());
      
      fll.write(sb.toString());
      flm.write(sb.toString());
      flh.write(sb.toString());
      fml.write(sb.toString());
      fmm.write(sb.toString());
      fmh.write(sb.toString());
      fhl.write(sb.toString());
      fhm.write(sb.toString());
      fhh.write(sb.toString());
      
      tll.write(sb.toString());
      tlm.write(sb.toString());
      tlh.write(sb.toString());
      tml.write(sb.toString());
      tmm.write(sb.toString());
      tmh.write(sb.toString());
      thl.write(sb.toString());
      thm.write(sb.toString());
      thh.write(sb.toString());
      
      rll.write(sb.toString());
      rlm.write(sb.toString());
      rlh.write(sb.toString());
      rml.write(sb.toString());
      rmm.write(sb.toString());
      rmh.write(sb.toString());
      rhl.write(sb.toString());
      rhm.write(sb.toString());
      rhh.write(sb.toString());
      
      for (Player p : stats.keySet()) {
    	  
        HashMap<State, Stats> sts = stats.get(p);
        
        for (State s : sts.keySet()) {
          sb.delete(0, sb.length());
          
          sb.delete(0, sb.length());
      	  sb.append(p.getName());
      	  sb.append("\t");

          sb.append(s.getPhase()).append("\t").append(s.getPotPercent());
          sb.append("\t").append(s.getStackPercent());

          Stats st = sts.get(s);

          ArrayList<Double> results = st.calculate();

          for (Double d : results) {
            sb.append("\t").append(d);
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
