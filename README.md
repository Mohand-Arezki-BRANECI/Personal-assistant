# Personal-assistant


### Controller
- ControllerRequest ->

### DTO
- RequestDto -> 

### Services 
- RequestProcessor -> redirige vers le bon service selon le mot présent dans la requête  
- DateTimeService -> permet de donner la date ou l'heure selon la demande mais égalemetn d'avoir le nombre de jours avnt la fin des cours ou le nombres de jours depuis la fin des cours formater en html
- MailService -> permet d'envoyer un mail à un destinataire donné, avec l'objet et le contenu passés en paramètres
- MeteoService -> permet d'obtenir la météo selon une longitude et une latitude données
- PokemonService -> permet d'avoir le nom et un image d'un pokemon ramdom fomrater dans des balises HTML

### DIFFICULTES RENCONTREES POUR LE FRONT

Le premier jour nous avons d'abord essayé de coder avec Angular mais vue les contraintes de temps nous avions decidé d'utiliser React, React est une bibliothèque JavaScript, tandis qu'Angular est un framework complet. Les applications React ont tendance a être plus rapide a compiler et exécuter. React est riche en bibliothèques tierces et en outils.
Nous avions deja une premiere experience sur React ce qui simplifie la tache.

Lorsequ'on veut recuperer les donnes nous avions eu un probleme de corps, utilisé un serveur proxy a permis de resoudre ce soucis
