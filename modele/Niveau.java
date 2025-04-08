/**
 * Un objet de la classe Niveau représente un niveau du jeu Pet Rescue Saga.
 * Un niveau est défini par un plateau, une ou des conditions de victoire, des bonus, et éventuellement un nombre maximal de déplacement (dans le cas contraire ce champ sera égal à -1)
 * Lors du déroulement d'une 'partie', l'instance de Niveau est modifiée. Cette classe garde donc également la progression du joueur dans le niveau en question.
 */
package modele;
import java.io.*;
import java.util.ArrayList;

public class Niveau implements Serializable{

	private static final long serialVersionUID = 1807100602185532691L;
	private Plateau plateau;
	private ArrayList<ConditionVictoire> condition;
	private int nbDeplacement;
	private int score;
	private ArrayList<Bonus> tabBonus;
	private int numero;
	private boolean remplir;
	private int meilleurScore=0;
	private int animauxSauves;

	/**
	 * Constructeur créant un niveau dont tous les attributs sont null sauf le numéro du niveau définit par n
	 * @param n numéro du niveau
	 */

	public Niveau(int n) {
		this.plateau=null;
		this.condition=null;
		this.nbDeplacement=0;
		this.score=0;
		this.tabBonus=null;
		this.numero=n;
	}

	public Niveau(int numero, Plateau plateau, ArrayList<ConditionVictoire> condition, int nDeplacement, ArrayList<Bonus> tab, boolean remplir){
		this.plateau=plateau;
		this.condition=condition;
		this.nbDeplacement=nDeplacement;
		this.score=0;
		this.tabBonus=tab;
		this.remplir=remplir;
		this.numero=numero;
	}

	/**
	 * Affichage des conditions de victoire
	 */
	public void afficheConditions() {
		for(ConditionVictoire c : this.condition) System.out.println(c);
	}
	
	/**
	 * Affichage textuel du niveau
	 */
	public String toString() {
		afficheConditions();
		System.out.println("Score: "+this.score+"\nNombre de déplacement restant: "+this.nbDeplacement+"\n");
		System.out.println(plateau);
		String listBonus = tabBonus.toString();
		System.out.println("Bonus: "+listBonus);
		return("");
	}

	//Getters

	/**
	 * Renvoie le plateau
	 * @return le plateau du niveau
	 */
	public Plateau getPlateau(){
		return plateau;
	}

	/**
	 * Renvoie le numéro
	 * @return le numéro du niveau
	 */

	public int getNumero() {
		return numero;
	}

	/**
	 * Renvoie le nombre d'animaux sauvés
	 * @return le nombre d'animaux sauvés
	 */

	public int getAnimauxSauves() {
		return animauxSauves;
	}

	/**
	 * Renvoie le score obtenu
	 * @return le score obtenu
	 */	
	public int getScore() {
		return score;
	}
	
	/**
	 * Renvoie le meilleur score obtenu à ce niveau
	 * @return le meilleur score obtenu à ce niveau
	 */
	public int getMeilleurScore() {
		return this.meilleurScore;
	}
	

	/**
	 * Renvoie le nombre de déplacements restants
	 * @return le nombre de déplacements restants
	 */
	public int getNbDeplacement() {
		return nbDeplacement;
	}
	
	/**
	 * Renvoie la liste des bonus disponibles du niveau
	 * @return la liste des bonus disponibles du niveau
	 */
	public ArrayList<Bonus> getTabBonus() {
		return tabBonus;
	}

	/**
	 * Renvoie la n-ième condition de victoire 
	 * @return la n-ième condition de victoire
	 */
	public ConditionVictoire getCondition(int n) {
		return condition.get(n);
	}
	
	/**
	 * Renvoie la liste des conditions de victoire
	 * @return la liste des conditions de victoire
	 */
	public ArrayList<ConditionVictoire> getConditions(){
		return condition;
	}

	//Setters

	/**
	 * Ajoute la valeur n au score
	 * @param n la valeur a ajouté au score
	 */
	public void setScore(int n) {
		this.score+=n;
	}
	
	/**
	 * Return true si b est remove de tabBonus, sinon false
	 */
	public boolean setTabBonus(Bonus b) {
		if(tabBonus.remove(b)) {
			return true;
		}
		return false;
	}


	public boolean hasMarteau(){
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Marteau) return true;
		}
		return false;
	}

	/**
	 * Vérifie si la liste des bonus contient au moins une fusée
	 * @return true si elle contient une fusée, faux sinon
	 */

	public boolean hasFusee(){
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Fusee) return true;
		}
		return false;
	}

	/**
	 * Vérifie si la liste des bonus contient au moins une bombe
	 * @return true si elle contient une bombe, faux sinon
	 */

	public boolean hasBombe(){
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Bombe) return true;
		}
		return false;
	}

	/**
	 * Vérifie si la liste des bonus contient au moins une paire de tenailles
	 * @return true si elle contient une paire de tenailles, faux sinon
	 */
	public boolean hasTenailles(){
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Tenailles) return true;
		}
		return false;
	}

	/**
	 * Renvoie le premier marteau de la liste s'il y en a un
	 * @return le premier marteau de la liste s'il y en a un, null sinon
	 */
	public Bonus getMarteau(){
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Marteau) return bonus;
		}
		return null;
	}

	/**
	 * Renvoie la première fusée de la liste s'il y en a un
	 * @return la première fusée de la liste s'il y en a un, null sinon
	 */
	public Bonus getFusee(){
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Fusee) return bonus;
		}
		return null;
	}

	/**
	 * Renvoie la première bombe de la liste s'il y en a un
	 * @return la première bombe de la liste s'il y en a un, null sinon
	 */
	public Bonus getBombe(){
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Bombe) return bonus;
		}
		return null;
	}

	/**
	 * Renvoie la première paire de tenailles de la liste s'il y en a un
	 * @return la première paire de tenailles de la liste s'il y en a un, null sinon
	 */
	public Bonus getTenailles(){
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Tenailles) return bonus;
		}
		return null;
	}

	/**
	 * @return le nombre de marteaux dans la liste
	 */
	public int nbMarteau() {
		int c=0;
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Marteau) c++;
		}
		return c;
	}
	
	/**
	 * @return le nombre de fusées dans la liste
	 */
	public int nbFusee() {
		int c=0;
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Fusee) c++;
		}
		return c;
	}
	
	/**
	 * @return le nombre de bombes dans la liste
	 */
	public int nbBombe() {
		int c=0;
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Bombe) c++;
		}
		return c;
	}

	/**
	 * @return le nombre de paires de tenailles dans la liste
	 */
	public int nbTenailles() {
		int c=0;
		for (Bonus bonus: tabBonus){
			if (bonus instanceof Tenailles) c++;
		}
		return c;
	}

	//******************************************************************************************************


	/**
	 * retire 1 du nombre de déplacements, si c'est possible (s'il est supérieur à 0) sinon ne fait rien
	 */
	public void moinsUnDeplacement() {
		if(this.nbDeplacement>0) this.nbDeplacement--;
	}


	/**
	 * Réalise une étape du jeu.
	 * Une étape consiste à:
	 * *réaliser le mouvement
	 * *ajuster le plateau
	 * *tant qu'il y a des animaux sur le plancher, les sauver et incrémenter nombre d'animaux en conséquence
	 * *remplir éventuellement le plateau de blocs
	 * *actualiser le sommet du plateau
	 * *ajouter au score le nombre de points remportés
	 * @param m mouvement à effectuer
	 * 
	 */
	public void etape(Mouvement m){

		ArrayList<Bloc> p= this.plateau.contenu();
		
		if (m!=null){
			m.effectuer();
		}

		plateau.ajusterVertical();
		plateau.ajusterHorizontal();

		while (plateau.animauxPlancher()!=0){
			animauxSauves+=plateau.sauverAnimaux();
			plateau.ajusterVertical();
			plateau.ajusterHorizontal();	
		}
		if (remplir) this.plateau.remplir();
		plateau.calculeSommet();
		score+=Plateau.pointsGagnes(plateau,p);
	}


	//Méthodes permettant d'enregistrer des niveaux et des parties sur le disque

	/**
	 * Enregistre le niveau courant sous forme d'un fichier .ser de nom nom
	 * @param nom nom du fichier
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void save(String nom) throws IOException, FileNotFoundException{
		File fichier = new File(nom); //on définit un fichier de sauvegarde
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichier)); //le résultat de la sérialisation sera envoyé dans le fichier (ouverture d'un flux sur le fichier)
		oos.writeObject(this); //on passe en paramètre l'objet à sérialiser
		oos.flush(); //pour vider le tampon dans le fichier
		oos.close(); //termine l'opération
	}

	/**
	 * Enregistre le niveau courant dans le répertoire des niveaux "initiaux" 
	 * (modèle du niveau au moment de commencer la partie) et dans celui des 
	 * niveaux "entamés" (fichiers où est conservée la progression du joueur)
	 * sous le nom "niveau[n].ser" où n est le numéro du niveau
	 * @throws IOException
	 */
	public void saveNewLevel() throws IOException{
		save("sauvegarde/initial/niveau"+numero+".ser");
		save("sauvegarde/progression/niveau"+numero+".ser");
		System.out.println("Sauvegarde effectuée.");
	}

	/**
	 * Enregistre le niveau courant dans le répertoire des 
	 * niveaux "entamés" (fichiers où est conservée la progression du joueur)
	 * sous le nom "niveau[n].ser" où n est le numéro du niveau en veillant à 
	 * la conservation du meilleur score enregistré sur le disque
	 * @throws IOException
	 */
	public void saveProgression() throws IOException{	
		//int n= Math.max(score, meilleurScore);
		//meilleurScore=n;
		save("sauvegarde/progression/niveau"+numero+".ser");
	}

	/**
	 * Dans le répertoire conservant la progression du joueur, enregistre la
	 *  version "vierge" du niveau, en conservant uniquement le meilleur score
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void reinitialiseLevel() throws IOException, ClassNotFoundException{
		int n=0;
		if (this.win()){
			n= Math.max(score, meilleurScore);
		} else {
			n=this.meilleurScore;
		}
		Niveau sauv= new Niveau(numero);
		sauv= sauv.recupNiveau("sauvegarde/initial/niveau"+numero+".ser");
		sauv.meilleurScore=n;
		sauv.save("sauvegarde/progression/niveau"+numero+".ser");
	}


	/**
	 * Désérialise le niveau dont le nom est entré en paramètre
	 * @param nom nom du fichier
	 * @return le niveau sérialisé
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	 public Niveau load(String nom) throws IOException, ClassNotFoundException{
		File fichier = new File(nom);
		if(!fichier.exists() || !fichier.isFile()) { //vérifie si le fichier existe
			return null;
		}
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier)); //ouverture d'un flux sur le fichier
		Niveau niveau = (Niveau) ois.readObject();	//désérialisation de l'objet
		System.out.println("Niveau chargé");	
		ois.close(); //termine l'opération
		return niveau;
	}
	

	/**
	 * Renvoie le niveau sérialisé sous le nom nom
	 * @param nom du fichier où le fichier est sérialisé
	 * @return le niveau 
	 * @throws IOException
	 * @throws ClassNotFoundException
 	*/	
	public Niveau recupNiveau(String nom) throws IOException, ClassNotFoundException{
		File fichier = new File(nom);
		if(!fichier.exists() || !fichier.isFile()) { //vérifie si le fichier existe
			return null;
		}
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier)); //ouverture d'un flux sur le fichier
		Niveau niveau = (Niveau) ois.readObject();
		ois.close();
		return niveau;
	}
	
	//--------------------------------------------------------------------------------
	
	/**
	 * @return true si toutes les conditions de victoire sont satisfaites, false sinon
	 */
	public boolean conditionsSatisfaites() {
		for(ConditionVictoire c : condition) {
			if(!c.estSatisfaite(this)) return false;
		}
		return true;
	}

	/**
	 * @return true si le niveau est gagné
	 */
	public boolean win() {
		if(conditionsSatisfaites() && (this.nbDeplacement>=0 || this.nbDeplacement==-1)) {
			return true;
		}
		return false;
	}

	/**
	 * @return true si le niveau est perdu
	 */
	public boolean lose() {
		if(!this.plateau.mouvementPossible() || (this.nbDeplacement==0 && !conditionsSatisfaites())) {
			//System.out.println("L'objectif n'est pas atteint ! C'est perdu...");
			return true;
		}
		return false;
	}

	/**
	 * vérifie si le niveau est fini
	 * @return true si le joueur a gagné ou perdu, false sinon
	 */
	public boolean fini(){
		return (win()|| lose());
	}

}
