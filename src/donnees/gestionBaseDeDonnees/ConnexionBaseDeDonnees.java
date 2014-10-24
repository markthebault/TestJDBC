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

package donnees.gestionBaseDeDonnees;

import interfaces.IntConnexion;
import java.sql.DriverManager;

public class ConnexionBaseDeDonnees implements IntConnexion<java.sql.Connection>
{  
    /**
     * Attribut permettant de conserver les informations concernant le
     * chemin au repertoire contenant les fichiers
     **/
    private java.sql.Connection connexion ;
    
    /**
     * Attribut contenant la connexion courante
     **/
    private static ConnexionBaseDeDonnees connexionCourante ;
    
    /**
     * Constructeur prive permettant de creer une connexion sur une base de donnees
     *@param pAdresse adresse Internet du serveur contenant le systeme de gestion de bases de donnees
     *@param pPort pot de connexion au systeme de gestion de bases de donnees
     *@param pBase nom de la base de donnees où se trouve le compte de l'utilisateur
     *@param pCompte nom du compte où se trouve les tables qui vont etre manipulees
     *@pMotDePasse mot de passe associe au compte où se trouve les tables qui vont etre manipulees
     **/
    private ConnexionBaseDeDonnees(String pAdresse, int pPort, String pBase, String pCompte, String pMotDePasse) throws java.sql.SQLException
    {
        //ouvre la connexion
        //ConnexionBaseDeDonnees.ouvrirConnexion(pAdresse, pPort, pBase, pCompte, pMotDePasse);
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
       this.connexion = DriverManager.getConnection("jdbc:oracle:thin:@"+pAdresse+":"+pPort+":"+pBase, pCompte, pMotDePasse);
       // this.connexion = DriverManager.getConnection("jdbc:oracle:thin:@ntelline.cict.fr:1522:etupre,);

    }
    
    /**
     * Methode permettant d'ouvrir une connexion sur une base de donnees
     * @param pAdresse adresse IP du systeme de base de donnees
     * @param pPort numero du port où la base est accessible
     * @param pBase nom de la base où se trouve les tables a acceder
     * @param pCompte identifiant de l'utilisateur
     * @param pMotDePasse mot de passe de l'utilisateur
     **/
    public static ConnexionBaseDeDonnees ouvrirConnexion(String pAdresse, int pPort, String pBase, String pCompte, String pMotDePasse)
    {
        try
        {
            if (ConnexionBaseDeDonnees.connexionCourante == null)
            {
                ConnexionBaseDeDonnees.connexionCourante = new ConnexionBaseDeDonnees(pAdresse, pPort, pBase, pCompte, pMotDePasse) ;
            }
        }
        catch(java.sql.SQLException ex)
        {
            ex.printStackTrace() ;
            ConnexionBaseDeDonnees.connexionCourante = null ;
        }        
        // Verifier que les parametres sont corrects
        
        return (ConnexionBaseDeDonnees.connexionCourante) ;
    }
    
    /**
     * Methode permettant de fermer une connexion sur la base de donnees courante
     **/
    public void fermerConnexion() throws java.sql.SQLException
    {
        this.connexion.commit();
        this.connexion.close();
    }
    
    /**
     * Methode permettant de retourner le chemin d'acces au repertoire
     *@return le parametre de la connexion
     **/
    public java.sql.Connection getConnexion()
    {
        return this.connexion ;
    }
}
