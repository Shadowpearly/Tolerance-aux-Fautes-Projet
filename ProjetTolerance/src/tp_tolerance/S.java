package tp_tolerance;

import java.util.Random;

public class S{
	int type, nombre_glissant, stop_S, crash, bug; // attribut temporaire necessaire au fonctionnement du service
	double res, res1, res2, res3, wait, val;
	long time_max;
	
	// Communication inter-objet
	
	FailureDetector fail_detector;
	Capteur C;
	Data bdd;
	S compagnon;
	Memoire memoire;
	
	// Constructeur
	public S(int type, Data bdd, Capteur c, int n, FailureDetector f) {
		this.type = type;
		this.bdd = bdd;
		this.C = c;
		this.nombre_glissant = n;
		this.fail_detector = f;
		this.res = 0.0;
		this.res1 = 0.0;
		this.res2 = 0.0;
		this.res3 = 0.0;
		this.stop_S = 0;
		this.wait = 1.0;
		this.crash = 1;
		this.bug = 0;
	}
	
	
	// Delay
	public void waitNext() {
		long start = System.currentTimeMillis();		
		while( System.currentTimeMillis() - start < this.wait*1000); //Attente de wait sec
	}
	
	public void setMemoire(Memoire m) {
		this.memoire = m;
	}
	
	public void setStop(int s) {
		this.stop_S = s;
	}
	
	public void setWait(double wait) { // cadence pour pouvoir voir ce qu'il se passe
		this.wait = wait;
	}
	
	public void setTimeMax(long time) { //temps en sec
		this.time_max = time*1000;
	}
	
	public void setCompagnon(S comp) {
		this.compagnon = comp;
	}
	
	
	public double calculMoy(double val) {
		int i;
		double res = val;
		Random rd;
		
		
		
		System.out.println("---------------------------------- ");
		System.out.println("Valeurs traites pour la moyenne : ");
		System.out.println(val);
		
		// recuperation des N derniere valeurs de la memoire local
		for(i=this.memoire.getMem().size()-1; i>=Math.max(this.memoire.getMem().size()-this.nombre_glissant,0); i--) {
			res += this.memoire.getMemoire().get(i);
			System.out.println(this.memoire.getMemoire().get(i));
		}
		System.out.println("- - - - - - - - - - - - - - - - -  ");
		res = res/Math.min(this.nombre_glissant, this.memoire.getMem().size()+1);
		
		// Creation d l'erreur pour faire apparaitre les differents cas de test
		if(this.crash %5 == 0 || this.crash %6 ==0 || this.crash %7 ==0) {
			rd = new Random();
			res+=rd.nextDouble();
			System.out.println("_____________________________Injection d'erreur sur la moyenne");
		}
		crash++;
		
		System.out.println("Moyenne calculée : "+res);
		System.out.println("---------------------------------- ");
		return res;
	}
	
	public boolean testunitaire()
	{
		if (! (this.res1 == this.res2) ) {
			
			//this.C.setArr(0.1);
			//this.memoire.setMem(this.C.getArr());
			
			System.out.println("Valeur recue pour moy 3 : "+this.val);
			this.res3 = calculMoy(this.val);
			if ((this.res1 == this.res3 )&& !(this.res2 == this.res3))
			{
				System.out.println("test unitaire  reussi");
				this.memoire.ajouter(this.val);
				this.res = this.res3;
				return true;
			}
			else if ( !(this.res1 == this.res3 )&& (this.res2 == this.res3) )
			{
				System.out.println("test unitaire reussi");
				this.memoire.ajouter(this.val);
				this.res = this.res3;
				return true;
			}
			else
			{
				System.out.println("failure test unitaire");
				return false;
			}
		}
		else
		{
			System.out.println("test unitaire reussi");
			this.memoire.ajouter(this.val);
			this.res = this.res1;
			return true;
		}
	}

	public void BEFORE() {
		// reveille WatchDog
		this.fail_detector.kick();
	}
	
	public void PROCEED() {		
		System.out.println("Debut Proceed \\\\ ");
		
		// lecture capteur
		this.val = this.C.getVal();
		
		System.out.println("Valeur recue pour moy 1 : "+this.val);
		
		
		this.res1 = calculMoy(this.val);
		
		// Creation bug S1 passe en mode SLEEP
		if(this.bug==1) {
			this.waitNext();
			this.waitNext();
			this.waitNext();
		}
		
		
		// Calcul de la moyenne pour une Deuxieme fois (TEST UNITAIRE)
		System.out.println("Valeur recue pour moy 2 : "+this.val);
		this.res2 = calculMoy(this.val);
		
		if(this.testunitaire()) { // test unitaire reussi
			System.out.println("AFFICHAGE ECRAN : Moyenne de : "+this.res+".");
			System.out.println("Fin Proceed \\\\ ");
			System.out.println("\n ");
		} else {
			System.out.println("Erreur test unitaire, pas de changements.");
			System.out.println("Fin Proceed \\\\ ");
			System.out.println("\n ");
		}
		
		
		
	}
	
	public void AFTER() {
		// Mise a jour memoire stable , acquittement capteur
		this.bdd.ajouter(this.val);
		this.C.acquitement();
	}
	
	public void BACKUP() {
		System.out.println("ETAT des donnees AVANT Backup :"+this.memoire.getMem());
		this.memoire.setMem(this.bdd.recover()); // MAJ de la memoire local avec les valeur de la memoire stable
		System.out.println("ETAT des donnees APRES Backup :"+this.memoire.getMem());
		
	}
	
	public void job() {
		if(this.stop_S != 1) {
			if(this.type == 1) {
				
				this.BEFORE(); // Kick sur S2
				this.PROCEED(); // capteur + calcul + affichage
				this.AFTER(); // sauvegarde dans memoire bdd
			
			} else if(this.type == 2) {
				if(this.fail_detector.testWatchdog()) { //Si le temps est immobile
					this.compagnon.setMode(0);
					System.out.println("S2 prend la main");
					this.type = 1;
					//S2 passe en mode primaire
					this.BACKUP();
				}
			} else if(this.type == 0) {
				System.out.println("__________________________S1 en mode REPARATION  _________________");
				this.reparation();
			}
		}
		this.waitNext();
	}
	
	public int getType() {
		return this.type;
	}
	
	public void setType(int t) {
		this.type = t;
	}
	
	public void reparation() {
		System.out.println("_____________________DEBUT REPARATION ________________");
		
		this.waitNext();
		this.waitNext();
		this.waitNext();
		this.waitNext();
		this.waitNext();
		
		System.out.println("_____________________FIN REPARATION ________________");
		
		this.compagnon.setMode(2);
		this.compagnon.memoire.clearMem();
		
		this.setMode(1);
		this.bug=0;
	}
	
	public void setBug(int i) {
		this.bug = i;
	}
	
	public void setMode(int i) {
		this.type = i;
	}
}
