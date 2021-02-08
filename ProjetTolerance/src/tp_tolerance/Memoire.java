/* Memoire local implementation des methodes classiques SET , GET */

package tp_tolerance;

import java.util.ArrayList;

public class Memoire {
	private ArrayList<Double> memoire;
	int n;
	int i;
	
	public Memoire(int n) {
		this.memoire = new ArrayList<Double>();
		
		this.n = n;
		this.i = 0;
	}

	public ArrayList<Double> getMemoire(){
		return this.memoire;
	}
	
	public void ajouter(double x) {
		
		if(this.memoire.size()>=n)
		{
			this.memoire.set(i, x);
		} else {
			this.memoire.add(x);
		}
		

		System.out.println("|||||||||||||||||||||||||| TEST MEMOIRE : "+this.memoire);
		i = (i+1)%n;
	}

	public void setMem(ArrayList<Double> mem) {
		this.memoire = mem;
	}
	
	public ArrayList<Double> getMem() {
		return this.memoire;
	}
	public void clearMem() {
		this.memoire = new ArrayList<Double>();
	}
}
