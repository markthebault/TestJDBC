/*
 * EnsDonnees.java
 *
 * Created on 20 septembre 2006, 21:39
 *
 * Cette classe permet de faire le lien entre les classes persistantes
 * et le sources physiques qui sont soit des fichiers soit des tables d'un
 * SGBD. 
 *
 * @author Thierry Millan
 */

package interfaces;


public interface IntSourceDonnees<E extends IntDonneesPersistantes> extends interfaces.IntIterateur<E>
{
    /**
     * Ensemble des modes qui peuvent etre definis lors de l'acces a une source
     */ 
    enum Mode {consultation, modification, insertion} ;

    /**
     * Ensemble des etats que peut prendre une source
     */ 
    enum Etat {ouverte, fermee} ;
       
   /**
     * Methode permettant d'ouvrir la source en fonction du mode passe 
     * en parametre
     * Precondition : la source est fermee
     * Postcondition : la source est ouverte
     * 
     * @param pMode mode d'ouverture de la table en consultation (consultation) ou en
     * insertion (modification)
     */
    void ouvrirSource(Mode pMode) throws java.sql.SQLException ;  
    
    /**
     * Fermeture de la table courante
     *Postcondition : la source est fermee
     **/
    void fermerSource() throws java.sql.SQLException ;
   
    /**
     * Methode permettant de mettre a jourune donnees
     * preconditions : le mode d'ouverture doit etre "modification"
     *                 la source doit etre ouverte
     * @param pDonnees donnees a mettre a jour
     */ 
    void mettreAJour(E pDonnees) throws java.io.IOException, java.sql.SQLException;

    /**
     * Methode permettant d'inserer en fin de table une donnees
     * preconditions : le mode d'ouverture doit etre "insertion"
     *                 la source doit etre ouverte
     * @param pDonnees donnees a inserer
     */ 
    void inserer(E pDonnees) throws java.io.IOException, java.sql.SQLException;    
    
    /** 
     * Methode permettant de supprimer toutes les donnees de la table
     *Precondition  : la source de donnees est fermee
     **/
    void detruire() throws java.io.IOException, java.sql.SQLException ;
    
}
