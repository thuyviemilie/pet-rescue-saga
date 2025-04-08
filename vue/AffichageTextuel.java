/**
 * Un objet de la classe AffichageTextuel représente l'interface textuel du jeu Pet Rescue Saga.
 * Il est défini par un scanner afin de communiquer avec le joueur à travers le terminal.
 */
package vue;
import modele.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class AffichageTextuel extends VueGenerale implements Serializable {

	private static final long serialVersionUID = -2181178558421337021L;
	transient Scanner sc=new Scanner(System.in);
	
	/**
	 * Met à jour le niveau
	 */
    @Override
    public void update() {
        System.out.println(niveau);
    }

    @Override
    public void request() {
        if(!niveau.fini()) {
        	creationMouvement();
            notifier();
        }
    }
    
    /**
     * Demande au joueur s'il souhaite ou non utiliser un bonus puis les coordonnées de son prochain mouvement
     */
    public void creationMouvement(){
        courant= new Mouvement(niveau);
        System.out.println("Voulez-vous utiliser un bonus? (O/n). Vous pouvez également demander un indice en répondant par 'i', lancer le bot aléatoire en répondant par 'b', abandonner en répondant 'exit' ou sauvegarder et quitter en répondant 'sq'.");
        String rep=sc.nextLine();
        while (!rep.equals("O") && !rep.equals("n") && !rep.equals("i") && !rep.equals("b") && !rep.equals("exit") && !rep.equals("sq")){
            System.out.println("Veuillez répondre par O, n, i, b, sq ou exit");
            rep=sc.nextLine();
        }

        if (rep.equals("n")){
            System.out.println("Quel bloc voulez-vous détruire?(Entrez des coordonnées du type A3,C7,...)");
            setCoordinate(courant);
        } else if (rep.equals("O")){
            System.out.println("Lequel?");
            courant.setBonus(this.choixBonus());
            System.out.println("À quelle position?(Entrez des coordonnées du type A3,C7,...)");
            setCoordinate(courant);
            while (!courant.getBonus().positionValide(niveau.getPlateau(), courant.getX(), courant.getY())){
                System.out.println("Cette position n'est pas possible");
                setCoordinate(courant);
            }
        } else if (rep.equals("i")){
            System.out.println(indice());
            creationMouvement();
        } else if (rep.equals("b")){
            controleur.randomBot();
        } else if(rep.contentEquals("exit")) {
        	this.accueil();
        } else if(rep.equals("sq")) {
        	try {
				niveau.saveProgression();
			} catch (IOException e) {
				System.out.println("La progression n'a pas pu être sauvegardé");
			}finally {
				this.accueil();
			}
        	
        }
        
    }


    /**
     * Renvoie l'emplacement du bloc qui rapporterait le plus de points en un coup sous la forme A2, B6 , etc.
     */
    private String indice(){
        String res="";
        
        for (int i=niveau.getPlateau().getSommet(); i<niveau.getPlateau().getSommet()+9; i++){
            for (int j=0; j<9; j++){
                if (!niveau.getPlateau().getTab()[i][j].estVide() 
                && niveau.getPlateau().getTab()[i][j].getBloc().equals(niveau.getPlateau().meilleurChoix())){
                    res+=((char) (i-niveau.getPlateau().getSommet()+65));
                    res+=Integer.toString(j);
                }
            }
        }

        return res;
    }


    /**
     * Setter des coordonnées (x,y) du @param m
     */
    private void setCoordinate(Mouvement m){
        String s=sc.nextLine();
        while (!coordonneesValides(s)){
            System.out.println("Veuillez entrer des coordonées valides(A3,C7,...)");
            s=sc.nextLine();
        }
        int x=(int) s.charAt(0)-65+niveau.getPlateau().getSommet();
        int y=Character.getNumericValue(s.charAt(1));
        m.setX(x);
        m.setY(y);
    }
    
    /**
     * @param s String coordonnées
     * @return true si les coordonnées s sont valides, false sinon
     */
    private boolean coordonneesValides(String s){
        if (s.length()!=2) return false;
        if (((int) s.charAt(0))>73 ||((int) s.charAt(0))<65 ||Character.getNumericValue(s.charAt(1))>8 ||Character.getNumericValue(s.charAt(1))<0) return false;
        return true;
    }
    
    /**
     * @param chaine String
     * @return true si chaine est uniquement composé de valeur numérique (0,1,2,...), false sinon
     */
    public boolean estUnEntier(String chaine) {
		try {
			Integer.parseInt(chaine);
		} catch (NumberFormatException e){
			return false;
		}
 
		return true;
	}
    
    /**
     * Vérifie si la sélection du bonus est correcte
     * @return le bonus sélectionné
     */
    private Bonus choixBonus(){
        for (int i=0; i<niveau.getTabBonus().size(); i++){
            System.out.println(i+":"+niveau.getTabBonus().get(i));
        }
        String rep=sc.nextLine();
        while(!estUnEntier(rep) || Integer.parseInt(rep)<0 || Integer.parseInt(rep)>=niveau.getTabBonus().size()) {
        	System.out.println("Veuillez sélectionner un bonus.");
        	rep=sc.nextLine();
        }
        boolean b=false;
        Bonus res=null;
        while (!b){
            res=niveau.getTabBonus().get(Integer.valueOf(rep));
            b=niveau.getTabBonus().contains(res);
        }
        return res;
    }
    
    /**
     * Affiché à la fin d'une partie et permet de revenir à l'accueil
     */
    @Override
    public void jeuFini() {
        if (niveau.win()){
            System.out.println("Gagné!");
            System.out.println("Score: "+niveau.getScore());
            try {
				niveau.saveProgression();
				niveau.reinitialiseLevel();
			} catch (IOException e) {
				System.out.println("La progression n'a pas pu être sauvegardé");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Le niveau n'a pas pu être réinitialisé");
				e.printStackTrace();
			}

        } else if (niveau.lose()){
            System.out.println("Perdu...");
            System.out.println("Vous n'avez plus de mouvement possible et/ou vous n'avez pas accompli votre mission :(");
        }
        System.out.println("Souhaitez-vous revenir à l'accueil? o/n");
        String rep=sc.nextLine();
        while(!rep.equals("o") && !rep.equals("n")) {
        	System.out.println("Veuillez entrer une réponse valide: o ou n");
        	rep=sc.nextLine();
        }
        if(rep.equals("o")) this.accueil();
        else if(rep.equals("n")) return;

    }
    
    /**
     * Affiche l'accueil textuel de Pet Rescue Saga.
     * Permet la sélection des niveaux et affiche le meilleur score atteint dans chaque niveau.
     */
    @Override
    public void accueil() {
        System.out.println("Bienvenue dans Pet Rescue Saga!");
        System.out.println("Principe: Les dinosaures sont en danger d'extinction, vous devez les sauver en détruisant des blocs, jusqu'à ce qu'ils arrivent sains et saufs sur l'arche de Noé!");
        System.out.println("Liste des niveaux :      Meilleur score:");
        ArrayList<Niveau> liste= Lanceur.niveauxEntames();
        int c=0;
        for(int i=1; i<=liste.size(); i++) {
        	int score = 0;
        	for(Niveau n : liste) {
        		if(n.getNumero()==(i)) {
        			score=n.getMeilleurScore(); 
        			if(score!=0) c++;
        		}
        	}
        	System.out.println("----------");
        	System.out.println("|NIVEAU "+(i)+"|                       "+score);
        	System.out.println("----------");
        	if(i<4) {
        		System.out.println("    ||");
        		System.out.println("    ||");
        	}
        }
        if(c==liste.size()) {
        	System.out.println("\uD83C\uDFC6");
            System.out.println("Félicitations, vous avez finis tous les niveaux et sauvés les dinosaures d'une extinction irréversible! Vous êtes libre de refaire les niveaux autant de fois que vous le voulez :D");
        }
      
        System.out.println("Entrez le numéro du niveau que vous voulez commencer (1,2,3,etc), ou 'quit' pour quitter le jeu.");
        String rep=sc.nextLine();
        while(!numeroNiveauValide(rep) && !rep.equals("quit")) {
        	System.out.println("Veuillez entrer un niveau valide ou quit pour quitter le jeu");
        	rep=sc.nextLine();
        }
        if(rep.equals("quit")){
            System.out.println("Bybye!");
            System.exit(0);
        } 
        else {
        	c = Integer.parseInt(rep)-1;
            try {
    			liste.get(c).reinitialiseLevel();
    		} catch (ClassNotFoundException e) {
    			e.printStackTrace();
    			return;
    		} catch (IOException e) {
    			e.printStackTrace();
    			return;
    		}
            Niveau lvl = liste.get(c);
            Lanceur l = new Lanceur(this, lvl);
            l.start();
        }
        
    }

    private boolean numeroNiveauValide(String s){
        
        if (!estUnEntier(s)) return false;
        ArrayList<Niveau> liste= Lanceur.niveauxEntames();
        if (Integer.parseInt(s)>liste.size() || Integer.parseInt(s)<1) return false;
        return true;
    }
    
}
