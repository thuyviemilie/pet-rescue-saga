/**
 * Un objet de la classe Case est défini par un bloc et des coordonnées.
 * C'est dans une case que sont rangés tous les éléments de type Bloc du jeu.
 */
package modele;
import java.io.*;

public class Case implements Serializable{

	private static final long serialVersionUID = -7639736437728659990L;
	private Bloc bloc;
	private int x, y;
	
	/**
	 * Constructeur créant une case null aux coordonnées (x,y)
	 */
	public Case(int x, int y){
		this.bloc=null;
		this.x=x;
		this.y=y;
	}
	
	/**
	 * Constructeur créant une case comportant le Bloc bloc aux coordonnées (x,y)
	 * @param bloc
	 * @param x
	 * @param y
	 */
	public Case(Bloc bloc,int x,int y) {
		this.bloc=bloc;
		this.x=x;
		this.y=y;
	}
	
	
	//Getters
	
	/**
	 * 
	 * @return le bloc
	 */
	public Bloc getBloc(){
		return bloc;
	}
	
	/**
	 * 
	 * @return la coordonnée x de la case
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * 
	 * @return la coordonnée y de la case
	 */
	public int getY() {
		return y;
	}
	
	//Fin getter
	
	
	/**
	 * @return true si le bloc est null, false sinon
	 */
	public boolean estVide(){
		return bloc==null;
	}
	
	/**
	 * Met le bloc à null
	 */
	public void vider(){
		bloc=null;
	}
	
	/**
	 * Si le bloc est null, met le bloc b
	 * @param b
	 */
	public void remplir(Bloc b){
		if (this.bloc==null){
			this.bloc=b;
		}
	}

	public Case clone(){
		Case res= new Case (bloc,x,y);
		if (bloc!=null) res.bloc=bloc.clone();
		return res;
	}

	public String toString(){
		if (bloc==null){
			return "   ";
		} else {
			return bloc.toString();
		}
	}

}
