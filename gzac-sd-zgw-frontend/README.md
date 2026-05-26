# Gemeente Den Haag, gzac-sd-zgw-frontend implementation

### Requirements

- Node v18
- NPM v10

### Starting up
Run the following commands from a terminal in the project root:
- nvm use 18
- npm install
- npm run start

After the frontend application has finished starting up, the service is available at http://localhost:4200

### Configure the plugins
At http://localhost:4200/plugins configure the required plugins, find the configuration details in keepass entry of social domain (Root / App related accounts / Den Haag - SD ZGW / Test)
Please configure in the following order:
* Openzaak
* Catalogi API
* Documenten API
* Zaken API
* Object token authenticatie - Object
* Object token authenticatie - Objecttypen
* Objecten API
* Objecttypen API
* OpenNotificaties
* Smartdocuments
* Notificatie API
* Portaaltaak

Note that when deleting the docker container **gzac-sd-zgw-backend-valtimo-core-db-1** all plugin configuration is lost.
