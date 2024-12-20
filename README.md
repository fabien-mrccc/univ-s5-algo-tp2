# Génération aléatoire d'arbres couvrants

## Informations utiles
- Membres du projet : FANANI Amina, MARCUCCINI Fabien
- Chargé de TP : ROLLAND Marius
- Matière : "Algorithmique 2" en troisième année de licence informatique à l'université d'Aix-Marseille
- Version du SDK : Azul Zulu 13.0.14 - aarch64

## But du projet
- L'énoncé du projet est fourni dans le fichier **/pdf/tp2.pdf**. <br>

## Commandes pour utiliser le projet
- Chaque commande *make* est à exécuter à la racine du projet.
- Pour lancer les tests unitaires : *make run_tests*
- Pour compiler et lancer le projet sans spécifications : *make*
- Pour compiler et lancer le projet avec spécification : *make ARGS="x y" avec x= index algo et y= index générateur de graphes*
- Pour exécuter le jar sans recompiler : *make exec ou make exec ARGS="x y"*
- Index des algorithmes de génération aléatoire d'arbres couvrants (x) :
  - 1 = MinimumWeightSpanningTree (Arbres couvrant de poids minimum aléatoire)
  - 2 = RandomWalkTree (Parcours aléatoire)
  - 3 = RandomEdgeInsertion (Insertion aléatoire d’arêtes)
  - 4 = AldousBroder (Algorithme d’Aldous-Broder)
- Index des générateurs de graphes (y) : 
  - 1 = Grid mode
  - 2 = Complete Graph mode
  - 3 = ErdosRenyi mode
  - 4 = Lollipop mode

## Avancement du projet
- Les classes du package Graph sont implémentées et testées (voir package Tests).
- Le code du projet a été restructuré et documenté pour faciliter sa lecture.
- Algorithmes de génération aléatoire d'arbres couvrants implémentés : 
  - Arbres couvrant de poids minimum aléatoire (implémentation testée avec Grid) ;
  - Parcours aléatoire (implémentation testée avec Grid) ;
  - Insertion aléatoire d’arêtes (implémentation assistée par ChatGPT avant rendu) ;
  - Algorithme d’Aldous-Broder (implémentation assistée par ChatGPT avant rendu).

## Comparaison des algorithmes 
...