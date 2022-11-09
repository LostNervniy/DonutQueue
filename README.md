# DonutQueue
Start: `docker-compose up`

`Java Version: 17`

`MySQL Version: 8`

`MySQL Port: 3307`

`API Port: 8080`
 

Nachdem alles gestartet ist, sind folgende Endpoints erreichbar:

`GET /api/clients` -> Liefert eine Liste aus allen Clients

`GET /api/clients/{clientId}` -> Liefert einen Client mit einer ClientID

`POST /api/clients` -> Erstellt einen neuen Client, payload: {"id":number, "name":string}

`PUT /api/clients/{clientId}` -> Editiert einen Client mit einer ClientID, payload: {"id":number, "name":string,"isPremium":boolean}

`DELETE /api/clients/{clientId}` -> Löscht einen Client mit einer ClientID

`GET /api/clients/premium` -> Liefert eine Liste aus allen Client mit Premiumstatus

`POST /api/clients/{clientId}/orders` -> Erstellt eine Order für einen Client, payload: {"quantity":number}, quantity ist hier zwischen 1 und 50

`DELETE /api/orders/{clientId}` -> Löscht eine Order des Clients mit ClientID

`GET /api/clients/orders/next` -> Liefert die nächste Order (nach sortierung)

`GET /api/clients/orders/queue` -> Liefert die gesammt Orderqueue (nach sortierung) mit geschätzter Lieferzeit in Sekunden

`GET /api/clients/{clientId}/orders/queue` -> Liefert die geschätzte Lieferzeit und Queue Position einer Order mittels ClientID
