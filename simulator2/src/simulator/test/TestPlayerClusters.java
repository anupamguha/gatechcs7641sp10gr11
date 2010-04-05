package simulator.test;

import simulator.stats.PlayerClusters;
import simulator.stats.State;
import simulator.stats.State.Amount;
import simulator.stats.State.Phase;

public class TestPlayerClusters {
	
	public static void main(String[] args)
	{
		try
		{
			PlayerClusters.load("C:\\Users\\hartsoka\\Documents\\Classes\\CS 7641\\project\\trunk\\simulator2\\data\\players.txt");
			
			for (Phase p : State.PHASES)
			{
				for (Amount po : State.POT_AMOUNTS)
				{
					for (Amount s : State.STACK_AMOUNTS)
					{
						State state = new State();
						state.setPhase(p);
						state.setPotPercent(po);
						state.setStackAmount(s);
						
						System.out.println(PlayerClusters.getCluster("JaVa-D", state));
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
