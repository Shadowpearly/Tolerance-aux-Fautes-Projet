/* CORE du projet , initialisation , creation des objets , lancement des threads. */

package tp_tolerance;

public class core {

	@SuppressWarnings({ "removal", "deprecation" })
	
	public static void main(String[] args) {
		int n = 4; //Nombre de données à traiter, mémoire glissante
		Data bdd = new Data(n); // instanciation de la memoire stable
		Capteur c = new Capteur(1); 
		
		FailureDetector fd = new FailureDetector();
		
		S s1 = new S(1, bdd, c, n, fd); // service 1
		S s2 = new S(2, bdd, c, n, fd); // service 2
		
		Memoire m1 = new Memoire(n); // memoire locale du service 1
		Memoire m2 = new Memoire(n); // memoire locale du service 2
		
		s1.setCompagnon(s2); // lien de communication S1-S2
		s1.setMemoire(m1); 
		 
		s2.setCompagnon(s1); // lien de communication S2-S1
		s2.setMemoire(m2);
		
		s1.setWait(1); //S1 attendra 1 sec avant de ré-effectuer son travail
		s2.setWait(2); //S2 attendra 2 sec avant de ré-effectuer son travail
		
		s2.setTimeMax(2); // si s1 met plus de 2 sec à répondre, s2 prendra la relève
		

		Calculateur P1 = new Calculateur(s1, m1);
		Calculateur P2 = new Calculateur(s2, m2);
				
		Thread thread1 = new Thread(P1);
		Thread thread2 = new Thread(P2);
		
		thread1.start();
		
		long start = System.currentTimeMillis();
		while( System.currentTimeMillis() - start < 1000);
		
		thread2.start();
		
		
		/* ----------------- Commentaire a desactiver si vous souhaiteriez réveiller une faute dans S1 ----------------- */
		

		/*
		 
		// attente de  secondes avant le réveille de la faute

		start = System.currentTimeMillis();
		while( System.currentTimeMillis() - start < 5*1000); //Attente de 5 sec
		System.out.println("ARRET FORCE DE S1");
		
		s1.setBug(1);
		
		start = System.currentTimeMillis();
		while( System.currentTimeMillis() - start < 3*1000); //Attente de 3 sec

		int type_s1 = s1.getType();
		
		thread1.stop();
		s1.setType(type_s1);
		P1 = new Calculateur(s1, m1);
		
		thread1 = new Thread(P1);
		thread1.start();
		 
		*/
	}

}
