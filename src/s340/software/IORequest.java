package s340.software;

public class IORequest {
	
	private int processNumber;
	private int systemCall;
	private int diskLength;
	private int start;
	
	// for both disks
	public IORequest(int pnumber, int scall, int start, int diskLength)
	{
		this.processNumber = pnumber;
		this.systemCall = scall;
		this.diskLength = diskLength;
		this.start = start;
	}
	
	// console
	public IORequest(int pnumber, int scall)
	{
		this.processNumber = pnumber;
		this.systemCall = scall;
	}

	public int getProcessNumber() {
		return processNumber;
	}

	public void setProcessNumber(int processNumber) {
		this.processNumber = processNumber;
	}

	public int getSystemCall() {
		return systemCall;
	}

	public void setSystemCall(int systemCall) {
		this.systemCall = systemCall;
	}

	public int getDiskLength() {
		return diskLength;
	}

	public void setDiskLength(int diskLength) {
		this.diskLength = diskLength;
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
}
