/*
 * NumBonExisteDeJa.java
 *
 * Created on 25 septembre 2006, 22:51
 *
 * Classe representant les erreurs se produisant lorsqu'un
 * numero de bon existe deja.
 *
 */

package donnees.exception;


public class NumBonExisteDeJa extends java.lang.RuntimeException
{
    /**
     * Attribut contenant le numero du bon erronee 
     **/
    long numeroBon ;
    
    /**
     * Constructeur de la classe permettant de remonter les numeros de bons erronees 
     *@param pNumeroBon numero du bon errone e
     **/
    public NumBonExisteDeJa(long pNumeroBon) 
    {
        this.numeroBon = pNumeroBon ;        
    }
    
    /**
     * Constructeur de la classe permettant de remonter les numeros de bons erronees 
     *@param pNumeroBon numero du bon errone 
     *@param pMessage message d'erreur
     **/
    public NumBonExisteDeJa(long pNumeroBon, String pMessage) 
    {
        super(pMessage) ;
        this.numeroBon = pNumeroBon ;
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
     * Methode permettant de mettre a jour le numero d'un bon
     *@param pNumeroBon le numero du bon
     **/
    public void setNumeroBon(long pNumeroBon)
    {
        this.numeroBon = pNumeroBon ;
    }    
}
