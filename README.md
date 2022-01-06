# Mobile Tracking : Android / Podomètre / Tracker / SQlite / SMS 
- Année : M2 IWOCS
- Matière: ANDROID
- TP : Suivi de parcours

## Auteur(s)

|Nom|Prénom|login|email|
|--|--|--|--|
| CARICAND | Maxime | @cm171529 | maxime.caricand@etu.univ-lehavre.fr |
| CASTET | Camille | @cc172805 | camille.castet@etu.univ-lehavre.fr |
| CRUVEILHER | Nicolas | @cn172692 | nicolas.cruveilher@etu.univ-lehavre.fr |
| HENRY | Florian | @hf171907 | florian.henry@etu.univ-lehavre.fr |
| LABBE | Alexis | @la172685 | alexis.labbe@etu.univ-lehavre.fr |

## Travail à réaliser

### Suivi de parcours

- il existe de nombreuses applications qui permettent de
compter le nombre de pas, etc.

- nous souhaitons concevoir une nouvelle application qui
regroupe un ensemble d’informations sur un parcours
effectue par une personne et qui permette egalement de
tenir au courant, en quasi temps reel, une autre personne
de son parcours

- l’application peut donc etre utilise en mode marcheur ou en
mode suiveur


### Mode marcheur

- l’application doit :
1. afficher le nombre de pas depuis le debut de la marche, la
vitesse instantanee, le nombre de metres/kilometres
parcourus depuis le depart et la vitesse moyenne
2. elle doit gerer le cycle de vie
3. a la fin ou en cas de changement d’application (retour au
bureau), elle doit conserver dans un fichier les informations
precedentes ainsi que la date et l’heure et (eventuellement)
des informations de meteo recuperees sur internet
4. si elle rec¸oit un sms d’un suiveur, elle peut accepter ou
refuser le suivi
5. si elle accepte le suivi, elle doit regulierement (toutes les 10
secondes par exemple) tenir informe le suiveur de sa
position courante.

- elle doit utiliser le GPS pour la position courante


### Mode suiveur

- l’application doit :
1. pouvoir demander a un numero de telephone particulier le
suivi de son parcours
2. afficher une carte indiquant la position courante de la
personne suivie
3. enregistrer le suivi (horodate) de la personne/l’appareil
4. mettre fin au suivi
5. gerer le cycle de vie

