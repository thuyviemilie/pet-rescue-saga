/**
 * Un objet de la classe BlocBombe représente un bloc spécial : une bombe.
 * Pattern : lorsque détruit, il détruit en même temps les 8 blocs cassables adjacents à sa position.
 */
package modele;
public class BlocBombe extends Bloc{

	private static final long serialVersionUID = -2147465915631777711L;
	
	/**
	 * Constructeur créant un bloc bombe (cassable et non fixe)
	 */
	public BlocBombe(){
        super(true,false);
    }
	
	/**
	 * @return true s'il détruit selon son pattern, sinon false
	 */
    @Override
    public boolean detruire(int x, int y, Plateau p) {
        p.explosion(x, y);
        return true;
    }
    
    /**
     * @return true, car peut être détruit
     */
    @Override
    public boolean destructionPossible(int x, int y, Plateau p) {
        return true;
    }
    
    /**
     * Affiche une bombe unicode
     */
    public String toString(){
    	return " \uD83D\uDCA3";
    }
    
    /**
     * @return un BlocBombe de référence différente
     */
	@Override
	public Bloc clone() {
		return new BlocBombe();
	}
    
}
