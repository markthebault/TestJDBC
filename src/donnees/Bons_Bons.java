/*
 * Bons_Bons.java
 *
 * Created on 20 septembre 2006, 21:42
 *
 * Cette classe implante la structure de donnees
 * correspondant a un bon de participation valide c'est-a-dire
 * un bon dont le numero n'est pas deja utilise et dont la reponse 1 
 * est correct.
 *
 * @author Thierry Millan
 */


package donnees;

import donnees.exception.NumBonExisteDeJa ;
import donnees.exception.Rep1Erronee ;
import donnees.exception.DonneesIncoherentes ;
import java.sql.SQLException;

/**
 *
 * @author millan
 */
public class Bons_Bons extends Bons_Depart 
{
    /**
     * Tableau permettant de sauvegarder les numeros de bons deja utilises
     **/
    static private byte [][][][][][][] TABLEAU_NUMEROBON ;   
    
    /**
     * Attribut contenant le nombre de premieres qualites bien classees
     **/
    private long nbQual ;
    
    /**
     * Attribut contenant la difference entre le nombre de reponses types et 
     * la reponse 2
     **/
    private long ecart ;    
    
    /**
     * Attribut contenant le classement
     **/
    private long classement ;    
    
    /**
     * Creates a new instance of Bons_Bons
     */
    public Bons_Bons() 
    {
        if (TABLEAU_NUMEROBON == null)
        {
            TABLEAU_NUMEROBON = new byte[10][10][10][10][10][10][10] ;
            
            for (int iI = 0; iI <= 9; iI++)
               for (int iJ = 0; iJ <= 9; iJ++)
                  for (int iK = 0; iK <= 9; iK++)
                     for (int iL = 0; iL <= 9; iL++)
                        for (int iM = 0; iM <= 9; iM++)
                           for (int iN = 0; iN <= 9; iN++)
                              for (int iO = 0; iO <= 9; iO++)
                                TABLEAU_NUMEROBON[iI] [iJ] [iK] [iL] [iM] [iN] [iO] = 0 ;
        }
    } 
    
    /**
     * Methode permettant de mettre a jour le numero d'un bon
     *@param pNumeroBon le numero du bon
     **/
    @Override
    public void setNumeroBon(long pNumeroBon)
    {
         int iI, iJ, iK, iL ;
         int iM, iN, iO ;
         long vAux ;
         boolean vNumeroExiste ;
         
         iI = (int) (pNumeroBon % 10) ;
         vAux = pNumeroBon / 10 ;
         iJ = (int) (vAux % 10) ;
         vAux = vAux / 10 ;
         iK = (int) (vAux % 10) ;
         vAux = vAux / 10 ;
         iL = (int) (vAux % 10) ;
         vAux = vAux / 10 ;
         iM = (int) (vAux % 10) ;
         vAux = vAux / 10 ;
         iN = (int) (vAux % 10) ;
         iO = (int) (vAux / 10) ;

         vNumeroExiste = (Bons_Bons.TABLEAU_NUMEROBON[iI][iJ][iK][iL][iM][iN][iO] == 1) ;
         
         if (vNumeroExiste)
         {
             throw new NumBonExisteDeJa(pNumeroBon) ;
         }
         else
         {
            Bons_Bons.TABLEAU_NUMEROBON[iI][iJ][iK][iL][iM][iN][iO] = 1 ;
            super.setNumeroBon(pNumeroBon) ;
         }
    } 
    
    /**
     * Methode permettant de mettre a jour la premiere reponse contenue dans le 
     * bon
     *@param pRep1 la premiere reponse
     **/    
    @Override
    public void setRep1(String pRep1)
    {
        String vLettres ;
        String vCtrlRep1 ;
        String vRep1 = pRep1.toLowerCase() ;
        
        boolean vTrouve ;
        int iL ;
        vLettres  = "abcdefghij" ;
        vCtrlRep1 = ".........." ;

        for (int iR = 0; iR < 10; iR++)
        {
            iL = 0 ;
            vTrouve = false;
            while ((iL < 10) && (!vTrouve))
            {
    
                if (vRep1.charAt(iR) == vLettres.charAt(iL))
                {
                    vTrouve = true ;
                }
                else
                {
                    iL ++ ;
                }
            }
       
            if (vTrouve)
            {
                String debut = (iL<1?"":vLettres.substring(0, iL)) ;
                String fin   = (iL==vRep1.length()-1?"":
                                vLettres.substring(iL+1, vRep1.length())) ;
                vLettres =  debut + " " + fin ;
            }
            else
            {
                String debut = (iR<1?"":vCtrlRep1.substring(0, iR)) ;
                String fin   = (iR==vRep1.length()-1?"":
                                vCtrlRep1.substring(iR+1, vRep1.length())) ;
                vCtrlRep1 = debut + "*" + fin ;
            }
        }
     
        if (!vLettres.trim().equalsIgnoreCase(""))
        {
            throw new Rep1Erronee(this.getNumeroBon(), pRep1.toLowerCase(), 
                                  vCtrlRep1) ;
        }
        else
        {
            super.setRep1(vRep1) ;
        }  
    }
    
    public static void liberationMemoire()
    {
        Bons_Bons.TABLEAU_NUMEROBON = null ;
        System.gc() ;
    }

    /**
     * Methode permettant d'acceder au nombre de premieres qualites bien 
     * classees
     *@return le nombre de premieres qualites bien classees
     **/     
    public long getNbQual()
    {
        return this.nbQual ;
    } 

    /**
     * Methode permettant de mettre a jour le nombre de premieres qualites bien 
     * classees
     *@param pNbQual nombre de premieres qualites bien classees
     **/
    public void setNbQual(long pNbQual)
    {
        this.nbQual = pNbQual ;
    } 
    
    /**
     * Methode permettant d'acceder a la difference entre le nombre de reponses 
     * types et la reponse 2
     *@return la difference entre le nombre de reponses types et la reponse 2
     **/     
    public long getEcart()
    {
        return this.ecart ;
    } 

    /**
     * Methode permettant de mettre a jour la difference entre le nombre de 
     * reponses types et la reponse 2
     *@param pEcart difference entre le nombre de reponses types et la reponse 2
     **/
    public void setEcart(long pEcart)
    {
        this.ecart = pEcart ;
    }    

    /**
     * Methode permettant d'acceder au classement
     *@return le classement
     **/     
    public long getClassement()
    {
        return this.classement ;
    } 

    /**
     * Methode permettant de mettre a jour le classement
     *@param pClassement classement 
     **/
    public void setClassement(long pClassement)
    {
        this.classement = pClassement ;
    }
    
    /**
     * Extraction des donnees contenues dans une chaine de caracteres. On 
     * considerera que toutes les donnees sont separees par des #
     *@param pDonnee chaine de caracteres contenant les donnees a extraire
     **/
    @Override
    public void deLaBaseDeDonnees(String pDonnee) throws DonneesIncoherentes
    {
        String [] lesDonnees = pDonnee.split("#") ;
        
        try
        {
            super.setNumeroBon(new Long(lesDonnees[0])) ;
            super.setRep1(lesDonnees[1]) ;
            this.setRep2(new Long(lesDonnees[2])) ;
            this.setNbQual(new Long(lesDonnees[3])) ;
            this.setEcart(new Long(lesDonnees[4])) ;
            this.setClassement(new Long(lesDonnees[5])) ;                        
            
        }
        catch (java.lang.Throwable e)
        {
            e.printStackTrace() ;
            throw new DonneesIncoherentes() ;
        }
    } 

    /**
     * Creation d'une chaine de caracteres contenant les donnees a mettre dans 
     * la base de donnees 
     *@return chaine de caracteres contenant les donnees sauvegarder
     **/
    @Override
    public String versLaBaseDeDonnees() 
    {
        String donnees ;
        donnees = super.versLaBaseDeDonnees() + "#" + this.getNbQual() + "#";
        donnees += this.getEcart() + "#" + this.getClassement() ;        
   
        return donnees ;
    }    
    
    /**
     * Extraction des donnees contenues dans un ResultSet 
     *@param pDonnee ResultSet contenant les donnees
     **/    
    @Override
    public void deLaBaseDeDonnees(java.sql.ResultSet pDonnee) 
                                                    throws DonneesIncoherentes
    {
        try
        {
            this.classement = pDonnee.getLong("class");
            this.ecart = pDonnee.getLong("ecart");
            this.nbQual = pDonnee.getLong("nbqual");
            this.setNumeroBon(pDonnee.getLong("numbon"));
            this.setRep1(pDonnee.getString("rep1"));
            this.setRep2(pDonnee.getLong("rep2"));

        }
        catch(SQLException se)
        {
            System.out.println("Une erreur est survenue, et ne me demande pas laquelle car je n'en sais rien\n"+se);
            throw new DonneesIncoherentes();
        }
    }  
    
    /**
     * Mise a jour du ResultSet afin d'ajouter les donnees dans la base de 
     * donnees 
     *@param pDonnee ResultSet a mettre a jour
     **/    
    @Override
    public void versLaBaseDeDonnees(java.sql.ResultSet pDonnee) 
                                                    throws DonneesIncoherentes
    {
        //faire l'inverse de deLaBaseDeDonnees
         try
        {
            pDonnee.updateLong("class",this.classement);
            pDonnee.updateLong("ecart",this.ecart);
            pDonnee.updateLong("nbqual",this.nbQual);
            pDonnee.updateLong("numbon", this.getNumeroBon());
            pDonnee.updateString("rep1",this.getRep1());
            pDonnee.updateLong("rep2", this.getRep2());

        }
        catch(SQLException se)
        {
            System.out.println("Une erreur est survenue, et ne me demande pas laquelle car je n'en sais rien\n"+se);
            throw new DonneesIncoherentes();
        }
    }    
}
