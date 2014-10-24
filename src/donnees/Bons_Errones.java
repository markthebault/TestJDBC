/*
 * Bons_Errones.java
 *
 * Created on 27 septembre 2006, 21:35
 *
 * Cette classe implante la structure de donnees
 * recapitulant les bons errones et la raison du rejet.
 *
 * @author Thierry Millan
 */

package donnees ;

import donnees.exception.DonneesIncoherentes ;
import interfaces.IntDonneesPersistantes;
import java.sql.SQLException;

public class Bons_Errones implements IntDonneesPersistantes
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
     * Attribut contenant la valeur de controle
     **/
    private String ctrlRep1 ;

    /**
     * Attribut contenant le motif de l'erreur
     **/
    private String motif ;     
    
    /** 
     * Creation d'une instance d'un bon de depart
     **/
    public Bons_Errones() 
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
     * Methode permettant d'acceder a la valeur de controle de la reponse REP1
     *@return la valeur de controle
     **/     
    public String getCtrlRep1()
    {
        return this.ctrlRep1 ;
    }
    
    /**
     * Methode permettant d'acceder au motif du rejet du bon
     *@return le motif du rejet
     **/     
    public String getMotif()
    {
        return this.motif ;
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
     * Methode permettant de mettre a jour la valeur de controle de la reponse REP1
     *@param pCtrlRep1 la valeur de controle
     **/     
    public void setCtrlRep1(String pCtrlRep1)
    {
        this.ctrlRep1 = pCtrlRep1 ;
    }

    /**
     * Methode permettant de mettre a jour le motif du rejet du bon
     *@param pMotif le motif du rejet
     **/     
    public void setMotif(String pMotif)
    {
        this.motif = pMotif ;
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
            this.setCtrlRep1(lesDonnees[2]) ;
            this.setMotif(lesDonnees[3]) ;
        }
        catch (java.lang.Throwable e)
        {
            throw new DonneesIncoherentes() ;
        }
     
    }

    /**
     * Extraction des donnees contenues dans un ResultSet 
     *@param pDonnee ResultSet contenant les donnees
     **/    
    public void deLaBaseDeDonnees(java.sql.ResultSet pDonnee) throws DonneesIncoherentes
    {
        try
        {
            this.setNumeroBon(pDonnee.getLong("numbon"));
            this.setRep1(pDonnee.getString("rep1"));
            this.ctrlRep1 = pDonnee.getString("ctrl_rep1");
            this.motif = pDonnee.getString("motif");

        }
        catch(SQLException se)
        {
            System.out.println("Une erreur est survenue, et ne me demande pas laquelle car je n'en sais rien\n"+se);
            throw new DonneesIncoherentes();
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
        donnees += this.getCtrlRep1() + "#" + this.getMotif() ;
        
        return donnees ;
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

            pDonnee.updateLong("numbon", this.getNumeroBon());
            pDonnee.updateString("rep1",this.getRep1());
            pDonnee.updateString("ctrl_rep1", this.ctrlRep1);
            pDonnee.updateString("motif", this.motif);


        }
        catch(SQLException se)
        {
            System.out.println("Une erreur est survenue, et ne me demande pas laquelle car je n'en sais rien\n"+se);
            throw new DonneesIncoherentes();
        }
    }
}
