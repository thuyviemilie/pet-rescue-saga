/**
 * Un objet de la classe Plateforme représente un bloc fixe et incassable donc les blocs ne peuvent pas passer à travers.
 */
package modele;
import java.io.*;

public class Plateforme extends Bloc implements Serializable{

	private static final long serialVersionUID = 1312221546397213758L;
	
	/**
	 * Constructeur créant une plateforme (incassable et fixe)
	 */
	public Plateforme (){
		super(false, true);
	}

	public String toString(){
		return "---";
	}
	
	/**
	 * @return false car incassable
	 */
	@Override
	public boolean detruire(int x, int y, Plateau p){ return false; }
	
	/**
	 * @return une Plateforme de référence différente
	 */
	@Override
	public Bloc clone() {
		return new Plateforme();
	}


}
