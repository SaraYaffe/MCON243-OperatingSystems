package mutualExclusion2;
//mutual exclusion achieved

public class Main {

	public static void main(String[] args) {
		int favoredThread = 1;
		boolean t1WantsToEnter = false;
		boolean t2WantsToEnter = false;
		
		Thread thread1 = new ME_Threads(1, favoredThread, t1WantsToEnter, t2WantsToEnter);
		Thread thread2 = new ME_Threads(2, favoredThread, t1WantsToEnter, t2WantsToEnter);
		
		
		thread2.start();
		thread1.start();
	}

}
