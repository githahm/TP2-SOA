# Web Services SOAP : rôles du Serveur, de l'Annuaire et du Client

Notes de révision basées sur les chapitres du cours « Web Service SOAP », « Mise en oeuvre d'un premier SOA » et « Mise en oeuvre du client SOAP ».

## 1. Les trois acteurs

| Acteur | Rôle | Ce qu'il fait |
|---|---|---|
| Serveur (producteur de service) | Crée et héberge le service web | Contient le code métier (`CalculWS`), publie le service avec `Endpoint.publish(...)`, génère le WSDL, répond aux requêtes SOAP |
| Annuaire (registre UDDI) | Registre de services | Stocke les contrats WSDL publiés par les serveurs et permet aux clients de les découvrir |
| Client (consommateur de service) | Utilise le service | Interroge l'annuaire, lit le WSDL, appelle le service via un proxy (stub) |

## 2. Schéma

```
                      +----------------------+
                      |   ANNUAIRE (UDDI)    |
                      | registre des         |
                      | contrats WSDL        |
                      +----------------------+
                        ^ 1             ^ |
                        | publication   | | 3 renvoie le
                        | du WSDL       | | contrat WSDL
                        |             2 | v
                        |      interrogation de service
                        |                 |
   +--------------------+--+       +------+----------------+
   |  SERVEUR (producteur) |       |   CLIENT (consommateur)|
   |  héberge le service   | <---- |   l'appelle via un     |
   |  web                  |  4    |   proxy (stub)         |
   |                       | ----> |                        |
   +-----------------------+  5    +------------------------+

   4 = Requête SOAP  (HTTP POST, enveloppe XML)
   5 = Réponse SOAP (HTTP, enveloppe XML contenant le résultat)
```

## 3. Les cinq étapes

1. Publication : le serveur publie son WSDL (le contrat) dans l'annuaire.
2. Interrogation de service : le client cherche un service dans l'annuaire.
3. L'annuaire renvoie les informations et le contrat WSDL au client.
4. Le client envoie une requête SOAP au serveur (HTTP POST, le corps contient le nom de la méthode et les paramètres en XML).
5. Le serveur renvoie une réponse SOAP (HTTP, le corps contient le résultat en XML).

L'annuaire est optionnel. Si le client connaît déjà l'adresse du service, il peut sauter les étapes 2 et 3, récupérer le WSDL directement sur le serveur (`http://localhost:8686/?wsdl`) et passer aux étapes 4 et 5.

Chronologie vue du côté client (bas du tableau) : recherche (clé UDDI), découverte, lecture du WSDL, appel, réponse.

## 4. Le rôle de chaque acteur en détail

### Serveur
- Interface Java annotée avec `@WebService`, `@WebMethod`, `@WebParam` (le contrat).
- Classe d'implémentation avec `@WebService(endpointInterface = "Service.ICalculWS")` (le code métier).
- Publication :
  ```java
  Endpoint.publish("http://localhost:8686/", new CalculWS());
  ```
- Jakarta XML Web Services génère automatiquement le WSDL.
- Côté serveur, le skeleton décode la requête SOAP, appelle la vraie méthode et sérialise le résultat dans une réponse SOAP.

### Annuaire (UDDI)
- Annuaire distribué normalisé de Web Services (il se comporte lui-même comme un web service appelé via SOAP).
- Pages blanches : les entreprises et leurs coordonnées.
- Pages jaunes : les Web Services de chaque entreprise (WSDL).
- Pages vertes : les informations techniques précises sur les services.

### Client
- Génère le proxy à partir du WSDL avec `wsimport` (rôle similaire à `idlj` en CORBA) :
  ```
  mvn clean jaxws:wsimport
  ```
- Appelle le service comme s'il s'agissait d'un objet local :
  ```java
  CalculWSService service = new CalculWSService();
  ICalculWS port = service.getCalculWSPort();
  int resultat = port.somme(34, 39);
  ```
- Le proxy construit la requête SOAP, l'envoie via HTTP, puis désérialise la réponse en objet Java.

## 5. Concepts clés

- SOAP : protocole basé sur XML pour structurer les messages échangés entre client et serveur, transportés via HTTP.
  Structure d'un message : en-têtes HTTP > enveloppe SOAP > en-tête SOAP + corps SOAP (appel de méthode et données).
- WSDL : fichier XML qui décrit le service. C'est le contrat entre le client et le serveur.
  Éléments : `definitions`, `types`, `message`, `portType` (partie abstraite), `binding`, `service` (partie concrète).
- UDDI : annuaire permettant de publier et de découvrir des Web Services.
- Proxy (stub) côté client et skeleton côté serveur : ils jouent le rôle de traducteurs entre les appels Java et les messages SOAP.

## 6. Tester le service

Utiliser SoapUI : créer un projet SOAP, fournir l'URL du WSDL (`http://localhost:8686/?wsdl`), choisir une méthode, saisir les paramètres, envoyer la requête et lire la réponse XML.
