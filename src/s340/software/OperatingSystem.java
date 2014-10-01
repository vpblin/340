package s340.software;

import s340.hardware.IInterruptHandler;
import s340.hardware.ISystemCallHandler;
import s340.hardware.ITrapHandler;
import s340.hardware.Machine;
import s340.hardware.Trap;
import s340.hardware.exception.MemoryFault;

/*
 * The operating system that controls the software running on the S340 CPU.
 *
 * The operating system acts as an interrupt handler, a system call handler, and
 * a trap handler.
 */

public class OperatingSystem implements IInterruptHandler, ISystemCallHandler, ITrapHandler
{
	// the machine on which we are running.

	private final Machine machine;

	// the currently running program

	private Program currentProgram;
	
	private final int MAX_NUM_PROCESS = 4;
	
	private ProcessControlBlock[] process_table;
	
	private int running_process = -1;
	
	private Program p1;
	

	/*
	 * Create an operating system on the given machine.
	 */

	public OperatingSystem(Machine machine) throws MemoryFault
	{
		this.machine = machine;
		currentProgram = null;
		// array of process control blocks of size 4
		
		process_table = new ProcessControlBlock[MAX_NUM_PROCESS];
		
		ProgramBuilder wait = new ProgramBuilder();
		wait.start(0);
		wait.jmp(0);
		p1 = wait.build();
		loadProgram(p1);
	}

	/*
	 * Load a program into a given memory address
	 */

	private int loadProgram(Program program) throws MemoryFault
	{
		int address = program.getStart();
		
		for (int i : program.getCode())
		{
			machine.memory.store(address++, i);
		}

		return address + program.getDataSize();
	}

	/*
	 * Scheduled a list of programs to be run.
	 * 
	 * 
	 * @param programs the programs to schedule
	 */

	public void schedule(Program... programs) throws MemoryFault
	{
		int i = 0;
		for (Program program : programs)
		{
			loadProgram(program);
			process_table[i] = new ProcessControlBlock(0, program.getStart(), 0, ProcessState.READY);
			i++;
		}

		// leave this as the last line
		machine.cpu.runProg = true;
	}
	
	// Uses Round Robin method to select new process
	private void runNextProcess()
	{
		
		for(int i = running_process+1; i < MAX_NUM_PROCESS; i++)
		{
			
			if(process_table[i] != null && process_table[i].getStatus() == ProcessState.READY)
			{
				process_table[i].setStatus(ProcessState.RUNNING);
				running_process = i;
				return;
			}
		}
		
		for(int i = 0; i < MAX_NUM_PROCESS; i++)
		{
			
			if(process_table[i] != null && process_table[i].getStatus() == ProcessState.READY)
			{
				process_table[i].setStatus(ProcessState.RUNNING);
				running_process = i;
				return;
			}
		}
		
		running_process = -1;
	
		
	}
	
	// set the Acc, X, and PC registers from the PCB
	private void setNextProcess()
	{
		if(running_process >= 0)
		{
			machine.cpu.acc = process_table[running_process].getAcc();
			machine.cpu.x = process_table[running_process].getX();
			machine.cpu.setPc(process_table[running_process].getPc());
		}
		else
		{
			machine.cpu.setPc(0);
		}
	}

	/*
	 * Handle a trap from the hardware.
	 * 
	 * @param programCounter -- the program counter of the instruction after the
	 * one that caused the trap.
	 * 
	 * @param trapNumber -- the trap number for this trap.
	 */

	@Override
	public void trap(int savedProgramCounter, int trapNumber)
	{
		CheckValid.trapNumber(trapNumber);
		if (!machine.cpu.runProg)
		{
			return;
		}

		switch (trapNumber)
		{
			case Trap.TIMER:
				if(running_process == -1)
				{
					runNextProcess();
					setNextProcess();
				}
				else if(running_process >= 0 && process_table[running_process].getStatus() == ProcessState.RUNNING)
				{
					process_table[running_process].setPc(savedProgramCounter);
					process_table[running_process].setAcc(machine.cpu.acc);
					process_table[running_process].setX(machine.cpu.x);
					process_table[running_process].setStatus(ProcessState.READY);
					runNextProcess();
					setNextProcess();
				}
				break;
			case Trap.END:
				if(running_process >= 0)
				{
					process_table[running_process].setStatus(ProcessState.TERMINATED);
					runNextProcess();
					setNextProcess();
				}
				else if(running_process == -1)
				{
					machine.cpu.setPc(0);
				}
				break;
			default:
				System.out.println(savedProgramCounter);
				System.out.println("UNHANDLED TRAP " + trapNumber);
				System.exit(1);
		}
	}

	/*
	 * Handle a system call from the software.
	 * 
	 * @param programCounter -- the program counter of the instruction after the
	 * one that caused the trap.
	 * 
	 * @param callNumber -- the callNumber of the system call.
	 * 
	 * @param address -- the memory address of any parameters for the system
	 * call.
	 */

	@Override
	public void syscall(int savedProgramCounter, int callNumber)
	{
		CheckValid.syscallNumber(callNumber);
		if (!machine.cpu.runProg)
		{
			return;
		}
	}

	/*
	 * Handle an interrupt from the hardware.
	 * 
	 * @param programCounter -- the program counter of the instruction after the
	 * one that caused the trap.
	 * 
	 * @param deviceNumber -- the device number that is interrupting.
	 */

	@Override
	public void interrupt(int savedProgramCounter, int deviceNumber)
	{
		CheckValid.deviceNumber(deviceNumber);
		if (!machine.cpu.runProg)
		{
			return;
		}
	}
}