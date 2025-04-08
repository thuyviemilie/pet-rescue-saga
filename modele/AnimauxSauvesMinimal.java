/**
 * Un objet de la classe AnimauxSauvesMinimal représente une condition de victoire : un certain nombre d'animaux doivent être sauvés.
 * Il est défini par un entier représentant le nombre d'animaux saufs à atteindre.
 */

package modele;

public class AnimauxSauvesMinimal extends ConditionVictoire{

	private static final long serialVersionUID = 3517173511093129104L;
	private int nombre;
	
	/**
	 * Constructeur
	 * @param n nombre d'animaux minimal à sauver
	 */
    public AnimauxSauvesMinimal(int n){
        nombre=n;
    }
    
    /**
     * @return true si la condition de victoire est remplie, sinon false
     */
    @Override
    public boolean estSatisfaite(Niveau niveau) {
        return (niveau.getAnimauxSauves()>=nombre);
    }
    
    /**
     * Affiche le message d'objectif
     */
    @Override
    public void affiche() {
        System.out.println("Sauvez au moins "+nombre+" dinosaures");
    }
    
    /**
     * @return un String de l'objectif à atteindre
     */
    @Override
    public String toString() {
        return ("Sauvez au moins "+nombre+" dinosaures!");
    }


}
