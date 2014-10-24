del affichage\*.class
del interface\*.class
del metier\*.class
del donnees\*.class
del donnees\exeption\*.class
del donnees\gestionBaseDeDonnees\*.class
del donnees\gestionFichier\*.class

"%JAVA_HOME%\bin\javac" -classpath ".\ojdbc6.jar;."  Yslav.java
