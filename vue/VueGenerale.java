/**
 * VueGenerale est une classe abtraite qu'étendent les classes AffichageTextuel et AffichageGraphique (i.e. les interfaces textuelles et graphiques).
 */

package vue;
import modele.*;
public abstract class VueGenerale {

    protected Lanceur controleur;
    protected Niveau niveau;
    protected Mouvement courant=null;
    
    //Setter
    
    public void setControleur(Lanceur controleur) {
        this.controleur = controleur;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public void setCourant(Mouvement courant) {
        this.courant = courant;
    }
    
    //Fin Setter
    
    /**
     * Fait le coup du mouvement courant
     */
    void notifier(){
        controleur.setCurrentMove(courant);
        controleur.coup();
    }

    /**
     * Affiche l'accueil
     */
    public void accueil(){}

    /**
     * Met à jour la vue après l'exécution d'un mouvement
     */
    public abstract void update();

    /**
     * Affichage de fin
     */
    public abstract void jeuFini();

    /**
     * Demande le prochain mouvement
     */
    public void request(){    }

    
}
