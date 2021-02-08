package tp_tolerance;

public class FailureDetector {
	private long time1, time_test;
	
	public FailureDetector() {
		this.time1 = 0;
		this.time_test = 0;
	}
	
	// timer start
	public void kick() {
		this.time1 = System.currentTimeMillis();
	}
	
	// si le temps T-1 = T retourner TRUE
	public boolean testWatchdog() {
		boolean res = this.time1 == this.time_test;
		time_test = time1;
		return res;
	}

}
