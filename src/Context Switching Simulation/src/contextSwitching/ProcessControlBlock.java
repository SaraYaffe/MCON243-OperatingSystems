package contextSwitching;

import java.util.Random;

public class ProcessControlBlock {
	SimProcess simProcess;
	private int currInstruction;
	private int register1;
	private int register2;
	private int register3;
	private int register4;
	
	public ProcessControlBlock(SimProcess simProcess) {
		
		this.simProcess = simProcess;
		this.currInstruction = 1;
		
		Random rand = new Random();
		setRegister1(rand.nextInt());
		setRegister2(rand.nextInt());
		setRegister3(rand.nextInt());
		setRegister4(rand.nextInt());
	}

	public int getCurrInstruction() {
		return currInstruction;
	}

	public void setCurrInstruction(int currInstruction) {
		this.currInstruction = currInstruction;
	}

	public int getRegister1() {
		return register1;
	}

	public void setRegister1(int register) {
		register1 = register;
	}

	public int getRegister2() {
		return register2;
	}

	public void setRegister2(int register) {
		register2 = register;
	}

	public int getRegister3() {
		return register3;
	}

	public void setRegister3(int register) {
		register3 = register;
	}

	public int getRegister4() {
		return register4;
	}

	public void setRegister4(int register) {
		register4 = register;
	}

	public SimProcess getSimProcess() {
		return simProcess;
	}
	
	
	

}
