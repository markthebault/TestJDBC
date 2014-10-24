/*
 * ConnexionFile.java
 *
 * Created on 20 septembre 2006, 21:39
 *
 * Cette interface implante les informations necessaires a la connexion 
 * de la source de donnees. Dans le cadre d'un fichier, cette classe 
 * permet d'acceder au chemin d'acces.
 *
 * @author Thierry Millan
 */

package interfaces;

public interface IntConnexion<E> 
{
    /**
     * Methode permettant de fermer une connexion sur la base de donnees courante
     **/
    void fermerConnexion() throws java.sql.SQLException ;
    
    /**
     * Methode permettant de retourner le chemin d'acces au repertoire
     *@return le parametre de la connexion
     **/
    E getConnexion() ;
}
