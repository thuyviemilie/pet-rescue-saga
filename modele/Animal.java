/**
 * Un objet de la classe Animal représente un bloc objectif.
 */
package modele;

import java.util.Random;

public class Animal extends Bloc {
	
	private static final long serialVersionUID = -2487620550615253543L;
	
	/**
	 * Constructeur créant un Animal (ni cassable, ni fixe)
	 */
	public Animal(){
		super(false,false);
	}
	
	/**
	 * Affiche un t-rex unicode
	 */
	public String toString(){
		Random rd = new Random();
		int c=rd.nextInt(2);
		if(c==0) return " 🦕";
		else return " 🦖";
	}
	
	/**
	 * @return false car ne peut être détruit
	 */
	@Override
	public boolean detruire(int x, int y, Plateau p){ return false; }
	
	/**
	 * @return un Animal de référence différente
	 */
	@Override
	public Bloc clone() {
		return new Animal();
	}
}
