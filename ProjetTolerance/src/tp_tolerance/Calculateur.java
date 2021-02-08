package tp_tolerance;


public class Calculateur implements Runnable{
	
	private S service;
	private Memoire memoire_interne;
	
	// Constructeur
	public Calculateur(S service, Memoire m) {
		this.service = service;
		this.memoire_interne = m;
	}
	
	// run threads
	public void run() {
		while(true) {
			this.service.job();
		}
	}
}
