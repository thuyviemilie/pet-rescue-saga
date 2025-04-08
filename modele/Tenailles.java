/**
 * Un objet de la classe Tenaille est un bonus : lorsqu'utilisé, il supprime la grille du bloc couleur engrillé choisi.
 */
package modele;

public class Tenailles extends Bonus{

	private static final long serialVersionUID = -5380798519443613799L;
	
	/**
	 * @param p Plateau du bonus
	 * @param x int coordonnée pour l'utilisation du bonus
	 * @param y int coordonnée pour l'utilisation du bonus
	 * @return true si sa position est valide, false sinon
	 */
	public boolean positionValide(Plateau p, int x, int y) {
		return (!p.getTab()[x][y].estVide() 
                && p.getTab()[x][y].getBloc() instanceof BlocCouleur 
                && ((BlocCouleur) p.getTab()[x][y].getBloc()).getGrille());
	}
	
	/**
	 * Supprime la grille du bloc couleur engrillé de coordonnées (i,j)
	 */
	@Override
	public void appliquer(Niveau n, int i, int j) {
		if(n.setTabBonus(this)) {	
			((BlocCouleur) n.getPlateau().getTab()[i][j].getBloc()).casser(i, j, n.getPlateau());
			return;
		}
	}

	@Override
	public String toString() {
		return "✂";
	}


}
