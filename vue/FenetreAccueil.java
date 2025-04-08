/**
 * Un objet de la classe FenetreAccueil représente la fenêtre d'accueil du jeu Pet Rescue Saga.
 * Il est défini par un lanceur, un panneau principal, notre palette de niveau et du bouton Jouer.
 * Une fois un niveau sélectionné, cliquer sur le bouton Jouer mène à une fenêtre issue de FenetreJeu et lancée par le lanceur.
 */

package vue;

import modele.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputListener;

@SuppressWarnings("serial")
public class FenetreAccueil extends JFrame {

    Lanceur lanceur;
    JPanel mainPanel;
    JLabel playButton;
    ArrayList<JLabel> niveauxButtons;
    
    /**
     * Constructeur créant une fenêtre d'accueil Pet Rescue Saga : sélection du niveau et bouton Jouer!
     * @param lanceur Lanceur
     */
    FenetreAccueil(Lanceur lanceur) {
        this.lanceur = lanceur;

        this.setPreferredSize(new Dimension(1400, 900));
        this.setMinimumSize(new Dimension(1400, 900));
        this.setMaximumSize(new Dimension(1400, 900));
        this.setTitle("Pet Rescue Saga");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        playButton = new StartButton();

        playButton.setEnabled(false);

        niveauxButtons = new ArrayList<JLabel>();

        for (Niveau n : Lanceur.niveauxEntames()) {
            JLabel niveauButton= new levelButton(n);
            niveauxButtons.add(niveauButton);
        }

        this.setLayout(null);

        
        JPanel niveauxPanneau = new JPanel();
        niveauxPanneau.setSize(300,900);

        //niveauxPanneau.setLayout(new FlowLayout());
        
        Icon ribbon= new ImageIcon("images/scoreRibbon.png");
        JLabel text = new JLabel("<html><center><p style=\"width:200px\">Sélectionnez un niveau à commencer\n ou poursuivre</p></center></html>",ribbon,JLabel.CENTER);
        text.setHorizontalTextPosition(JLabel.CENTER);

        text.setOpaque(false);
        int height=0;
        for (JLabel b : niveauxButtons) {
            niveauxPanneau.add(b);
            height+=100;
        }

        JPanel mainPanel = new fondPanneau();
        mainPanel.setLayout(null);

        mainPanel.add(text);
        text.setBounds(1000,50,300,50);

        niveauxPanneau.setPreferredSize(new Dimension(300,height));
        niveauxPanneau.setLayout(new FlowLayout());
        JScrollPane panneauScroll = new JScrollPane(niveauxPanneau, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(panneauScroll);
        niveauxPanneau.setOpaque(false);   
        panneauScroll.setOpaque(false);
        panneauScroll.getViewport().setOpaque(false);
        panneauScroll.setBounds(1000, 100, 300, 400);

        mainPanel.add(playButton);
        playButton.setBounds(1050, 600, 300, 100);

        this.setContentPane(mainPanel);

    }
    
    /**
     * Classe s'occupant de l'image de fond de l'accueil
     */
    private class fondPanneau extends JPanel {
        
        /**
         *	Met une image en background dans la fenêtre d'accueil
         */
    	@Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {
                BufferedImage fond = ImageIO.read(new File("images/accueil.jpg"));
                g.drawImage(fond, 0, 0, null);

            } catch (IOException e) {

            }
        }

    }
    
    /**
     * Classe s'occupant du bouton de niveau (création, sélection)
     */
    private class levelButton extends JLabel implements MouseInputListener {

        Niveau niveauBouton;
        
        /**
         * Constructeur créant un bouton du Niveau n
         * @param n Niveau lié au bouton
         */
        private levelButton(Niveau n){
            this.niveauBouton=n;
            this.setText("Niveau "+n.getNumero());

            Icon buttonIcon= new ImageIcon("images/button.png");
            this.setIcon(buttonIcon);
            this.setHorizontalTextPosition(JLabel.CENTER);
            this.addMouseListener(this);

        }
        
        //Redéfinition des méthodes de l'interface MouseInputListener
        
        /**
         * Permet de sélectionner le niveau à lancer et active le bouton pour jouer
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (lanceur.getNiveauEnCours() == null || !lanceur.getNiveauEnCours().equals(niveauBouton)) {
                lanceur.setNiveauEnCours(niveauBouton);
                playButton.setEnabled(true);
                for (JLabel b : niveauxButtons) {
                    b.setBorder(null);   
                }
                this.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.ORANGE));

            } else {
                lanceur.setNiveauEnCours(null);
                this.setBorder(null);
                playButton.setEnabled(false);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {        }

        @Override
        public void mouseReleased(MouseEvent e) {        }

        @Override
        public void mouseEntered(MouseEvent e) {        }

        @Override
        public void mouseExited(MouseEvent e) {        }

        @Override
        public void mouseDragged(MouseEvent e) {        }

        @Override
        public void mouseMoved(MouseEvent e) {        }

    }
    

    /**
     * Classe s'occupant du bouton Jouer (création, lancement de la partie)
     */
    private class StartButton extends JLabel implements MouseInputListener{
    	
    	/**
    	 * Créée un bouton Jouer
    	 */
        StartButton(){
            this.setText("Jouer!");
            Icon buttonIcon= new ImageIcon("images/button.png");
            this.setIcon(buttonIcon);
            this.setHorizontalTextPosition(JLabel.CENTER);
            this.addMouseListener(this);
        }
        
        //Redéfinition des méthodes de l'interface MouseInputListener
        
        /**
         * Démarre la partie du lanceur et ferme la fenêtre d'accueil si on clique sur le StartButton activé
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if (this.isEnabled()){
                if (lanceur.getNiveauEnCours() != null) {
                    lanceur.start();
                }
                dispose();
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