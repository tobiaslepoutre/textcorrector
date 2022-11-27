Ce programme a été fait par Tobias Lepoutre et Maxence Puech.
	
	
	Ce texte est une description pour mieux comprendre le projet de mini correcteur. Tout d'abord nous avons crée 
l'interface graphique qui est fixé a une taille. Cette interface graphique est composée de deux parties bien disctinctes 
la première en haut de page sont: les bouttons d'interraction qui permette d'accès a son répertoire pour charger le 
un fichier texte; ensuite nous avons un boutton qui accède a un repertoire pour choisir le dictinnaire qui la
base de données pour la correction; enfin nous avons le button vérifier qui une fois cliqué va "corrigé" le texte.
La deuxième partie est un texte éditable que permet d'écrire pour pouvoir ensuite le corrigé.

	La correction du texte se fait en deux parties la première est que chaque mot inconnu du dictionnaire va etre 
surligné dans le texte et si l'utilisateur clique sur le mot surligné un menu va s'offrir offrant la possibilité
à l'utilisateur de choisir un mot parmis 5 qui se rapporche le plus possible des mots du dictionnaire. 

	Le code est composé d'une classe main qui contient tout les interractions avec l'interface, avec l'ensemble 
des actions emise par l'utilisateur. Ensuite nous avons aussi des autres classes qui permette notamment, l'algorithme 
Levenshtein (qui permet de calcul les distances entre les mots inconnus et les mots du dictionnaire), ou encore 
une fonction qui permet de proposée 5 mots du dictionnaire qui se rapproche le plus possible du mot inconnu.
Nous avons choisis de rassembler la création de l'interface graphique avec toute ses interractions et ses fonctions
annexes pour permettre une meilleure comprehension avec le partenaire et moins de classe éparpillés.