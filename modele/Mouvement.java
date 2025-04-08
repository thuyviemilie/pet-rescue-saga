/**
 * Un objet de la classe Mouvement représente une action (déplacement ou utilisation d'un bonus) sur le plateau d'un niveau aux coordonnées (x,y). 
 * Il est définie par des coordonnées, un niveau et un bonus.
 */
package modele;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Mouvement implements Serializable {
    
	private static final long serialVersionUID = 4832661957765821302L;
    private int x,y;
    Niveau n;
    private Bonus bonus;

    /**
     * Constructeur créant un Mouvement dans le niveau n
     * @param n
     */
    public Mouvement(Niveau n){
        this.n=n;
    }
    
    /**
     * Constructeur créant un Mouvement utilisant un bonus b dans le niveau n
     * @param n
     * @param b
     */
    public Mouvement(Niveau n, Bonus b) {
        this(n);
        this.bonus=b;
    }
    
    //Getter & Setter
    
    /**
     * @return la coordonnée x
     */
    public int getX() {
        return x;
    }
    
    /**
     * @return la coordonnée y
     */
    public int getY() {
        return y;
    }
    
    /**
     * @return le bonus du Mouvement
     */
    public Bonus getBonus() {
        return bonus;
    }
    
    /**
     * Modifie le champ x par le @param x
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Modifie le champ y par le @param y
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Modifie le champ bonus par le @param bonus
     */
    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }
    
    //Fin getter & setter
    
    /**
     * réalise le mouvement sur le tableau du niveau aux coordonnées attribuées 
     */
    public void effectuer(){
        if (estPossible()){
            if(this.bonus==null) n.getPlateau().detruireBloc(x, y);
            else this.bonus.appliquer(n, x, y);
            n.moinsUnDeplacement();
        }
    }
    
    /**
     * vérifie si le mouvement est possible aux coordonnées attribuées
     * @return true s'il est possible et false sinon
     */
    public boolean estPossible(){
        if (bonus==null){
            return (!n.getPlateau().getTab()[x][y].estVide() 
            && n.getPlateau().getTab()[x][y].getBloc().destructionPossible(x, y, n.getPlateau()));  
        }
        return bonus.positionValide(n.getPlateau(), x, y);
    }

    /**
     * Renvoie un mouvement pseudo-aléatoire possible pour le niveau n
     * @param n
     */
    public static Mouvement randomMove(Niveau n){
        Random rand = new Random();
        Bonus bo=null;
        if (!n.getTabBonus().isEmpty()){
            int de= rand.nextInt(2);
            boolean b= (de==1);
            if (b){
                bo=n.getTabBonus().get(rand.nextInt(n.getTabBonus().size()));
            }
        }

        ArrayList<Mouvement> mouvementsPossibles= new ArrayList<Mouvement>();
        for (int i=n.getPlateau().getSommet(); i<n.getPlateau().getSommet()+9; i++){
            for (int j=0; j<9; j++){
                Mouvement temp= new Mouvement(n);
                temp.bonus=bo;
                temp.x=i;
                temp.y=j;
                if (temp.estPossible()){
                    mouvementsPossibles.add(temp);
                }
            }
        }
        
        while (mouvementsPossibles.size()==0){
            bo=null;
            if (!n.getTabBonus().isEmpty()){
                int de= rand.nextInt(2);
                boolean b= (de==1);
                if (b){
                    bo=n.getTabBonus().get(rand.nextInt(n.getTabBonus().size()));
                }
            }
    
            mouvementsPossibles= new ArrayList<Mouvement>();
            for (int i=n.getPlateau().getSommet(); i<n.getPlateau().getSommet()+9; i++){
                for (int j=0; j<9; j++){
                    Mouvement temp= new Mouvement(n);
                    temp.bonus=bo;
                    temp.x=i;
                    temp.y=j;
                    if (temp.estPossible()){
                        mouvementsPossibles.add(temp);
                    }
                }
            }   
        }

        return mouvementsPossibles.get(rand.nextInt(mouvementsPossibles.size()));
    }

}
