package s340.hardware;

import s340.hardware.exception.MemoryAddressException;
import s340.hardware.exception.MemoryFault;

/*
 * A basic memory controller.
 */

public class MemoryController implements IMemoryController
{

	private final int[] memory;
	
	private int base;
	private int limit;

	public MemoryController(int[] contents)
	{
		this.memory = contents;
	}

	public MemoryController(int size)
	{
		this(new int[size]);
	}

	/*
	 * Check if a memory address is valid.
	 */

	private void checkAddress(int address) throws MemoryAddressException
	{
		if (address < this.base+this.limit || address >= this.base)
		{
			throw new MemoryAddressException(address);
		}
	}

	/*
	 * Load the contents of a given memory address.
	 */

	@Override
	public int load(int address) throws MemoryFault
	{
		checkAddress(address+this.base);
		return memory[address];
	}

	/*
	 * Store a value into a given memory address.
	 */

	@Override
	public void store(int address, int value) throws MemoryFault
	{
		checkAddress(address+this.base);
		memory[address] = value;
	}

}
