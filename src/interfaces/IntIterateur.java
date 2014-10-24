/*
 * IntIterateur.java
 *
 * Created on 21 octobre 2006, 19:17
 *
 * Interface permettant d'iterer des sources de donnees
 */

package interfaces;

/**
 *
 * @author millan
 */
public interface IntIterateur<E> 
{
    /**
     * Ensemble des etats que peut prendre un iterateur
     */ 
    enum Etat {ouvert, ferme} ;
    
   /**
    * Methode permettant d'ouvrir la source en mode consultation
    * Si la source de donnees est fermee alors elle est ouverte sinon
    *Precondition : l'iterateur est ferme 
    *               la source est ouverte en mode consultation ou elle est fermee 
    *Postcondition : l'iterateur et la source sont ouverts 
    **/
    void ouvrirIterateur() throws java.sql.SQLException;
    
   /**
    * Methode permettant de lire l'element courant de
    * la source de donnees
    *Precondition : l'iterateur est ouvert
    *@return l'element courant
    **/
   E lireIterateur() throws java.io.IOException, java.sql.SQLException ;
   
   /**
    * Methode retournant vrai si la fin de la source de donnees est
    * atteinte
    *Precondition : l'iterateur est ouvert
    *@return vrai si le dernier element a ete lu
    **/
   boolean finIterateur() ;
   
   /**
     * Metode permettant de se positionner sur l'element suivantIterateur
     * de la source de donnees
     * Precondition : l'iterateur est ouvert
     */ 
//   public void suivantIterateur() ;
   
    /**
     * Fermeture de l'iterateur
     *Postcondition : l'iterateur et la source sont fermes
     **/
    void fermerIterateur() throws java.sql.SQLException ;
}
