package affichage ;

import javax.swing.* ;
import java.awt.* ;
import java.awt.event.* ;


/**
 * Classe permettant de creer la fenetre de presentation des resultats du concours
 *@author Thierry MILLAN
 *@version 1.5 
 *@see javax.swing.JComponent 
**/
public class FenetreYSLAV extends JFrame        
{
    /**
     * Attribut representant le contenu de la fenetre resultat
     **/
    private PresentationResultats presentation ;
    
    public FenetreYSLAV ()
    {
        /******** Code necessaire pour reinitialiser le jeu *********/ 
        presentation = new PresentationResultats (this) ;

        Container c = this.getContentPane () ;
        c.add (presentation) ;    
        /*************************************************************/ 
        this.setTitle ("Concours YSLAV") ;
        this.setSize(850, 450) ;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );
        this.setResizable(false);
        this.setVisible (true) ;
    } 
}

