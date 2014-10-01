package s340;

import s340.hardware.Machine;
import s340.software.OperatingSystem;
import s340.software.Program;
import s340.software.ProgramBuilder;

public class HW1_Assembly {

	public static void main(String[] args) throws Exception {
		
//		setup the hardware, the operating system, and power up
			//	do not remove this

			Machine machine = new Machine();
			OperatingSystem os = new OperatingSystem(machine);
			machine.powerUp(os);

			//Question 1
			//Loop that adds 1+2+3...+100 and prints the output
		
			ProgramBuilder b1 = new ProgramBuilder();
			b1.start(300);
			b1.loadi(0);			//Mem[200] = Acc value
			b1.store(200);
			b1.load(200);			//Mem[202] = Added value
			b1.inca();
			b1.store(200);
			b1.add(202);
			b1.store(202);
			b1.load(200);
			b1.subi(200);
			b1.jneg(304);
			b1.load(202);
			b1.output();
			b1.end();
			
			Program p1 = b1.build();
			//System.out.println(p1);

			// schedule the program

			//os.schedule(p1);
		
			//Question 2
			//Use nested loops to do 1*1, 1*2...5*5
			
			ProgramBuilder b2 = new ProgramBuilder();
			b2.start(400);
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
			b2.jneg(408);
			// end of K-loop
			b2.load(100);
			b2.inca();
			b2.store(100);
			b2.subi(6);
			b2.jneg(404);
			b2.end();
			
			Program p2 = b2.build();
			//System.out.println(p2);

			// schedule the program

			os.schedule(p1, p2);
/*			
			//Question 3
			
			ProgramBuilder b3 = new ProgramBuilder();
			// SUBI isn't necessary
			// n = 5, ACC = 6
			b3.loadi(5);
			b3.store(100);
			b3.loadi(6);
			b3.sub(100);
			b3.output();
			// ADDI isn't necessary
			// n = 5, ACC = 6
			b3.loadi(5);
			b3.store(100);
			b3.loadi(6);
			b3.add(100);
			b3.output();
				
			Program p3 = b3.build();
			System.out.println(p3);

			// schedule the program

			os.schedule(p3);
			
			//Question 4
			//You can just use jpos, jneg, and jzero and it will guarantee that you jmp to your location
			
			ProgramBuilder b4 = new ProgramBuilder();
			//endless loop using the above jmp commands
			b4.loadi(5);
			b4.store(100);
			b4.loadi(6);
			b4.sub(100);
			b4.output();
			b4.jpos(0);
			b4.jneg(0);
			b4.jzero(0);
				
			Program p4 = b4.build();
			System.out.println(p4);

			// schedule the program

			os.schedule(p4);
			
			//Question 5
			//if statement
			
			ProgramBuilder b5 = new ProgramBuilder();
			//Mem[100] = a = 5, Acc = b = 6
			b5.loadi(5);
			b5.store(100);
			b5.loadi(6);
			b5.sub(100);
			b5.jpos(12);
			b5.jmp(16);
			b5.load(100);
			b5.output();
			b5.end(); //do_the_next_thing
				
			Program p5 = b5.build();
			System.out.println(p5);

			// schedule the program

			os.schedule(p5);
			
			//Question 6
			//storing numbers into addresses 121 to 140
			
			ProgramBuilder b6 = new ProgramBuilder();
			//Mem[121] = starting location of storage, Mem[100] = placeholder
			b6.loadi(0);
			b6.tax();
			b6.loadi(4);
			b6.store(100);
			b6.load(100);
			b6.storex(121);
			b6.incx();
			b6.inca();
			b6.store(100);
			b6.subi(24);
			b6.jneg(8);
			b6.loadi(0);
			b6.tax();
			b6.loadi(4);
			b6.store(100);
			b6.load(100);
			b6.loadx(121);
			b6.output();
			b6.incx();
			b6.subi(23);
			b6.jneg(30);
			b6.end();
				
			Program p6 = b6.build();
			System.out.println(p6);

			// schedule the program

			os.schedule(p6);
*/
	}

}
