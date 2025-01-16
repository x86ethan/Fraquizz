# Fraquizz

Voici une version alpha de Fraquizz, le jeu qui te fait apprendre les départements français et leurs spécificités en t'amusant !

## 1. Principe du jeu

Le jeu consiste à deviner avec un choix de questions (à chaque fois facile, normal et difficile) un département.
Les questions sont de type :
 - Quel département des Hauts de France porte le numéro 59  ? (facile)
 - Quel département abrite la ville de Lille, mondialement connue pour sa braderie annuelle ? (normal)
 - Quel est le département le plus peuplé de France ? (difficile)

Il suffit ensuite de taper le nom du département que l'on pense être la réponse à notre question.  
*Notons qu'il n'est pas nécéssaire de le taper selon sa casse exacte, car les noms sont normalisés. Ainsi, "hauts de france" suffira pour le département "Hauts-de-France".*

Pour arrêter le jeu, il suffit d'écrire STOP.

## 2. Compilation et éxécution

Le script "fraquizz" permet de compiler et d'éxécuter à la fois simplement et rapidement Fraquizz. Il ne prend qu'un seul argument. Voici comment il fonctionne : 
 - Sans argument : compile et éxécute le jeu
 - En passant "compile" : compile uniquement le jeu
 - En passant "run" : lance le jeu (uniquement après compilation, sinon le jeu ne se lance pas)

Notons que la commande "run" supprime également les fichiers de compilation du jeu à la fin de son éxécution. 

Note : Fraquizz dépend uniquement de Java, car il embarque déjà une version de ap.jar pour iJava.


## 3. Plans d'amélioration pour la suite

Bien que cette version ne soit qu'une Alpha, nous avons prévu de nombreuses fonctionnalités supplémentaires. En voici la liste (liste non-exhaustive, car il se peut que nous ayons des idées en cours de route) :
 [x] Une carte de France en ASCII-Art, avec la séparation de tous les départements, qui se remplirait dynamiquement au fur et à mesure de la progression dans le jeu
 [ ] La possibilité de sauvegarder sa partie, pour pouvoir continuer plus tard
 [ ] La sauvegarde du score dans un Leaderboards, qui serait accessible depuis une page web (enregistrement du pseudonyme de l'utilisateur pour les deux cas)
 [ ] Des questions supplémentaires pour les départements (par exemple, deux ou trois questions choisies aléatoirement pour chaque niveau)
 [ ] Un classement des départements selon leur niveau de difficulté*

 - Tests
 - Format de l'archive
 - Niveaux de difficultés + type questions
 - Améliorations IHM

* Nous avons remarqué que certaines questions, comme celle des Landes (Quel département est connu pour ses plages immenses et ses forêts de pins ?) sont assez vagues et peu précises. Il serait donc préférable de classer ces questions comme difficiles, et d'autres plus faciles, comme celle de Paris (Quel département est la capitale de France et le plus peuplé ?)