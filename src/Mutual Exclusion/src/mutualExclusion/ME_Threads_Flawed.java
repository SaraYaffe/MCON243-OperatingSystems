package mutualExclusion;
//flawed mutual exclusion

public class ME_Threads_Flawed extends Thread {
	private int thread;
	private int favoredThread;
	private boolean t1WantsToEnter;
	private boolean t2WantsToEnter;
	
	
	public ME_Threads_Flawed(int thread, int favoredThread, boolean t1WantsToEnter, boolean t2WantsToEnter)
	{
		this.thread = thread;
		this.favoredThread = favoredThread;
		this.t1WantsToEnter = t1WantsToEnter;
		this.t2WantsToEnter = t2WantsToEnter;
	}



	@Override
	public void run()
	{
		if(thread == 1)
		{
				for(int i = 0; i < 10; i++)
			{
				
				t1WantsToEnter = true;
				while (favoredThread == 2)
				{
					while (t2WantsToEnter);
					favoredThread = 1;
				}
				System.out.println(thread + " has entered its critical section");
				//critical section code
				try {
					sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(thread + " has exited its critical section");
				t1WantsToEnter = false;
				//non-critical code
			}
		}
		
		else
		{
			for(int i = 0; i < 10; i++)
			{
				t2WantsToEnter = true;
				while (favoredThread == 1)
				{
					while (t1WantsToEnter);
					try {
						sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					favoredThread = 2;
				}
				System.out.println(thread + " has entered its critical section");
				//critical section code
				System.out.println(thread + " has exited its critical section");
				t2WantsToEnter = false;
				//non-critical code
			}
		}
		
	}
	
}
