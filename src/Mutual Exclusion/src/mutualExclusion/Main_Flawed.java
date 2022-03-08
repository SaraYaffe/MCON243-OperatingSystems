package mutualExclusion;
//flawed mutual exclusion

public class Main_Flawed {

	public static void main(String[] args) {
		int favoredThread = 1;
		boolean t1WantsToEnter = false;
		boolean t2WantsToEnter = false;
		
		//mutual exclusion fails
		Thread t1 = new ME_Threads_Flawed(1, favoredThread, t1WantsToEnter, t2WantsToEnter);
		Thread t2 = new ME_Threads_Flawed(2, favoredThread, t1WantsToEnter, t2WantsToEnter);

		t2.start();
		t1.start();
	
		
		
	}

}
