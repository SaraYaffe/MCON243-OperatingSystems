package dijkstrasSimulation;

import java.util.ArrayList;
import java.util.Random;

public class Program1 {

	final static int NUM_PROCS = 6; // How many concurrent processes
	final static int TOTAL_RESOURCES = 30; // Total resources in the system
	final static int MAX_PROC_RESOURCES = 13; // Highest amount of resources any process could need
	final static int ITERATIONS = 30; // How long to run the program
	static Random rand = new Random();

	public static void main(String[] args) {

		// The list of processes:
		ArrayList<Proc> processes = new ArrayList<Proc>();
		int totalHeldResources = 0;
		for (int i = 0; i < NUM_PROCS; i++) {
			Proc newProc = new Proc(MAX_PROC_RESOURCES - rand.nextInt(3));
			processes.add(newProc); // Initialize to a new Proc, with some small range for its max
			newProc.addResources(rand.nextInt(6));
			totalHeldResources += newProc.getHeldResources(); //initialize totalHeldResources
		}
		
		
		System.out.println("\n***** STATUS *****");
		System.out.println("Total Available: " + (TOTAL_RESOURCES - totalHeldResources));
		for (int k = 0; k < processes.size(); k++)
			System.out.println("Process " + k + " holds: " + processes.get(k).getHeldResources() + ", max: "
					+ processes.get(k).getMaxResources() + ", claim: "
					+ (processes.get(k).getMaxResources() - processes.get(k).getHeldResources()));
		System.out.println("***** STATUS *****\n");
		
		
		// Run the simulation:
		for (int i = 0; i < ITERATIONS; i++) {
			// loop through the processes and for each one get its request
			for (int j = 0; j < processes.size(); j++) {
				// Get the request
				int currRequest = processes.get(j).resourceRequest(TOTAL_RESOURCES - totalHeldResources);

				// just ignore processes that don't ask for resources
				if (currRequest == 0)
				{
					//System.out.println("no resources requested for process " + j + "\n");
					continue;
				}

				// Here you have to enter code to determine whether or not the request can be granted,
				// and then grant the request if possible. Remember to give output to the console
				// this indicates what the request is, and whether or not its granted.
				
				
				if (currRequest < 0)
				{
					totalHeldResources += currRequest;
					System.out.println("Process " + j + " finished, returned " + (-currRequest));
				}
				
				else {
					//copy of processes
					ArrayList<Proc> processesCopy = new ArrayList<Proc>();
					for (Proc process : processes) {
						processesCopy.add(process);
					}
					
					Boolean found = false;
					processes.get(j).addResources(currRequest);
					totalHeldResources += currRequest;
					int available = TOTAL_RESOURCES - totalHeldResources;
					
					while (!processesCopy.isEmpty()) {
						found = false;
						for (int p = 0; p < processesCopy.size(); p++) {
							if (processesCopy.get(p).getMaxResources()
									- processesCopy.get(p).getHeldResources() <= available) {
								available += processesCopy.get(p).getMaxResources();
								processesCopy.remove(p);
								found = true;
								

							}
						}
						
						if (!found) {
							processes.get(j).addResources(-currRequest);
							totalHeldResources -= currRequest;
							System.out.println("Process " + j + " requested " + currRequest + ", denied.");
							break;
						}
					}
					if(found)
					{
						System.out.println("Process " + j + " requested " + currRequest + ", granted.");
					}

				}

				// At the end of each iteration, give a summary of the current status:
				System.out.println("\n***** STATUS *****");
				System.out.println("Total Available: " + (TOTAL_RESOURCES - totalHeldResources));
				for (int k = 0; k < processes.size(); k++)
					System.out.println("Process " + k + " holds: " + processes.get(k).getHeldResources() + ", max: "
							+ processes.get(k).getMaxResources() + ", claim: "
							+ (processes.get(k).getMaxResources() - processes.get(k).getHeldResources())
							//+ (" " + processes.get(k).isRunning())
							);
				
				System.out.println("***** STATUS *****\n");

			}
		}

	}

}
