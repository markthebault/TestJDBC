/*
 * Rep1Erronee.java
 *
 * Created on 25 septembre 2006, 22:52
 *
 * Classe representant les erreurs se produisant lorsque la
 * reponse 1 est mal formee c'est-a-dire lorsque deux criteres
 * ont le meme rang.
 *
 */

package donnees.exception;


public class Rep1Erronee extends java.lang.RuntimeException 
{
    /**
     * Attribut contenant la valeur de la reponse 1 erronee
     **/
    String rep1 ;
    
    /**
     * Attribut contenant le numero du bon erronee 
     **/
    long numeroBon ;
    
    /**
     * Attribut contenant la valeur de controle
     **/
    String ctrlRep1 ;
        
    /**
     * Constructeur de la classe permettant de remonter les valeurs erronees 
     *@param pNumeroBon numero du bon errone 
     *@param pRep1 reponse erronee
     *@param pCtrlRep1 valeur de controle de la premiere reponse
     **/
    public Rep1Erronee(long pNumeroBon, String pRep1, String pCtrlRep1) 
    {
        this.numeroBon = pNumeroBon ;
        this.rep1 = pRep1 ;
        this.ctrlRep1 = pCtrlRep1 ;
    }

    /**
     * Constructeur de la classe permettant de remonter les valeurs erronees 
     *@param pNumeroBon numero du bon errone 
     *@param pRep1 reponse erronee
     *@param pCtrlRep1 valeur de controle de la premiere reponse
     *@param pMessage message d'erreur
     **/
    public Rep1Erronee(long pNumeroBon, String pRep1, String pCtrlRep1, String pMessage) 
    {
        super(pMessage) ;
        this.numeroBon = pNumeroBon ;
        this.rep1 = pRep1 ;
        this.ctrlRep1 = pCtrlRep1 ;
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
}
