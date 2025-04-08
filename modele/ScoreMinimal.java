/**
 * Un objet de la classe ScoreMinimal représente une condition de victoire : un certain score doit être atteint.
 * Il est défini par un entier représentant le score à atteindre.
 */
package modele;

public class ScoreMinimal extends ConditionVictoire{

	private static final long serialVersionUID = -2009209852302565580L;
	private int score;
	
	/**
	 * Constructeur
	 * @param n nombre du score minimal à atteindre
	 */
    public ScoreMinimal(int n){
        score=n;
    }
    
    /**
     * @return true si la condition de victoire est remplie, sinon false
     */
    @Override
    public boolean estSatisfaite(Niveau niveau) {
        return (niveau.getScore()>=score);
    }
    
    /**
     * Affiche le message d'objectif
     */
    @Override
    public void affiche() {
        System.out.println("Obtenez au moins "+score+" points!");
    }
    
    /**
     * @return un String de l'objectif à atteindre
     */
    @Override
    public String toString() {
        return ("Obtenez au moins "+score+" points!");
    }
    
}