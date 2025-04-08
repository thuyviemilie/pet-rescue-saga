/**
 * La classe Bombe est abstraite. Elle regroupe tous les bonus à usage limité du jeu :
 * Marteau, Fusée, Bombe et Tenaille.
 */
package modele;
import java.io.Serializable;

public abstract class Bonus implements Serializable{
	
	private static final long serialVersionUID = 5909869396950701060L;
	
	//Méthodes abstraites redéfinies dans les sous-classes
	
	/**
	 * Vérifie que la case en position (x,y) du plateau p est un endroit où peut être appliqué le bonus
	 * @param p plateau
	 * @param x ligne
	 * @param y colonne
	 * @return true si c'est coup possible, false sinon
	 */
	public abstract boolean positionValide(Plateau p, int x, int y);

	/**
	 * Applique le bonus sur le niveau n , au bloc de coordonnée (i,j)
	 * @param n niveau
	 * @param i ligne
	 * @param j colonne
	 */
	public abstract void appliquer(Niveau n, int i, int j);
	
	public abstract String toString();
}
