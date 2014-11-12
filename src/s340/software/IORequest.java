package s340.software;

public class IORequest {
	
	private int processNumber;
	private int systemCall;
	
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

	
}
