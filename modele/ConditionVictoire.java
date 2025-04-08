/**
 * La classe ConditionVictoire est une classe abstraite. Elle regroupe toutes les conditions nécessaires à la victoire des niveaux.
 */
package modele;
import java.io.*;

public abstract class ConditionVictoire implements Serializable{

	private static final long serialVersionUID = 8010349947233404110L;
	
	//Méthodes abstraites redéfinies dans les sous-classes
	
	/**
	 * vérifie si la condition de victoire est satisfaite
	 * @param niveau niveau testé
	 * @return true si la condition est satisfaite pour le niveau n
	 */
	public abstract boolean estSatisfaite(Niveau niveau); 

	/**
	 * affiche l'objectif
	 */
	public abstract void affiche();


}
