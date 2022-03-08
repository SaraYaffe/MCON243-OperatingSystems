package contextSwitching;

import java.util.Random;

public class SimProcessor {
	private SimProcess currentProcess;
	private int reg1;
	private int reg2;
	private int reg3;
	private int reg4;
	private int currInstruction;
	private ProcessState currentState;
	
	
	
	public SimProcessor() 
	{
		this.currInstruction = 1;
	}
		
	
	
	public SimProcess getCurrentProcess() {
		return currentProcess;
	}
	public void setCurrentProcess(SimProcess currentProcess) {
		this.currentProcess = currentProcess;
	}
	public int getReg1Value() {
		return reg1;
	}
	public void setReg1Value(int reg1) {
		this.reg1 = reg1;
	}
	public int getReg2Value() {
		return reg2;
	}
	public void setReg2Value(int reg2) {
		this.reg2 = reg2;
	}
	public int getReg3Value() {
		return reg3;
	}
	public void setReg3Value(int reg3) {
		this.reg3 = reg3;
	}
	public int getReg4Value() {
		return reg4;
	}
	public void setReg4Value(int reg4) {
		this.reg4 = reg4;
	}
	public int getCurrInstruction() {
		return currInstruction;
	}
	public void setCurrInstruction(int currInstruction) {
		this.currInstruction = currInstruction;
	}
	
	
	public ProcessState getCurrentState() {
		return currentState;
	}



	public void setCurrentState(ProcessState currentState) {
		this.currentState = currentState;
	}



	/**
	 * @param pcb
	 */
	public void executeNextInstruction(ProcessControlBlock pcb)
	{
		Random rand = new Random();
		
		ProcessState state = this.currentProcess.execute(pcb.getCurrInstruction());
		
		
		++ this.currInstruction;
		
		setReg1Value(rand.nextInt());
		setReg2Value(rand.nextInt());
		setReg3Value(rand.nextInt());
		setReg4Value(rand.nextInt());
		
		this.currentState = state;
		
		
	}
}
