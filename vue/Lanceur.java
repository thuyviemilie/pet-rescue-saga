/**
 * La classe Lanceur permet de mettre en relation la vue générale, un niveau, un mouvement et la communication avec le joueur.
 * C'est ici que se trouve la méthode main, que l'on créée les niveaux et demandons au joueur s'il souhaite jouer à Pet Rescue Saga sur l'interface textuel ou graphique.
 */
package vue;

import modele.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.awt.EventQueue;

public class Lanceur implements Serializable{

	private static final long serialVersionUID = -8012423432042911691L;
	private VueGenerale vue;
	private Niveau niveauEnCours;
	Mouvement currentMove= null;
	Scanner sc;
	
	/**
	 * Constructeur créant un lanceur de VueGénérale vue et appelant l'accueil de l'interface, mettant les autres champs à null.
	 * @param vue
	 */
	Lanceur(VueGenerale vue){
		this.vue=vue;
		this.niveauEnCours=null;
		vue.accueil();
	}
	
	/**
	 * Constructeur créant un Lanceur avec tous les champs à null, sauf le Scanner.
	 */
	Lanceur(){
		this.sc= new Scanner(System.in);
		vue=null;
		niveauEnCours=null;
	}
	
	/**
	 * Constructeur créant un Lançeur avec une vue, un niveau et un scanner.
	 * @param vue
	 * @param niveau
	 */
	Lanceur(VueGenerale vue, Niveau niveau){
		this.vue=vue;
		this.niveauEnCours=niveau;
		sc= new Scanner (System.in);
		vue.setControleur(this);
		vue.setNiveau(niveau);
	}
	
	/**
	 * Appelle la méthode coup()
	 */
	void start(){
		coup();
	}

	
	// Getter & Setter ---------------------
	
	public VueGenerale getVue() {
		return vue;
	}

	public Niveau getNiveauEnCours() {
		return niveauEnCours;
	}

	public void setVue(VueGenerale vue) {
		this.vue = vue;
		vue.setControleur(this);
	}

	public void setCurrentMove(Mouvement currentMove) {
		this.currentMove = currentMove;
	}
	
	public void setNiveauEnCours(Niveau niveauEnCours) {
		this.niveauEnCours = niveauEnCours;
		if (niveauEnCours!=null){
			vue.setNiveau(niveauEnCours);
		}
	}
	
	//Fin Getter & Setter -------------------
	
	/**
	 * Fait un "tour" puis vérifie si le niveau est fini afin d'appeler les méthodes adaptées
	 */
	public void coup(){
		niveauEnCours.etape(currentMove);
		if (!niveauEnCours.fini()){
			vue.update();
			vue.request();
		} else {
			vue.update();
			vue.jeuFini();
		}
	}
	
	/**
	 * @return une liste des niveaux "vierges "sauvegardés
	 */
	public static ArrayList<Niveau> nouveauxNiveaux(){
		ArrayList<Niveau> res= new ArrayList<Niveau>();
		boolean b=true;
		int i=1;
		while (b){
			File f = new File ("sauvegarde/initial/niveau"+i+".ser");
			if (f.exists() && f.isFile()){
				Niveau temp= new Niveau(i);
				try {
					temp=temp.recupNiveau("niveau"+i+".ser");
				} catch (Exception e){
					return res;
				}
				res.add(temp);
				i++;
			}
			i++;
		}
		return res;
	} 
	
	/**
	 * @return une liste des niveaux entamés et sauvegardés
	 */
	public static ArrayList<Niveau> niveauxEntames(){
		ArrayList<Niveau> res= new ArrayList<Niveau>();
		boolean b=true;
		int i=1;
		while (b){
			File f = new File ("sauvegarde/progression/niveau"+i+".ser");
			if (f.exists() && f.isFile()){
				Niveau temp= new Niveau(i);
				try {
					temp=temp.recupNiveau("sauvegarde/progression/niveau"+i+".ser");
				} catch (Exception e){
					return res;
				}
				res.add(temp);
				i++;
			} else {
				return res;
			}
		}
		return res;
	}
	

	/**
	 * Réalise des mouvements aléatoires jusqu'à la fin du niveau
	 */
	public void randomBot(){
		if (niveauEnCours!=null){
				
			while (!niveauEnCours.fini()){
				Mouvement rand= Mouvement.randomMove(niveauEnCours);
				currentMove=rand;
				
				niveauEnCours.etape(currentMove);
				
				vue.update();
				
				//Les instructions mises en commentaires ci-dessous étaient supposées retarder 
				//l'exécution du tour suivant pour que l'utilisateur ait le temp d'observer chaque coup
				/*try{
					Thread.sleep(2000);
				} catch (InterruptedException e){
					System.out.println("problème");
				}*/
				

			}
			vue.jeuFini();

		}
	}

	public static void main(String[]args) throws IOException, FileNotFoundException, ClassNotFoundException {
		
		//creationNiveaux();
		Lanceur l3= new Lanceur();
		System.out.println("Vous vous apprêtez à lancer Pet Rescue Saga, voulez-vous lancer la version textuelle ou préferez-vous l'interface graphique?");
		System.out.println("0: Textuel				1:Graphique");
		String rep= l3.sc.nextLine();
		while (!rep.equals("0") && !rep.equals("1")){
			System.out.println("Veuillez répondre par 0 pour l'affichage textuel ou 1 pour l'interface graphique!");
			rep= l3.sc.nextLine();			
		}
		int r=Integer.parseInt(rep);
		if (r==0){
			l3.setVue(new AffichageTextuel());
		} else {
			l3.setVue(new AffichageGraphique());
		}
		EventQueue.invokeLater(()->{
				l3.vue.accueil();
			});
	
		/* catch (InterruptedException e) {}
		catch (InvocationTargetException e) {}
*/
		/*for (Niveau n: Lanceur.niveauxEntames()){
			System.out.println(n.getNumero());
		}*/

		
	}
	
	/**
	 * Méthode créant puis sauvegardant tous les niveaux vierges
	 * @throws IOException
	 */
	public static void creationNiveaux() throws IOException{
		
		/**
		 * WARNING: pour le bon fonctionnement de l'interface graphique, ajouter AnimauxSauvesMinimal/AnimauxSauves en premier dans la liste de condition de victoire
		 */
		//NIVEAU 4
		AnimauxSauvesMinimal a4 = new AnimauxSauvesMinimal(7);
		ArrayList<ConditionVictoire> cv4 = new ArrayList<ConditionVictoire>();
		cv4.add(a4);
		Plateau p4 = new Plateau(9);
		p4.setBloc(2, 0, new BlocCouleur('r'));
		p4.setBloc(3, 1, new BlocCouleur('g'));
		p4.setBloc(4, 1, new BlocBombe());
		p4.setBloc(7, 1, new BlocCouleur('g'));
		p4.setZone(0, 0, 7, 1, new BlocCouleur('b'));
		p4.setBloc(8, 0, new BlocCouleur('y'));
		p4.setBloc(8, 2, new BlocCouleur('y'));
		p4.setBloc(8, 6, new BlocCouleur('y'));
		p4.setBloc(8, 8, new BlocCouleur('y'));
		p4.setBloc(8, 4, new BlocBombe());
		p4.setZone(8, 1, 8, 7, new BlocCouleur('p',true,1));
		p4.setBloc(3, 2, new Animal());
		p4.setBloc(4, 2, new Plateforme());
		p4.setBloc(6, 2, new BlocCouleur('g'));
		p4.setBloc(7, 2, new Animal());
		p4.setZone(0, 2, 5, 2, new BlocCouleur('r'));
		p4.setBloc(0, 3, new BlocCouleur('g'));
		p4.setBloc(3, 3, new BlocBombe());
		p4.setBloc(6, 3, new Plateforme());
		p4.setBloc(1, 4, new BlocCouleur('r'));
		p4.setBloc(2, 4, new Animal());
		p4.setBloc(3, 4, new Plateforme());
		p4.setZone(4, 4, 4, 5, new BlocCouleur('g'));
		p4.setBloc(6, 4, new Animal());
		p4.setBloc(7, 4, new BlocCouleur('r'));
		p4.setBloc(2, 5, new BlocBombe());
		p4.setBloc(5, 5, new Plateforme());
		p4.setBloc(6, 6, new BlocCouleur('r'));
		p4.setZone(6, 5, 7, 6, new BlocCouleur('g'));
		p4.setZone(0, 3, 7, 5, new BlocCouleur('b'));
		p4.setBloc(1, 6, new Animal());
		p4.setBloc(5, 6, new Animal());
		p4.setBloc(2, 6, new Plateforme());
		p4.setZone(0, 6, 3, 6, new BlocCouleur('g'));
		p4.setBloc(4, 6, new BlocCouleur('b'));
		p4.setZone(6, 8, 7, 8, new BlocCouleur('g'));
		p4.setBloc(1, 7, new BlocBombe());
		p4.setBloc(0, 8, new Animal());
		p4.setBloc(4, 7, new Plateforme());
		p4.setBloc(4, 8, new Animal());
		p4.setBloc(0, 7, new BlocCouleur('b'));
		p4.setBloc(2, 8, new BlocCouleur('b'));
		p4.setBloc(3, 7, new BlocCouleur('b'));
		p4.setBloc(5, 8, new BlocCouleur('b'));
		p4.setBloc(7, 7, new BlocCouleur('b'));
		p4.setZone(1, 7, 6, 8, new BlocCouleur('r'));
		
		ArrayList<Bonus> tb = new ArrayList<Bonus>();
		Marteau m1=new Marteau();
		Marteau m2=new Marteau();
		Marteau m3=new Marteau();
		Bombe b1=new Bombe();
		Bombe b2=new Bombe();
		Bombe b3=new Bombe();
		tb.add(m1);
		tb.add(m2);
		tb.add(m3);
		tb.add(b1);
		tb.add(b2);
		tb.add(b3);
		Niveau lvl4 = new Niveau(4,p4, cv4, 30, tb,true);
		/*VueGenerale at= new AffichageGraphique();
		Lanceur l= new Lanceur(at,lvl4);
		l.start();*/
		lvl4.saveNewLevel();
		
		
		//NIVEAU 3
		AnimauxSauves a3 = new AnimauxSauves();
		ScoreMinimal sm3 = new ScoreMinimal(5000);
		ArrayList<ConditionVictoire> cv3 = new ArrayList<ConditionVictoire>();
		cv3.add(a3);
		cv3.add(sm3);
		Plateau p3 = new Plateau(20);
		p3.setBloc(1, 1, new Ballon('b'));
		p3.setBloc(10, 0, new Ballon('y'));
		p3.setBloc(14, 0, new Ballon('b'));
		p3.setBloc(19, 0, new BlocCouleur('p'));
		p3.setZone(18, 1, 19, 1, new BlocCouleur('p'));
		p3.setBloc(17, 1, new BlocCouleur('b'));
		p3.setBloc(16, 1, new BlocCouleur('g'));
		p3.setZone(14, 1, 15, 1, new BlocCouleur('y'));
		p3.setZone(12, 1, 13, 1, new BlocCouleur('b'));
		p3.setBloc(11, 1, new BlocCouleur('p'));
		p3.setBloc(10, 1, new BlocCouleur('g'));
		p3.setZone(0, 0, 18, 1, new Plateforme());
		p3.setBloc(0, 5, new Ballon('p'));
		p3.setZone(4, 4, 4, 5, new Animal());
		p3.setBloc(5, 3, new Ballon('p'));
		p3.setZone(1, 4, 1, 5, new Plateforme());
		p3.setZone(5, 4, 5, 5, new Plateforme());
		p3.setBloc(8, 3, new BlocCouleur('b',true,1));
		p3.setBloc(7, 2, new BlocCouleur('b',true,1));
		p3.setBloc(6, 3, new BlocCouleur('p',true,1));
		p3.setBloc(7, 4, new BlocCouleur('p',true,1));
		p3.setBloc(7, 3, new BlocCouleur('p'));
		p3.setZone(6, 2, 8, 4, new BlocCouleur('y',true,1));
		p3.setBloc(8, 5, new Animal());
		p3.setZone(9, 2, 9, 3, new BlocCouleur('p'));
		p3.setZone(10, 2, 10, 3, new BlocCouleur('y'));
		p3.setZone(9, 4, 9, 5, new Plateforme());
		p3.setBloc(12, 2, new BlocCouleur('y'));
		p3.setBloc(13, 2, new BlocCouleur('b'));
		p3.setBloc(15, 2, new BlocCouleur('b'));
		p3.setZone(16, 2, 16, 5, new BlocCouleur('y'));
		p3.setZone(11, 2, 17, 3, new BlocCouleur('g'));
		p3.setBloc(17, 4, new BlocCouleur('b'));
		p3.setBloc(18, 3, new BlocCouleur('b'));
		p3.setBloc(18, 2, new BlocCouleur('p'));
		p3.setBloc(19, 3, new BlocCouleur('y'));
		p3.setZone(17, 3, 19, 5, new Plateforme());
		p3.setBloc(11, 4, new BlocCouleur('b',true,1));
		p3.setZone(10, 4, 13, 4, new BlocCouleur('p',true,1));
		p3.setBloc(12, 5, new BlocCouleur('y'));
		p3.setZone(11, 5, 13, 5, new BlocCouleur('y',true,1));
		p3.setBloc(10, 5, new Animal());
		p3.setBloc(15, 4, new BlocCouleur('y',true,1));
		p3.setBloc(14, 5, new BlocCouleur('b'));
		p3.setZone(14, 4, 15, 5, new BlocCouleur('b',true,1));
		p3.setZone(12, 6, 12, 7, new Plateforme());
		p3.setZone(11, 6, 13, 6, new BlocCouleur('p'));
		p3.setZone(11, 7, 13, 7, new Animal());
		p3.setZone(0, 6, 19, 8, new Plateforme());
		
		ArrayList<Bonus> tb3 = new ArrayList<Bonus>();
		Fusee f1=new Fusee();
		Fusee f2=new Fusee();
		Fusee f3=new Fusee();
		Marteau m13=new Marteau();
		Marteau m23=new Marteau();
		Marteau m33=new Marteau();
		Marteau m43=new Marteau();
		Bombe b13=new Bombe();
		Bombe b23=new Bombe();
		Tenailles t13=new Tenailles();
		Tenailles t23=new Tenailles();
		tb3.add(f1);
		tb3.add(f2);
		tb3.add(f3);
		tb3.add(m13);
		tb3.add(m23);
		tb3.add(m33);
		tb3.add(m43);
		tb3.add(b13);
		tb3.add(b23);
		tb3.add(t13);
		tb3.add(t23);
		Niveau lvl3 = new Niveau(3,p3, cv4, -1, tb3,false);
		/*VueGenerale at= new AffichageGraphique();
		Lanceur l= new Lanceur(at,lvl3);
		l.start();
		*/
		lvl3.saveNewLevel();
		
		
		
		//NIVEAU 2
		AnimauxSauvesMinimal a2 = new AnimauxSauvesMinimal(4);
		ScoreMinimal sm2 = new ScoreMinimal(50000);
		ArrayList<ConditionVictoire> cv2 = new ArrayList<ConditionVictoire>();
		cv2.add(a2);
		cv2.add(sm2);
		Plateau p2 = new Plateau(9);
		p2.setBloc(0, 8, new Animal());
		p2.setBloc(2, 2, new Animal());
		p2.setBloc(2, 4, new Animal());
		p2.setBloc(2, 6, new Animal());
		p2.setBloc(4, 8, new Animal());
		p2.setZone(0,3,0,5,new Plateforme());
		p2.setBloc(1, 0, new Plateforme());
		p2.setBloc(1, 8, new Plateforme());
		p2.setZone(3, 2, 3, 3, new Plateforme());
		p2.setZone(4, 2, 4, 3, new Trou());
		p2.setZone(3, 5, 3, 6, new Plateforme());
		p2.setZone(4, 5, 4, 6, new Trou());
		p2.setZone(6, 0, 8, 0, new Plateforme());
		p2.setBloc(5, 8, new Plateforme());
		p2.setZone(6, 8, 8, 8, new Trou());
		p2.setBloc(0, 0, new Ballon('r'));
		p2.setBloc(4, 4, new BlocCouleur('r'));
		p2.setBloc(7, 1, new BlocCouleur('r'));
		p2.setBloc(4, 7, new BlocCouleur('r'));
		p2.setBloc(3, 4, new BlocCouleur('g'));
		p2.setZone(5, 4, 8, 4, new BlocCouleur('g',true,3));
		p2.setZone(5, 3, 8, 3, new BlocCouleur('b',true,2));
		p2.setBloc(5, 2, new BlocCouleur('b',true,2));
		p2.setZone(0, 6, 1, 7, new BlocCouleur('p'));
		p2.setZone(2, 8, 3, 8, new BlocCouleur('p'));
		p2.setZone(2, 7, 3, 7, new BlocCouleur('r',true,2));
		p2.setZone(2, 0, 5, 0, new BlocCouleur('b'));
		p2.setBloc(3, 1, new BlocCouleur('b'));
		p2.setZone(0, 1, 1, 1, new BlocCouleur('b'));
		p2.setBloc(0, 2, new BlocCouleur('b'));
		p2.setBloc(1, 2, new BlocCouleur('g'));
		p2.setBloc(2, 1, new BlocCouleur('g'));
		p2.setBloc(4, 1, new BlocCouleur('g'));
		p2.setBloc(8, 2, new BlocCouleur('g'));
		p2.setBloc(5, 7, new BlocCouleur('g'));
		p2.setBloc(8, 1, new BlocCouleur('p'));
		p2.setBloc(5, 6, new BlocCouleur('p'));
		p2.setBloc(8, 6, new BlocCouleur('p'));
		p2.setZone(7, 6, 7, 7, new BlocCouleur('g'));
		p2.setZone(5, 1, 7, 2, new BlocCouleur('p'));
		p2.setZone(5, 5, 8, 7, new BlocCouleur('b'));
		p2.setZone(1, 3, 2, 3, new BlocCouleur('p'));
		p2.setZone(1, 5, 2, 5, new BlocCouleur('b'));
		p2.setBloc(1, 4, new BlocBombe());
		
		ArrayList<Bonus> tb2 = new ArrayList<Bonus>();
		Fusee f12=new Fusee();
		Fusee f22=new Fusee();
		Marteau m12=new Marteau();
		Marteau m22=new Marteau();
		Marteau m32=new Marteau();
		Tenailles t12=new Tenailles();
		Tenailles t22=new Tenailles();
		Tenailles t32=new Tenailles();
		tb2.add(f12);
		tb2.add(f22);
		tb2.add(m12);
		tb2.add(m22);
		tb2.add(m32);
		tb2.add(t12);
		tb2.add(t22);
		tb2.add(t32);
		Niveau lvl2 = new Niveau(2,p2, cv2, 30, tb2,false);
		
		lvl2.saveNewLevel();
		
		/*VueGenerale at= new AffichageGraphique();
		Lanceur l= new Lanceur(at,lvl2);
		l.start();*/
		
		
		//NIVEAU 1
		AnimauxSauves a1 = new AnimauxSauves();
		ArrayList<ConditionVictoire> cv1 = new ArrayList<ConditionVictoire>();
		cv1.add(a1);
		Plateau p1 = new Plateau(9);
		p1.setBloc(2, 2, new Animal());
		p1.setBloc(2, 6, new Animal());
		p1.setZone(0, 0, 2, 8, new Plateforme());
		p1.setZone(3, 0, 8, 0, new Plateforme());
		p1.setZone(3, 8, 8, 8, new Plateforme());
		p1.setZone(3, 1, 4, 2, new BlocCouleur('r'));
		p1.setZone(5, 1, 6, 2, new BlocCouleur('g'));
		p1.setZone(3, 6, 4, 7, new BlocCouleur('b'));
		p1.setZone(5, 6, 6, 7, new BlocCouleur('y'));
		p1.setZone(3, 3, 3, 5, new BlocCouleur('g'));
		p1.setZone(4, 3, 6, 5, new BlocCouleur('p'));
		p1.setBloc(7, 1,  new BlocCouleur('r'));
		p1.setBloc(8, 1,  new BlocCouleur('r'));
		p1.setZone(7, 2, 8, 3, new BlocCouleur('b'));
		p1.setZone(7, 4, 8, 4, new BlocCouleur('g'));
		p1.setZone(7, 5, 8, 6, new BlocCouleur('r'));
		p1.setZone(7, 7, 8, 7, new BlocCouleur('g'));

		ArrayList<Bonus> tb1 = new ArrayList<Bonus>();
		Marteau ma = new Marteau();
		Marteau mar = new Marteau();
		Bombe bo = new Bombe();
		Fusee fu = new Fusee();
		Bombe bombo = new Bombe();
		tb1.add(ma);
		tb1.add(mar);
		tb1.add(bo);
		tb1.add(bombo);
		tb1.add(fu);
		Niveau lvl1 = new Niveau(1,p1, cv1, 6, tb1, false);
		//System.out.println(lvl1);
		/*VueGenerale at= new AffichageGraphique();
		Lanceur l= new Lanceur(at,lvl1);
		l.start()*/
		lvl1.saveNewLevel();
		
	}

}
