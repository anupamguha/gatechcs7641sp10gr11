package simulator.sim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import simulator.deck.Card;
import simulator.game.Game;
import simulator.game.Player;
import simulator.stats.PlayerClusters;
import simulator.stats.Tracker;

/**
 * This class reads in the input files, constructs the simulation, then runs it
 * 
 * @author christina
 */
public class Simulation {

  private static HashMap<String, String> hroster;
  private static HashMap<String, HashMap<String, String>> pdb;
  
  /**
   * Main program
   * 
   * @param args
   *   arguments to main
   */
  public static void main(String[] args) {
    // if invalid or user asked for help, print help message
    if (args.length < 2 || args[0].equals("-help") || args[0].equals("-h")) {
      usage();
      return;
    }
    
    // construct hash maps
    hroster = new HashMap<String, String>();
    pdb = new HashMap<String, HashMap<String, String>>();
    
    // make sure directory ends with a /
    String dir = args[1];
    if (!dir.endsWith("/")) {
      dir += "/";
    }
    
    boolean interactive = false;
    if (args.length > 2) {
      interactive = true;
    }
    
    // read the roster and player input files
    readInput(dir);
    
    boolean next = true;
    
    // iterate through game file, construct games and play them
    try {
      // load player clusters
      PlayerClusters.load("players.txt");
      
      BufferedReader br = new BufferedReader(new FileReader(new File(dir + "hdb")));
      BufferedReader isr = new BufferedReader(new InputStreamReader(System.in));
      String line = br.readLine();
      while (line != null && next == true) {
       Game g = constructGame(line);
       g.play();
       
       if (interactive) {
         System.out.print("Play next game? y/n ?> ");

         String s = isr.readLine();
       
         if (!s.contains("y")) {
           next = false;
         }
       }
       
       line = br.readLine();
      }
      
      Tracker tracker = Tracker.getInstance();
      tracker.save("stats.tab");
      
      br.close();
      isr.close();
    }
    catch (FileNotFoundException fnfe) {
      System.err.println("Cannot find file " + dir + "hdb");
      System.exit(1);
    }
    catch (IOException ioe) {
      System.err.println("Error reading from file " + dir + "hdb");
      System.exit(1);
    }

  }

  /**
   * Prints a message indicating how the code should be run
   */
  public static void usage() {
    System.out.println("Usage:  java Simulation <directory>");
    System.out.println("<directory>:  the directory in which the files can be found");
  }
  
  /**
   * Reads the player and roster input from the input files
   * 
   * @param dir
   *   the directory the files are located in
   */
  private static void readInput(String dir) {
    BufferedReader br = null;
    
    // read roster file
    
    try {
      br = new BufferedReader(new FileReader(new File(dir + "hroster")));
      String s = br.readLine();
      while (s != null) {
        int i = s.indexOf(' ');
        hroster.put(s.substring(0, i).trim(), s.substring(i).trim());
        s = br.readLine();
      }
      br.close();
    }
    catch (FileNotFoundException fnfe) {
      System.err.println("Cannot read file " + dir + "hroster -- file does not exist");
      System.exit(1);
    }
    catch (IOException ioe) {
      System.err.println("Error reading from file " + dir + "hroster");
      System.exit(1);
    }
    
    // read in player info
    
    File d = new File(dir + "pdb");
    String[] files = d.list();
    if (files == null) { // error
      System.err.println("Cannot get file list from " + dir + "pdb directory");
      System.exit(1);
    }
    
    try {    
      for (String s : files) {
        br = new BufferedReader(new FileReader(new File(dir + "pdb/" + s)));
        HashMap<String, String> map = new HashMap<String, String>();
        String key = s.substring(s.indexOf('.') + 1);
        String line = br.readLine();
        while (line != null) {
          if (line.indexOf(' ') == -1) {
            line = br.readLine();
            continue;
          }
          line = line.substring(line.indexOf(' ')).trim(); // pull off user name
          int i = line.indexOf(' '); // this space separates game timestamp from actions
          map.put(line.substring(0, i).trim(), line.substring(i).trim()); // add to hash map
          line = br.readLine();
        }
        pdb.put(key, map); // add map to player hash
      }
      br.close();
    }
    catch (FileNotFoundException fnfe) {
      System.err.println(fnfe.getMessage());
      System.exit(1);
    }
    catch (IOException ioe) {
      System.err.println(ioe.getMessage());
      System.exit(1);
    }
  }
  
  /**
   * Constructs a game based on the information in the passed in String
   *  
   * @param line
   *   String containing information about the game to construct
   *   
   * @return
   *   the created game object, ready to play
   */
  private static Game constructGame(String line) {
    Game game = new Game();
    
    String[] arr = line.split("\\s+");
    String timestamp = arr[0];
    game.setTimestamp(Integer.parseInt(timestamp));
    
    Card[] cards = null;
    
    // check if there are community cards
    if (arr.length > 8) {
      cards = new Card[arr.length - 8];
      for (int i = 0; i + 8 < arr.length; i++) {
        Card c = new Card(arr[i + 8]);
        cards[i] = c;
      }
    }
    
    game.setCards(cards);
    
    // get pot sizes
    game.setFlopPot(Integer.parseInt(arr[4].substring(arr[4].indexOf('/') + 1)));
    game.setTurnPot(Integer.parseInt(arr[5].substring(arr[5].indexOf('/') + 1)));
    game.setRiverPot(Integer.parseInt(arr[6].substring(arr[6].indexOf('/') + 1)));
    game.setShowdownPot(Integer.parseInt(arr[7].substring(arr[7].indexOf('/') + 1)));

    // need to find the players now and create them and their actions
    
    String roster = hroster.get(timestamp);
    
    if (roster == null) { // this is a problem
      System.err.println("No roster for game " + timestamp);
      return null;
    }
    
    arr = roster.split("\\s+");
    
    for (int i = 1; i < arr.length; i++ ) { // 1st index is number of players
      Player p = new Player();
      p.setName(arr[i]);
      String acts = pdb.get(arr[i]).get(timestamp);
      String[] s = acts.split("\\s+");
      p.setLocation(Integer.parseInt(s[1]));
      p.setPreflop(s[2]);
      p.setFlop(s[3]);
      p.setTurn(s[4]);
      p.setRiver(s[5]);
      p.setBankroll(Integer.parseInt(s[6]));
      p.setChipsPlayed(Integer.parseInt(s[7]));
      if (Integer.parseInt(s[8]) != 0) {
        p.setAsWinner();
      }
      
      if (s.length > 9) { // hole cards are given
        String[] hand = {s[9], s[10]};
        p.setHand(hand);
      }
      
      game.addPlayer(p); // add player to game
    }
    
    return game;
  }
}
