/**
 * La classe Bloc est une classe abstraite, elle définie si un bloc est cassable et fixe.
 */
package modele;
import java.io.*;

public abstract class Bloc implements Serializable{

	private static final long serialVersionUID = 1460511326397811914L;
	protected final boolean cassable;
	protected final boolean fixe;
	
	/**
	 * Constructeur
	 * @param cassable
	 * @param fixe
	 */
	public Bloc(boolean cassable, boolean fixe){
		this.cassable=cassable;
		this.fixe=fixe;
	}
	
	/**
	 * @return true si le bloc est cassable, false sinon
	 */
	public boolean estCassable(){
		return cassable;
	}
	
	/**
	 * @return true si le bloc est fixe, false sinon
	 */
	public boolean estFixe(){
		return fixe;
	}
	
	/**
	 * Vérifie si la destruction du bloc courant, s'il est en position (x,y) du plateau p est possible
	 * @param x int coordonnée du bloc
	 * @param y int coordonnée du bloc
	 * @param p Plateau du bloc
	 * @return false par défaut
	 */
	public boolean destructionPossible(int x, int y, Plateau p){
		return false;
	}
	
	/**
	 * Détruit le blocs et les blocs qui y sont liés (dépend du type de bloc)
	 * @return false par défaut
	 */
	public boolean detruire(int x, int y, Plateau p){
		return false;
	}

	/**
	 * return E pour empty
	 */
	public char getCouleur() {
		return 'E';
	}
	
	public abstract Bloc clone();
}
