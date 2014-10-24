/*
 * Bons_Depart.java
 *
 * Created on 20 septembre 2006, 21:41
 * Cette classe implante la structure de donnees
 * correspondant a un bon de participation saisie par
 * un participant.
 *
 * @author Thierry Millan
 */

package donnees;

import donnees.exception.DonneesIncoherentes ;
import interfaces.IntDonneesPersistantes;
import java.sql.SQLException;

public class Bons_Depart implements IntDonneesPersistantes
{

    /**
     * Attribut contenant le numero du bon
     **/
    private long numeroBon ;
    
    /**
     * Attribut contenant la premiere reponse du concours
     **/
    private String rep1 ;
    
    /**
     * Attribut contenant la second reponse du concours
     **/
    private long rep2 ;
    
    
    /** Creation d'une instance d'un bon de depart
     **/
    public Bons_Depart() 
    {

    }     
    
    /**
     * Methode permettant d'acceder au numero d'un bon
     *@return le numero du bon
     **/
    public long getNumeroBon()
    {
        return this.numeroBon ;
    }

    /**
     * Methode permettant d'acceder a la premiere reponse contenue dans le bon
     *@return la premiere reponse
     **/    
    public String getRep1()
    {
        return this.rep1 ;
    }

    /**
     * Methode permettant d'acceder a la seconde reponse contenue dans le bon
     *@return la seconde reponse
     **/     
    public long getRep2()
    {
        return this.rep2 ;
    } 

    /**
     * Methode permettant de mettre a jour le numero d'un bon
     *@param pNumeroBon le numero du bon
     **/
    public void setNumeroBon(long pNumeroBon)
    {
        this.numeroBon = pNumeroBon ;
    }

    /**
     * Methode permettant de mettre a jour la premiere reponse contenue dans le bon
     *@param pRep1 la premiere reponse
     **/    
    public void setRep1(String pRep1)
    {
        this.rep1 = pRep1 ;
    }

    /**
     * Methode permettant d'acceder a la seconde reponse contenue dans le bon
     *@param pRep2 la seconde reponse
     **/     
    public void setRep2(long pRep2)
    {
        this.rep2 = pRep2 ;
    }
    
    /**
     * Extraction des donnees contenues dans une chaine de caracteres. On considerera
     * que toutes les donnees sont separees par des #
     *@param pDonnee chaine de caracteres contenant les donnees a extraire
     **/
    public void deLaBaseDeDonnees(String pDonnee) throws DonneesIncoherentes
    {
        String [] lesDonnees = pDonnee.split("#") ;
        
        try
        {
            this.setNumeroBon(new Long(lesDonnees[0])) ;
            this.setRep1(lesDonnees[1]) ;
            this.setRep2(new Long(lesDonnees[2])) ;
        }
        catch (java.lang.Throwable e)
        {
            e.printStackTrace() ;
            throw new DonneesIncoherentes() ;
        }
    }
    
    /**
     * Creation d'une chaine de caracteres contenant les donnees a mettre dans la base de donnees 
     *@return chaine de caracteres contenant les donnees sauvegarder
     **/
    public String versLaBaseDeDonnees() 
    {
        String donnees ;
        donnees = this.getNumeroBon() + "#" + this.getRep1() + "#";
        donnees += this.getRep2() ;
   
        return donnees ;
    }
    
   /**
     * Extraction des donnees contenues dans un ResultSet 
     *@param pDonnee ResultSet contenant les donnees
     **/    
    public void deLaBaseDeDonnees(java.sql.ResultSet pDonnee) throws DonneesIncoherentes
    {
        try
        {
            this.numeroBon = (pDonnee.getLong("numbon"));
            this.rep1 = (pDonnee.getString("rep1"));
            this.rep2 = (pDonnee.getLong("rep2"));
        }
        catch(SQLException se)
        {
            System.out.println("Une erreur est survenue, et ne me demande pas laquelle car je n'en sais rien\n"+se);
            throw new DonneesIncoherentes();
        }
    }
        
    /**
     * Mise a jour du ResultSet afin d'ajouter les donnees dans la base de donnees 
     *@param pDonnee ResultSet a mettre a jour
     **/    
    public void versLaBaseDeDonnees(java.sql.ResultSet pDonnee) throws DonneesIncoherentes
    {
        //faire l'inverse de deLaBaseDeDonnees
         try
        {

            pDonnee.updateLong("numbon", this.numeroBon);
            pDonnee.updateString("rep1",this.rep1);
            pDonnee.updateLong("rep2", this.rep2);

        }
        catch(SQLException se)
        {
            System.out.println("Une erreur est survenue, et ne me demande pas laquelle car je n'en sais rien\n"+se);
            throw new DonneesIncoherentes();
        }
    }
}
