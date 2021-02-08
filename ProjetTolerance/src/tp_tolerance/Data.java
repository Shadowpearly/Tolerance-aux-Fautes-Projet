package tp_tolerance;
import java.util.ArrayList;

public class Data {
	ArrayList<Double> donnees;
	int n;
	static int i;
	
	public Data(int n) {
		this.donnees = new ArrayList<Double>();
		
		this.n = n;
		this.i = 0;
	}
	
	// retourner les valeurs stocké dans la mémoire stable
	public ArrayList<Double> recover(){
		return this.donnees;
	}
	
	// ajouter un element dans la memoire stable de facon circulaire
	public void ajouter(double x) {
		
		
		if(this.donnees.size()>=n)
		{
			this.donnees.set(i, x);
		} else {
			this.donnees.add(x);
		}
		
		i = (i+1)%n;
	}
}
