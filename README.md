# Fraquizz

## Quick notes
- Affichage de l'emplacement du département si le joueur tombe au plus bas niveau (pour la dernière devinette)

## Types à définir
Voici une liste de types à définir pour le déroulement du projet
 
### 1. Type département
Le type département permet de simplifier la structuration des données dans le jeu. Il permet la simlification des devinettes, en rassemblant les questions, numéros de départements, etc.

#### Attributs
 - Nom du département (String)
 - Numéro de département (int)
 - Questions (String[])
 - Découvert (boolean)
 
# À rajouter après l'Alpha
 - Possibilité de gagner 1/2 points si on devine le numéro du département
 (ou alors 1 points pour le nom et 1 point pour le numéro + 1 point pour la région) --> enlever les questions du style "quel département de la région tatata porte le numéro tatata"