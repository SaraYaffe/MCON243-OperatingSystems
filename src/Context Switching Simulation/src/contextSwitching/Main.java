package contextSwitching;

import java.util.ArrayList;
import java.util.SplittableRandom;

/**
 * @author Sara Yaffe
 * 
 * Main Class creates 10 processes processed by a Processor implemented with context switching
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimProcessor processor = new SimProcessor();

		SimProcess proc1 = new SimProcess(1, "Process1", 106);
		ProcessControlBlock pcb1 = new ProcessControlBlock(proc1);

		SimProcess proc2 = new SimProcess(2, "Process2", 200);
		ProcessControlBlock pcb2 = new ProcessControlBlock(proc2);

		SimProcess proc3 = new SimProcess(3, "Process3", 108);
		ProcessControlBlock pcb3 = new ProcessControlBlock(proc3);

		SimProcess proc4 = new SimProcess(4, "Process4", 120);
		ProcessControlBlock pcb4 = new ProcessControlBlock(proc4);

		SimProcess proc5 = new SimProcess(5, "Process5", 350);
		ProcessControlBlock pcb5 = new ProcessControlBlock(proc5);

		SimProcess proc6 = new SimProcess(6, "Process6", 115);
		ProcessControlBlock pcb6 = new ProcessControlBlock(proc6);

		SimProcess proc7 = new SimProcess(7, "Process7", 398);
		ProcessControlBlock pcb7 = new ProcessControlBlock(proc7);

		SimProcess proc8 = new SimProcess(8, "Process8", 340);
		ProcessControlBlock pcb8 = new ProcessControlBlock(proc8);

		SimProcess proc9 = new SimProcess(9, "Process9", 400);
		ProcessControlBlock pcb9 = new ProcessControlBlock(proc9);

		SimProcess proc10 = new SimProcess(10, "Process10", 250);
		ProcessControlBlock pcb10 = new ProcessControlBlock(proc10);

		ArrayList<ProcessControlBlock> readyProcesses = new ArrayList<ProcessControlBlock>();
		ArrayList<ProcessControlBlock> blockedProcesses = new ArrayList<ProcessControlBlock>();

		readyProcesses.add(pcb1);
		readyProcesses.add(pcb2);
		readyProcesses.add(pcb3);
		readyProcesses.add(pcb4);
		readyProcesses.add(pcb5);
		readyProcesses.add(pcb6);
		readyProcesses.add(pcb7);
		readyProcesses.add(pcb8);
		readyProcesses.add(pcb9);
		readyProcesses.add(pcb10);

		Boolean isContextSwitchRestore = false;
		Boolean isContextSwitchSave = false;

		ProcessControlBlock saveOrRestore = null;
		ProcessControlBlock currPcb;
		
		int counter = 0;
		int index = 0;
		
		final int QUANTUM = 5;
		

		// 3,000 steps
		for (int i = 1; i <= 4001; ++i) {

			// tested with and without this step since will not be common for processor to
			// need to idle if run this after every step
			wakeUpBlockedProcesses(blockedProcesses, readyProcesses);
			ifNoReadyProcess(blockedProcesses, readyProcesses, index);
			currPcb = readyProcesses.get(index);

			if (isContextSwitchSave) {
				contextSwitchSave(saveOrRestore, processor, i);
				isContextSwitchSave = false;
				counter = 0;

				if (index + 1 >= readyProcesses.size()) {
					index = 0;
				}

				
				ProcessControlBlock nextPcb = readyProcesses.get(index);
				if (nextPcb.getCurrInstruction() > 1) {
					isContextSwitchRestore = true;
				} else {
					isContextSwitchRestore = false;
				}

			}

			else if (isContextSwitchRestore) {
				index++;
				if (index >= readyProcesses.size()) {
					index = 0;
				}
				currPcb = readyProcesses.get(index);
				contextSwitchRestore(currPcb, processor, i);
				isContextSwitchRestore = false;
				counter = 0;

			}

			else {
				counter++;
				System.out.println("\nStep " + i + ":  ");

				processor.setCurrentProcess(currPcb.simProcess);
				processor.executeNextInstruction(currPcb);
				processor.setCurrInstruction(currPcb.getCurrInstruction());
				currPcb.setCurrInstruction(processor.getCurrInstruction() + 1);

				if (processor.getCurrentState() == ProcessState.FINISHED) {
					System.out.println("----Process Completed----");
					readyProcesses.remove(currPcb);
					counter = 0;

					if (index >= readyProcesses.size()) {
						index = 0;
					}

					if (readyProcesses.isEmpty() && blockedProcesses.isEmpty()) {
						System.out.println("All Processes Completed!");
						System.exit(0);
					} else {
						ifNoReadyProcess(blockedProcesses, readyProcesses, index);

						ProcessControlBlock nextPcb = readyProcesses.get(index);
						if (nextPcb.getCurrInstruction() > 1) {
							isContextSwitchRestore = true;

						} else {
							isContextSwitchRestore = false;
						}
					}

				}

				if (processor.getCurrentState() == ProcessState.BLOCKED) {
					System.out.println("----Process Blocked----");
					saveOrRestore = currPcb; 
					readyProcesses.remove(currPcb);
					blockedProcesses.add(currPcb);

					isContextSwitchSave = true;
					counter = 0;

					ifNoReadyProcess(blockedProcesses, readyProcesses, index);

					if (index >= readyProcesses.size()) {
						index = 0;
					}

					ProcessControlBlock nextPcb = readyProcesses.get(index);
					if (nextPcb.getCurrInstruction() != 1) {
						isContextSwitchRestore = true;

					} else {
						isContextSwitchRestore = false;
					}

				}

				else if (counter == QUANTUM) {
					System.out.println("----Quantum Expired----");
					isContextSwitchSave = true;
					saveOrRestore = currPcb;
					counter = 0;
					if (index + 1 >= readyProcesses.size()) {
						index = 0;
					} else {
						index++;
					}

				}

			}

		}
	}

	/**
	 * while readyProcesses list is empty, call wakeUpBlockedProcesses method
	 * 
	 * @param blockedProcesses
	 * @param readyProcesses
	 * @param index
	 */
	public static void ifNoReadyProcess(ArrayList<ProcessControlBlock> blockedProcesses,
			ArrayList<ProcessControlBlock> readyProcesses, int index) {
		while (readyProcesses.size() == 0) {
			System.out.println("Processor is idling");
			wakeUpBlockedProcesses(blockedProcesses, readyProcesses);
			index = 0;
		}
	}

	/**
	 * method wakes up blocked processes with 30% probability and returns nothing
	 * 
	 * @param blockedProcesses
	 * @param readyProcesses
	 */
	public static void wakeUpBlockedProcesses(ArrayList<ProcessControlBlock> blockedProcesses,
			ArrayList<ProcessControlBlock> readyProcesses) {

		Boolean makeReady;

		if (!blockedProcesses.isEmpty()) {
			for (int i = 0; i < blockedProcesses.size(); i++) {
				SplittableRandom rand = new SplittableRandom();
				makeReady = rand.nextInt(1, 101) <= 30;

				if (makeReady) {
					ProcessControlBlock process = blockedProcesses.get(i);
					readyProcesses.add(process);
					blockedProcesses.remove(process);
					--i;
					System.out.println("\n *****Woke up " + process.simProcess.getProcName() + "*****");

				}

			}
		}
	}

	/**
	 * method saves process that has been blocked, finished, or whose quantum
	 * expired
	 * 
	 * @param currPcb
	 * @param processor
	 * @param i         - step number
	 */
	public static void contextSwitchSave(ProcessControlBlock currPcb, SimProcessor processor, int i) {
		System.out.println("\nStep " + i + ":  Context Switch - saving " + currPcb.simProcess.getProcName());
		System.out.print("Instruction: " + currPcb.getCurrInstruction());
		System.out.print("    R1: " + currPcb.getRegister1() + "  R2: " + currPcb.getRegister2() + "  R3: "
				+ currPcb.getRegister3() + "  R4: " + currPcb.getRegister4());
		System.out.println();
	}

	/**
	 * method restores the next process to be processed after a contextSwitchSave()
	 * method has been called
	 * 
	 * @param currPcb
	 * @param processor
	 * @param i         - step number
	 */
	public static void contextSwitchRestore(ProcessControlBlock currPcb, SimProcessor processor, int i) {
		if (currPcb.getCurrInstruction() != 1) {
			System.out.println("\nStep " + i + ":  Context Switch - restoring " + currPcb.simProcess.getProcName());
			System.out.print("Instruction: " + currPcb.getCurrInstruction());
			System.out.print("    R1: " + currPcb.getRegister1() + "  R2: " + currPcb.getRegister2() + "  R3: "
					+ currPcb.getRegister3() + "  R4: " + currPcb.getRegister4());
			System.out.println();
		} else {
			--i;
		}
	}

}
