/**
 * Un objet de la classe FenetreJeu représente l'affichage graphique d'un niveau.
 * Il est défini par un panneau principal, le niveau en cours, le plateau du niveau, des bonus et de ses étiquettes,
 * d'étiquettes indiquant le score, l'objectif et le nombre de mouvement restant, du mouvement courant et d'un AffichageGraphique.
 * C'est sur cette fenêtre que se jouera et évoluera le niveau.
 * Il y a plusieurs manières de quitter la fenêtre de jeu : par abandon, par sauvegarde et quitter, par victoire et par défaite.
 * Chacunes ramènent à la fenêtre d'accueil, mais ont des conséquences différentes sur la sauvegarde du niveau joué.
 */

package vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import modele.*;
import vue.FenetreJeu.PanneauPlateau.Carreau;

public class FenetreJeu extends JFrame{
    
    
	private static final long serialVersionUID = -8619231480649323279L;
    private PanneauJeu mainPanel;
    private PanneauPlateau plateau;
    private boolean tour;
    private boolean bonusSelected;
    private Bonus bonus;
    private Niveau niveau;
    private JLabel fuseeButton;
    private JLabel marteauButton;
    private JLabel bombeButton;
    private JLabel tenaillesButton;
    private JLabel indiceButton;
    private JLabel scoreLabel;
    private JLabel goalLabel;
    private JLabel movesLabel;
    private Mouvement courant;
    private AffichageGraphique observeur;
    private TimerTask indice;

    
    /**
     * Constructeur créant une fenêtre de jeu du Niveau n
     * @param n Niveau en cours
     * @param affichage AffichageGraphique qui gère l'affichage générale
     */
    public FenetreJeu(Niveau n, AffichageGraphique affichage){

        setPreferredSize(new Dimension(1000,1000));
        setTitle("Partie");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        mainPanel= new PanneauJeu();
        setContentPane(mainPanel);

        niveau=n;
        observeur=affichage;
        
        plateau= new PanneauPlateau(n.getPlateau());
        
        JPanel buttons= new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new GridLayout(4,1));

        fuseeButton= new FuseeButton();

        marteauButton= new MarteauButton();

        bombeButton= new BombeButton();

        tenaillesButton= new TenaillesButton();

        Icon buttonIcon= new ImageIcon("images/button.png");
        JLabel sauveButton= new JLabel("Sauvegarder et quitter",buttonIcon,JLabel.CENTER);
        sauveButton.setHorizontalTextPosition(JLabel.CENTER);
        sauveButton.addMouseListener(new Savior());

        JLabel abonButton= new JLabel("Abandonner",buttonIcon,JLabel.CENTER);
        abonButton.setHorizontalTextPosition(JLabel.CENTER);
        abonButton.addMouseListener(new Reseter());

        indiceButton= new HintButton();
        JLabel botButton= new RandomButton();

        buttons.add(fuseeButton);
        buttons.add(marteauButton);
        buttons.add(bombeButton);
        buttons.add(tenaillesButton);

        updateButtons();
        Icon scoreRibbon= new ImageIcon("images/scoreRibbon.png");
        Icon goalsRibbon= new ImageIcon("images/goalRibbon.png");
        scoreLabel=new JLabel("Score: "+n.getScore()+"<br>Meilleur score :"+niveau.getMeilleurScore(),scoreRibbon,JLabel.CENTER);
        scoreLabel.setHorizontalTextPosition(JLabel.CENTER);
        
        String conditions="";
        for (ConditionVictoire c: niveau.getConditions()){
            conditions+=c.toString()+"<br/>";
        }
        goalLabel= new JLabel(goalsRibbon);
        goalLabel.setText("<html>Objectif: "+conditions+"Animaux sauvés :"+niveau.getAnimauxSauves()+"</html>");
        goalLabel.setHorizontalTextPosition(JLabel.CENTER);

        movesLabel=new JLabel(goalsRibbon);
        if (n.getNbDeplacement()==-1){
            movesLabel.setText("Mouvements illimités");
        } else {
            movesLabel.setText("Mouvements restants:"+n.getNbDeplacement());
        }
        movesLabel.setHorizontalTextPosition(JLabel.CENTER);

        JPanel eta= new JPanel();
        eta.setOpaque(false);
        eta.setLayout(new FlowLayout());
        eta.add(goalLabel);
        eta.add(movesLabel);
        eta.add(scoreLabel);
                
        mainPanel.setLayout(null);
        mainPanel.add(plateau);
        plateau.setBounds(230, 230, 540, 540);
        mainPanel.add(eta);
        eta.setBounds(0, 0,1000,200);
        mainPanel.add(buttons);
        buttons.setBounds(880,100,100,400);

        mainPanel.add(sauveButton);
        sauveButton.setBounds(100,800,300,100);

        mainPanel.add(abonButton);
        abonButton.setBounds(600,800,300,100);

        mainPanel.add(indiceButton);
        indiceButton.setBounds(100,400,88,88);

        mainPanel.add(botButton);
        botButton.setBounds(100,550,88,88);

        pack();
    }
    
    //Getter & Setter
    
    /**
     * @return le mouvement courant
     */
    public Mouvement getCourant() {
        return courant;
    }
    
    /**
     * @return le boolean tour
     */
    public boolean getTour(){
        return tour;
    }
    
    /**
     * Modifie tour avec le boolean en paramètre
     * @param tour
     */
    public void setTour(boolean tour) {
        this.tour = tour;
    }
    
    /**
     * Met à jour l'affichage des bonus
     */
    void updateButtons(){
        bombeButton.setEnabled(niveau.hasBombe());
        fuseeButton.setEnabled(niveau.hasFusee());
        marteauButton.setEnabled(niveau.hasMarteau());
        tenaillesButton.setEnabled(niveau.hasTenailles());

        fuseeButton.setText(Integer.toString(niveau.nbFusee()));
        marteauButton.setText(Integer.toString(niveau.nbMarteau()));
        bombeButton.setText(Integer.toString(niveau.nbBombe()));
        tenaillesButton.setText(Integer.toString(niveau.nbTenailles()));
    }
    
    /**
     * Enlève le contour cyan du bonus joué
     */
    void reinitialiseButtons(){
        tenaillesButton.setBorder(null);    
        bombeButton.setBorder(null);    
        fuseeButton.setBorder(null);    
        marteauButton.setBorder(null);    
    }
    
    /**
     * Met à jour l'affichage du panneau représentant le plateau
     */
    void updatePlateau(){
        plateau.update();
    }
    
    /**
     * Met à jour l'affichage des scores, des objectifs et du nombre de déplacement 
     */
    void updateLabels(){
        scoreLabel.setText("<html>Score: "+niveau.getScore()+"<br/>Meilleur score :"+niveau.getMeilleurScore()+"</html>");
        String conditions="";
        for (ConditionVictoire c: niveau.getConditions()){
            conditions+=c.toString()+"<br/>";
        }
        goalLabel.setText("<html>Objectif: "+conditions+"Animaux sauvés :"+niveau.getAnimauxSauves()+"</html>");
        if (niveau.getNbDeplacement()==-1){
            movesLabel.setText("Mouvements illimités");
        } else {
            movesLabel.setText("Mouvements restants:"+niveau.getNbDeplacement());
        }
    
    }
    
    /**
     * Réunie toutes les méthodes d'update
     * Met à jour tous les affichages de la fenêtre de jeu
     */
    void update(){
        updateButtons();
        updateLabels();
        updatePlateau();
        mainPanel.update();
        lancerChronoIndice();
    }

    /**
     * Réinitialise le chronomètre du boutton "indice" et l'active après 20 secondes
     */
    void lancerChronoIndice(){
        if (indice!=null) indice.cancel();
        indiceButton.setEnabled(false);
        indice = new TimerTask(){
            public void run(){
                indiceButton.setEnabled(true);
            }
        };
        long delay=20000;
        Timer timer= new Timer("timer");
        timer.schedule(indice, delay);
    }


    /**
     * Affiche l'écran de victoire
     */
    void affichageVictoire(){
        
        try{
            mainPanel.fond=ImageIO.read(new File("images/background.jpg"));
        } catch (IOException e){
        }
        
        mainPanel.removeAll();

        Icon imgIcon = new ImageIcon("images/reussite.gif");

        JLabel panneauVictoire= new JLabel(imgIcon);
        mainPanel.add(panneauVictoire);
        panneauVictoire.setBounds(0, 0, 1000, 1000);

        Icon congratsIcon= new ImageIcon("images/scoreRibbon.png");
        JLabel congrats= new JLabel("<html><center>Felicitation! Vous avez atteint votre objectif!</center></html>",congratsIcon,JLabel.CENTER);
        congrats.setHorizontalTextPosition(JLabel.CENTER);
        panneauVictoire.add(congrats);
        congrats.setBounds(350, 200, 300, 51);


        Icon buttonIcon= new ImageIcon("images/button.png");
        JLabel accueilButton= new JLabel("Accueil",buttonIcon,JLabel.CENTER);
        accueilButton.setHorizontalTextPosition(JLabel.CENTER);
        accueilButton.addMouseListener(new Reseter());
        panneauVictoire.add(accueilButton);
        accueilButton.setBounds(400, 800, 179, 83);

        mainPanel.repaint();
    }
    
    /**
     * Fenêtre affichée en cas de défaite
     */
    void affichageDefaite(){
        try{
            mainPanel.fond=ImageIO.read(new File("images/background.jpg"));
        } catch (IOException e){
        }
        
        mainPanel.removeAll();

        Icon imgIcon = new ImageIcon("images/defaite.png");

        JLabel panneauVictoire= new JLabel(imgIcon);
        mainPanel.add(panneauVictoire);
        panneauVictoire.setBounds(0, 0, 1000, 1000);

        Icon congratsIcon= new ImageIcon("images/goalRibbon.png");
        JLabel congrats= new JLabel("<html><center>Dommage... Vous n'avez pas atteint votre objectif!</center></html>",congratsIcon,JLabel.CENTER);
        congrats.setHorizontalTextPosition(JLabel.CENTER);
        panneauVictoire.add(congrats);
        congrats.setBounds(350, 200, 300, 51);


        Icon buttonIcon= new ImageIcon("images/button.png");
        JLabel accueilButton= new JLabel("Accueil",buttonIcon,JLabel.CENTER);
        accueilButton.setHorizontalTextPosition(JLabel.CENTER);
        accueilButton.addMouseListener(new Reseter());
        panneauVictoire.add(accueilButton);
        accueilButton.setBounds(400, 800, 179, 83);

        mainPanel.repaint();
    }
    
    /**
     * Classe s'occupant du panneau de jeu (mainPanel)
     * Un panneau de jeu est défini par une image de fond et est le support des autres panneaux de la fenêtre de jeu
     */
    @SuppressWarnings("serial")
	public class PanneauJeu extends JPanel{

        BufferedImage fond;
        
        /**
         * Constructeur associant fond à une image par défaut
         */
        PanneauJeu(){
            try{
                fond= ImageIO.read(new File("images/back.png"));
            } catch (IOException e){
            }
        }
        
        /**
         * Constructeur associant fond à l'image fileName
         * @param fileName String représentant le chemin jusqu'à l'image
         */
        PanneauJeu(String fileName){
            try{
                fond= ImageIO.read(new File(fileName));
            } catch (IOException e){
            }
        }
        
        /**
         * Met le fond en background de la fenêtre de jeu
         */
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(fond, 0, 0, null);
        }
        
        /**
         * Met à jour l'affichage du fond de la fenêtre de jeu
         */
        void update(){
            String fileName="images/back";
            if (niveau.getPlateau().getSommet()==0){
                fileName+="t";
            }
            if (niveau.getPlateau().getSommet()+9>=niveau.getPlateau().getTab().length){
                fileName+="b";
            }
            fileName+=".png";
            try{
                fond= ImageIO.read(new File(fileName));
            } catch (IOException e){
            }
            repaint();
        }

    }

    
    /**
     * Classe s'occupant de l'affichage du plateau (plateau)
     * Un objet de PanneauPlateau est défini par un tableau de carreaux et représente le plateau de jeu
     */
    public class PanneauPlateau extends JPanel{

		private static final long serialVersionUID = 2475277370881732285L;
		ArrayList<Carreau> carreaux;
        
		/**
		 * Constructeur créant l'affichage graphique du plateau p
		 * @param p Plateau associé au PanneauPlateau
		 */
        public PanneauPlateau(Plateau p){
            setSize(540, 540);
            setLayout(new GridLayout(9,9));
            carreaux= new ArrayList<Carreau>();
            for (int i=p.getSommet(); i<p.getSommet()+9; i++){
                for (int j=0; j<9; j++){
                    if (!p.getTab()[i][j].estVide()){
                        Carreau c=new Carreau(p.getTab()[i][j]);
                        carreaux.add(c);
                        add(c);
                    } else {
                        add(new Carreau());
                    }
                }
            }
            this.setOpaque(false);
        }
        
        /**
         * Met à jour l'affichage du plateau
         */
        void update(){
            removeAll();
            carreaux.clear();
            for (int i=niveau.getPlateau().getSommet(); i<niveau.getPlateau().getSommet()+9; i++){
                for (int j=0; j<9; j++){
                    if (!niveau.getPlateau().getTab()[i][j].estVide()){
                        Carreau c=new Carreau(niveau.getPlateau().getTab()[i][j]);
                        carreaux.add(c);
                        add(c);
                    } else {
                        add(new Carreau());
                    }
                }
            }
        }
        
        /**
         * Met l'image de fond du plateau
         */
        @Override
        protected void paintComponent(Graphics g){
        	super.paintComponent(g);
            try{
            	BufferedImage fond= ImageIO.read(new File("images/mur.png"));
                g.drawImage(fond, 0, 0, null);
            } catch (IOException e){
            }
        }
        
        /**
         * Classe s'occupant de la gestion d'un carreau (création, action, affichage)
         * Un carreau est défini par une case et une image
         */
        public class Carreau extends JPanel implements MouseInputListener{

			private static final long serialVersionUID = 8332577556513948218L;
			Case c;
			BufferedImage icon;
			
			/**
			 * Constructeur créant un carreau invisible (i.e case null)
			 */
            public Carreau(){
                this.setOpaque(false);
            }
            
            /**
             * Constructeur créant un carreau associé à la case c et attribuant l'icône en conséquence (plateforme, trou ...)
             * Le carreau est rendu sensible aux actions si sa case est cassable
             * @param c Case
             */
            public Carreau(Case c){
            	
                this.c=c;
                this.setSize(60,60);
                String fileName="images/";
                if (c.getBloc() instanceof Plateforme){
                    fileName+="pl";
                } else if (c.getBloc() instanceof Trou){
                    fileName+="tr";
                } else if (c.getBloc() instanceof BlocCouleur){
                    fileName+=c.getBloc().getCouleur();
                    if (((BlocCouleur )c.getBloc()).getCombo()!=1){
                        fileName+="d";
                    }
                    if (((BlocCouleur )c.getBloc()).getGrille()){
                        fileName+="g";
                    }
                    if (c.getBloc() instanceof Ballon){
                        fileName+="b";
                    }
                } else if (c.getBloc() instanceof Animal){
                    fileName+="A";
                    int num= c.getBloc().hashCode()%5+1;
                    fileName+=Integer.toString(num);
                } else if (c.getBloc() instanceof BlocBombe){
                    fileName+="bo";
                }

                fileName+=".png";
                try{
                    icon= ImageIO.read(new File(fileName));
                } catch (IOException e){
                    icon= null;
                }
                this.setOpaque(false);
                if (c.getBloc().estCassable()){
                    this.addMouseListener(this);
                    this.addMouseMotionListener(this);
                }

            }
            
            /**
             * Affiche l'icone du carreau
             */
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(icon, 0, 0, null);
            }
            
            
            //Redéfinition des méthodes de l'interface MouseInputListener
            
            /**
             * Réalise le mouvement s'il est possible puis remet les bonus à null (sélection et contour) pour le tour suivant
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                Mouvement test= new Mouvement(niveau,bonus);
                test.setX(c.getX());
                test.setY(c.getY());
                if (test.estPossible()){
                    courant=test;
                    observeur.setCourant(courant);
                    observeur.notifier();
                }
                reinitialiseButtons();
                bonus=null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {}
            
            /**
             * Affiche un contour orange au carreau survolé si un mouvement est possible
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                Mouvement test= new Mouvement(niveau,bonus);
                test.setX(c.getX());
                test.setY(c.getY());
                if (test.estPossible()){
                    setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.ORANGE));
                }
            }
            
            /**
             * Enlève le contour orange une fois le curseur hors du carreau
             */
            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(null);                
            }

            @Override
            public void mouseMoved(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}
        
        }
        
    }


    /**
     * Classe s'occupant de la transition FenetreJeu à FenetreAccueil lors d'un abandon
     */
    private class Reseter implements MouseInputListener{
    	
    	//Redéfinition des méthodes de l'interface MouseInputListener
    	
    	/**
    	 * Si on clique sur le bouton "Abandonner" :
    	 * - réinitialise le niveau en conesrvant le meilleur score
    	 * - affiche la fenêtre d'accueil et met fin à la fenêtre de jeu
    	 */
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                niveau.reinitialiseLevel();
                observeur.setFenetre(null);
                observeur.accueil();
                dispose();
            } catch (Exception ex){}
        }

        @Override
        public void mousePressed(MouseEvent e) {        }

        @Override
        public void mouseReleased(MouseEvent e) {        }

        @Override
        public void mouseEntered(MouseEvent e) {		}

        @Override
        public void mouseExited(MouseEvent e) {        }

        @Override
        public void mouseDragged(MouseEvent e) {        }

        @Override
        public void mouseMoved(MouseEvent e) {        }
        
    }
    
    /**
     * Classe s'occupant de la transition FenetreJeu à FenetreAccueil lors d'une sauvegarde pour continuer plus tard
     */
    private class Savior implements MouseInputListener{
    	
    	//Redéfinition des méthodes de l'interface MouseInputListener

    	/**
    	 * Si on clique sur le bouton "Sauvegarder et quitter" :
    	 * - sauvegarde la progression du niveau en cours
    	 * - affiche une fenêtre d'accueil à jour et met fin à la fenêtre de jeu
    	 */
        @Override
        public void mouseClicked(MouseEvent e) {
            try {
                niveau.saveProgression();
                Lanceur l= new Lanceur();
                AffichageGraphique af= new AffichageGraphique();
                l.setVue(af);
                af.accueil();
                dispose();
            } catch (IOException ex){}
        }

        @Override
        public void mousePressed(MouseEvent e) {        }

        @Override
        public void mouseReleased(MouseEvent e) {        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {        }

        @Override
        public void mouseDragged(MouseEvent e) {        }

        @Override
        public void mouseMoved(MouseEvent e) {        }
        
    }
    
    //---------------------------------------------------------------------
    
    /**
     * Classes s'occupant du bouton du bonus spécifié
     * Elles possèdent chacune :
     *  - un constructeur créant le bouton du bonus
     *  - les méthodes redéfinies de l'interface MouseInputListener
     *  - dont mouseClicked() permettant de sélectionner le bonus (alors contouré d'une bordure cyan) ou de le désélectionner si précédemment sélectionné
     */
  
  
    @SuppressWarnings("serial")
	private class BombeButton extends JLabel implements MouseInputListener{
    	
        BombeButton(){
            this.setText(Integer.toString(niveau.nbBombe()));
            Icon buttonIcon= new ImageIcon("images/bobu.png");
            this.setIcon(buttonIcon);
            this.setHorizontalTextPosition(JLabel.CENTER);
            this.addMouseListener(this);
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if (this.isEnabled()){
                if (!bonusSelected || !(bonus instanceof Bombe)){
                    bonusSelected=true;
                    bonus=niveau.getBombe();
                    reinitialiseButtons();    
                    this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.CYAN));
                } else {
                    reinitialiseButtons();
                    bonusSelected=false;
                    bonus=null;
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}
        
    }

    @SuppressWarnings("serial")
	private class MarteauButton extends JLabel implements MouseInputListener{

        MarteauButton(){
            this.setText(Integer.toString(niveau.nbMarteau()));
            Icon buttonIcon= new ImageIcon("images/mabu.png");
            this.setIcon(buttonIcon);
            this.setHorizontalTextPosition(JLabel.CENTER);
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (this.isEnabled()){
                if (!bonusSelected || !(bonus instanceof Marteau)){
                    bonusSelected=true;
                    bonus=niveau.getMarteau();
                    reinitialiseButtons();    
                    this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.CYAN));
                } else {
                    reinitialiseButtons();
                    bonusSelected=false;
                    bonus=null;
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}
        
    }

    @SuppressWarnings("serial")
	private class FuseeButton extends JLabel implements MouseInputListener{

        FuseeButton(){
            this.setText(Integer.toString(niveau.nbFusee()));
            Icon buttonIcon= new ImageIcon("images/fubu.png");
            this.setIcon(buttonIcon);
            this.setHorizontalTextPosition(JLabel.CENTER);
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (this.isEnabled()){
                if (!bonusSelected || !(bonus instanceof Fusee)){
                    bonusSelected=true;
                    bonus=niveau.getFusee();
                    reinitialiseButtons();    
                    this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.CYAN));
                } else {
                    reinitialiseButtons();
                    bonusSelected=false;
                    bonus=null;
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}
        
    }

    @SuppressWarnings("serial")
	private class TenaillesButton extends JLabel implements MouseInputListener{

        TenaillesButton(){
            this.setText(Integer.toString(niveau.nbTenailles()));
            Icon buttonIcon= new ImageIcon("images/tebu.png");
            this.setIcon(buttonIcon);
            this.setHorizontalTextPosition(JLabel.CENTER);
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (this.isEnabled()){
                if (!bonusSelected || !(bonus instanceof Tenailles)){
                    bonusSelected=true;
                    bonus=niveau.getTenailles();
                    reinitialiseButtons();    
                    this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.CYAN));
                } else {
                    reinitialiseButtons();
                    bonusSelected=false;
                    bonus=null;
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}
        
    }

    private class HintButton extends JLabel implements MouseInputListener{

        HintButton(){
            Icon buttonIcon= new ImageIcon("images/hibu.png");
            this.setIcon(buttonIcon);
            this.addMouseListener(this);
            this.setEnabled(false);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (this.isEnabled()){
                for (Carreau car: plateau.carreaux){
                    if (car.c!=null && !car.c.estVide()){
                        if (car.c.getBloc().equals(niveau.getPlateau().meilleurChoix())){
                            car.setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.RED));
                        }
                    }
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}
        
    }

    private class RandomButton extends JLabel implements MouseInputListener{

        RandomButton(){
            Icon buttonIcon= new ImageIcon("images/rabu.png");
            this.setIcon(buttonIcon);
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (this.isEnabled()){
                observeur.controleur.randomBot();
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {}
        
    }

}
