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

		ProgramBuilder b1 = new ProgramBuilder();
		b1.loadi(0);			// Acc value
		b1.store(100);			//store to mem 100
		b1.load(100);			//acc is mem 100
		b1.inca();				//incrememnt acc
		b1.store(100);			//store acc to mem 100
		b1.mul(100);			//multiply acc by content of 100
		b1.store(101);			//store acc to mem 100
		b1.add(102);  			//add mem 102
		b1.store(102);			//store acc to mem 102
		b1.load(100);			//acc equal mem 100
		b1.subi(100);			//subtract 100 from acc
		b1.jneg(4);				//if acc is neg go to mem 4
		b1.load(102);			//acc is mem 102
		b1.output();			//output acc
		b1.end();
		
		
		/*ProgramBuilder b2 = new ProgramBuilder();
		b2.loadi(1);			// Mem[100] = J value
		b2.store(100);			// Mem[102] = K value
		// J-loop
		b2.loadi(1);
		b2.store(102);
		// K-loop
		b2.load(100);
		b2.mul(102);
		b2.output();
		b2.load(102);
		b2.inca();
		b2.store(102);
		b2.subi(6);
		b2.jneg(8);
		// end of K-loop
		b2.load(100);
		b2.inca();
		b2.store(100);
		b2.subi(6);
		b2.jneg(4);
		b2.end();*/
		
		Program p2 = b1.build();
		//System.out.println(p2);
		System.out.println(p2);

		// schedule the program

		os.schedule(p2);
	}
}
