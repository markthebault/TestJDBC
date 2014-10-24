/*
 * SourceDonneesBaseDeDonnees.java
 *
 * Created on 20 septembre 2006, 21:39
 *
 * Cette classe permet de faire le lien entre les classes persistantes
 * et le sources physiques qui sont soit des fichiers soit des tables d'un
 * SGBD. 
 *
 * @author Thierry Millan
 */

package donnees.gestionBaseDeDonnees;
import interfaces.IntConnexion;
import interfaces.IntDonneesPersistantes;
import interfaces.IntSourceDonnees;
import interfaces.IntIterateur ;
import donnees.exception.DonneesIncoherentes ;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SourceDonneesBaseDeDonnees <E extends IntDonneesPersistantes> 
        implements IntSourceDonnees <E>
{

    /**
     * Attribut contenant le mode d'ouverture de la table
     **/
    private IntSourceDonnees.Mode mode ;
    
    /**
     * Attribut contenant le mode d'ouverture de la table
     **/
    private interfaces.IntIterateur.Etat etatIterateur ;
    
    /**
     * Attribut contenant le nom de la table accedee
     **/
    private Class<E> table ;
    
    /**
     * Attribut contenant lma source de donnees
     **/
    private java.sql.ResultSet donnees ;
               
    /**
     * Attribut contenant la connection a la source de donnees
     **/
    private IntConnexion<java.sql.Connection> connexion ;
    
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
     * Attribut contenant le nom de la table mise a jour lors des ajouts
     * 
     **/
    private String nomTable ;
    
    /**
     * Attribut indiquant si on a atteint la fin de l'iterateur ou non
     * 
     **/
    private boolean finIterateur ;

    /**
     * Contient les modes d'ouverture de la connexion
     */
    private int typeCurseur;
    private int typeConcurrence;
    
    /** 
     * Creation d'une nouvelle instance de la table
     *precondition pConnexion est initialisee et la table n'est pas deja ouverte
     *@param pTable classe representant la structure de la table
     *@param pConnexion connection a la source de donnees
     *@param pSource requete permetant d'acceder aux donnees en respectant la structure java qui recevra
     *les donnees. Cette requete pourra contenir des jointures afin de deleguer au SGBD ce calcul delicat
     *@param pNomTable nom de la table recevant les donnees
     */
    public SourceDonneesBaseDeDonnees(Class<E> pTable, IntConnexion<java.sql.Connection> pConnexion, String pSource, String pNomTable) 
    {
        if ((pConnexion == null) || (pConnexion.getClass() == null))
        {
            throw new RuntimeException("La connexion n'existe pas") ;
        }
        this.source = pSource ;
        this.table = pTable ;
        this.connexion = pConnexion ;
        this.nomTable = pNomTable;
        this.etatSource = IntSourceDonnees.Etat.fermee ;
        this.etatIterateur = IntIterateur.Etat.ferme ;
        this.finIterateur = true ;

    }

   /**
    * Methode permettant d'ouvrir la source en mode consultation
    * Si la source de donnees est fermee alors elle est ouverte sinon
    *Precondition : l'iterateur est ferme 
    *               la source est ouverte en mode consultation ou elle est fermee 
    *Postcondition : l'iterateur et la source sont ouverts 
    **/
    public void ouvrirIterateur() throws java.sql.SQLException 
    {
        //on teste si l'itérateur est deja ouvert
        if (this.etatIterateur == IntIterateur.Etat.ouvert)
        {
            throw new RuntimeException("L'iterateur est deja ouvert") ;
        }

        //verification du mode de la source de données
        if ((this.etatSource == IntSourceDonnees.Etat.ouverte) &&
            (this.mode == IntSourceDonnees.Mode.insertion))
        {
            throw new RuntimeException("La source est ouverte en mode insertion...") ;
        }

        //si la source de donnée est fermée on l'ouvre dans le mode correct
        if (this.etatSource == IntSourceDonnees.Etat.fermee)
        {
            this.ouvrirSource(IntSourceDonnees.Mode.consultation) ;
        }

        //creer un état JDBC sertvant a acceder a la base de donnée
        Statement st = this.connexion.getConnexion().createStatement(this.typeCurseur, this.typeConcurrence);

        //execution de la requette
        this.donnees = st.executeQuery(this.source);

        //positionnement de l'état de l'itérateur
        this.etatIterateur = IntIterateur.Etat.ouvert;
        this.finIterateur = !this.donnees.next();
    }
    
   /**
     * Methode permettant d'ouvrir la source en fonction du mode passe 
     * en parametre
     * Precondition : la source est fermee
     * Postcondition : la source est ouverte
     * 
     * @param pMode mode d'ouverture de la table en consultation (select) ou en
     * insertion (ajout)
     */
    public void ouvrirSource(Mode pMode)
    {
        //vérifie que la source est fermée
        if (this.etatSource == IntSourceDonnees.Etat.ouverte)
        {
            throw new RuntimeException("La source est deja ouverte") ;
        }

        //recupère les paramètres du curseur en fonction du mode
        switch (pMode)
        {
            case consultation :
                this.typeCurseur = ResultSet.TYPE_FORWARD_ONLY;
                this.typeConcurrence = ResultSet.CONCUR_READ_ONLY;
            break;

            case modification :
                this.typeCurseur = ResultSet.TYPE_SCROLL_SENSITIVE;
                this.typeConcurrence = ResultSet.CONCUR_UPDATABLE;
            break;

            case insertion :
                this.typeCurseur = ResultSet.TYPE_SCROLL_SENSITIVE;
                this.typeConcurrence = ResultSet.CONCUR_UPDATABLE;
            break;

        }
        
        //ouvrir la source
        this.etatSource = IntSourceDonnees.Etat.ouverte;

    }
    
    /**
     * Fermeture de la source de donnees courante et de l'iterateur
     *Postcondition : la source est fermee
     **/
    public void fermerSource() throws java.sql.SQLException
    {
        if (this.etatSource == IntSourceDonnees.Etat.fermee)
        {
            throw new SQLException();
        }
        if(this.donnees != null)
        {
            this.donnees.close();
        }
        this.etatSource = IntSourceDonnees.Etat.fermee ;
        this.etatIterateur =  IntIterateur.Etat.ferme ;

    }
    
    /**
     * Fermeture de l'iterateur
     *Postcondition : l'iterateur et la source sont fermes
     **/
    public void fermerIterateur() throws java.sql.SQLException
    {
        if(this.etatIterateur == IntIterateur.Etat.ferme)
        {
            throw new SQLException();
        }

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
        return this.finIterateur;
    }
    
    /**
     * Methode permettant d'inserer en fin de table une donnees
     * preconditions : le mode d'ouverture doit etre ajout
     *                la source doit etre ouverte
     * 
     * @param pDonnees pDonnees a inserer
     */ 
    @Override
    public void inserer(E pDonnees) throws java.sql.SQLException
    {
        if (this.mode != IntSourceDonnees.Mode.insertion)
        {
            throw new RuntimeException("Le mode doit etre insert...") ;
        }
        if (this.etatSource == IntSourceDonnees.Etat.fermee)
        {
            throw new RuntimeException("La source est fermee") ;
        }
        if (this.donnees == null)
        {
            java.sql.PreparedStatement etatSimple = this.connexion.getConnexion().prepareStatement(
                "SELECT "+this.nomTable+".* FROM "+this.nomTable,
                java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE) ;
            this.connexion.getConnexion().setAutoCommit(false) ;

            this.donnees = etatSimple.executeQuery() ;
            this.donnees.moveToInsertRow() ;
        }
        try
        {
            pDonnees.versLaBaseDeDonnees(this.donnees) ;
            this.donnees.insertRow() ;
        }
        catch(DonneesIncoherentes ex)
        {
            ex.printStackTrace() ;
        }
    }

    /**
     * Methode permettant d'inserer en fin de table une donnees
     * preconditions : le mode d'ouverture doit etre ajout
     *                la source doit etre ouverte
     * 
     * @param pDonnees pDonnees a inserer
     */ 
    @Override
    public void mettreAJour(E pDonnees) throws java.sql.SQLException
    {
        if (this.mode != IntSourceDonnees.Mode.modification)
        {
            throw new RuntimeException("Le mode doit etre modification...") ;
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
            pDonnees.versLaBaseDeDonnees(this.donnees) ;
            this.donnees.updateRow();
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
    @Override
    public void detruire() throws java.sql.SQLException
    {
        if (this.etatSource == IntSourceDonnees.Etat.ouverte)
        {
            throw new RuntimeException("La source de donnees est ouverte") ;
        }
        java.sql.Statement etatSimple = this.connexion.getConnexion().createStatement() ;
        this.connexion.getConnexion().setAutoCommit(false) ;
        etatSimple.execute("DELETE FROM "+this.nomTable) ;
        this.connexion.getConnexion().commit() ;
        etatSimple.close() ;
    }
    
   /**
    * Methode permettant de lire l'element courant de
    * la source de donnees
    *Precondition : l'iterateur est ouvert
    *@return l'element courant
    **/
   public E lireIterateur() throws java.sql.SQLException 
   {
        //on teste si l'itérateur est deja ouvert
        if (this.etatIterateur == IntIterateur.Etat.ferme)
        {
            throw new RuntimeException("L'iterateur est deja ouvert") ;
        }

        //lire la prochaine donné de l'instance ResultSet
        //positionner l'indicateur de fin de l'itérateur en conséquence
        this.finIterateur = !this.donnees.next();

        //si la derniere valeur du ResultSet est atteinte alors retourner null
        if(this.finIterateur)
        {
            return null;
        }
        else
        {
            //creer une nouvelle instance de E et décoder la valeur du résultset
            E resultat = null;
            try 
            {
                resultat = this.table.newInstance();
                resultat.deLaBaseDeDonnees(this.donnees);
            }
            catch (InstantiationException ex)
            {
                Logger.getLogger(SourceDonneesBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IllegalAccessException ex)
            {
                Logger.getLogger(SourceDonneesBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (DonneesIncoherentes ex)
            {
                Logger.getLogger(SourceDonneesBaseDeDonnees.class.getName()).log(Level.SEVERE, null, ex);
            }
            

            //revoyer le résultat
            return resultat;

        }


   }   
}
