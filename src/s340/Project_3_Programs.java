package s340;

import s340.hardware.Machine;
import s340.software.OperatingSystem;
import s340.software.Program;
import s340.software.ProgramBuilder;
import s340.software.SystemCall;

public class Project_3_Programs {

	public static void main(String[] args) throws Exception {
		
		Machine machine = new Machine();
		OperatingSystem os = new OperatingSystem(machine);
		machine.powerUp(os);

		//p1
	
		ProgramBuilder b1 = new ProgramBuilder();
		b1.size(1000);
		for(int i = 1; i < 41; i++)
		{
			b1.loadi(i);
			b1.store(i+500);
		}
		//write 
		b1.loadi(Machine.DISK);
		b1.store(900);
		b1.loadi(6);
		b1.store(901);
		b1.loadi(31);
		b1.store(902);
		b1.loadi(40);
		b1.store(903);
		b1.loadi(501);
		b1.store(904);
		b1.loadi(900);
		b1.syscall(SystemCall.WRITE);
		b1.loadi(Machine.DISKTWO);
		b1.store(900);
		b1.loadi(6);
		b1.store(901);
		b1.loadi(31);
		b1.store(902);
		b1.loadi(40);
		b1.store(903);
		b1.loadi(501);
		b1.store(904);
		b1.loadi(900);
		b1.syscall(SystemCall.WRITE);

		//read Accumulator values
		b1.loadi(Machine.DISKTWO);
		b1.store(900);
		b1.loadi(6);
		b1.store(901);
		b1.loadi(31);
		b1.store(902);
		b1.loadi(40);
		b1.store(903);
		b1.loadi(401);
		b1.store(904);
		b1.loadi(900);
		b1.syscall(SystemCall.READ);
		//write console
		for(int i = 1; i < 41; i++)
		{
			b1.load(400+i);
			b1.syscall(SystemCall.WRITE_CONSOLE);
		}
		int z = 1;
		int t = 0;
		b1.load(400+40);
		b1.syscall(SystemCall.WRITE_CONSOLE);

		while(z != 42 && t < 2000)
		{	
			
			b1.load(400+z);
			b1.syscall(SystemCall.WRITE_CONSOLE);
			if(z < 20){
				z++;
			}else{
				z--;
			}
			t++;
		}
		

		Program p1 = b1.build();

		System.out.println(p1);
		
		os.schedule(p1);
		
		
		ProgramBuilder b2 = new ProgramBuilder();
		b2.size(1000);
		for(int i = 1; i < 41; i++)
		{
			b2.loadi(i);
			b2.store(i+500);
		}
		//write 
		b2.loadi(Machine.DISK);
		b2.store(900);
		b2.loadi(6);
		b2.store(901);
		b2.loadi(31);
		b2.store(902);
		b2.loadi(40);
		b2.store(903);
		b2.loadi(501);
		b2.store(904);
		b2.loadi(900);
		b2.syscall(SystemCall.WRITE);
		for(int i = 0; i < 7; i++){
			b2.loadi(Machine.DISKTWO);
			b2.store(900);
			b2.loadi(6);
			b2.store(901);
			b2.loadi(31);
			b2.store(902);
			b2.loadi(40);
			b2.store(903);
			b2.loadi(501);
			b2.store(904);
			b2.loadi(900);
	
			b2.syscall(SystemCall.WRITE);
		}
		b2.loadi(Machine.DISKTWO);
		b2.store(900);
		b2.loadi(6);
		b2.store(901);
		b2.loadi(31);
		b2.store(902);
		b2.loadi(40);
		b2.store(903);
		b2.loadi(501);
		b2.store(904);
		b2.loadi(900);
		b2.syscall(SystemCall.WRITE);

		//read Accumulator values
		b2.loadi(Machine.DISKTWO);
		b2.store(900);
		b2.loadi(6);
		b2.store(901);
		b2.loadi(31);
		b2.store(902);
		b2.loadi(40);
		b2.store(903);
		b2.loadi(401);
		b2.store(904);
		b2.loadi(900);
		b2.syscall(SystemCall.READ);
		//write console
		for(int i = 1; i < 41; i++)
		{
			b2.load(400+i);
			b2.syscall(SystemCall.WRITE_CONSOLE);
		}
		
		Program p2 = b2.build();

		System.out.println(p2);
		
		os.schedule(p2);

	}

}
	

