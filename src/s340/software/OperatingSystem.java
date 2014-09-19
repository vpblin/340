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

	/*
	 * Create an operating system on the given machine.
	 */

	public OperatingSystem(Machine machine) throws MemoryFault
	{
		this.machine = machine;
		currentProgram = null;
	}

	/*
	 * Load a program into a given memory address
	 */

	private int loadProgram(int startAddress, Program program) throws MemoryFault
	{
		int address = startAddress;
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
		int address = 0;
		for (Program program : programs)
		{
			address = loadProgram(address, program);
		}

		currentProgram = programs[0];

		// leave this as the last line
		machine.cpu.runProg = true;
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
				if (currentProgram != null)
				{
					currentProgram = null;
					machine.cpu.setPc(0);
				}
				break;
			case Trap.END:
				System.exit(1);
			default:
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