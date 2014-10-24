/*
 * DonneesIncoherentes.java
 *
 * Created on 25 septembre 2006, 21:43
 *
 * Classe representant les erreurs se produitant lors
 * du chargement d'une donnee contenue dans une base 
 * de donnees ou dans un fichier dont la structure ne correspond
 * pas a la structure materialisee par la classe.
 *
 */

package donnees.exception;

/**
 *
 * @author millan
 */
public class DonneesIncoherentes extends Exception
{
    
    /** 
     * Creation d'une instance de la classe DonneesIncoherentes
     **/
    public DonneesIncoherentes() 
    {
        
    }
    
    /** 
     * Creation d'une instance de la classe DonneesIncoherentes
     * prenant un parametre qui est le message d'erreur associe 
     * a l'exception
     *@param pMessage message d'erreur
     **/
    public DonneesIncoherentes(String pMessage) 
    {
        super(pMessage) ;
    }
    
}
