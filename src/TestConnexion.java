/*
 * TestConnexion.java
 *
 * Created on 6 mars 2007, 13:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author millan
 */
public class TestConnexion {
    
    public static void main(String [] argv) throws java.sql.SQLException
    {
        interfaces.IntConnexion<java.sql.Connection> connexion = 
                donnees.gestionBaseDeDonnees.ConnexionBaseDeDonnees.ouvrirConnexion(
                    "ntelline.cict.fr", 1522, "etupre", "c4bd9", "jesaispas") ;

        if (connexion != null)
        {
            System.out.println("Connexion reussie") ;
            connexion.fermerConnexion() ;
        }
        else
        {
            System.out.println("Connexion non reussie") ;            
        }
    }
}