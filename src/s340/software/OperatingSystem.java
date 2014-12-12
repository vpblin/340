package s340.software;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import s340.hardware.DeviceControllerOperations;
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

public class OperatingSystem implements IInterruptHandler, ISystemCallHandler,
		ITrapHandler {
	// the machine on which we are running.

	private final Machine machine;

	// the max number of processes that can be run

	private final int MAX_NUM_PROCESS = 5;

	// an array (process table) that contains the PCBs of our processes

	private ProcessControlBlock[] process_table;

	// setting the running_process to -1 means that the wait program should be
	// running

	private int running_process = -1;

	// setting free spaces

	private List<FreeSpace> free_space;

	// p1 is the wait program

	private Program p1;	
	
	// array of device queues
	private LinkedList<IORequest>[] device_q;

	/*
	 * Create an operating system on the given machine.
	 */

	public OperatingSystem(Machine machine) throws MemoryFault {
		this.machine = machine;

		// array of process control blocks of size 4
		process_table = new ProcessControlBlock[MAX_NUM_PROCESS];

		// LinkedList of free space starting at Mem[500]
		free_space = new LinkedList<FreeSpace>();
		free_space.add(new FreeSpace(4, machine.MEMORY_SIZE - 4));
		
		// loop of queues
		device_q = new LinkedList[machine.NUM_DEVICES];
		for(int i = 0; i < device_q.length; i++)
		{
			device_q[i] = new LinkedList<IORequest>();
		}

		// wait program
		ProgramBuilder wait = new ProgramBuilder();
		wait.jmp(0);
		p1 = wait.build();

		machine.memory.setBase(0);
		machine.memory.setLimit(4);
		loadProgram(p1);
	}

	/*
	 * Load a program into a given memory address
	 */

	private int loadProgram(Program program) throws MemoryFault {
		int address = 0;
		for (int i : program.getCode()) {
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

	public void schedule(Program... programs) throws MemoryFault {
		int i = 0;
		for (Program program : programs) {
			int address = findSpace(program.getDataSize()
					+ program.getCode().length);
			process_table[i] = new ProcessControlBlock(0, 0, 0,
					ProcessState.READY, address, program.getDataSize()
							+ program.getCode().length);
			machine.memory.setBase(process_table[i].getBase());
			machine.memory.setLimit(process_table[i].getLimit());
			loadProgram(program);
			i++;
		}

		machine.memory.setBase(0);
		machine.memory.setLimit(4);
		// leave this as the last line
		machine.cpu.runProg = true;

		//diagnostic();
	}

	// Uses Round Robin method to select new process
	private void runNextProcess() {

		for (int i = running_process + 1; i < MAX_NUM_PROCESS; i++) {

			if (process_table[i] != null
					&& process_table[i].getStatus() == ProcessState.READY) {
				process_table[i].setStatus(ProcessState.RUNNING);
				running_process = i;
				return;
			}
		}

		for (int i = 0; i < MAX_NUM_PROCESS; i++) {

			if (process_table[i] != null
					&& process_table[i].getStatus() == ProcessState.READY) {
				process_table[i].setStatus(ProcessState.RUNNING);
				running_process = i;
				return;
			}
		}

		running_process = -1;

	}

	// set the Acc, X, and PC registers from the PCB
	private void setNextProcess() {
		if (running_process >= 0) {
			machine.cpu.acc = process_table[running_process].getAcc();
			machine.cpu.x = process_table[running_process].getX();
			machine.cpu.setPc(process_table[running_process].getPc());
			machine.memory.setBase(process_table[running_process].getBase());
			machine.memory.setLimit(process_table[running_process].getLimit());
		} else {
			machine.memory.setBase(0);
			machine.memory.setLimit(4);
			machine.cpu.setPc(0);
		}
	}

	// method that finds free space for our programs
	// returns our new starting space
	private int findSpace(int size) {
		int newStart = -1; // will throw memory fault
		for (FreeSpace space : free_space) {
			if (space.getLimit() >= size) {
				newStart = space.getBase();
				space.setBase(space.getBase() + size);
				space.setLimit(space.getLimit() - size);
				return newStart;
			}
		}
		return newStart;
	}

	// method determines if space is free, helps to find open freespace
	// returns our new starting space
	private int nextSpace(int size) {
		int newStart = -1; // will throw memory fault
		for (FreeSpace space : free_space) {
			if (space.getLimit() >= size) {
				newStart = space.getBase();
				return newStart;
			}
		}
		return newStart;

	}

	// moves a process to new space
	private void moveSpace(int oldBase, int oldLimit, int newBase, int newLimit) {
		int address = newBase;
		machine.memory.setBase(0);
		machine.memory.setLimit(machine.MEMORY_SIZE);
		for (int i = 0; i < oldLimit; i++) // may need to be <= oldLimit
		{
			try {
				machine.memory.store(address++,
						machine.memory.load(oldBase + i));
			} catch (MemoryFault e) {
				e.printStackTrace();
			}

		}

		machine.memory.setBase(newBase);
		machine.memory.setLimit(newLimit);
	}

	// checks to see if there is freespace to merge together

	public List<FreeSpace> mergeSpace() {
		Collections.sort(free_space);
		LinkedList<FreeSpace> new_list = new LinkedList<FreeSpace>();
		new_list.add(free_space.get(0));
		for(int i = 1; i < free_space.size(); i++)
		{
			if(free_space.get(i).getBase() == new_list.getLast().getBase() + new_list.getLast().getLimit())
			{
				new_list.getLast().setLimit(new_list.getLast().getLimit() + free_space.get(i).getLimit());
			}
			else
			{
				new_list.add(free_space.get(i));
			}
		}
		return new_list;
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
	public void trap(int savedProgramCounter, int trapNumber) {
		CheckValid.trapNumber(trapNumber);
		if (!machine.cpu.runProg) {
			return;
		}

		switch (trapNumber) {
		case Trap.TIMER:
			if (running_process == -1) {
				runNextProcess();
				setNextProcess();
			} else if (running_process >= 0
					&& process_table[running_process].getStatus() == ProcessState.RUNNING) {
				process_table[running_process].setPc(savedProgramCounter);
				process_table[running_process].setAcc(machine.cpu.acc);
				process_table[running_process].setX(machine.cpu.x);
				process_table[running_process].setStatus(ProcessState.READY);
				runNextProcess();
				setNextProcess();
			}
			break;
		case Trap.END: {
			process_table[running_process].setStatus(ProcessState.TERMINATED);
			free_space.add(new FreeSpace(process_table[running_process]
					.getBase(), process_table[running_process].getLimit()));
			// merge
			free_space = mergeSpace();
			//diagnostic();
			// done merging
			runNextProcess();
			setNextProcess();
		}

			break;
		default:
			System.out.println(savedProgramCounter);
			System.out.println("UNHANDLED TRAP " + trapNumber);
			System.exit(1);
		}
	}
	
	// saves state of current process
	public void savestate(int savedProgramCounter)
	{
		if(running_process > -1)
		{
			process_table[running_process].setPc(savedProgramCounter);
			process_table[running_process].setAcc(machine.cpu.acc);
			process_table[running_process].setX(machine.cpu.x);
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
	public void syscall(int savedProgramCounter, int callNumber) {
		CheckValid.syscallNumber(callNumber);
		if (!machine.cpu.runProg) {
			return;
		}
		switch (callNumber) {
		case SystemCall.SBRK:
			savestate(savedProgramCounter);
			sbrk(machine.cpu.acc);
			break;
		case SystemCall.WRITE_CONSOLE:
			savestate(savedProgramCounter);
			device_q[machine.CONSOLE].add(new IORequest(running_process, callNumber));
			process_table[running_process].setStatus(ProcessState.WAITING);
			// if only 1 in que
			if(device_q[machine.CONSOLE].size() == 1)
			{
				write_console();
			}
			runNextProcess();
			setNextProcess();
			break;
		case SystemCall.READ:
			savestate(savedProgramCounter);
			try {
				int device = machine.memory.load(process_table[running_process].getAcc());
				device_q[device].add(new IORequest(running_process, callNumber, machine.memory.load(process_table[running_process].getAcc()+2), machine.memory.load(process_table[running_process].getAcc()+3)));
				process_table[running_process].setStatus(ProcessState.WAITING);
				if(device_q[device].size() == 1)
				{
					read_disk(device);
				}
			} catch (MemoryFault e) {
				e.printStackTrace();
			}
			runNextProcess();
			setNextProcess();
			break;
		case SystemCall.WRITE:
			savestate(savedProgramCounter);
			try {
				int device = machine.memory.load(process_table[running_process].getAcc());
				device_q[device].add(new IORequest(running_process, callNumber, machine.memory.load(process_table[running_process].getAcc()+2), machine.memory.load(process_table[running_process].getAcc()+3)));
				process_table[running_process].setStatus(ProcessState.WAITING);
				if(device_q[device].size() == 1)
				{
					write_disk(device);
				}
			} catch (MemoryFault e) {
				e.printStackTrace();
			}
			runNextProcess();
			setNextProcess();
			break;
		default:
			System.out.println("UNHANDLED SYSCALL " + callNumber);
			System.exit(1);

		}
	}
	
	// write console call
	public void write_console()
	{
		machine.devices[machine.CONSOLE].controlRegister.register[1] = process_table[device_q[machine.CONSOLE].getFirst().getProcessNumber()].getAcc();
		machine.devices[machine.CONSOLE].controlRegister.startOperation();
	}
	
	// read disk call
	public void read_disk(int device)
	{
		ProcessControlBlock pcb = process_table[device_q[device].getFirst().getProcessNumber()];
		int index = pcb.getAcc()+1;
		machine.devices[device].controlRegister.register[0] = DeviceControllerOperations.READ;
		try {
			machine.memory.setBase(pcb.getBase());
			machine.memory.setLimit(pcb.getLimit());
			machine.devices[device].controlRegister.register[1] = machine.memory.load(index);
			if(device == Machine.DISK)
			{
				machine.devices[device].controlRegister.register[2] = machine.memory.load(index+1);
				machine.devices[device].controlRegister.register[3] = machine.memory.load(index+2);
			} else {
				machine.devices[device].controlRegister.register[2] = ((machine.memory.load(index+1) + machine.memory.load(index+2)) - device_q[device].getFirst().getDiskLength());
				if(machine.devices[device].buffer.length < device_q[device].getFirst().getDiskLength())
					machine.devices[device].controlRegister.register[3] = machine.devices[device].buffer.length;
				else
					machine.devices[device].controlRegister.register[3] = device_q[device].getFirst().getDiskLength();
			}
			machine.devices[device].controlRegister.startOperation();
		} catch (MemoryFault e) {
			e.printStackTrace();
		}

	}
	
	// write disk call
	public void write_disk(int device)
	{
		ProcessControlBlock pcb = process_table[device_q[device].getFirst().getProcessNumber()];
		int index = pcb.getAcc()+1;
		machine.devices[device].controlRegister.register[0] = DeviceControllerOperations.WRITE;
		try {
			machine.memory.setBase(pcb.getBase());
			machine.memory.setLimit(pcb.getLimit());
			machine.devices[device].controlRegister.register[1] = machine.memory.load(index);
			if(device == Machine.DISK)
			{
				machine.devices[device].controlRegister.register[2] = machine.memory.load(index+1);
				int start = machine.memory.load(index+3);
				machine.devices[device].controlRegister.register[3] = machine.memory.load(index+2);
				for(int i = 0; i < machine.memory.load(index+2); i++)
				{
					machine.devices[device].buffer[i] = machine.memory.load(start);
					start++;
				}
			} else if(device == Machine.DISKTWO) {
				machine.devices[device].controlRegister.register[2] = ((machine.memory.load(index+1) + machine.memory.load(index+2)) - device_q[device].getFirst().getDiskLength());
				int start = ((machine.memory.load(index+3) + machine.memory.load(index+2)) - device_q[device].getFirst().getDiskLength());
				int buf;
				if(machine.devices[device].buffer.length < device_q[device].getFirst().getDiskLength())
				{
					machine.devices[device].controlRegister.register[3] = machine.devices[device].buffer.length;
					buf = machine.devices[device].buffer.length;
				}else{
					machine.devices[device].controlRegister.register[3] = device_q[device].getFirst().getDiskLength();
					buf = device_q[device].getFirst().getDiskLength();
				}
				
				for(int i = 0; i < buf; i++)
				{
					machine.devices[device].buffer[i] = machine.memory.load(start);
					start++;
				}
			}
			machine.devices[device].controlRegister.startOperation();

		} catch (MemoryFault e) {
			e.printStackTrace();
		}

	}

	public void sbrk(int spacesNeeded) {
		
		// check to add on to end of process, doesn't check for two free spaces
		for (FreeSpace space : free_space) {
			if (space.getBase() == process_table[running_process].getBase()
					+ process_table[running_process].getLimit()
					&& space.getLimit() >= spacesNeeded) // might need +1
			{
				space.setBase(space.getBase() + spacesNeeded);
				space.setLimit(space.getLimit() - spacesNeeded);
				process_table[running_process]
						.setLimit(process_table[running_process].getLimit()
								+ spacesNeeded);
				machine.memory.setLimit(process_table[running_process]
						.getLimit());
				//diagnostic();
				return;
			}
		}

		// find next open space
		int newSpace = nextSpace(process_table[running_process].getLimit()
				+ spacesNeeded);
		for (FreeSpace space : free_space) {
			if (newSpace == space.getBase()) {
				int oldBase = process_table[running_process].getBase();
				int oldLimit = process_table[running_process].getLimit();
				space.setBase(space.getBase() + spacesNeeded + oldLimit);
				space.setLimit(space.getLimit() - (spacesNeeded + oldLimit));
				if(oldBase < space.getBase())
				{
					free_space.add(new FreeSpace(oldBase, oldLimit));
				}
				process_table[running_process].setBase(newSpace);
				process_table[running_process]
						.setLimit(process_table[running_process].getLimit()
								+ spacesNeeded);
				moveSpace(oldBase, oldLimit,
						process_table[running_process].getBase(),
						process_table[running_process].getLimit());
				// merge
				free_space = mergeSpace();
				//diagnostic();
				// done merging
				return;
			}
		}

		// compact, then move processes that are after the process that needs
		// memory

		// get rid of terminated processes
		LinkedList <ProcessControlBlock> pcb = new LinkedList<>();
		for (int i = 0; i < MAX_NUM_PROCESS; i++) {
			if (process_table[i] != null && process_table[i].getStatus() != ProcessState.TERMINATED)
			{
				pcb.add(process_table[i]);
			}
		}
		Collections.sort(pcb);
		
		// find the running process in our sorted list and set its index value equal to x
		int x = 0;
		for (int i = 0; i < pcb.size(); i++) {
			
			if(pcb.get(i) == process_table[running_process])
			{
				x = pcb.indexOf(pcb.get(i));
			}

		}
		System.out.println(pcb);

		// move processes up in memory "compact"
		for (int i = 0; i < pcb.size(); i++) {
			int oldBase = pcb.get(i).getBase();
			if (i == 0) {
				pcb.get(i).setBase(4);
				moveSpace(oldBase, pcb.get(i).getLimit(),
						pcb.get(i).getBase(),
						pcb.get(i).getLimit());
			} else {
				pcb.get(i).setBase(pcb.get(i-1).getBase()
						+ pcb.get(i-1).getLimit());
				moveSpace(oldBase, pcb.get(i).getLimit(),
						pcb.get(i).getBase(),
						pcb.get(i).getLimit());
			}
		}
	
		// move processes down that are after x
		for (int i = pcb.size() - 1; i > x; i--) {
				moveSpace(pcb.get(i).getBase(),
						pcb.get(i).getLimit(), pcb.get(i).getBase()
								+ spacesNeeded, pcb.get(i).getLimit());
				pcb.get(i).setBase(pcb.get(i).getBase() + spacesNeeded);
		}

		// move running_process down, giving it its space
		pcb.get(x).setLimit(pcb.get(x)
				.getLimit() + spacesNeeded);
		machine.memory.setBase(pcb.get(x).getBase());
		machine.memory.setLimit(pcb.get(x).getLimit());

		// adjust free space
		free_space.clear();
		free_space.add(new FreeSpace(pcb.getLast().getBase() + pcb.getLast().getLimit(), machine.MEMORY_SIZE - (pcb.getLast().getBase() + pcb.getLast().getLimit())));

		//diagnostic();

	}

	// diagnostic check for sbrk
	public void diagnostic() {
		System.out
				.println("+++++++++++++++++++++Process Table +++++++++++++++++++++++++++++");

		for (int i = 0; i < MAX_NUM_PROCESS; i++) {
			if (process_table[i] != null) {
				System.out.println("P" + i + " " + "Base : "
						+ process_table[i].getBase() + "  Limit : "
						+ process_table[i].getLimit() + "  Status  "
						+ process_table[i].getStatus());
			}
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out
				.println("+++++++++++++++++++++Free Space +++++++++++++++++++++++++++++");

		for (FreeSpace space : free_space) {
			System.out.println("Base : " + space.getBase() + "  Limit : "
					+ space.getLimit());

		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
		
		System.out.println("+++++++++++++++++++++IO Requests +++++++++++++++++++++++++++++");
				for(int i = 0; i < device_q.length; i++){
					System.out.println("Queue : " + device_q[i].toString());		
				}
				System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");
	}

	public void sstf(int device, int start)
	{
		int nextRequest = 0;
		int gap = Math.abs(start - device_q[device].getFirst().getStart());
		for(int i = 0; i < device_q[device].size(); i++)
		{
			int newgap = Math.abs(device_q[device].get(i).getStart() - start);
			if(Math.abs(device_q[device].get(i).getStart() - start) < gap)
			{
				gap = newgap;
				nextRequest = i;
			}
		}
		device_q[device].addFirst(device_q[device].remove(nextRequest));
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
	public void interrupt(int savedProgramCounter, int deviceNumber) {
		CheckValid.deviceNumber(deviceNumber);
		if (!machine.cpu.runProg) {
			return;
		}
		// save status and unflag interrupt register
		savestate(savedProgramCounter);
		machine.interruptRegisters.register[deviceNumber] = false;
		
		// remove IO and set status
		IORequest oldHead = device_q[deviceNumber].removeFirst();
		process_table[oldHead.getProcessNumber()].setStatus(ProcessState.READY);
		
		if(deviceNumber == machine.CONSOLE)
		{
			// run next IO
			if(!device_q[deviceNumber].isEmpty())
			{
				write_console();
			}
			setNextProcess();
			return;
		}
		if(deviceNumber == machine.DISK)
		{
						
			// if read, store buffer into memory
			if(oldHead.getSystemCall() == SystemCall.READ)
			{
				int ourAcc = process_table[oldHead.getProcessNumber()].getAcc()+1;
				machine.memory.setBase(process_table[oldHead.getProcessNumber()].getBase());
				machine.memory.setLimit(process_table[oldHead.getProcessNumber()].getLimit());
				try {
					int start = machine.memory.load(ourAcc+3);
					for(int i = 0; i < machine.memory.load(ourAcc+2); i++)
					{
						machine.memory.store(start, machine.devices[deviceNumber].buffer[i]);
						start++;
					}
				} catch (MemoryFault e) {
					e.printStackTrace();
				}
			}
			
			
			// run next IO
			if(!device_q[deviceNumber].isEmpty())
			{
				// adjust the head's start
				oldHead.setStart(oldHead.getStart() + oldHead.getDiskLength());
				// sstf disk ordering
				sstf(deviceNumber, oldHead.getStart());
				if(device_q[deviceNumber].getFirst().getSystemCall() == SystemCall.READ)
				{
					read_disk(deviceNumber);
				}
				else
				{
					write_disk(deviceNumber);
				}
			}
			setNextProcess();
			return;
		}
		if(deviceNumber == machine.DISKTWO)
		{
						
			// if read, store buffer into memory
			if(oldHead.getSystemCall() == SystemCall.READ)
			{
				int ourAcc = process_table[oldHead.getProcessNumber()].getAcc()+1;
				machine.memory.setBase(process_table[oldHead.getProcessNumber()].getBase());
				machine.memory.setLimit(process_table[oldHead.getProcessNumber()].getLimit());
				try {
					int start = ((machine.memory.load(ourAcc+3) + machine.memory.load(ourAcc+2)) - oldHead.getDiskLength());
					int buf;
					if(machine.devices[deviceNumber].buffer.length < oldHead.getDiskLength())
						buf = machine.devices[deviceNumber].buffer.length;
					else
						buf = oldHead.getDiskLength();
					
					for(int i = 0; i < buf; i++)
					{
						machine.memory.store(start, machine.devices[deviceNumber].buffer[i]);
						start++;
					}
				} catch (MemoryFault e) {
					e.printStackTrace();
				}
			}
			
			// adjust length of read/write and start
			oldHead.setDiskLength(oldHead.getDiskLength() - machine.devices[deviceNumber].buffer.length);
			
			// if not done, add to the end of que
			if(oldHead.getDiskLength() > 0)
			{
				process_table[oldHead.getProcessNumber()].setStatus(ProcessState.WAITING);
				device_q[deviceNumber].add(oldHead);
				oldHead.setStart(oldHead.getStart()+machine.devices[deviceNumber].buffer.length);
			}
			
			// run next IO
			if(!device_q[deviceNumber].isEmpty())
			{
				// sstf disk ordering
				sstf(deviceNumber, oldHead.getStart());
				if(device_q[deviceNumber].getFirst().getSystemCall() == SystemCall.READ)
				{
					read_disk(deviceNumber);
				}
				else
				{
					write_disk(deviceNumber);
				}
			}
			setNextProcess();
			return;
		}
	}
}