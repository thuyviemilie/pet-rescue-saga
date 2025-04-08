/**
 * Un objet de la classe BlocCouleur est défini par une couleur, un combo et s'il possède une grille.
 * Un BlocCouleur possédant une grille doit être cassé deux fois avant d'être définitivement détruit : 
 * un coup pour détruire la grille (alors à false), et un deuxième pour être détruit comme tout autre bloc lambda.
 * Pattern : lorsque détruit, il détruit les autres BlocCouleurs adjacents de la même couleur, ou casse simplement la grille d'un BlocCouleur adjacent indépendamment de sa couleur.
 */
package modele;
import java.util.ArrayList;
import java.util.Random ;

public class BlocCouleur extends Bloc{

	private static final long serialVersionUID = 410030941163843329L;
	protected final char couleur;
	protected final int combo;
	protected boolean grille;
	
	/**
	 * Constructeur créant un BlocCouleur de couleur couleur (cassable et non fixe) et de combo 1
	 * @param couleur
	 */
	public BlocCouleur(char couleur){
		super(true,false);
		this.couleur=couleur;
		this.combo=1;
	}
	
	/**
	 * Constructeur créant un BlocCouleur de couleur couleur (cassable et non fixe), de combo combo et avec grille si grille=true
	 * @param couleur
	 * @param grille 
	 * @param combo
	 */
	public BlocCouleur(char couleur,boolean grille, int combo){
		super(true,false);
		this.couleur=couleur;
		this.grille=grille;
		this.combo=combo;
	}


	/**
	 * Constructeur créant un bloc d'une couleur aléatoire :
	 * jaune (y), bleu (b), rouge (r), violet (p), vert (g)
	 */
	public BlocCouleur() {
		super(true, false);
		Random rd = new Random();
		int n = rd.nextInt(5);
		int d= rd.nextInt(2)+1;
		boolean g= (rd.nextInt(4)==1);
		if(n==0){ this.couleur='y'; }
		else if(n==1){ this.couleur='b'; }
		else if(n==2){ this.couleur='r'; }
		else if(n==3){ this.couleur='p'; }
		else if(n==4){ this.couleur='g'; }
		else { this.couleur='E'; } //Error
		combo=d;
		grille=g;
	}
	
	//Getter & Setter
	
	/**
	 * @return si le BlocCouleur a un grille
	 */
	public boolean getGrille(){
		return grille;
	}
	
	/**
	 * @return le nombre du combo
	 */
	public int getCombo() {
		return combo;
	}
	
	/**
	 * @return la couleur du BlocCouleur
	 */
	@Override
	public char getCouleur() {
		return couleur;
	}
	
	/**
	 * Modifie le champ grille avec le booléen grille
	 * @param grille
	 */
	public void setGrille(boolean grille) {
		this.grille = grille;
	}
	
	//Fin getter & setter

	/**
	 * Détruit les blocs qui entourent et qui sont de la même couleur que le bloc de coordonnées (x,y) dans le plateau p 
	 */
	@Override
	public boolean detruire(int x, int y, Plateau p){
		return this.detruire(x, y, p, false);
	}

	/**
	 * 
	 * @param x	
	 * @param y
	 * @param p
	 * @param s
	 * @return true si le BlocCouleur est détruit selon son pattern
	 */
	public boolean detruire(int x, int y, Plateau p,boolean s){
			boolean b=s;
			if (grille){
				return false;
			}
			for (Case c : p.blocsAdjacents(x,y)){
				if (c.getBloc() instanceof BlocCouleur && ((BlocCouleur) c.getBloc()).couleur==couleur && !(((BlocCouleur) c.getBloc()).grille)){
					b=true;
				}
			}
			if (b){
				ArrayList<Case> t= p.blocsAdjacents(x, y);
				p.getTab()[x][y].vider();
				for (Case c : t){
					if (c.getBloc() instanceof BlocCouleur && ((BlocCouleur) c.getBloc()).couleur==couleur){
						((BlocCouleur) c.getBloc()).detruire(c.getX(),c.getY(),p,true);
					}
					if (c.getBloc() instanceof BlocCouleur && ((BlocCouleur) c.getBloc()).grille){
						((BlocCouleur) c.getBloc()).grille=false;
					}

				}
			}
			return b;
	}
	
	/**
	 * @return true si le BlocCouleur peut être détruit, false sinon
	 */
	@Override
	public boolean destructionPossible(int x, int y, Plateau p){

		if (grille) return false;

		for (Case c : p.blocsAdjacents(x,y)){
			if (c.getBloc() instanceof BlocCouleur && ((BlocCouleur) c.getBloc()).couleur==couleur && !(((BlocCouleur) c.getBloc()).grille)){
				return true;
			}
		}

		return false;
	
	}
	
	/**
	 * Détruit les BlocCouleurs selon son pattern
	 * @param x
	 * @param y
	 * @param p
	 */
	public void casser(int x, int y, Plateau p){
		if (grille){
			grille=false;
			return;
		} else {
			p.getTab()[x][y].vider();
		}
	}

	public String toString(){
		if (grille){
			return "{"+couleur+"}";
		} 
		return " "+couleur+" ";
	}
	
	/**
	 * @return un BlocCouleur aux attributs identiques mais au référencement différent
	 */
	@Override
	public Bloc clone() {
		return new BlocCouleur(this.couleur, this.grille, this.combo);
	}

}
