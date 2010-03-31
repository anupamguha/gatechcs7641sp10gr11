package simulator.stats;

import java.util.ArrayList;

public class Stats {
  
  private double numSituations;
  
  private double bets;
  private double folds;
  private double allIn;
  
  private ArrayList<Integer> betAmount;
  private ArrayList<Integer> foldAmount;
  
  public Stats() {
    betAmount = new ArrayList<Integer>();
    foldAmount = new ArrayList<Integer>();
    
    numSituations = 0;
    
    bets = 0;
    folds = 0;
    allIn = 0;
  }
  
  public void addSituation() {
    ++numSituations;
  }
  public void addBet() {
    ++bets;
  }
  public void addFold() {
    ++folds;
  }
  public void addAllIn() {
    ++allIn;
  }
  
  public void addBetAmt(int amt) {
    betAmount.add(amt);
  }
  public void addFoldAmt(int amt) {
    foldAmount.add(amt);
  }
  
  public ArrayList<Double> calculate() {
    ArrayList<Double> results = new ArrayList<Double>();
    
    // bet percentage
    double betPercentage = bets / numSituations;
    results.add(betPercentage);
    
    // calculate average raise amount
    double sum = 0;
    
    for (Integer i : betAmount) {
      sum += i;
    }
    
    double betAvg = sum / bets;
    results.add(betAvg);
    
    // calculate raise std dev
    double squares = 0;
    
    for (Integer i : betAmount) {
      squares += Math.pow((i - betAvg), 2);
    }
    
    double raiseStdDev = Math.sqrt(squares / bets);
    results.add(raiseStdDev);
    
    // all in percentage
    double aiPercentage = allIn / numSituations;
    results.add(aiPercentage);
    
    // fold percentage
    double foldPercentage = folds / numSituations;
    results.add(foldPercentage);
    
    // average amount causing a fold
    sum = 0;
    
    for (Integer i : foldAmount) {
      sum += i;
    }
    
    double foldAvg = sum / folds;
    results.add(foldAvg);
    
    
    return results;
  }
  

}
