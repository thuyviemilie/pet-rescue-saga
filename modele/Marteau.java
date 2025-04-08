/**
 * Un objet de la classe Marteau est un bonus : lorsqu'utilisé, il détruit le bloc cassable choisi.
 */
package modele;
import java.io.Serializable;

public class Marteau extends Bonus implements Serializable{
	
	private static final long serialVersionUID = 2671705138033616771L;
	
	/**
	 * @param p Plateau du bonus
	 * @param x int coordonnée pour l'utilisation du bonus
	 * @param y int coordonnée pour l'utilisation du bonus
	 * @return true si sa position est valide, false sinon
	 */
	@Override
	public boolean positionValide(Plateau p, int x, int y) {
		if (!p.getTab()[x][y].estVide() 
			&& p.getTab()[x][y].getBloc().cassable 
			&& !(p.getTab()[x][y].getBloc() instanceof BlocCouleur && ((BlocCouleur) p.getTab()[x][y].getBloc()).getGrille())){
				return true;
			}
		return false;
	}
	
	/**
	 * Détruit le bloc cassable de coordonnées (i,j)
	 */
	@Override
	public void appliquer(Niveau n, int i, int j) {
		if(n.setTabBonus(this)) {	
			n.getPlateau().viderCassable(i, j);
		}
	}
	
	@Override
	public String toString() {
		return "🔨";
	}

}
