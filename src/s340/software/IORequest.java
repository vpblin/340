package s340.software;

public class IORequest {
	
	private int processNumber;
	private int systemCall;
	private int diskLength;
	
	public IORequest(int pnumber, int scall)
	{
		this.processNumber = pnumber;
		this.systemCall = scall;
	}
	
	public IORequest(int pnumber, int scall, int diskLength)
	{
		this.processNumber = pnumber;
		this.systemCall = scall;
		this.diskLength = diskLength;
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
	
}
