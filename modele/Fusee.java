/**
 * Un objet de la classe Fusee est un bonus : lorsqu'utilisé, il détruit tous les blocs cassables de la colonne choisie.
 */
package modele;
import java.io.Serializable;

public class Fusee extends Bonus implements Serializable{
	
	private static final long serialVersionUID = -7294197026234042590L;
	
	/**
	 * @param p Plateau du bonus
	 * @param x int coordonnée pour l'utilisation du bonus
	 * @param y int coordonnée pour l'utilisation du bonus
	 * @return true si sa position est valide, false sinon
	 */
	@Override
	public boolean positionValide(Plateau p, int x, int y) {
		if (y<0 || y>8 || p.colonneVide(y)) return false;
		return true;
	}
	
	/**
	 * Détruit tous les blocs cassables de la colonne qui possède le bloc de coordonnées (i,j)
	 */
	@Override
	public void appliquer(Niveau n, int i, int j) {
		if(n.setTabBonus(this)) {	
			n.getPlateau().decollage(i, j);
			return;
		}
	}

	@Override
	public String toString() {
		return "🚀";
	}

}
