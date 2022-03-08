package contextSwitching;

import java.util.SplittableRandom;

public class SimProcess {
	private int pid;
	private String procName;
	private int totalInstructions;
	
	public SimProcess(int pid, String procName, int totalInstructions)
	{
		if(pid>0)
			this.pid = pid;
	
		if(procName != null)
			this.procName = procName;
			
		if(totalInstructions > 0)
			this.totalInstructions = totalInstructions;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		if(pid>0)
			this.pid = pid;
	}

	public String getProcName() {
		return procName;
	}

	public void setProcName(String procName) {
		if(procName != null)
			this.procName = procName;
	}

	public int getTotalInstructions() {
		return totalInstructions;
	}

	public void setTotalInstructions(int totalInstructions) {
		if(totalInstructions > 0)
			this.totalInstructions = totalInstructions;
	}
	
	
	/**
	 * @param i
	 * @return ProcessState
	 */
	public ProcessState execute(int i) {
		
		System.out.println("ID: " + this.getPid()  + "  Process Name: " + this.getProcName() + "  Executing Instruction: " + i);
		
		if (i >= this.totalInstructions) {
			return ProcessState.FINISHED;
		}
		
		else {
			SplittableRandom random = new SplittableRandom();
			Boolean decideState = random.nextInt(1, 101) <= 15;
			
			if(decideState)
				return ProcessState.BLOCKED;
			else
				return ProcessState.READY;
			
		}
		
		
		
		
	}

	@Override
	public String toString() {
		return "SimProcess [pid=" + pid + ", procName=" + procName + ", totalInstructions=" + totalInstructions + "]" + "\n";
		
	}
	
	

}
