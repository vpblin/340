package s340.software;

public class ProcessControlBlock {
	
	public int acc;
	public int pc;
	public int x;
	public ProcessState status;

	public ProcessControlBlock(int acc, int pc, int x, ProcessState status) {
		super();
		this.acc = acc;
		this.pc = pc;
		this.x = x;
		this.status = status;
	}

	public int getAcc() {
		return acc;
	}

	public void setAcc(int acc) {
		this.acc = acc;
	}

	public int getPc() {
		return pc;
	}

	public void setPc(int pc) {
		this.pc = pc;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public ProcessState getStatus() {
		return status;
	}

	public void setStatus(ProcessState status) {
		this.status = status;
	}
	
	
	

}
