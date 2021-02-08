package tp_tolerance;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Capteur {
	int id;
	double constval, valmem;
	int cnt;
	boolean ack;
	ArrayList<Double> arr ;
	
	// Constructeur
	public Capteur(int id) {
		this.id = id;
		this.cnt = 0;
		this.arr = new ArrayList<>();
		this.ack = true;
		}
	
	public ArrayList<Double> getArr() {
		return arr;
	}


	public void setArr(double a) {
		
		this.arr.clear();
		for ( int i = 0 ; i < 5; i++) {
			this.arr.add(i*a);	
		}
		this.arr = arr;
	}

	// retourner une valeur aléatoire
	public double getVal() {
		Random rd;
		if(this.ack) {
			rd = new Random();
			this.valmem = rd.nextDouble();
		}
		this.ack = false;
		
		return this.valmem;
		
	}
	
	public void acquitement() {
		this.ack = true;
	}
}
