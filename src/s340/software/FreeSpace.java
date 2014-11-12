package s340.software;

public class FreeSpace implements Comparable<FreeSpace>{
	
	private int base;
	private int limit;

	public FreeSpace(int base, int limit) {
		super();
		this.base = base;
		this.limit = limit;
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
	public int compareTo(FreeSpace space) {
		// TODO Auto-generated method stub		
	       if(space.getBase() <  base) return 1;
	       if(space.getBase() ==  base) return 0;
	       return -1;

	}
	
	

}
