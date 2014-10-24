package affichage ;

import java.awt.* ;
import javax.swing.* ;
import javax.swing.table.DefaultTableModel ;
import java.awt.event.* ;
import java.io.File ;

/**
 * Classe permettant de representer graphiquement les resultats du
 * concours YSLAV
 *@author Thierry MILLAN 
 *@version 1.5 
 *@see javax.swing.JComponent

*/
public class PresentationResultats extends JPanel
{     
    private enum ChoixUtilisateur {fichier, bd, procedurecataloguee} ;

    private static String POLICE = "Times" ;
    
    /**
     * Attribut contenant le choix de l'utilisateur
    **/
    private ChoixUtilisateur choixUtilisateur ;    
    
    /**
     * Attribut pemettant d'avoir des onglets pour les resultats et les erreurs
    **/
    private JTabbedPane onglets ;

    /**
     * Table contenant les resultats
    **/
    //private DefaultTableModel modeleTableRes ;
    private JTable tableDesResultats  ;
    
    /**
     * Table contenant les bons errones
    **/
    //private DefaultTableModel modeleTableErr ;
    private JTable tableErreurs ;
    
    /**
     * Etiquette contenant la duree de la generation
    **/
    private JLabel duree ; 
    
    /**
     * Champ de saisie contenant l'adresse de connexion
     * a la base de donnees
    **/
    private javax.swing.JTextField adresse;
    
    /**
     * Champ de saisie contenant le numero de port pour la connexion
     * a la base de donnees
    **/    
    private javax.swing.JTextField port;
    
    /**
     * Champ de saisie contenant le nom de la base de donnees
    **/      
    private javax.swing.JTextField base;
    
    /**
     * Champ de saisie contenant le login a la base de donnees
    **/    
    private javax.swing.JTextField login; 
    
    /**
     * Champ de saisie contenant le mot de passe a la base de donnees
    **/    
    private javax.swing.JPasswordField motDePasse;
    
    /**
     * Attribut permettant de d'acceder au bouton valider du fileChooser
     **/
    private JButton ouvrirJFileChooser ;
    
    /**
     * Attribut permettant de d'acceder au bouton valider du selecteur de BD
     **/
    private JButton ouvrirBD ;    
    
    /**
     * Attribut permettant de d'acceder au bouton fermer du selecteur de BD
     **/
    private JButton fermerBD ; 

    /**
     * Attribut contenant le bouton radio pour activer le calcul sur
     * les fichiers
     **/    
    private JRadioButton fichier ;

    /**
     * Attribut contenant le bouton radio pour activer le calcul sur
     * les donnees de la base de donnees
     **/     
    private JRadioButton baseDeDonnees ;

    /**
     * Attribut contenant le bouton radio pour activer le calcul en
     * utilisant une procedure cataloguee
     **/      
    private JRadioButton procedureCataloguee ;

    /**
     * Attribut contenant les differents champs de saisie pour une
     * connexion a la base de donnees
     **/ 
    private JPanel configurationBD ;
    
    /**
     * Attribut contenant le nom du repertoire
     **/ 
    private String nomRep ;

     /**
      * Attribut contenant le selecteur de fichier
      **/
    private javax.swing.JFileChooser choixRepertoire ;
    
     /**
      * Attribut contenant la fenetre principale de l'application
      **/
    private javax.swing.JFrame fenetre ;    
    
     /**
      * Attribut contenant une barre de progression lors des
      * caculs du resultat du concours
      **/    
    private javax.swing.JProgressBar barreDeProgression ;   
    
    public PresentationResultats (javax.swing.JFrame f)
    {   
        GestionnaireRadioBouton gBR = new GestionnaireRadioBouton () ;
        GestionnaireBoutonBD gBBD = new GestionnaireBoutonBD () ;

        PresentationResultats.this.choixUtilisateur = PresentationResultats.ChoixUtilisateur.fichier ;
        this.fenetre = f ;
        
//        String [] nomsColonnesRes = {"Rang", "Numero de bon", "Reponse 1", "Reponse 2"} ;
//        this.modeleTableRes = new DefaultTableModel(nomsColonnesRes, 0) ;       
        this.tableDesResultats = new JTable() ;
        
//        String [] nomsColonnesErr = {"Numero de bon", "Reponse 1", "Raison"} ;
//        this.modeleTableErr = new DefaultTableModel(nomsColonnesErr, 0) ;
        this.tableErreurs = new JTable() ; 
        
        this.onglets = new JTabbedPane() ;

        JPanel configuration = new JPanel () ;
        configuration.setLayout(new BorderLayout()) ;
        
        this.fichier =              new JRadioButton("Utilisation de fichier", true) ;
        this.baseDeDonnees =        new JRadioButton("Utilisation d'une base de donnees", false) ;
        this.procedureCataloguee =  new JRadioButton("Utilisation d'une procedure stockee Oracle", false) ;
        
        this.fichier.addActionListener(gBR) ;
        this.baseDeDonnees.addActionListener(gBR) ;
        this.procedureCataloguee.addActionListener(gBR) ;
        
        ButtonGroup group = new ButtonGroup() ;
        group.add(this.fichier) ;
        group.add(this.baseDeDonnees) ;
        group.add(this.procedureCataloguee) ;        

        // Definition des groupe de bouton.
        JPanel groupeBoutons = new JPanel() ;
        groupeBoutons.setLayout(new GridLayout(20, 1)) ;//BoxLayout(groupeBoutons, BoxLayout.Y_AXIS)) ;
        groupeBoutons.add(this.fichier) ;
        groupeBoutons.add(this.baseDeDonnees) ;  
        groupeBoutons.add(this.procedureCataloguee) ;
        
        groupeBoutons.add(new JPanel()) ;
        javax.swing.JSeparator separateur = new javax.swing.JSeparator() ;
        groupeBoutons.add(separateur) ;
        groupeBoutons.add(new JPanel()) ;
        groupeBoutons.add(new JPanel()) ;
        groupeBoutons.add(new JPanel()) ;
        groupeBoutons.add(new JPanel()) ;
        groupeBoutons.add(new JPanel()) ;
                                        
        this.barreDeProgression = new javax.swing.JProgressBar() ;
        JPanel panneauBP = new JPanel() ;
        panneauBP.setLayout(new GridLayout(1, 3)) ;
        panneauBP.add(new javax.swing.JLabel("Progression : ")) ;        
        panneauBP.add(this.barreDeProgression) ;
        panneauBP.add(new JPanel()) ;
        groupeBoutons.add(panneauBP) ;        
        
        configuration.add(groupeBoutons, BorderLayout.CENTER) ;        
        //--------------------------------------------------------------------------------------
 
        // Saisie des parametres pour l'utilisation des fichiers        
        this.choixRepertoire = new SelecteurDeFichier(".") ;
        this.choixRepertoire.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY) ;
        this.choixRepertoire.setFileFilter(new monFiltre()) ;
        this.choixRepertoire.setAcceptAllFileFilterUsed(false) ;  
        this.extraireBouton(this.choixRepertoire) ;
        
        //--------------------------------------------------------------------------------------

        // Saisie des parametres pour la connection a une BD
        this.configurationBD = new JPanel() ; 
        JPanel lAdresse = new JPanel(new GridLayout(1, 2)) ;
        JPanel lPort = new JPanel(new GridLayout(1, 2)) ; 
        JPanel lBase = new JPanel(new GridLayout(1, 2)) ; 
        JPanel lLogin = new JPanel(new GridLayout(1, 2)) ; 
        JPanel lMotDePasse = new JPanel(new GridLayout(1, 2)) ;
        
        javax.swing.event.CaretListener gCL = new GestionnaireChampsSaisis() ;
        this.adresse = new javax.swing.JTextField("ntelline.cict.fr") ;
        this.adresse.addCaretListener(gCL) ;
        this.port = new javax.swing.JTextField("1522") ;
        this.port.addCaretListener(gCL) ;        
        this.base = new javax.swing.JTextField("etupre") ;
        this.base.addCaretListener(gCL) ;
        this.login = new javax.swing.JTextField() ; 
        this.login.addCaretListener(gCL) ;
        this.motDePasse = new javax.swing.JPasswordField() ;
        this.motDePasse.addCaretListener(gCL) ;
        
        lAdresse.add(new JLabel("Adresse : ")).setFont(new Font(PresentationResultats.POLICE, Font.BOLD, 20)) ;
        lPort.add(new JLabel("Port : ")).setFont(new Font(PresentationResultats.POLICE, Font.BOLD, 20)) ;
        lBase.add(new JLabel("Nom de la base : ")).setFont(new Font(PresentationResultats.POLICE, Font.BOLD, 20)) ;
        lLogin.add(new JLabel("Login : ")).setFont(new Font(PresentationResultats.POLICE, Font.BOLD, 20)) ;
        lMotDePasse.add(new JLabel("Mot de passe : ")).setFont(new Font(PresentationResultats.POLICE, Font.BOLD, 20)) ;
        
        lAdresse.add(this.adresse) ;
        lPort.add(this.port) ;
        lBase.add(this.base) ;
        lLogin.add(this.login) ;
        lMotDePasse.add(this.motDePasse) ;
                
        this.configurationBD.setLayout(new BoxLayout(this.configurationBD, BoxLayout.Y_AXIS)) ;
        this.configurationBD.add(lAdresse) ;
        this.configurationBD.add(lPort) ;
        this.configurationBD.add(lBase) ;        
        this.configurationBD.add(lLogin) ;        
        this.configurationBD.add(lMotDePasse) ;
        
        JPanel lesBoutons = new JPanel(new GridLayout(1, 5)) ;
        this.ouvrirBD = new JButton("Executer") ;
        this.fermerBD = new JButton("Fermer") ;
        lesBoutons.add(new JPanel()) ;        
        lesBoutons.add(this.ouvrirBD) ;
        this.ouvrirBD.setEnabled(false) ;
        this.ouvrirBD.addActionListener(gBBD) ;
        lesBoutons.add(new JPanel()) ;
        lesBoutons.add(this.fermerBD) ;
        this.fermerBD.addActionListener(gBBD) ;
        
        this.configurationBD.add(new JPanel()) ;
        this.configurationBD.add(new JSeparator()) ;
        this.configurationBD.add(new JPanel()) ;        
        this.configurationBD.add(lesBoutons) ;
        
        JPanel ess1 = new JPanel() ;
        ess1.setLayout(new FlowLayout(FlowLayout.CENTER)) ;
        ess1.add(this.configurationBD);
        
        // Assemblage des composants pour les fichiers et la BD
        JPanel configurationFichierBD = new JPanel () ;       
        configurationFichierBD.setLayout(new FlowLayout(FlowLayout.CENTER)) ;        
        configurationFichierBD.add(ess1 /*this.configurationBD*/);
        configurationFichierBD.add(this.choixRepertoire) ;
        
        //-------------------------------------------------------------
        
        configuration.add(configurationFichierBD, BorderLayout.EAST) ; 
        
        this.onglets.addTab("Configuration", configuration) ; 
        this.onglets.addTab("Resultats", new JScrollPane(this.tableDesResultats)) ;
        this.onglets.addTab("Erreurs", new JScrollPane(this.tableErreurs)) ;

        this.duree = new JLabel() ;
        
        this.setLayout (new BorderLayout ()) ;
        this.add (BorderLayout.CENTER, this.onglets) ;
        this.add (BorderLayout.SOUTH, this.duree) ;
        this.duree.setFont(new Font("Times", Font.BOLD, 20)) ;
        this.duree.setHorizontalAlignment(JLabel.CENTER) ;
        this.duree.setText("0 min 0 sec") ;
        
        this.configurationBD.setVisible(false) ;
        this.choixRepertoire.setVisible(true) ; 
    } 
    
    /**
     * Methode permettant de mettre a jour la duree d'execution 
     * du calcul.
     *@param pDuree duree d'execution
     **/
    public void setDuree(String pDuree)
    {
        this.duree.setText(pDuree) ;
    }

    /**
     * Methode permettant de generer les resultats du concours 
     **/    
    public void genererResultatConcours() throws java.io.IOException,java.sql.SQLException
    {

        long vDuree = 0 ;
        PresentationResultats.this.duree.setText("0 min 0 sec") ;
        PresentationResultats.this.ouvrirJFileChooser.setEnabled(false) ;
        PresentationResultats.this.fichier.setEnabled(false) ;
        PresentationResultats.this.baseDeDonnees.setEnabled(false) ;
        PresentationResultats.this.procedureCataloguee.setEnabled(false) ;
        PresentationResultats.this.ouvrirBD.setEnabled(false) ;
        PresentationResultats.this.adresse.setEnabled(false) ;
        PresentationResultats.this.port.setEnabled(false) ;
        PresentationResultats.this.base.setEnabled(false) ;
        PresentationResultats.this.login.setEnabled(false) ;
        PresentationResultats.this.motDePasse.setEnabled(false) ;        
        
        PresentationResultats.this.barreDeProgression.setIndeterminate(true) ;

        String [] nomsColonnesRes = {"Rang", "Numero de bon", "Reponse 1", "Reponse 2"} ;
        DefaultTableModel modeleTableRes = new DefaultTableModel(nomsColonnesRes, 0) ;       
        this.tableDesResultats.setModel(modeleTableRes) ;
        
        String [] nomsColonnesErr = {"Numero de bon", "Reponse 1", "Raison"} ;
        DefaultTableModel modeleTableErr = new DefaultTableModel(nomsColonnesErr, 0) ;
        this.tableErreurs.setModel(modeleTableErr) ;
        
        metier.TraitementConcoursFichier leTraitement = null ;
        long debut = System.currentTimeMillis() ;        
        switch(PresentationResultats.this.choixUtilisateur)
        {
            case fichier :
                leTraitement = new metier.TraitementConcoursFichier(this.nomRep) ;
                break ;
            case bd :
                leTraitement = new metier.TraitementConcoursBaseDeDonnees(
                        PresentationResultats.this.adresse.getText().trim(),
                        new Integer(PresentationResultats.this.port.getText().trim()),
                        PresentationResultats.this.base.getText().trim(),
                        PresentationResultats.this.login.getText().trim(),
                        (new String(PresentationResultats.this.motDePasse.getPassword())).trim()) ;
                break ;
            case procedurecataloguee :
                 leTraitement = new metier.TraitementConcoursProcedureOracle(
                        PresentationResultats.this.adresse.getText().trim(),
                        new Integer(PresentationResultats.this.port.getText().trim()),
                        PresentationResultats.this.base.getText().trim(),
                        PresentationResultats.this.login.getText().trim(),
                        (new String(PresentationResultats.this.motDePasse.getPassword())).trim()) ;               
                break ;
            default : break ;
        }
                 
        vDuree = (System.currentTimeMillis() - debut)/1000 ;
        this.duree.setText(vDuree/60+" min "+ vDuree%60+" sec") ;    
        System.out.println("La duree de traitement est de "+vDuree/60+" min et "+vDuree%60+" sec." ) ;
        
        interfaces.IntIterateur<donnees.Bons_Bons> itRes = leTraitement.getResultats() ;
        
        itRes.ouvrirIterateur() ;
        donnees.Bons_Bons unBon_Class = itRes.lireIterateur() ;
        
        while (!itRes.finIterateur())
        {
            Object [] resultat = new Object[4] ;

            resultat[0] = unBon_Class.getClassement() ;
            resultat[1] = unBon_Class.getNumeroBon() ;
            resultat[2] = unBon_Class.getRep1() ;
            resultat[3] = unBon_Class.getRep2() ;
            ((DefaultTableModel) this.tableDesResultats.getModel()).addRow(resultat) ;
            unBon_Class = itRes.lireIterateur() ;
        }
        
        itRes.fermerIterateur() ;
        
        interfaces.IntIterateur<donnees.Bons_Errones> itErr = leTraitement.getErreurs() ;
        
        itErr.ouvrirIterateur() ;
        donnees.Bons_Errones unBon_Errone = itErr.lireIterateur() ;
        
        while (!itErr.finIterateur())
        {
            Object [] resultat = new Object[3] ;
            String ctrlRep1 = unBon_Errone.getCtrlRep1() ;
            resultat[0] = unBon_Errone.getNumeroBon() ;
            resultat[1] = unBon_Errone.getRep1() + (ctrlRep1!=null?ctrlRep1:"") ;
            resultat[2] = unBon_Errone.getMotif() ;
            ((DefaultTableModel) this.tableErreurs.getModel()).addRow(resultat) ;
            unBon_Errone = itErr.lireIterateur() ;
        }
        itErr.fermerIterateur() ;
        
        PresentationResultats.this.ouvrirJFileChooser.setEnabled(true) ;
        PresentationResultats.this.fichier.setEnabled(true) ;
        PresentationResultats.this.baseDeDonnees.setEnabled(true) ;
        PresentationResultats.this.procedureCataloguee.setEnabled(true) ;
        PresentationResultats.this.ouvrirBD.setEnabled(true) ;
        PresentationResultats.this.adresse.setEnabled(true) ;
        PresentationResultats.this.port.setEnabled(true) ;
        PresentationResultats.this.base.setEnabled(true) ;
        PresentationResultats.this.login.setEnabled(true) ;
        PresentationResultats.this.motDePasse.setEnabled(true) ;          
        
        PresentationResultats.this.barreDeProgression.setIndeterminate(false) ;
    }
    
    
    private void extraireBouton(Container pContainer)
    {  
        Component [] t = pContainer.getComponents() ;
        for (Component i : t)
        {
            if (i instanceof JButton) 
            {              
                JButton bt = (JButton) i ;

                if ((bt.getText()!= null) && (bt.getText().equalsIgnoreCase("Ouvrir")))
                {
                    this.ouvrirJFileChooser = bt ;
                }
            }
            if (i instanceof Container)
            {
                this.extraireBouton((Container) i) ;
            }
        }        
    }
    
    /**
     * Classe interne gerant les evenements venant des boutons radios
    **/
    private class GestionnaireRadioBouton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Object o = e.getSource() ;
            
            if (o instanceof JRadioButton)
            {
                JRadioButton btR = (JRadioButton) o ;
   
                if (btR == PresentationResultats.this.fichier)
                {
                    PresentationResultats.this.configurationBD.setVisible(false) ;
                    PresentationResultats.this.choixRepertoire.setVisible(true) ;
                    PresentationResultats.this.choixUtilisateur = PresentationResultats.ChoixUtilisateur.fichier ;
                    PresentationResultats.this.nomRep = null ;
                }
                else if ((btR == PresentationResultats.this.baseDeDonnees) ||
                         (btR == PresentationResultats.this.procedureCataloguee))
                {
                    PresentationResultats.this.nomRep = null ;
                    PresentationResultats.this.configurationBD.setVisible(true) ;
                    PresentationResultats.this.choixRepertoire.setVisible(false) ;
                    
                    if (btR == PresentationResultats.this.baseDeDonnees) 
                    {
                        PresentationResultats.this.choixUtilisateur = PresentationResultats.ChoixUtilisateur.bd ;
                    }
                    else
                    {
                        PresentationResultats.this.choixUtilisateur = PresentationResultats.ChoixUtilisateur.procedurecataloguee ;
                    }
                }
            }
        }
    }    

    /**
     * Classe interne gerant les evenements venant des boutons champs de saisie
    **/
    private class GestionnaireChampsSaisis implements javax.swing.event.CaretListener 
    {
        public void caretUpdate(javax.swing.event.CaretEvent e)
        {
            Object o = e.getSource() ;
            
            if (o instanceof JTextField)
            {
                if ((PresentationResultats.this.adresse.getText().trim().length() == 0) ||
                    (PresentationResultats.this.port.getText().trim().length() == 0) ||
                    (PresentationResultats.this.base.getText().trim().length() == 0) ||
                    (PresentationResultats.this.login.getText().trim().length() == 0) ||
                    (PresentationResultats.this.motDePasse.getPassword().length == 0))
                {
                    PresentationResultats.this.ouvrirBD.setEnabled(false) ;
                }
                else
                {
                    PresentationResultats.this.ouvrirBD.setEnabled(true) ;
                } 
            }
        }
    }    

    /**
     * Classe interne gerant les evenements venant des boutons radios
    **/
    private class GestionnaireBoutonBD implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Object o = e.getSource() ;
            
            if (o instanceof JButton)
            {
                JButton bt = (JButton) o ;
   
                if (bt == PresentationResultats.this.ouvrirBD)
                {
                    ThreadFichier resultat = new ThreadFichier() ;
                    resultat.start() ;
                }
                else if ((bt == PresentationResultats.this.fermerBD))
                {
                    System.exit(0) ;
                }
            }
        }
    }    
    
    private class SelecteurDeFichier extends javax.swing.JFileChooser
    {     
        
        public SelecteurDeFichier(String pRepertoire)
        {
            super(pRepertoire) ;
            this.setApproveButtonText("Ouvrir") ;
        }
        
        @Override
        public void cancelSelection ()
        {
            System.exit(0);
        }
        
        @Override
        public void approveSelection()
        {
            String chemin = this.getSelectedFile().getAbsolutePath() ;
            PresentationResultats.this.nomRep = chemin.replace(File.separatorChar+"Bons_Depart.txt", "") ;
            ThreadFichier resultat = new ThreadFichier() ;
            resultat.start() ;
        }
        
        @Override
        public boolean isTraversable(java.io.File f)
        {
            return f.isDirectory() ;
        }
    }

     
    private class monFiltre extends javax.swing.filechooser.FileFilter
    {
        public boolean accept(java.io.File f)
        {        
            
            return f.isDirectory() || (f.getName().equals("Bons_Depart.txt")) ;
        }
        
        public String getDescription()
        {
            return "Repertoire ou Bons_Depart.txt" ;
        }
    }

        
    private class ThreadFichier extends Thread
    {
        @Override
        public void run () 
        {
            try
            {
               PresentationResultats.this.genererResultatConcours () ;
            }
            catch (java.lang.Throwable e)
            {
                e.printStackTrace() ;
                javax.swing.JDialog f = new javax.swing.JDialog(PresentationResultats.this.fenetre, e.toString(), true) ;
                f.setSize(400, 150) ;
                f.add(new JLabel(e.getMessage()+" - "+e.toString())) ;
                f.setLocation(300, 200) ;
                f.setVisible(true) ;
                PresentationResultats.this.ouvrirJFileChooser.setEnabled(true) ;
                PresentationResultats.this.fichier.setEnabled(true) ;
                PresentationResultats.this.baseDeDonnees.setEnabled(true) ;
                PresentationResultats.this.procedureCataloguee.setEnabled(true) ;
                PresentationResultats.this.ouvrirBD.setEnabled(true) ;
                PresentationResultats.this.adresse.setEnabled(true) ;
                PresentationResultats.this.port.setEnabled(true) ;
                PresentationResultats.this.base.setEnabled(true) ;
                PresentationResultats.this.login.setEnabled(true) ;
                PresentationResultats.this.login.setText("") ;
                PresentationResultats.this.motDePasse.setEnabled(true) ;
                PresentationResultats.this.motDePasse.setText("") ;
                PresentationResultats.this.barreDeProgression.setIndeterminate(false) ;
                PresentationResultats.this.ouvrirBD.setEnabled(false) ;
            }  
        }
    }        
}
            
        
        
        
    
    
