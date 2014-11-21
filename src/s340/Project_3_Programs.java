package s340;

import s340.hardware.Machine;
import s340.software.OperatingSystem;
import s340.software.Program;
import s340.software.ProgramBuilder;

public class Project_3_Programs {

	public static void main(String[] args) throws Exception {
		
//		setup the hardware, the operating system, and power up
			//	do not remove this

		Machine machine = new Machine();
		OperatingSystem os = new OperatingSystem(machine);
		machine.powerUp(os);

		//p1
	
		ProgramBuilder b1 = new ProgramBuilder();
		
		b1.loadi(200);
		b1.syscall(0);
		b1.loadi(1);
		b1.store(40);
		b1.store(41);
		b1.store(42);
		b1.store(43);
		b1.store(44);
		b1.load(40);

		b1.syscall(1);
		b1.load(41);

		b1.syscall(1);
		b1.load(42);

		b1.syscall(1);
		b1.load(43);

		b1.syscall(1);
		b1.load(44);

		b1.syscall(1);


		
		Program p1 = b1.build();
		
		//p2
		
		ProgramBuilder b2 = new ProgramBuilder();
		
		b2.loadi(200);
		b2.syscall(0);
		b2.loadi(2);
		b2.store(40);
		b2.store(41);
		b2.store(42);
		b2.store(43);
		b2.store(44);
		b2.load(40);
		b2.syscall(1);
		b2.load(41);
		b2.syscall(1);
		b2.load(42);
		b2.syscall(1);
		b2.load(43);

		b2.syscall(1);
		b2.load(44);

		b2.syscall(1);
		
		Program p2 = b2.build();
		
		
		//p3
		
		ProgramBuilder b3 = new ProgramBuilder();
		
		b3.loadi(200);
		b3.syscall(0);
		b3.loadi(3);
		b3.store(40);
		b3.store(41);
		b3.store(42);
		b3.store(43);
		b3.store(44);
		b3.load(40);

		b3.syscall(1);
		b3.load(41);

		b3.syscall(1);
		b3.load(42);

		b3.syscall(1);
		b3.load(43);

		b3.syscall(1);
		b3.load(44);

		b3.syscall(1);

		
		Program p3 = b3.build();

		
		
		
		os.schedule(p1, p2, p3);
	}

}
	

