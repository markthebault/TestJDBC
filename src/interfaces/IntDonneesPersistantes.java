/*
 * donneesPersistante.java
 *
 * Created on 25 septembre 2006, 20:39
 *
 * Cette interface fournit le prototype des operation necessaires pour acceder
 * a des donnees aussi bien dans un fichier que dans une table geree par un SGBD.
 *
 * @author Thierry Millan
 */

package interfaces;

import donnees.exception.DonneesIncoherentes;


public interface IntDonneesPersistantes
{
    /**
     * Attribut statique contenant le nom de la source de donnees c'est-a-dire
     * soit le nom du fichier, soit la requete SQL permettant le chargement des
     * donnees
     **/
    static String sourceDonnees = "";
    
    
    /**
     * Extraction des donnees contenues dans une chaine de caracteres 
     *@param pDonnee chaine de caracteres contenant les donnees a extraire
     **/
    void deLaBaseDeDonnees(String pDonnee) throws DonneesIncoherentes ;

    /**
     * Extraction des donnees contenues dans un ResultSet 
     *@param pDonnee ResultSet contenant les donnees
     **/    
    void deLaBaseDeDonnees(java.sql.ResultSet pDonnee) throws DonneesIncoherentes ;
    
    /**
     * Creation d'une chaine de caracteres contenant les donnees a mettre dans la base de donnees 
     *@return chaine de caracteres contenant les donnees sauvegarder
     **/
    String versLaBaseDeDonnees() throws DonneesIncoherentes ; 
    
    /**
     * Mise a jour du ResultSet afin d'ajouter les donnees dans la base de donnees 
     *@param pDonnee ResultSet a mettre a jour
     **/    
    void versLaBaseDeDonnees(java.sql.ResultSet pDonnee) throws DonneesIncoherentes ; 

}
