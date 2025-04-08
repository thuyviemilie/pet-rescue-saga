/**
 * Un objet de la classe Trou représente un bloc incassable et fixe dont les blocs non-fixes peuvent passer au travers.
 */
package modele;
public class Trou extends Bloc{

	private static final long serialVersionUID = -9084315653040449555L;
	
	/**
	 * Constructeur créant un trou (incassable et fixe)
	 */
	public Trou (){
		super(false, true);
	}

	public String toString(){
		return "[ ]";
	}
	
	/**
	 * @return false car incassable
	 */
	@Override
	public boolean detruire(int x, int y, Plateau p){ return false; }
	
	/**
	 * @return un trou de référence différente
	 */
	@Override
	public Bloc clone() {
		return new Trou();
	}

	
}
