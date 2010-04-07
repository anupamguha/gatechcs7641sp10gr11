package simulator.stats;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class PlayerClusters {
	
	public String name;
	public int[] clusters;
	
	public boolean valid;
	
	private static Map<String,PlayerClusters> data;
	
	public static void load(String filepath) throws IOException
	{
		data = new TreeMap<String, PlayerClusters>();
		
		FileReader fr = new FileReader(filepath);
		BufferedReader br = new BufferedReader(fr);
		
		String line = br.readLine();
		while (line != null)
		{
			PlayerClusters pc = new PlayerClusters(line);
			data.put(pc.name, pc);
			line = br.readLine();
		}
	}
	
	public PlayerClusters(String line)
	{
		clusters = new int[36];
		valid = true;
		
		String tokens[] = line.split("\t");
		if (tokens.length == 37)
		{
			name = tokens[0];
			for (int i = 0; i < 36; ++i)
			{
				if(tokens[i+1].equalsIgnoreCase("?"))
					clusters[i] = -1;
				else
					clusters[i] = Integer.parseInt(tokens[i+1]);
			}
		}
		else
		{
			valid = false;
		}
	}
	
	public static int getCluster(String name, State state)
	{
	  if (data.get(name) == null) {
	    return -1;
	  }
		return data.get(name).clusters[state.getClusterIndex()];
	}

}
