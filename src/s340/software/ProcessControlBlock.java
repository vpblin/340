package s340.software;


public class ProcessControlBlock implements Comparable<ProcessControlBlock> {
	
	public int acc;
	public int pc;
	public int x;
	public ProcessState status;
	public int base, limit;

	public ProcessControlBlock(int acc, int pc, int x, ProcessState status, int base, int limit) {
		super();
		this.acc = acc;
		this.pc = pc;
		this.x = x;
		this.status = status;
		this.base = base;
		this.limit = limit;
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
	
	public int getBase() {
		return base;
	}

	public void setBase(int base) {
		this.base = base;
	}
	
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	@Override
	public int compareTo(ProcessControlBlock space) {
		// TODO Auto-generated method stub		
	       return Integer.compare(this.getBase(), space.getBase());
	}

	@Override
	public String toString() {
		return "ProcessControlBlock [acc=" + acc + ", pc=" + pc + ", x=" + x
				+ ", status=" + status + ", base=" + base + ", limit=" + limit
				+ "]";
	}
	
	
	

}
