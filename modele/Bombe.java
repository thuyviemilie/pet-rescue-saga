/**
 * Un objet de la classe Bombe est un bonus : lorsqu'utilisé, il explose dans un rayon 3x3 depuis un bloc choisi et valable.
 */
package modele;
import java.io.Serializable;

public class Bombe extends Bonus implements Serializable{
	//Détruit dans une zone 3x3 les blocs cassables
	
	private static final long serialVersionUID = 7628310198949474867L;
	
	/**
	 * @param p Plateau du bonus
	 * @param x int coordonnée pour l'utilisation du bonus
	 * @param y int coordonnée pour l'utilisation du bonus
	 * @return true si sa position est valide, false sinon
	 */
	@Override
	public boolean positionValide(Plateau p, int x, int y) {
		if (p.getTab()[x][y].estVide() || !p.getTab()[x][y].getBloc().cassable 
									   || (p.getTab()[x][y].getBloc() instanceof BlocCouleur && ((BlocCouleur) p.getTab()[x][y].getBloc()).getGrille())){
										   return false;
									   }
		return true;
	}
	
	/**
	 * Détruit les blocs cassables dans un rayon 3x3 depuis un bloc de coordonnées (i,j)
	 */
	@Override
	public void appliquer(Niveau n, int i, int j) {
		if(n.setTabBonus(this)) {	
			n.getPlateau().explosion(i, j);
			return;
		}
	}

	@Override
	public String toString() {
		return "💣";
	}

}
