/*
 * TestIterateur.java
 *
 * Created on 7 mars 2007, 22:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import donnees.* ;
import java.io.IOException;
/**
 *
 * @author millan
 */
public class TestIterateur 
{
    public static void main(String [] argv) 
            throws java.sql.SQLException, IOException
    {
        interfaces.IntConnexion<java.sql.Connection> connexion = 
          donnees.gestionBaseDeDonnees.ConnexionBaseDeDonnees.ouvrirConnexion(
                         "ntelline.cict.fr", 1522, "etupre", "c4bd9", "jesaispas") ;

        if (connexion != null)
        {
            Bons_Bons unBon ;
            interfaces.IntSourceDonnees<Bons_Bons> itBonsClass =      
                 new donnees.gestionBaseDeDonnees.SourceDonneesBaseDeDonnees
                      <Bons_Bons> (Bons_Bons.class, connexion, 
                        "select * from BONS_GAGNANTS",
                        "Bons_bons") ;             
            itBonsClass.ouvrirSource(
                    interfaces.IntSourceDonnees.Mode.consultation) ;
            itBonsClass.ouvrirIterateur() ;
            
            unBon = itBonsClass.lireIterateur() ;
            while (!itBonsClass.finIterateur())
            {
                System.out.println(unBon.getClassement() + " - " + 
                        unBon.getNumeroBon() + " - " + unBon.getRep1()) ;
                unBon = itBonsClass.lireIterateur() ;
            }
            
            itBonsClass.fermerIterateur() ;
            itBonsClass.fermerSource() ;
            
            System.out.println("Fin de l'iteration") ;
            connexion.fermerConnexion() ;
        }
        else
        {
            System.out.println("Connexion non reussie") ;            
        }
    }
}
