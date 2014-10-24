/*
 * TraitementConcoursFichier.java
 *
 * Created on 30 septembre 2006, 23:57
 *
 * Classe implantant la logique metier du traitement du concours.
 * Elle regroupe l'ensemble des operations necessaires a l'elaboration des
 * resultats lorsque seule la persistance des donnees est deleguee au
 * systeme de gestion de bases de donnees.
 *
 */

package metier;

import donnees.Bons_Bons;
import donnees.Bons_Depart;
import donnees.Bons_Errones;
import donnees.gestionBaseDeDonnees.ConnexionBaseDeDonnees;
import interfaces.IntSourceDonnees;
import donnees.gestionBaseDeDonnees.SourceDonneesBaseDeDonnees;

public class TraitementConcoursBaseDeDonnees extends TraitementConcoursFichier
{
    /**
     * Attribut contenant la connexion courante
     **/
    protected ConnexionBaseDeDonnees connexion ;
    
    /**
     * Sources de donnees utilisees pour effectuer le traitement 
     * consistant a calculer les resultats du concours.
     * On surcharge la definition de donneesBonsBons afin
     * de ne plus utiliser la classe Bons_Bons qui effectue les
     * verification en Java mais la classe Bons_Depart qui a la
     * meme structure que Bons_Bons mais sans verification. La
     * source des donnees doit etre celle des Bons_Bons. Les verifications
     * sont realisees par le SGBD.
     **/
     protected IntSourceDonnees<Bons_Depart> donneesBonsBonsSansVerif  ;
    
    /**
     * 
     * Creation d'une instance d'une classe TraitementConcoursFichier
     * 
     * @param pAdresse adresse IP du systeme de base de donnees
     * @param pPort numero du port où la base est accessible
     * @param pBase nom de la base où se trouve les tables a acceder
     * @param pLogin identifiant de l'utilisateur
     * @param pMotDePasse mot de passe de l'utilisateur
     */
    public TraitementConcoursBaseDeDonnees(String pAdresse, int pPort, String pBase, String pLogin, String pMotDePasse) 
    {   
        try
        {
            this.connexion = ConnexionBaseDeDonnees.ouvrirConnexion (pAdresse, pPort, pBase, pLogin, pMotDePasse) ;
            this.donneesBonsDepart =    new SourceDonneesBaseDeDonnees<Bons_Depart>  (Bons_Depart.class,  this.connexion, 
                    "Select NUMBON, REP1, REP2 from Bons_Depart", "Bons_Depart") ;
            this.donneesBonsBons =      new SourceDonneesBaseDeDonnees<Bons_Bons>    (Bons_Bons.class,    this.connexion, 
                    "Select Bons_Bons.* from BONS_BONS","BONS_BONS") ;
            this.donneesBonsBonsSansVerif =      new SourceDonneesBaseDeDonnees<Bons_Depart>    (Bons_Depart.class,    this.connexion, 
                    "Select Bons_Bons.* from BONS_BONS","BONS_BONS") ;
            this.donneesBonsErrones =   new SourceDonneesBaseDeDonnees<Bons_Errones> (Bons_Errones.class, this.connexion, 
                    "Select NUMBON, REP1, CTRL_REP1, MOTIF from BONS_ERR", "BONS_ERR") ;
            this.donneesResultats =   new SourceDonneesBaseDeDonnees<Bons_Bons> (Bons_Bons.class, this.connexion, 
                    "Select BONS_GAGNANTS.* from BONS_GAGNANTS", "BONS_BONS") ;            
            
            this.creerBonsBons() ;
            this.calculerRepType() ;
            this.creerBonsNbqualCalcNbRepType() ;
            this.creerBonsEcart() ;
            this.extractionDesResultats() ;
        }
        catch(java.sql.SQLException ex)
        {
            ex.printStackTrace() ;
            System.exit(1) ;
        }
        catch(java.io.IOException ex1)
        {
            ex1.printStackTrace() ;
            System.exit(1) ;
        }
    }
    
    
    @Override
    protected void creerBonsBons () throws java.io.IOException, java.sql.SQLException
    {
          System.out.println("Debut creerBonsBons") ;
          this.donneesBonsBonsSansVerif.detruire() ;
          this.donneesBonsErrones.detruire() ;

          this.donneesBonsDepart.ouvrirSource(IntSourceDonnees.Mode.consultation);
          this.donneesBonsBonsSansVerif.ouvrirSource(IntSourceDonnees.Mode.insertion) ;
          this.donneesBonsErrones.ouvrirSource(IntSourceDonnees.Mode.insertion) ;

          this.donneesBonsDepart.ouvrirIterateur() ;
          Bons_Depart unBonDepart = donneesBonsDepart.lireIterateur() ;

          while (!this.donneesBonsDepart.finIterateur())
          {
              try
              {
                this.donneesBonsBonsSansVerif.inserer(unBonDepart) ;
              }
              catch (java.sql.SQLException ex)
              {
                  String message = ex.getMessage() ;

                  String ctrlRep1 = "" ;
                  Bons_Errones unBonErrone = new Bons_Errones() ;
                  unBonErrone.setNumeroBon(unBonDepart.getNumeroBon()) ;
                  unBonErrone.setRep1(unBonDepart.getRep1()) ;
                  switch(ex.getErrorCode())
                  {
                      case 1 :
                          ctrlRep1 = " " ;
                          message = unBonDepart.getNumeroBon()+" existe deja" ;
                          break ;
                      case 20001 :
                          ctrlRep1 = message.substring(29, 39) ;
                          message =  message.substring(11, 29) ;
                          break ;
                      default :
                              message = ex.getMessage() ;
                  }
                  unBonErrone.setCtrlRep1(ctrlRep1) ;
                  unBonErrone.setMotif(message) ;
                  this.donneesBonsErrones.inserer(unBonErrone) ;
              }
              unBonDepart = donneesBonsDepart.lireIterateur() ;
          }

          this.donneesBonsDepart.fermerSource() ;
          this.donneesBonsBonsSansVerif.fermerSource() ;
          this.donneesBonsErrones.fermerSource() ;

          System.out.println("Fin creerBonsBons") ;
    }
    
    @Override
    protected void extractionDesResultats () throws java.io.IOException, java.sql.SQLException
    {
        //Ouvrir la source de donn�es
        
    }    
}
