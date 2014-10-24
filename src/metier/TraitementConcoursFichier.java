/*
 * TraitementConcoursFichier.java
 *
 * Created on 30 septembre 2006, 23:57
 *
 * Classe implantant la logique metier du traitement du concours.
 * Elle regroupe l'ensemble des operations necessaires a l'elaboration des
 * resultats
 *
 */

package metier;

import donnees.Bons_Bons;
import donnees.Bons_Depart;
import donnees.Bons_Errones;
import donnees.gestionFichier.ConnexionFichier;
import interfaces.IntSourceDonnees;
import donnees.gestionFichier.SourceDonneesFichier;

public class TraitementConcoursFichier 
{
    /**
     * Attribut contenant la connexion courante
     **/
    private ConnexionFichier connexion ;
    
    /**
     * Attribut contenant le nombre de reponse type 
     **/
    private long nbRepType ;
    
    /**
     * Attribut contenant la reponse type 
     **/
    private String repType ;    
    
    /**
     * Constante representant le nombre de gagnant
     **/
    protected static final int CST_NB_GAGNANTS = 1000 ;
    
    /**
     * Nombre de donnees traitees
     **/
    private int nbDonneesTraitees ;
    
    /**
     * Sources de donnees utilisees pour effectuer le traitement 
     * consistant a calculer les resultats du concours
     **/
     protected IntSourceDonnees<Bons_Depart> donneesBonsDepart  ;
     protected IntSourceDonnees<Bons_Bons> donneesBonsBons  ;
     protected IntSourceDonnees<Bons_Bons> donneesResultats  ;     
     protected IntSourceDonnees<Bons_Errones> donneesBonsErrones ;
    
    protected TraitementConcoursFichier()
    {
        
    }
    /**
     * 
     * Creation d'une instance d'une classe TraitementConcoursFichier
     * @param pPath chemin d'acces au fichier devant etre traite
     */
    public TraitementConcoursFichier(String pPath) 
    {   
        try
        {
            this.connexion = ConnexionFichier.ouvrirConnexion (pPath) ;
            this.donneesBonsDepart =    new SourceDonneesFichier<Bons_Depart>  (Bons_Depart.class,  this.connexion, "Bons_Depart.txt") ;
            this.donneesBonsBons =      new SourceDonneesFichier<Bons_Bons>    (Bons_Bons.class,    this.connexion, "Bons_Bons.txt") ;
            this.donneesResultats =      new SourceDonneesFichier<Bons_Bons>    (Bons_Bons.class,    this.connexion, "Bons_Resultats.txt") ;            
            this.donneesBonsErrones =   new SourceDonneesFichier<Bons_Errones> (Bons_Errones.class, this.connexion, "Bons_Errones.txt") ;
  
            this.nbDonneesTraitees = 0;
            this.creerBonsBons() ;
            this.calculerRepType() ;
            this.creerBonsNbqualCalcNbRepType() ;
            this.creerBonsEcart() ;
            this.extractionDesResultats() ;
        }
        catch(java.io.IOException ex)
        {
            ex.printStackTrace() ;
            System.exit(1) ;
        }
        catch(java.sql.SQLException ex2)
        {
            ex2.printStackTrace() ;
            System.exit(2) ;
        }
    }

  protected void creerBonsBons () throws java.io.IOException, java.sql.SQLException
  {
      System.out.println("Debut creerBonsBons") ;
      this.donneesBonsBons.detruire() ;
      this.donneesBonsErrones.detruire() ;
      
      this.donneesBonsDepart.ouvrirSource(IntSourceDonnees.Mode.consultation);
      this.donneesBonsBons.ouvrirSource(IntSourceDonnees.Mode.insertion) ;
      this.donneesBonsErrones.ouvrirSource(IntSourceDonnees.Mode.insertion) ;
      
      this.donneesBonsDepart.ouvrirIterateur() ;
      Bons_Depart bd = donneesBonsDepart.lireIterateur() ;
      
      while (!donneesBonsDepart.finIterateur())
      {
          try
          {
              Bons_Bons bb = new Bons_Bons () ;

              bb.setNumeroBon(bd.getNumeroBon()) ;
              bb.setRep1(bd.getRep1()) ;
              bb.setRep2(bd.getRep2()) ;
              donneesBonsBons.inserer(bb) ;
              this.nbDonneesTraitees++ ;
          }
          catch (donnees.exception.NumBonExisteDeJa e1)
          {
              Bons_Errones be = new Bons_Errones() ;
              be.setNumeroBon(e1.getNumeroBon()) ;
              be.setRep1("") ;
              be.setCtrlRep1("") ;
              be.setMotif("Numero de bon deja traite - Bon rejete") ;
              donneesBonsErrones.inserer(be) ;
          }
          catch (donnees.exception.Rep1Erronee e2)
          {
              Bons_Errones be = new Bons_Errones() ;
              be.setNumeroBon(e2.getNumeroBon()) ;
              be.setRep1(e2.getRep1()) ;
              be.setCtrlRep1(e2.getCtrlRep1()) ;
              be.setMotif("La reponse 1 est mal formee - Bon rejete") ;
              donneesBonsErrones.inserer(be) ;
          } 
          bd = donneesBonsDepart.lireIterateur() ;
      }
      donneesBonsDepart.fermerSource() ;
      donneesBonsBons.fermerSource() ;
      donneesBonsErrones.fermerSource() ;
      
      Bons_Bons.liberationMemoire() ;
      System.out.println("Fin creerBonsBons") ;
  }
  
  private class EnrType
  {
      public char lettre ;
      public long poids ;
  }

  protected void calculerRepType() throws java.io.IOException, java.sql.SQLException 
  {
      EnrType [] tabPoids = new EnrType[10] ;
      boolean poidsPermute  ;
      EnrType auxEnrPoids ;
      Bons_Bons unBon ;
      
      this.donneesBonsBons.ouvrirSource(IntSourceDonnees.Mode.consultation) ;

      for (int iL = 0 ; iL <10 ; iL++)
      {
        tabPoids[iL] = new EnrType() ;
        tabPoids[iL].poids = 0L ;
        tabPoids[iL].lettre = (char) (97 + (int) iL) ;
      }
      
      this.donneesBonsBons.ouvrirIterateur() ;
      unBon = this.donneesBonsBons.lireIterateur() ;
      
      while (!donneesBonsBons.finIterateur())
      {
         int iL = 0 ;

         for (int iR = 0 ; iR < 10 ; iR++)
         {
               iL = (int) unBon.getRep1().charAt(iR) - 97 ;
               tabPoids[iL].poids = tabPoids[iL].poids + 11 - iR ;
         }
         unBon = this.donneesBonsBons.lireIterateur() ;
      }
      
      poidsPermute = true ;
      while (poidsPermute)
      {
        poidsPermute = false ;
        for (int iL = 0 ; iL <9 ; iL++)
        {
           if (tabPoids[iL].poids < tabPoids[iL+1].poids)
           {
              poidsPermute = true ;
              auxEnrPoids = tabPoids[iL] ;
              tabPoids[iL] = tabPoids[iL + 1] ;
              tabPoids[iL+1] = auxEnrPoids ;
           }
        }
      }

      this.repType = "          " ;
      for (int iL = 0 ; iL < 10 ; iL++)
      {
          String debut = (iL<1?"":this.repType.substring(0, iL)) ;
          String fin   = (iL==this.repType.length()-1?"":this.repType.substring(iL+1, this.repType.length())) ;
          this.repType =  debut + tabPoids[iL].lettre + fin ;
      }

      donneesBonsBons.fermerSource() ;
      System.out.println("Fin calculerRepType. Reponse type = "+this.repType) ;
  }

  protected void creerBonsNbqualCalcNbRepType () throws java.io.IOException, java.sql.SQLException
  {
      this.donneesBonsBons.ouvrirSource(IntSourceDonnees.Mode.modification) ;
//      this.donneesBonsNbqual.ouvrirSource(IntSourceDonnees.Mode.modification) ;
      
      this.nbRepType = 0 ;
      long nbEqual ;
      Bons_Bons unBon ;
//      Bons_Nbqual unBonNbqual ;

      this.donneesBonsBons.ouvrirIterateur() ;
      unBon = this.donneesBonsBons.lireIterateur() ;      
      
      while (!this.donneesBonsBons.finIterateur())
      {
        if (!unBon.getRep1().equals(this.repType))
        {
          int iL = 0 ;
          while (unBon.getRep1().charAt(iL)==this.repType.charAt(iL))
          {
             iL = iL + 1 ;
          }
          nbEqual = iL ;
        }
        else
        {
          nbEqual = 10 ;
          this.nbRepType = this.nbRepType + 1 ;
        }
        unBon.setNbQual(nbEqual) ;
        this.donneesBonsBons.mettreAJour(unBon) ;
        unBon = this.donneesBonsBons.lireIterateur() ;  
      }
      donneesBonsBons.fermerSource() ;
      System.out.println("Fin creerBonsNbqualCalcNbRepType") ;
  }


  protected void creerBonsEcart() throws java.io.IOException, java.sql.SQLException
  {
      long ecart ;
      Bons_Bons unBon ;

//      this.donneesBonsNbqual.ouvrirSource(IntSourceDonnees.Mode.consultation) ;
      this.donneesBonsBons.ouvrirSource(IntSourceDonnees.Mode.modification) ; 
      
      this.donneesBonsBons.ouvrirIterateur() ;
      unBon = this.donneesBonsBons.lireIterateur() ;         

      while (!this.donneesBonsBons.finIterateur()) 
      {
        ecart = Math.abs(this.nbRepType - unBon.getRep2()) ;     
        unBon.setEcart(ecart) ;        
        this.donneesBonsBons.mettreAJour(unBon) ; 
        unBon = this.donneesBonsBons.lireIterateur() ;        
      }
      this.donneesBonsBons.fermerSource() ;
      System.out.println("Fin creerBonsEcart") ;
  }

    protected static boolean comparaisonNbqualEcart(
                                  long pPremierNbqual,
                                  long pPremierEcart,
                                  long pSecondNbqual,
                                  long pSecondEcart)

    {
      boolean resultat = true ;
      if (pPremierNbqual == pSecondNbqual)
      {
        resultat = (pPremierEcart < pSecondEcart) ;
      }
      else
      {
        resultat =  (pPremierNbqual > pSecondNbqual) ;
      }
      return (resultat) ;
    }
    

   protected void extractionDesResultats () throws java.io.IOException, java.sql.SQLException
    {
        int nbElements = 0 ; 
        this.donneesResultats.detruire() ;
        
        if (this.nbDonneesTraitees < TraitementConcoursFichier.CST_NB_GAGNANTS)
        {
            nbElements = this.nbDonneesTraitees ;
        }
        else
        {
            nbElements = TraitementConcoursFichier.CST_NB_GAGNANTS ;            
        }
        
        Bons_Bons [] lesBonsBons_Ecart = new Bons_Bons [nbElements + 1] ;
        Bons_Bons [] lesBons_Classement = new Bons_Bons [nbElements + 1] ;
        Bons_Bons unBonBon ;
        boolean trouve = false ;      
        
        this.donneesBonsBons.ouvrirSource(IntSourceDonnees.Mode.consultation) ;
        
        int nbElt = 0 ;
        int iM, iG, iD ; 
        
        this.donneesBonsBons.ouvrirIterateur() ;
        unBonBon = this.donneesBonsBons.lireIterateur() ;   
        
        while (!this.donneesBonsBons.finIterateur()) 
        {
            iG = 0 ;
            iD = nbElt - 1 ;

            while (iG <= iD)
            {
               iM = (iG + iD) / 2 ;

               if (TraitementConcoursFichier.comparaisonNbqualEcart(
                       lesBonsBons_Ecart[iM].getNbQual(), 
                       lesBonsBons_Ecart[iM].getEcart(),
                       unBonBon.getNbQual(), 
                       unBonBon.getEcart()))
               {
                    iG = iM + 1 ;
               }
               else
               {
                 iD = iM - 1 ;
               }
            }

            for (int iL = nbElt  ; iL > iG ; iL--)
            {
               lesBonsBons_Ecart[iL] = lesBonsBons_Ecart[iL - 1] ;
            }

            lesBonsBons_Ecart[iG] = unBonBon ;

            if (nbElt < nbElements) 
            {
               nbElt = nbElt + 1 ;
            }
            unBonBon = this.donneesBonsBons.lireIterateur() ;  
        }
        this.donneesBonsBons.fermerSource() ;
        
        this.donneesBonsBons.ouvrirSource(IntSourceDonnees.Mode.consultation) ;        
        this.donneesBonsBons.ouvrirIterateur() ;
        unBonBon = this.donneesBonsBons.lireIterateur() ;   


        // Jointure entre les sources Bons_Class et Bons_Bons
        while (!this.donneesBonsBons.finIterateur()) 
        {         
            trouve = false ;     
            
            for (int iL = 0 ; ((iL < nbElements) && (!trouve)) ; iL++)
            {
                if (unBonBon.getNumeroBon() == lesBonsBons_Ecart[iL].getNumeroBon())
                { 
                    lesBons_Classement[iL] = unBonBon ;
                    lesBons_Classement[iL].setClassement(iL+1) ;
                    trouve = true ;                                         
                }
            }
            unBonBon = this.donneesBonsBons.lireIterateur() ;
        }
        this.donneesBonsBons.fermerIterateur() ;
        
        this.donneesResultats.ouvrirSource(IntSourceDonnees.Mode.insertion) ;
        for (int iL = 0 ; iL < nbElements ; iL++)
        {
            this.donneesResultats.inserer(lesBons_Classement[iL]) ;
        }
        
        this.donneesResultats.fermerSource() ;
        System.out.println("Fin extractionDesResultats") ;        
    }
    
    /**
     * Methode retournant un iterateur sur les resultats
     *@return l'iterateur sur la collection contenant les resultats
     **/
    public interfaces.IntIterateur<Bons_Bons> getResultats()
    {
        return this.donneesResultats ;
    }
    
    /**
     * Methode retournant un iterateur sur les bons errones
     *@return l'iterateur sur la collection contenant les bons errones
     **/
    public interfaces.IntIterateur<Bons_Errones> getErreurs()
    {
        return this.donneesBonsErrones ;
    } 
}
