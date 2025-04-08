/**
 * Un objet de la classe AnimauxSauves représente une condition de victoire :
 * tous les animaux doivent être sauvés, i.e. il n'y en a plus sur le plateau.
 * Il est défini par un booléen de victoire.
 */
package modele;
public class AnimauxSauves extends ConditionVictoire{
  
	private static final long serialVersionUID = -4923244191758949689L;

	protected boolean victoire;
	
	/**
	 * Constructeur mettant à false le boolean victoire
	 */
	public AnimauxSauves() {
		this.victoire=false;
	}
	
	/**
	 * @return true si la condition de victoire est satisfaite, false sinon
	 */
	@Override
	public boolean estSatisfaite(Niveau niveau) {
		if(niveau.getPlateau().nbAnimal()==0) {
			this.victoire=true;
			return true;
		}
		return false;
	}
	
	/**
	 * @return un String de la condition de victoire
	 */
	@Override
	public String toString() {
		return "Sauvez tous les dinosaures!";
	}
	
	/**
	 * Affiche un message de victoire en cas de victoire, sinon l'objectif
	 */
	@Override
	public void affiche() {
		if(victoire) {
			System.out.println("Tous les dinosaures ont été sauvés !");
		}else {
			System.out.println("Objectif : "+this.toString());
		}
	}
	
	
}
