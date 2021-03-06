# Twister (twitter alternative)
Projet du module 3I017 Technologies Web

## Projet
Réalisation d'un réseau social de type twitter.
L'application sera en architecture client-serveur, en modèle orienté services


### Serveur
* Serveur Apache Tomcat 9
* Java 10.0.1
* SGBDR : MySQL 8.0.15 (pour stockage des utilisateurs, sessions et amis)
* SGBD NoSQL : MongoDB v4.0.3 (pour stockage des messages)

#### Fonctionalitées implémentées
* Gestion utilisateur (création, login, logout,ajout d'une photo de profile, recupération d'un compte, changement du mot de passe)
* Gestion messages (ajout d'un message, ajout d'un commentaires, listage des messages (all/user/friend), supression de méssage, ajout d'une photo au méssage, possibilité de mettre des "j'aime" aux méssages)
* Gestion amis (demande de following, acceptation de la demande, supression de la demande,supression d'un follow)
* Recherche (recherche des utilisteurs par mot clés,recherche de message avec la technologie Map-Reduce et calcul du score)

### Client
* HTML (From scratch)
* CSS (From scratch)
* React
* Axios

#### Pages implémentées
##### Sans login:  
* Page d'authentification
* Page d'enregistrement
* Page de recupération du compte
* Page de changement de mot de passe

##### Avec login:
* Page d'acceuil
* Page de profile
* Page de profile d'un utilisateur
* Page des resultats de la recherche

![Screenshot](https://github.com/SalimNedjam/Twister/blob/master/Profile.jpg)

![Screenshot](https://github.com/SalimNedjam/Twister/blob/master/Post.jpg)

# Legal
Interdiction d'utilisation et de modification de tout code dont je suis l'auteur sans autorisation.
