/**
 * La classe Plateau représente la grille de Cases
 */
package modele;
import java.io.*;
import java.util.*;

public class Plateau implements Serializable{

	private static final long serialVersionUID = -2546021274305130836L;
	private Case[][] tab;
	private int sommet=0;

	/**
	 * Créé un plateau vide de hauteur hauteur et de largeur 9
	 */
	public Plateau(int hauteur){
		tab= new Case[hauteur][9];
		for (int i=0; i<hauteur; i++){
			for (int j=0; j<9;j++){
				tab[i][j]= new Case(i,j);
			}
		}
		calculeSommet();
	}

	/**
	 * Crée le plateau représenté par le tableau de cases tab
	 */
	public Plateau(Case[][] tab){
		this.tab=tab;
	}

	//Getters

	/**
	 * @return le tableau de cases
	 */
	public Case[][] getTab(){
		return tab;
	}

	/**
	 * 
	 * @return la ligne contenant le sommet du plateau
	 */
	public int getSommet() {
		return sommet;
	}


	//Fonctions permettant de remplir le tableau*****************************************************

	/**
	 * Place le bloc b dans la case de coordonnées (x;y)
	 * @param x ligne
	 * @param y colonne
	 * @param bloc bloc
	 */
	public void setBloc(int x, int y, Bloc b){
		tab[x][y].remplir(b);
	}

	/**
	 * Remplie la zone du coin supérieur gauche (x0;y0) au coin inférieur droit (x1;y1) de bloc identiques au bloc b
	 * @param x0 ligne du coin supérieur
	 * @param y0 colonne du coin supérieur
	 * @param x1 ligne du coin inférieur
	 * @param y1 colonne du coin inférieur
	 * @param bloc bloc à copier
 	 */
	public void setZone(int x0, int y0, int x1, int y1, Bloc b) {
		if(x0>x1) {
			int tmp=x1;
			x1=x0;
			x0=tmp;
		}
		if(y0>y1) {
			int tmp=y1;
			y1=y0;
			y0=tmp;
		}
		for(int i=x0;i<=x1;i++) {
			for(int j=y0;j<=y1;j++) {
				setBloc(i,j,b.clone());
			}
		}
	}


	// Fonctions permettant d'intéragir avec le plateau

	/**
	 * Renvoie le nombre d'Animal sur le plateau
	 * @return le nombre de blocs du type Animal sur le plateau
	 */
	public int nbAnimal() {
		int c=0;
		for(int i=0;i<tab.length;i++) {
			for(int j=0;j<tab[i].length;j++) {
				if(!tab[i][j].estVide() && tab[i][j].getBloc() instanceof Animal) {
					c++;
				}
			}
		}
		return c;
	}

	/**
	 * Vérifie si le plateau courant est vide (ne contient pas de blocs autres que les plateformes)
	 * @return true s'il est vide sinon renvoie false
	 */
	public boolean estVide(){
		for(int i=0; i<tab.length; i++) {
			for(int j=0; j<9; j++) {
				if(!tab[i][j].estVide() && tab[i][j].getBloc().estCassable()) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Vérifie si le bloc aux coordonnées (x,y) est cassable puis le casse.
	 * "casser" signifie retirer la grille ou vider la case selon le bloc
	 * @return true si le bloc aux coordonnées (x,y) est cassable puis le casse, sinon renvoie false 
	 */
	public boolean viderCassable(int x, int y) {
		if(tab[x][y].getBloc().cassable) {
			
			if (tab[x][y].getBloc() instanceof BlocCouleur){
				((BlocCouleur) tab[x][y].getBloc()).casser(x, y, this);
			} else {
				tab[x][y].vider();
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Casse tous les blocs colorés de la couleur couleur
	 * @param couleur couleur des blocs à casser
	 */
	public void casserCouleur(char couleur){

		for (int i=sommet; i<sommet+9; i++){
			for (int j=0; j<9; j++){
				if (!tab[i][j].estVide() 
					&& tab[i][j].getBloc() instanceof BlocCouleur 
					&& !(tab[i][j].getBloc() instanceof Ballon)
					&& tab[i][j].getBloc().getCouleur()==couleur){
						((BlocCouleur) tab[i][j].getBloc()).casser(i,j,this);
					}
			}
		}

	}


	/**
	 * Vérifie si on ne peut pas ajouter de blocs par le haut
	 * @return true si on ne pas ajouter de blocs par le haut, sinon renvoie false
	 */
	public boolean estPlein(){
		for (int i=0; i<9; i++){
			if (!this.colonnePleine(i)){
				return false;
			}
		}
		return true;
	}

	/**
	 * @return true si on ne peut pas ajouter des blocs au sommet de la colonne n
	 */
	private boolean colonnePleine(int n){
		for (int i=0; i<tab.length; i++){
			if (tab[i][n].estVide()) return false;
			if (!(tab[i][n].getBloc() instanceof Trou)) return true;
		}
		return true;
	}

	/**
	 * @return true si la colonne n est vide
	 */
	public boolean colonneVide(int n) {
		for(int i=0; i<tab.length; i++) {
			if(!tab[i][n].estVide() && tab[i][n].getBloc().estCassable()) {
				return false;
			}
		}
		return true;
	}



	/**
	 * Ajoute un bloc nouveau bloc au sommet de la colonne colonne
	 * @param colonne colonne à laquelle ajouter le bloc
	 * @param nouveauBloc bloc à ajouter
	 */
	public void ajouterCase (int colonne, Bloc nouveauBloc){
		int i=0;
		while (i<tab.length && (tab[i][colonne].estVide() || tab[i][colonne].getBloc() instanceof Trou)){
			i++;
		}
		if (i>0){
			int j=1;
			while (tab[i-j][colonne].getBloc() instanceof Trou){
				j++;
			}
			if (i-j>=0) tab[i-j][colonne].remplir(nouveauBloc);
		}
	}
	

	/**
	 * Ajoute des blocs aléatoires au sommet de chaque colonne jusqu'à ce que le plateau soit plein
	 */
	public void remplir(){
		for (int i=0; i<9; i++){
			while (!this.colonnePleine(i)){
				this.ajouterCase(i, new BlocCouleur());
			}
		}
	}



	//Méthodes permettant d'ajuster le tableau (de remplir les vides laissés par les blocs détruits)*************************


	/**
	 * @return true si le bloc (non fixe) situé aux coordonées (x;y) ne 'flotte' pas
	 */
	private boolean blocPose(int x, int y){
		if (tab[x][y].estVide() || tab[x][y].getBloc().estFixe()) return true;
		if (x==tab.length-1 || tab[x+1][y].getBloc() instanceof Plateforme) return true;
		for (int i=x+1; i<tab.length; i++){
			if (!(tab[i][y].getBloc() instanceof Trou)){
				if (tab[i][y].estVide()) return false;
				else return true;
			}
		}
		return true;
	}

	/**
	 * Fait "tomber" les blocs de la colonne n
	 * @param n colonne à ajuster
	 */
	private void ajusterVerticalColonne(int n){
		for (int i=tab.length-1; i>-1; i--){
			if (!this.blocPose(i,n)){
				int j=i+1;
				while (j<tab.length && (tab[j][n].estVide() || tab[j][n].getBloc() instanceof Trou)){
					j++;
				}
				if (j>i){
					int k=1;
					while (!tab[j-k][n].estVide()){
						k++;
					}
					if (j-k>=0){
						tab[j-k][n].remplir(tab[i][n].getBloc());
						tab[i][n].vider();
					}

				}
			}
		}
	}

	/**
	 * Fait tomber tous les blocs de la grille
	 */
	 public void ajusterVertical(){
		 for (int i=0; i<9; i++){
			 this.ajusterVerticalColonne(i);
		 }
	 }

	/**
	 * Décale tous les blocs d'une colonne n vers la gauche
	 * @param n colonne à ajuster
	 */
	public void decaleColonne(int n){

		for (int i=0; i<tab.length; i++){
			tab[i][n-1].remplir(tab[i][n].getBloc());
			tab[i][n].vider();
		}

	}

	/**
	 * Remplit la colonne j-1 en décalant les bloc de la colonne j à gauche
	 */
	public void ajusterHorizontalColonne(int j){
		if (!tab[sommet+8][j].estVide() && !(tab[sommet+8][j].getBloc() instanceof Plateforme)){
			int k=sommet+8;
				while (!tab[k][j].estVide() && (tab[k][j].getBloc() instanceof Trou)){
					k--;
				}
				if (!tab[k][j].estVide() && !(tab[k][j].getBloc().fixe)){
					int n=espaceVideColonne(k, j-1);
					int m=caseColonne(k, j);
					if (n>=m){
						ArrayList<Bloc> deplaces=viderPortionColonne(k, j);
						remplirPortionColonne(k, j-1, deplaces);
					}
				}
		}

		for (int i=sommet+8; i>sommet; i--){
			if (!tab[i][j].estVide() && (tab[i][j].getBloc() instanceof Plateforme) && !tab[i-1][j].estVide() && !(tab[i-1][j].getBloc() instanceof Plateforme)){
				int k=i-1;
				while (!tab[k][j].estVide() && (tab[k][j].getBloc() instanceof Trou)){
					k--;
				}
				if (!tab[k][j].estVide() && !(tab[k][j].getBloc().fixe)){
					int n=espaceVideColonne(k, j-1);
					int m=caseColonne(k, j);
					if (n>=m){
						ArrayList<Bloc> deplaces=viderPortionColonne(k, j);
						remplirPortionColonne(k, j-1, deplaces);
					}
				}
			}
		}
	}
	
	/**
	 * En partant d'un bloc de coordonnée (i,j), compte le nombre de cases pouvant être remplies par les blocs de la colonne adjacente
	 * @param i ligne du bloc de départ
	 * @param j colonne du bloc de départ
	 * @return le nombre de cases libres
	 */

	private int espaceVideColonne(int i, int j){
		int res=0;
		int k=i;
		while (k>sommet-1 && (tab[k][j].estVide() ||(!tab[k][j].estVide() && tab[k][j].getBloc() instanceof Trou))){
			if (tab[k][j].estVide()){
				res++;	
			}
			k--;
		}
		k=i+1;
		while (k<sommet+8 && (tab[k][j].estVide() ||(!tab[k][j].estVide() && tab[k][j].getBloc() instanceof Trou))){
			if (tab[k][j].estVide()){
				res++;	
			}
			k++;
		}
		return res;
	}

	/**
	 * En partant d'une case de coordonnées (i,j) compte le nombre de cases pouvant être déplacées vers la colonne à gauche
	 * @param i ligne
	 * @param j colonne
	 * @return le nombre de blocs à déplacer
	 */
	private int caseColonne(int i, int j){
		int res=0;
		int k=i;
		while (k>sommet-1 && (tab[k][j].estVide() ||(!tab[k][j].estVide() && !(tab[k][j].getBloc() instanceof Plateforme)))){
			if (!tab[k][j].estVide() && !tab[k][j].getBloc().fixe){
				res++;	
			}
			k--;
		}
		return res;
	}

	/**
	 * vide la portion de la colonne commençant par la ligne i et délimitée par une plateforme
	 * @param i ligne de départ
	 * @param j colonne concernée
	 * @return les blocs pouvant être déplacés vers la colonne à gauche
	 */
	private ArrayList<Bloc> viderPortionColonne(int i, int j){
		ArrayList<Bloc> res= new ArrayList<Bloc> ();
		int k=i;
		while (k>sommet-1 && (tab[k][j].estVide() ||(!tab[k][j].estVide() && !(tab[k][j].getBloc() instanceof Plateforme)))){
			if (!tab[k][j].estVide() && !tab[k][j].getBloc().fixe){
				res.add(tab[k][j].getBloc());
				tab[k][j].vider();	
			}
			k--;
		}
		return res;
	}

	/**
	 * Remplit la portion de la colonne j délimitées par des blocs (non trou) avec les blocs de la liste déplacés
	 * @param i ligne de départ
	 * @param j colonne de départ
	 * @param deplaces blocs à déplacer
	 */
	private void remplirPortionColonne(int i, int j, ArrayList<Bloc> deplaces){
		int k=i;
		while (k<sommet+9 && (tab[k][j].estVide() ||(!tab[k][j].estVide() && tab[k][j].getBloc() instanceof Trou))){
			k++;
		}		
		k--;
		while (k>sommet-1 && (tab[k][j].estVide() ||(!tab[k][j].estVide() && tab[k][j].getBloc() instanceof Trou)) && !deplaces.isEmpty()){
			if (tab[k][j].estVide()){
				tab[k][j].remplir(deplaces.remove(0));	
			}
			k--;
		}
	}

	/**
	 * Ajuste la grille horizontale (remplit les portions de colonnes vides avec les blocs de la colonne à droite, s'il y en a)
	 */
	public void ajusterHorizontal(){
		for (int i=0; i<9; i++){
			for (int j=1;j<9; j++){
				ajusterHorizontalColonne(j);
			}
		}
	}


	
	/**
	 * Renvoie les quatre blocs adjacents au bloc situé aux coordonnées (x;y)
	 */
	public ArrayList<Case> blocsAdjacents(int x, int y){
		ArrayList<Case> res= new ArrayList<Case>();
		if (x>0 && !tab[x-1][y].estVide()) {
			res.add(tab[x-1][y]); 
		}
		if (y>0 && !tab[x][y-1].estVide()) {
			res.add(tab[x][y-1]); 
		}
		if (x<tab.length-1 && !tab[x+1][y].estVide()) {
			res.add(tab[x+1][y]); 
		}
		if (y<tab[0].length-1 && !tab[x][y+1].estVide()) {
			res.add(tab[x][y+1]); 
		}
		return res;
	}
	
	/**
	 * Détruit les blocs cassables dans un rayon 3x3 depuis un bloc de coordonnées (x,y)
	 * Méthode utilisée par Bombe
	 */
	public void explosion(int x, int y){
		Plateau p = this.surround();
		int px=x+1;
		int py=y+1;
		for(int i=px-1; i<px+2; i++) {
			for(int j=py-1; j<py+2; j++) {
				if(!p.getTab()[i][j].estVide() && p.getTab()[i][j].getBloc().cassable) {
					int ti=i-1;
					int tj=j-1;
					if (p.getTab()[i][j].getBloc() instanceof BlocCouleur) ((BlocCouleur) p.getTab()[i][j].getBloc()).casser(ti,tj,this);
					else tab[ti][tj].vider();
				}
			}
		}
	}
	
	/**
	 * Détruit les blocs cassables dans la colonne y
	 * Méthode utilisée par Fusée
	 */
	public void decollage(int x, int y) {
		for(int i=sommet; i<sommet+9; i++) {
			if(tab[i][y].getBloc() instanceof BlocCouleur) this.viderCassable(i, y);
		}
	}
	
	/**
	 * Si c'est valable, détruit le bloc dans la case de coordonnées (x,y) et renvoie true, sinon renvoie false
	 */
	 //Remarque: cette méthode renvoie un boolean pour pouvoir compter les déplacements à l'avenir	
	public boolean detruireBloc(int x, int y){
		if (!this.estVisible(x)) return false;
		if (!tab[x][y].estVide()){
			return tab[x][y].getBloc().detruire(x,y,this);
		}
		return false;
	}

	/**
	 * Renvoie une copie du plateau entourée de bloc plateforme
	 */
	private Plateau surround(){
		Case[][] copieCase = new Case[tab.length+2][11];
		for(int i=0; i<tab.length; i++) {
			for(int j=0; j<9; j++) {
				copieCase[i+1][j+1]=tab[i][j];
			}
		}
		for(int i=0; i<copieCase.length; i++) {
			copieCase[i][0] = new Case(new Plateforme(),i,0);
			copieCase[i][10] = new Case(new Plateforme(),i,10);
		}
		for(int j=0; j<11; j++) {
			copieCase[0][j] = new Case(new Plateforme(),0,j);
			copieCase[copieCase.length-1][j] = new Case(new Plateforme(),copieCase.length-1,j);
		}
		Plateau copiePlateau = new Plateau(copieCase);
		return copiePlateau;
	}

	/**
	 * Return true s'il reste des blocs à détruire sur le plateau entier
	 */
	public boolean mouvementPossible() {
		for (int i=sommet; i<sommet+9; i++){
			for (int j=0; j<9; j++)	{
				Niveau n= new Niveau(0,this, null,-1,null, false);
				Mouvement m= new Mouvement(n);
				m.setX(i);
				m.setY(j);
				if (m.estPossible()) return true;
			}
		}

		return false;
	}

	/**
	 * Met à jour la valeur du sommet (plus haute ligne contenant un bloc non fixe)
	 */
	public void calculeSommet(){
		for (int i=0; i<tab.length; i++){
			for (int j=0; j<9; j++){
				if (!tab[i][j].estVide() && !tab[i][j].getBloc().fixe){
					if (tab.length-i>9){
						sommet=i;
					} else {
						sommet=tab.length-9;
					}
					return;
				}
			}
		}
	}


	/**
	 * Renvoie une copie du plateau (les blocs ayant la même réference que ceux du tableau courant)
	 * @return la copie du plateau courant
	 */
	public Plateau clone (){
		Case[][] copie= new Case[tab.length][9];
		for (int i=0; i<tab.length; i++){
			for (int j=0; j<9; j++){
				copie[i][j]=tab[i][j].clone();
			}
		}
		return new Plateau(copie);
	}

	/**
	 * Vérifie que la ligne x est visible
	 * @param x ligne
	 * @return true si x est visible
	 */
	public boolean estVisible(int x){
		calculeSommet();
		return (x<sommet+9);
	}

	/**
	 * Renvoie le contenu du plateau
	 * @return une liste des blocs (non fixes) du plateau
	 */
	public ArrayList<Bloc> contenu (){
		ArrayList<Bloc> res= new ArrayList<Bloc>();

		for (Case[] l: tab){
			for (Case c: l){
				if (!c.estVide() && !c.getBloc().fixe){
					res.add(c.getBloc());
				}
			}
		}

		return res;
	}
	
	/**
	 * Renvoie les blocs qui sont dans la liste entrée en paramètre mais pas dans le tableau courant
	 * @param precedent liste des blocs (destiné à représenter les blocs du plateau avant un mouvement) 
	 * @return
	 */
	public ArrayList<Bloc> difference (ArrayList<Bloc> precedent){
		ArrayList<Bloc> courant=this.contenu();
		ArrayList<Bloc> res= new ArrayList<Bloc>();
		for (Bloc b: precedent){
			res.add(b);
		}
		res.removeAll(courant);
		return res;
	}

	/**
	 * Renvoie le score remporté en passant du plateau p au plateau courant
	 * @param p Plateau après destruction/sauvetage
	 * @param list liste des blocs du plateau avant un mouvement, 
	 */

	 public static int pointsGagnes (Plateau p, ArrayList<Bloc> list){
		int res=0;
		int nbBloc=0;
		int nbAnimaux=0;
		int coef=1;
		ArrayList<Bloc> liste=p.difference(list);
		for (Bloc b: liste){
			
			if (b instanceof Animal){
				nbAnimaux++;
			}else{
				nbBloc++;
			}
			if (b instanceof BlocCouleur){
				coef*=((BlocCouleur) b).combo;
			}
		}
		res=nbBloc*nbBloc*10*coef+(nbAnimaux*1000);
		return res;
	 }

	/**
	 * Retire les animaux arrivés au plancher
	 * @return le nombre d'animaux sauvés
	 */
	public int sauverAnimaux(){
		int res=0;
		for (int i=0; i<9; i++){
			if (!tab[tab.length-1][i].estVide() &&  tab[tab.length-1][i].getBloc() instanceof Animal ){
				tab[tab.length-1][i].vider();
				res++;
			}
		}
		return res;
	}

	/**
	 * 
	 * @return le nombre d'animaux actuellement arrivés à la dernière ligne du tableau
	 */
	public int animauxPlancher(){
		int res=0;
		for (int i=0; i<9; i++){
			if (!tab[tab.length-1][i].estVide() &&  tab[tab.length-1][i].getBloc() instanceof Animal ){
				res++;
			}
		}
		return res;
	}


	/**
	 * Renvoie le bloc visible rapportant le plus de points au joueur
	 * @return le bloc visible rapportant le plus de points au joueur
	 */
	public Bloc meilleurChoix(){
		int nbPointMax=-1;
		Bloc res=null;
		for (int i=this.sommet; i<this.sommet+9; i++){
			for (int j=0; j<9; j++){
				Plateau copie=this.clone();
				ArrayList<Bloc> temp =copie.contenu();
				Niveau n= new Niveau(0,copie,null,0,null, false);
				Mouvement m= new Mouvement(n);
				m.setX(i);
				m.setY(j);
				if (m.estPossible()){
					m.effectuer();
					int benef= pointsGagnes(copie, temp);
					if (benef>nbPointMax){
						nbPointMax=benef;
						res=this.tab[i][j].getBloc();
					}	
				}
			}
		}
		return res;
	}


	public String toString(){
		System.out.print("  ");
		for(int i=0; i<tab[0].length; i++) {
			System.out.print(" "+i+" ");
		}
		System.out.println();
		String res="";
		int ascii=65;
		for (int i=sommet; i<sommet+9; i++){
			res+=(char)ascii+" ";
			ascii++;
			for (Case carreau : tab[i]){
				res+=carreau.toString();
			}
			res+="\n";
		}
		if (sommet+9==tab.length) res+="  ▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤▤"; //ponton de Noé :)
		return res;
	}
}
