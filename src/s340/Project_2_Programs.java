package s340;

import s340.hardware.Machine;
import s340.software.OperatingSystem;
import s340.software.Program;
import s340.software.ProgramBuilder;

public class Project_2_Programs {

	public static void main(String[] args) throws Exception {
		
//		setup the hardware, the operating system, and power up
			//	do not remove this

			Machine machine = new Machine();
			OperatingSystem os = new OperatingSystem(machine);
			machine.powerUp(os);

			//p1
		
			ProgramBuilder b1 = new ProgramBuilder();
			
			b1.size(17); //size
			
			b1.loadi(9);
			b1.store(0); //store count
			b1.loadi(10);
			b1.store(56);
			
			//Jumperino
			b1.loadi(1);
			b1.storex(44);
			b1.incx();
			//end of loop
			b1.load(0);
			b1.subi(1);	//change this number
			b1.store(0);
			b1.jpos(10);

			b1.load(58);
			b1.addi(1); //change this number for programs
			b1.store(58);
			
			b1.load(56);
			b1.subi(1);
			b1.store(56);
			b1.jpos(22);
			
			b1.load(58);
			b1.output();
			
			Program p1 = b1.build();
			
			//p2
			
			ProgramBuilder b2 = new ProgramBuilder();
			
			b2.size(17); //size
			
			b2.loadi(9);
			b2.store(0); //store count
			b2.loadi(10);
			b2.store(56);
			
			//Jumperino
			b2.loadi(1);
			b2.storex(44);
			b2.incx();
			//end of loop
			b2.load(0);
			b2.subi(2);	//change this number
			b2.store(0);
			b2.jpos(10);

			b2.load(58);
			b2.addi(2); //change this number for programs
			b2.store(58);
			
			b2.load(56);
			b2.subi(1);
			b2.store(56);
			b2.jpos(22);
			
			b2.load(58);
			b2.output();
			
			Program p2 = b2.build();
			
			
			//p3
			
			ProgramBuilder b3 = new ProgramBuilder();
			
			b3.size(17); //size
			
			b3.loadi(9);
			b3.store(0); //store count
			b3.loadi(10);
			b3.store(56);
			
			//Jumperino
			b3.loadi(1);
			b3.storex(44);
			b3.incx();
			//end of loop
			b3.load(0);
			b3.subi(3);	//change this number
			b3.store(0);
			b3.jpos(10);

			b3.load(58);
			b3.addi(3); //change this number for programs
			b3.store(58);
			
			b3.load(56);
			b3.subi(1);
			b3.store(56);
			b3.jpos(22);
			
			b3.load(58);
			b3.output();
			
			Program p3 = b3.build();
			
			//p4
			
			ProgramBuilder b4 = new ProgramBuilder();
			
			b4.size(17); //size
			
			b4.loadi(9);
			b4.store(0); //store count
			b4.loadi(10);
			b4.store(56);
			
			//Jumperino
			b4.loadi(1);
			b4.storex(44);
			b4.incx();
			//end of loop
			b4.load(0);
			b4.subi(4);	//change this number
			b4.store(0);
			b4.jpos(10);

			b4.load(58);
			b4.addi(4); //change this number for programs
			b4.store(58);
			
			b4.load(56);
			b4.subi(1);
			b4.store(56);
			b4.jpos(22);
			
			b4.load(58);
			b4.output();
			
			Program p4 = b4.build();
			
			//p5
			
			ProgramBuilder b5 = new ProgramBuilder();
			
			b5.size(17); //size
			
			b5.loadi(9);
			b5.store(0); //store count
			b5.loadi(10);
			b5.store(56);
			
			//Jumperino
			b5.loadi(1);
			b5.storex(44);
			b5.incx();
			//end of loop
			b5.load(0);
			b5.subi(5);	//change this number
			b5.store(0);
			b5.jpos(10);

			b5.load(58);
			b5.addi(5); //change this number for programs
			b5.store(58);
		
			b5.load(56);
			b5.subi(1);
			b5.store(56);
			b5.jpos(22);
			
			b5.load(58);
			b5.output();
			
			Program p5 = b5.build();
			
			os.schedule(p1, p2, p3, p4, p5);
	}
}
