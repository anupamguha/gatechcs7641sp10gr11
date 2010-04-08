package simulator.stats;

import java.util.ArrayList;

public class Stats {
  
  private double numSituations;
  
  private double bets;
  private double raises;
  private double calls;
  private double folds;
  private double allIn;
  private double check;
  
  private ArrayList<Double> betAmount;
  private ArrayList<Double> foldAmount;
  
  public Stats() {
    betAmount = new ArrayList<Double>();
    foldAmount = new ArrayList<Double>();
    
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
  public void addRaise() {
    ++raises;
  }
  public void addCall() {
    ++calls;
  }
  public void addCheck() {
    ++check;
  }
  
  public void addBetAmt(int amt, int tableAmt) {
    betAmount.add((double)amt / tableAmt);
  }
  public void addFoldAmt(int amt, int tableAmt) {
    foldAmount.add((double)amt / tableAmt);
  }
  
  public ArrayList<Double> calculate() {
    ArrayList<Double> results = new ArrayList<Double>();
    
    // check percentage
    double checkPercentage = check / numSituations;
    results.add(checkPercentage);
    
    // bet percentage
    double betPercentage = bets / numSituations;
    results.add(betPercentage);
    
    // call percentage
    double callPercentage = calls / numSituations;
    results.add(callPercentage);
    
    // raise percentage
    double raisePercentage = raises / numSituations;
    results.add(raisePercentage);
    
    // calculate average raise amount
    if ((bets + calls + raises) == 0) {
      results.add(0.0);
      results.add(0.0);
    }
    else {
      double sum = 0;
    
      for (Double d : betAmount) {
        sum += d;
      }
    
      double betAvg = sum / (bets + calls + raises);
      results.add(betAvg);
    
    
      // calculate raise std dev
      double squares = 0;
    
      for (Double d : betAmount) {
        squares += Math.pow((d - betAvg), 2);
      }
    
      double raiseStdDev = Math.sqrt(squares / (bets + calls + raises));
      results.add(raiseStdDev);
    }
    
    // all in percentage
    double aiPercentage = allIn / numSituations;
    results.add(aiPercentage);
    
    // fold percentage
    double foldPercentage = folds / numSituations;
    results.add(foldPercentage);
    
    // average amount causing a fold
    if (folds == 0) { // if no folds, will cause / by 0
      results.add(0.0);
    }
    else {
      double sum = 0;
    
      for (Double d : foldAmount) {
        sum += d;
      }
    
      double foldAvg = sum / folds;
      results.add(foldAvg);
    }
    
    
    return results;
  }
  
  public double getNumSituations()
  {
	  return this.numSituations;
  }
  

}
