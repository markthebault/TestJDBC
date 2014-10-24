/*
 * ConnexionFichier.java
 *
 * Created on 20 septembre 2006, 21:39
 *
 * Cette classe implante les informations necessaires a la connexion 
 * de la source de donnees. Dans le cadre d'une base de donnees geree par un
 * SGBD, cette classe permet d'acceder au parametre de connexion au SGBD.
 *
 * @author Thierry Millan
 */

package donnees.gestionFichier;

import interfaces.IntConnexion;

public class ConnexionFichier implements IntConnexion<String>
{  
    /**
     * Attribut permettant de conserver les informations concernant le
     * chemin au repertoire contenant les fichiers
     **/
    private static String chemin ;
    
    /**
     * Attribut contenant la connexion courante
     **/
    private static ConnexionFichier connexionCourante ;
    
    /**
     * Constructeur prive permettant de creer une connexion sur une source de donnees
     *@param pChemin reperoire où se trouve les fichiers
     **/
    private ConnexionFichier(String pChemin) 
    {
        this.chemin = pChemin ;
    }
    
    /**
     * Methode permettant d'ouvrir une connexion sur une base de donnees
     *@param pChemin reperoire où se trouve les fichiers
     **/
    public static ConnexionFichier ouvrirConnexion(String pChemin)
    {
        if ((ConnexionFichier.connexionCourante == null) || (!ConnexionFichier.chemin.equals(pChemin)))
        {
            ConnexionFichier.connexionCourante = new ConnexionFichier(pChemin) ;
        }
        
        // Verifier que les parametres sont corrects
        
        return (ConnexionFichier.connexionCourante) ;
    }
    
    /**
     * Methode permettant de fermer une connexion sur la base de donnees courante
     **/
    public void fermerConnexion() throws java.sql.SQLException
    {
        ConnexionFichier.chemin = null ;
        ConnexionFichier.connexionCourante = null ;
    }
    
    /**
     * Methode permettant de retourner le chemin d'acces au repertoire
     *@return le parametre de la connexion
     **/
    public String getConnexion()
    {
        return ConnexionFichier.chemin ;
    }
}
