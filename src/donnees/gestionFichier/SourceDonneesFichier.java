/*
 * SourceDonneesFichier.java
 *
 * Created on 20 septembre 2006, 21:39
 *
 * Cette classe permet de faire le lien entre les classes persistantes
 * et le sources physiques qui sont soit des fichiers soit des tables d'un
 * SGBD. 
 * @author Thierry Millan
 */

package donnees.gestionFichier;
import interfaces.IntConnexion;
import interfaces.IntDonneesPersistantes;
import interfaces.IntSourceDonnees;
import donnees.exception.DonneesIncoherentes;
import interfaces.IntIterateur;
import java.io.RandomAccessFile ;
import java.io.File ;

public class SourceDonneesFichier <E extends IntDonneesPersistantes> implements IntSourceDonnees <E>
{

    /**
     * Nom du fichier temporaire
     **/
    private static final String nomFichierTmp = "fich.tmp" ;
    
    /**
     * Attribut contenant le vMode d'ouverture de la table
     **/
    private IntSourceDonnees.Mode mode ;
    
    /**
     * Attribut contenant le vMode d'ouverture de la table
     **/
    private interfaces.IntIterateur.Etat etatIterateur ;
    
    /**
     * Attribut contenant le nom de la table accedee
     **/
    private Class<E> table ;
    
    /**
     * Attribut contenant la source de donnees
     **/
    private RandomAccessFile donnees ;

    /**
     * Attribut contenant la source de donnees
     **/
    private RandomAccessFile tmp ;    
    
    /**
     * Attribut contenant la connection a la source de donnees
     **/
    private IntConnexion<String> connexion ;
    
    /**
     * Attribut statique indiquant si une source est fermee ou ouverte
     **/
    private IntSourceDonnees.Etat etatSource ;
    
    /**
     * Attribut contenant la source de donnees traitee
     * 
     **/
    private String source ;
    
    /**
     * Attribut indiquant si on a atteint la fin de l'iterateur ou non
     * 
     **/
    private boolean finIterateur ;
    
    /**
     * Attribut contenant la position courante dans le fichier temporaire
     */
    private long index ;
    
      
    
    /** 
     * Creation d'une nouvelle instance de la table
     *precondition pConnexion est initialisee et la table n'est pas deja ouverte
     *@param pTable classe representant la structure de la table
     *@param pConnexion connection a la source de donnees
     *@param pSource contient le nom du fichier representant la source de donnees
     */
    public SourceDonneesFichier(Class<E> pTable, IntConnexion<String> pConnexion, String pSource) 
    {
        if ((pConnexion == null) || (pConnexion.getClass() == null))
        {
            throw new RuntimeException("La connexion n'existe pas") ;
        }
        this.source = pSource ;
        this.table = pTable ;
        this.connexion = pConnexion ;
        this.etatSource = IntSourceDonnees.Etat.fermee ;
        this.etatIterateur = IntIterateur.Etat.ferme ;
        this.finIterateur = true ;
        this.tmp = null ;
    }

   /**
    * Methode permettant d'ouvrir la source en vMode consultation
    * Si la source de donnees est fermee alors elle est ouverte sinon
    *Precondition : l'iterateur est ferme 
    *               la source est ouverte en vMode consultation ou elle est fermee 
    *Postcondition : l'iterateur et la source sont ouverts 
    **/
    public void ouvrirIterateur()  
    {
        if (this.etatIterateur == IntIterateur.Etat.ouvert)
        {
            throw new RuntimeException("L'iterateur est deja ouvert") ;
        }  
        if ((this.etatSource == IntSourceDonnees.Etat.ouverte) &&
            (this.mode == IntSourceDonnees.Mode.insertion))
        {
            throw new RuntimeException("La source est ouverte en mode insertion...") ;
        }
        
        
        if (this.etatSource == IntSourceDonnees.Etat.fermee)
        {
            this.ouvrirSource(IntSourceDonnees.Mode.consultation) ;  
        }
        else
        {
            try
            {
                this.donnees.seek(0L) ;
                this.index = 0L ;
            }
            catch (java.io.IOException ex)
            {
                ex.printStackTrace() ;
            }
        }
        this.etatIterateur = IntIterateur.Etat.ouvert ;
        this.finIterateur = true ;
    }
    
   /**
     * Methode permettant d'ouvrir la source en fonction du vMode passe 
     * en parametre
     * Precondition : la source est fermee
     * Postcondition : la source est ouverte
     * 
     * @param pMode vMode d'ouverture de la table en consultation (select) ou en
     * insertion (ajout)
     */
    public void ouvrirSource(IntSourceDonnees.Mode pMode)
    {
        if (this.etatSource == IntSourceDonnees.Etat.ouverte)
        {
            throw new RuntimeException("La source est deja ouverte") ;
        }
        this.etatSource = IntSourceDonnees.Etat.ouverte ;
        this.mode = pMode ;
        
        String vMode = "r" ;
        
        if (this.mode == IntSourceDonnees.Mode.consultation)
        {
            vMode ="r" ;
        }
        else if (this.mode == IntSourceDonnees.Mode.insertion ||
                 this.mode == IntSourceDonnees.Mode.modification)
        {
            vMode = "rwd" ;
        }
     
        try
        { 
            String monChemin =  this.connexion.getConnexion() + 
                                File.separatorChar + this.source ;
            this.donnees = new RandomAccessFile(monChemin, vMode) ;
            if (this.mode == IntSourceDonnees.Mode.modification)
            {
                String nomCheminTmp =   this.connexion.getConnexion() + 
                                        File.separatorChar + 
                                        SourceDonneesFichier.nomFichierTmp ;
                this.tmp = new RandomAccessFile(nomCheminTmp, vMode) ;                
            }
        }
        catch (IllegalArgumentException e1)
        {
            e1.printStackTrace() ;
        }
        catch (java.io.FileNotFoundException e2)
        {
            e2.printStackTrace() ;
        }
        catch (SecurityException e3)
        {
            e3.printStackTrace() ;
        } 
    }
    
    /**
     * Fermeture de la source de donnees courante et de l'iterateur
     *Postcondition : la source est fermee
     **/
    public void fermerSource()
    {
        if (this.etatSource == IntSourceDonnees.Etat.ouverte)
        {
            try
            {
                this.donnees.close() ;
                this.etatSource = IntSourceDonnees.Etat.fermee ;
                this.etatIterateur =  IntIterateur.Etat.ferme ;
                if (this.mode == IntSourceDonnees.Mode.modification)
                {
                    String monChemin =  this.connexion.getConnexion() + 
                                File.separatorChar + this.source ;

                    String nomCheminTmp =   this.connexion.getConnexion() + 
                                        File.separatorChar + 
                                        SourceDonneesFichier.nomFichierTmp ; 
                    this.tmp.close() ;
                    java.io.File fichierTmp = new java.io.File(nomCheminTmp) ;
                    java.io.File fichierDonnees = new java.io.File(monChemin) ;
                    fichierDonnees.delete() ;
                    fichierTmp.renameTo(fichierDonnees) ;   
                }
            }
            catch (java.io.IOException e1)
            {
                e1.printStackTrace() ;
            } 
        }
    }
    
    /**
     * Fermeture de l'iterateur
     *Postcondition : l'iterateur et la source sont fermes
     **/
    public void fermerIterateur() 
    {
        this.finIterateur = true ;
        this.fermerSource() ;
    }
    
   /**
    * Methode retournant vrai si la fin de la source de donnees est
    * atteinte
    *Precondition : l'iterateur est ouvert
    *@return vrai si le dernier element a ete lu
    **/
    public boolean finIterateur() 
    {
        if (this.etatIterateur == IntIterateur.Etat.ferme)
        {
            throw new RuntimeException("L'iterateur n'est pas ouvert") ;
        }        
        return (this.finIterateur) ;
    }
    
    /**
     * Methode permettant d'inserer en fin de table une donnees
     * preconditions : le vMode d'ouverture doit etre ajout
     *                la source doit etre ouverte
     * 
     * @param pDonnees donnees a inserer
     */ 
    public void inserer(E pDonnees) throws java.io.IOException
    {
        if (this.mode != IntSourceDonnees.Mode.insertion)
        {
            throw new RuntimeException("Le mode doit etre insert...") ;
        }
        if (this.etatSource == IntSourceDonnees.Etat.fermee)
        {
            throw new RuntimeException("La source est fermee") ;
        }
        try
        {
            String donneesChaine = pDonnees.versLaBaseDeDonnees() ;
            this.donnees.writeBytes(donneesChaine+"\n") ;
        }
        catch(DonneesIncoherentes ex)
        {
            ex.printStackTrace() ;
        }
    }

    /**
     * Methode permettant d'inserer en fin de table une donnees
     * preconditions : le vMode d'ouverture doit etre ajout
     *                la source doit etre ouverte
     * 
     * @param pDonnees donnees a inserer
     */ 
    public void mettreAJour(E pDonnees) throws java.io.IOException
    {
        if (this.mode != IntSourceDonnees.Mode.modification)
        {
            throw new RuntimeException("Le mode doit etre mise a jour...") ;
        }
        if (this.etatSource == IntSourceDonnees.Etat.fermee)
        {
            throw new RuntimeException("La source est fermee") ;
        }
        if (this.etatIterateur == IntIterateur.Etat.ferme)
        {
            throw new RuntimeException("L'iterateur est ferme") ;
        }         
        try
        {                                                  
            String donneesChaine = pDonnees.versLaBaseDeDonnees() ;
            this.tmp.seek(this.index) ;
            this.tmp.writeBytes(donneesChaine + '\n') ;                                               
        }
        catch(DonneesIncoherentes ex)
        {
            ex.printStackTrace() ;
        }
    }    
    
    /** 
     * Methode permettant de supprimer toutes les donnees de la table
     *Postcondition : la source de donnees est fermee
     **/
    public void detruire() throws java.io.IOException
    {
        if (this.etatSource == IntSourceDonnees.Etat.ouverte)
        {
            throw new RuntimeException("La source de donnees est ouverte") ;
        }
        
        java.io.File fichierADetruire = new java.io.File(
                this.connexion.getConnexion()+
                File.separatorChar+this.source) ;
        fichierADetruire.delete() ;
        fichierADetruire.createNewFile() ;
    }
    
   /**
    * Methode permettant de lire l'element courant de
    * la source de donnees
    *Precondition : l'iterateur est ouvert
    *@return l'element courant
    **/
   public E lireIterateur() throws java.io.IOException 
   {
       if (this.etatIterateur == IntIterateur.Etat.ferme)
       {
            throw new RuntimeException("L'iterateur est ferme") ;
       }
       
       String chaineContenantLaDonnee ;
       E resultat = null ;
        
       try
       {            
            chaineContenantLaDonnee = this.donnees.readLine() ;
            if (chaineContenantLaDonnee == null)
            {
                resultat = null ;
                this.finIterateur = true ;
            }
            else
            {
                resultat = this.table.newInstance() ;
                resultat.deLaBaseDeDonnees(chaineContenantLaDonnee) ;
                this.finIterateur = false ;
                if (this.mode == IntSourceDonnees.Mode.modification)
                {
                    this.index = this.tmp.getFilePointer() ;
                    String donneesChaine = resultat.versLaBaseDeDonnees() ;
                    this.tmp.writeBytes(donneesChaine + '\n') ;           
                }
            }                                 
       }
       catch (java.io.IOException e1)
       {
            e1.printStackTrace() ;
       }
       catch (java.lang.InstantiationException e2)
       {
            e2.printStackTrace() ;
       }
       catch (java.lang.IllegalAccessException e3)
       {
            e3.printStackTrace() ;
       }
       catch (donnees.exception.DonneesIncoherentes e4)
       {
            e4.printStackTrace() ;
       }
        
       return resultat ;
   }   
}
