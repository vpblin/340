package s340;

import s340.hardware.Machine;
import s340.software.OperatingSystem;
import s340.software.Program;
import s340.software.ProgramBuilder;

public class Project_1_Programs {
	
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
			b1.loadi(100); //load count
			b1.store(70); //store count
			b1.loadi(1); //load subtract value
			b1.store(76); //store in memory address
			b1.loadi(0); //load first value	
			b1.store(71); //store first value into mem address 1
			b1.loadi(1); //load second value
			b1.store(72); //store second value in mem 2
			b1.load(71);
			b1.store(74);
			//Jumperino
			b1.load(71);
			b1.add(72);
			b1.store(71);
			b1.load(72);
			b1.inca();
			b1.store(72);
			b1.load(70);
			b1.subi(1);
			b1.store(70);
			b1.jpos(320);
			b1.load(71);
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

	}


}
