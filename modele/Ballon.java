/**
 * Un objet de la classe Ballon représente un BlocCouleur spécial : un ballon coloré.
 * Pattern : lorsque détruit, il casse tous les blocs cassables de sa couleur
 */
package modele;

public class Ballon extends BlocCouleur{

	private static final long serialVersionUID = -481585564200217909L;
	
	/**
	 * Constructeur créant un ballon de couleur couleur (pas de grilles, ni de combo)
	 * @param couleur du ballon
	 */
	public Ballon(char couleur){
        super(couleur,false,1);
    }
	
	/**
	 * @return true si le ballon et tous les blocs cassables de sa couleur sont détruis, sinon false
	 */
    @Override
    public boolean detruire(int x, int y, Plateau p){       
        p.casserCouleur(couleur);
        p.getTab()[x][y].vider();
        return true;
    }
    
    /**
     * @return true, car peut être détruit
     */
    @Override
    public boolean destructionPossible(int x, int y, Plateau p) {
        return true;
    }
    
    public String toString(){
    	return "("+this.couleur+")";
    }
    
}
