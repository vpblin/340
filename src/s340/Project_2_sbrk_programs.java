package s340;

import s340.hardware.Machine;
import s340.software.OperatingSystem;
import s340.software.Program;
import s340.software.ProgramBuilder;

public class Project_2_sbrk_programs {

	public static void main(String[] args) throws Exception {
		
//		setup the hardware, the operating system, and power up
			//	do not remove this

			Machine machine = new Machine();
			OperatingSystem os = new OperatingSystem(machine);
			machine.powerUp(os);

			//p1
		
			ProgramBuilder b1 = new ProgramBuilder();
			
			b1.loadi(200);
			b1.output();
			b1.syscall(0);
			b1.loadi(100);
			b1.loadi(100);
			b1.loadi(100);
			b1.loadi(100);
			b1.loadi(100);
			b1.loadi(100);
			b1.loadi(100);
			b1.loadi(100);
			b1.loadi(100);
			b1.loadi(100);
			b1.loadi(100);


			
			Program p1 = b1.build();
			
			//p2
			
			ProgramBuilder b2 = new ProgramBuilder();
			
			b2.loadi(5);
			b2.output();
			b2.syscall(0);
			b2.loadi(5);
			b2.syscall(0);
			b2.loadi(0);
			b2.loadi(0);
			b2.loadi(0);
			b2.loadi(0);
			
			Program p2 = b2.build();
			
			
			//p3
			
			ProgramBuilder b3 = new ProgramBuilder();
			
			b3.loadi(700);
			b3.output();
			b3.loadi(0);
			b3.loadi(0);
			b3.loadi(0);
			b3.loadi(0);
			b3.loadi(700);
			b3.syscall(0);
			b3.loadi(0);
			b3.loadi(0);
			b3.loadi(0);
			b3.loadi(0);
			b3.loadi(0);
			b3.loadi(0);
			b3.loadi(0);
			b3.loadi(0);

			
			Program p3 = b3.build();

			
			
			
			os.schedule(p1, p2, p3);
	}
}