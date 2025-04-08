/**
 * Un objet de la classe AffichageGraphique représente l'interface graphique générale du jeu Pet Rescue Saga.
 * Il est défini par une fenêtre de jeu et un booléen tourJoue.
 * Il gère les différentes fenêtres : accueil, partie et les fins de partie.
 */
package vue;
public class AffichageGraphique extends VueGenerale {

    FenetreJeu fenetre;
    boolean tourJoue;
    
    /**
     * Ouvre une fenêtre d'accueil
     */
    @Override
    public void accueil() {
        FenetreAccueil f= new FenetreAccueil(controleur);
        f.setVisible(true);
    }
    
    /**
     * Met à jour fenetre
     */
    @Override
    public void update() {
        if (fenetre!=null){
            fenetre.update();
        } else {
            fenetre= new FenetreJeu(niveau,this);
            fenetre.setVisible(true);
            fenetre.update();
        }
    }

    /**
     * Affiche la fenêtre de fin d'une partie par victoire ou défaite
     */
    @Override
    public void jeuFini() {
        if (niveau.win()){
            fenetre.affichageVictoire();            
        } else if (niveau.lose()){
            fenetre.affichageDefaite();
        }
    }
    
    
    //Setter
    
    public void setTourJoue(boolean b){
        tourJoue=b;
    }
    
    public void setFenetre(FenetreJeu fenetre) {
        this.fenetre = fenetre;
    }

}
