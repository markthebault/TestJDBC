/*
 * TraitementConcoursFichier.java
 *
 * Created on 30 septembre 2006, 23:57
 *
 * Classe implantant la logique metier du traitement du concours.
 * Elle regroupe l'ensemble des operations necessaires a l'elaboration des
 * resultats lorsque la totalite des traitements est deleguee au systeme de 
 * gestion de bases de donnees.
 *
 */

package metier;

public class TraitementConcoursProcedureOracle 
        extends TraitementConcoursBaseDeDonnees
{   
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
    public TraitementConcoursProcedureOracle(String pAdresse,
            int pPort, String pBase, String pLogin, String pMotDePasse)
    {
        super(pAdresse, pPort, pBase, pLogin, pMotDePasse);
    }
    
    @Override
   protected void creerBonsBons() 
   {
       System.out.println("CreerBonsBons est inutile...") ;
   }
   
    @Override
   protected void calculerRepType() 
   {
       System.out.println("calculerRepType est inutile...") ;       
   }
   
    @Override
   protected void creerBonsNbqualCalcNbRepType() 
   {
       System.out.println("creerBonsNbqualCalcNbRepType est inutile...") ;              
   }
   
    @Override
   protected void creerBonsEcart() 
   {
       System.out.println("creerBonsEcart est inutile...") ;              
   }
   
    @Override
   protected void extractionDesResultats() 
   {
       System.out.println("extractionDesResultats est inutile...") ;              
   }
  
}
