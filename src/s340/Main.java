package s340;

import s340.hardware.Machine;
import s340.software.Program;
import s340.software.OperatingSystem;
import s340.software.ProgramBuilder;

/**
 *
 */
public class Main
{

	public static void main(String[] args) throws Exception
	{
		//	setup the hardware, the operating system, and power up
		//	do not remove this

		Machine machine = new Machine();
		OperatingSystem os = new OperatingSystem(machine);
		machine.powerUp(os);

		// First test program.
		// Stores 0, 2, 4, ... 38 into memory locations 200 through 219

		ProgramBuilder b1 = new ProgramBuilder();
		b1.loadi(0);						// ACC = 0
		b1.store(0);						// Mem[0] = Acc = 0
		b1.tax();							// X = Acc 0
		int pos = b1.load(0);				// Acc = Mem[0]
		b1.storex(200);						// Mem[X + 200] = Acc
		b1.incx();							// X++
		b1.inca();
		b1.inca();							// ACC += 2
		b1.store(0);						// Mem[0] = Acc
		b1.subi(39);						// Acc -= 39
		b1.jneg(pos);

		for (int i = 200; i < 220; i++)
		{
			b1.load(i);
			b1.output();
		}

		Program p1 = b1.build();
		System.out.println(p1);

		// schedule the program

		os.schedule(p1);
	}
}
