# Gemeente Den Haag, gzac-sd-zgw-backend implementation

## Requirements

- Ensure docker desktop is installed and running.
- Java version 17

## Environment variables

The `.env.properties` file was updated on **22-08-2025**. Please ensure you have the latest version.

* Download the `.env.properties` file from the keepass entry of social domain (Development folder) into the root of the
  application.
* Now run the `bootRun` gradle task

## Configuration

Plugins and process-links are autodeployed during startup for all environments.
Configure all your environment specific process link variables after deployment when necessary and
keep an eye out on application logs for autodeployment errors.

## Local OpenZaak Setup

This guide helps you to configure OpenZaak for development.

### 1. Docker Services
Make sure the following containers are running:

- `openzaak`
- `openzaak-database`

Once the containers are up and running, OpenZaak application is available at `http://localhost:8001`.

### 2. Initial Database State
- On the first startup, the OpenZaak database contains one *catalogi API* with a preconfigured test *zaaktype*.
- To add your **own zaaktype**, use the **export/import functionality** of OpenZaak.

    - Official documentation can be found [here](https://open-zaak.readthedocs.io/en/stable/manual/catalogi/index.html#exporteren-importeren-van-een-zaaktype).

- A pre-exported zaaktype .zip is provided here in the repository: `imports/open-zaak/zaaktype`.

---

### 3. Configure Your Zaaktype
After importing your zaaktype into OpenZaak:

1. **Publish** the zaaktype and related objects.

   In the OpenZaak UI, publish the newly imported **zaaktype** and any related objects (e.g. **Roltypen**, **Zaakeigenschappen**, **Informatieobjecttypen**)
2. **Update environment variables**:
    - Update the UUIDs of the following variables so they match the newly imported zaaktype.
        - `Roltype`
        - `Zaakeigenschap`
        - `Informatieobjecttypen`
3. **Link** the zaaktype to the case.

At this point, your local OpenZaak should be fully set up and ready to use.

## Known errors:

### Open Notifications abonnement creation
#### Error
```
com.ritense.plugin.exception.PluginEventInvocationException: Failed to run events on plugin Notificaties API (Autodeployed) with id 020e6ca3-1c15-4496-a4cf-67bfaa36332f. Error:             Request:
            HTTP Method = POST
            Request URI = http://localhost:8002/api/v1/abonnement
```
#### Solution
Go to http://localhost:8002, login with admin/admin, go to abonnementen/subscriptions and remove all (there is probably only one) abonnementen/subscriptions

* plugin autodeployment fails to create and validate the notificaties api plugin and all that use the former as a
  dependency
  if opennotificaties can not reach gzac's callback endpoint.

## Manual configuration

THE FOLLOWING STEPS ARE ONLY NEEDED IF YOU WANT TO WORK ON THE MENTIONED CASE DEFINITIONS.  
NEW PROJECTS ARE DONE WITH AUTODEPLOYMENT

### BBZ
#### Setup for local Verzoek BBZ Aanvraag flow

- Docker compose — Ensure the docker compose stack is running
- Plugins and object management configurations are **auto-deployed**.
- Test - Use the http requests in [examples/objects-api-requests/bbz](examples/objects-api-requests/bbz) or [examples/objects-api-requests/bbz-krediet](examples/objects-api-requests/bbz-krediet) 
to create an object in the objects-api.
  - You have to create an environment private file with the url and token of the objects-api.

#### BBZ Zaaktype links

| ...                               | Zaaktype link (BBZ Aanvraag)                                |                                                             |
|-----------------------------------|-------------------------------------------------------------|-------------------------------------------------------------|
| Go to                             | Admin -> Dossiers -> BBZ Aanvraag -> Tabblad Configuratie    |                                                             |
| Uploadproces koppelen aan dossier |                                                             |                                                             |
| -                                 | Kies welk proces het uploaden van bestanden moet afhandelen | Documenten API upload document                              |
| OpenZaak-koppelingen              |                                                             |                                                             |
| -                                 | Zaak koppeling                                              | Aanvraag financiële ondersteuning ondernemers behandelen v6 |
| -                                 | Zaken Api plugin                                            | Zaken API (Autodeployed)                                    |
| -                                 | RSIN gebruikt bij aanmaken zaak                             | 100000009                                                   |
| -                                 | Automatisch aanmaken voor elk dossier                       | Aan                                                         |
| -                                 | Dit type dossier kan een behandelaar hebben                 | Aan                                                         |

| ...                               | Zaaktype link (BBZ Krediet)                                 |                                        |
|-----------------------------------|-------------------------------------------------------------|----------------------------------------|
| Go to                             | Admin -> Dossiers -> BBZ Krediet -> Tabblad Configuratie    |                                        |
| Uploadproces koppelen aan dossier |                                                             |                                        |
| -                                 | Kies welk proces het uploaden van bestanden moet afhandelen | Documenten API upload document         |
| OpenZaak-koppelingen              |                                                             |                                        |
| -                                 | Zaak koppeling                                              | Aanvraag bedrijfskrediet behandelen v6 |
| -                                 | Zaken Api plugin                                            | Zaken API (Autodeployed)               |
| -                                 | RSIN gebruikt bij aanmaken zaak                             | 100000009                              |
| -                                 | Automatisch aanmaken voor elk dossier                       | Aan                                    |
| -                                 | Dit type dossier kan een behandelaar hebben                 | Aan                                    |

### Aanvraag Schuldhulpverlenening
#### Setup for local Verzoek Aanvraag Schuldhulpverlening flow

- Docker compose - Ensure the docker compose stack is running
- Create the plugins (if not already done, see above) and object management Configuration
  Configurations for the local dev environment can be found at:
  https://pps.ritense.com:10001/WebClient/Main?itemId=2afc90a0-158b-4423-bc71-af55009317de
- Test - Use the http request in examples/verzoek-bvshv-<VERSION>.http to create an object in the objects-api. You have
  to create an Environment with private file with the token of the objects-api.

####
| ..                                | zaaktype link (Aanvraag schuldhulpverlening)                             |                                            |
|-----------------------------------|--------------------------------------------------------------------------|--------------------------------------------|
| Go to                             | Admin -> Dossiers -> Aanvraag schuldhulpverlening -> Tabblad Configuratie |                                            |
| Uploadproces koppelen aan dossier |                                                                          |                                            |
| -                                 | Kies welk proces het uploaden van bestanden moet afhandelen              | Custom Documenten API upload document      |
| OpenZaak-koppelingen              |                                                                          |                                            |
| -                                 | Zaak koppeling                                                           | Beoordelen aanvraag schuldhulpverlening v4 |
| -                                 | Informatieobject-type                                                    | Bijlage                                    |
| -                                 | Automatisch aanmaken voor elk dossier                                    | Aan                                        |

| SHV object toevoegen aan object management.<br/> Dit moet ingesteld worden voor het instellen van de verzoek plugin |                                                                                                                                                                                                |                                            |
|-------------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------|
| Go to                                                                                                             | Admin -> Objecten                                                                                                                                                                              |                                            |
| Voeg nieuw object toe                                                                                             |                                                                                                                                                                                                |                                            |
| Titel                                                                                                             | Verzoek schuldhulpverlening bewindvoerders                                                                                                                                                     |    |
| Objecten API plugin configuratie                                                                                  | Objects API (Autodeployed)                                                                                                                                                                     |                                            |
| Objecttypen API plugin configuratie                                                                               | Objecttypes API (Autodeployed)                                                                                                                                                                 | |
| Objecttype ID                                                                                                     | Ophalen uit de objecttypen api. Ga via docker naar de objecttypen api en login. Ga naar objecttypen. Open object "Verzoek schuldhulpverlening bewindvoerders". Kopieer de UUID en voer het in. |                                     |
| Objecttype versie                                                                                                 | 1                                                                                                                                                                                              |                                         |


#### Aanvraag Schuldhulpverlenening verzoek plugin

| ...                            | Plugin                                       |                                                                      |
|--------------------------------|----------------------------------------------|----------------------------------------------------------------------|
| Plugin                         | Verzoek                                      |                                                                      |
| Configuratienaam               | Aanvraag Schuldhulpverlening                 |                                                                      |
| Notificaties API-configuratie  | docker - Notificaties API - Notificaties API |                                                                      |
| Object management-configuratie | Verzoek Schuldhulpverlening Bewindvoerders   |                                                                      |
| Proces                         | Generic: Create Zaakdossier niet natuurlijk persoon   |                                                                      |
| RSIN                           | 100000009                                    |                                                                      |
| Verzoektypen                   |                                              |                                                                      |
| -                              | type                                         | Verzoek schuldhulpverlening bewindvoerders                           |
| -                              | Dossierdefinitie                             | aanvraag-schuldhulpverlening                                         |
| -                              | Roltype                                      | initiator                                                            |
| -                              | Rolbeschrijving                              | Initiator                                                            |
| -                              | Procesdefinitie                              | SHV: Behandelen aanvraag schuldhulpverlening                         |
| -                              | Kopieerstrategie                             | Gespecifieerde velden                                                |
| -                              | Mapping                                      | zie examples/verzoek-plugin/mapping-bvshv.json in sd backend project |


## Process-links

| Process link                 | Activity                              | Configuration                                                                                              |
|------------------------------|---------------------------------------|------------------------------------------------------------------------------------------------------------|
| Start Zaak hersteltermijn    | Bepalen nieuw einddatum beslistermijn | **plugin**:<br>Zaken API<br>**action**:<br>Start hersteltermijn<br>**input**:<br>days: `pv:hersteltermijn` |
| Beëindig Zaak hersteltermijn | Beëindig hersteltermijn               | **plugin**:<br>Zaken API<br>**action**:<br>Beëindig hersteltermijn                                         |

| Process link                                | SHV: Behandelen aanvraag schuldhulpverlening                                                        |                                                                          | 
|---------------------------------------------|-----------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Task                                        | ControlerenAanvraagTask                                                                             ||
| Form                                        | shv-aanvraag.controleren-aanvraag                                                                   ||
| Form type                                   | form                                                                                                ||
| Displaytype                                 | panel                                                                                               ||
| Form size                                   | medium                                                                                              ||
|                                             |                                                                                                     ||
| Plugin                                      | Zaken API                                                                                           ||
| Plugin action                               | Zaakrol aanmaken - niet natuurlijk persoon                                                          ||
| Roltype URL                                 | https://openzaak-zgw.test.denhaag.nl/catalogi/api/v1/roltypen/ba6d4c5c-1d74-4ab1-8823-9ffc2794569c  ||
| Roltoelichting                              | Initiator                                                                                           ||
| Ander niet natuurlijk persoon identificatie | doc:/kvkBewindvoerder                                                                               ||

| Process link | SHV: Inhoudelijke beoordeling aanvraag                                           |                                                                          | 
|--------------|----------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Task         | ControlerenEnAanvullenClientEnPartnerInformatieTask                              ||
| Form         | aanvraag-schuldhulpverlening.controleren-client-partner:latest                   ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenSituatieTask                                               ||
| Form         | aanvraag-schuldhulpverlening.controleren-situatie:latest                         ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenInkomstenUitgavenTask                                      ||
| Form         | aanvraag-schuldhulpverlening.controleren-en-aanvullen-inkomsten-uitgaven:latest  ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenBezitVermogenTask                                          ||
| Form         | aanvraag-schuldhulpverlening.controleren-bezit-vermogen:latest                   ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenBelastingenTask                                            ||
| Form         | aanvraag-schuldhulpverlening.controleren-belastingen:latest                      ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenOndernemingenTask                                          ||
| Form         | aanvraag-schuldhulpverlening.controleren-ondernemingen:latest                    ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenSchuldenTask                                               ||
| Form         | aanvraag-schuldhulpverlening.controleren-en-aanvullen-schulden-informatie:latest ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | VastleggenAfloscapaciteitTask                                                    ||
| Form         | aanvraag-schuldhulpverlening.vastleggen-afloscapaciteit                          ||
| Form type    | Form                                                                             ||
| Displaytype  | panel                                                                            ||
| Form size    | medium                                                                           ||

| Process link | SHV: Inzetten instrumenten |                                                                          | 
|--------------|----------------------------------------------|--------------------------------------------------------------------------|
| Task         | InzettenInstrumentenTask                      ||
| Form         | aanvraag-schuldhulpverlening.inzetten-instrumenten.inzetten-instrument           ||
| Form type    | form                                         ||
| Displaytype  | panel                                        ||
| Form size    | medium                                       ||

| Process link | SHV: Opvragen Informatie |                                                                          | 
|--------------|----------------------------------------------|--------------------------------------------------------------------------|
| Task         | VastleggenReactieInformatieverzoekTask                      ||
| Form         | shv-aanvraag.shv-opvragen-informatie.vastleggen-reactie-informatieverzoek           ||
| Form type    | form                                         ||
| Displaytype  | panel                                        ||
| Form size    | medium                                       ||
||||
| Task         | BeoordelenResultaatInformatieverzoekTask                      ||
| Form         | shv-aanvraag.shv-opvragen-informatie.beoordelen-resultaat-informatieverzoek           ||
| Form type    | form                                         ||
| Displaytype  | panel                                        ||
| Form size    | medium                                       ||

| Process link   | SHV: Opvragen Informatie |                                                                          | 
|----------------|--------------------------|--------------------------------------------------------------------------|
| Process        | SHV: Genereer document   |
| Portaaltaak    | Aanleveren informatie    |
| Plugin         | Portaaltaak              |
| Plugin action  | Portaaltaak aanmaken     |
| Formulier type | URL                      |
| Formulier      |    https://objecten-zgw.test.denhaag.nl/api/v2/objects/28cf4981-a885-48a6-b7d1-2d6ecc831ec9                      |
|                |                          |
|  Taakgegevens voor de ontvanger |                          |
|     Pad in het object           | Waarde                   |
|      /verzoek          | pv:verzoek               |
|     /deadlineInformatieverzoek           | pv:prettyDeadline        |
|                |                          |
|      Ingevulde gegevens door de ontvanger          |                          |
|       Bestemming         | Bron                     |
|        pv:bestanden        |      /bestanden                    |
|     pv:informatiereactie           |            /reactie              |

| Process link | SHV: Overdragen naar schuldregelen |                                                                          | 
|--------------|----------------------------------------------|--------------------------------------------------------------------------|
| Task         | BeoordelenDossierTask                      ||
| Form         | shv-aanvraag.shv-completeren-dossier.beoordelen-dossier           ||
| Form type    | form                                         ||
| Displaytype  | panel                                        ||
| Form size    | medium                                       ||

| Process link | SHV: Uitzetten instrumenten |                                                                          | 
|--------------|----------------------------------------------|--------------------------------------------------------------------------|
| Task         | VastleggenResultaatBudgetbeheerTask                      ||
| Form         | shv-aanvraag.shv-uitzetten-instrumenten.vastleggen-terugkoppeling-budgetbegeleiding           ||
| Form type    | form                                         ||
| Displaytype  | panel                                        ||
| Form size    | medium                                       ||
||||
| Task         | VastleggenInschrijvingTask                      ||
| Form         | aanvraag-schuldhulpverlening.vastleggen-inschrijving-financiele-trainers           ||
| Form type    | form                                         ||
| Displaytype  | panel                                        ||
| Form size    | medium                                       ||
||||
| Task         | VastleggenResultaatBelastingcheckTask                      ||
| Form         | aanvraag-schuldhulpverlening.vastleggen-belastingcheck          ||
| Form type    | form                                         ||
| Displaytype  | panel                                        ||
| Form size    | medium                                       ||
||||
| Task         | VastleggenResultaatStabilisatieTask                      ||
| Form         | aanvraag-schuldhulpverlening.vastleggen-stabilisatie          ||
| Form type    | form                                         ||
| Displaytype  | panel                                        ||
| Form size    | medium                                       ||

| Process link | SHV: Wijzigen inhoudelijke beoordeling aanvraag                                         |                                                                          | 
|--------------|----------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Task         | ControlerenEnAanvullenClientEnPartnerInformatieTask                              ||
| Form         | aanvraag-schuldhulpverlening.controleren-client-partner:latest                   ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenSituatieTask                                               ||
| Form         | aanvraag-schuldhulpverlening.controleren-situatie:latest                         ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenInkomstenUitgavenTask                                      ||
| Form         | aanvraag-schuldhulpverlening.controleren-en-aanvullen-inkomsten-uitgaven:latest  ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenBezitVermogenTask                                          ||
| Form         | aanvraag-schuldhulpverlening.controleren-bezit-vermogen:latest                   ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenBelastingenTask                                            ||
| Form         | aanvraag-schuldhulpverlening.controleren-belastingen:latest                      ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenOndernemingenTask                                          ||
| Form         | aanvraag-schuldhulpverlening.controleren-ondernemingen:latest                    ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | ControlerenEnAanvullenSchuldenTask                                               ||
| Form         | aanvraag-schuldhulpverlening.controleren-en-aanvullen-schulden-informatie:latest ||
| Form type    | Form flow                                                                        ||
| Displaytype  | panel                                                                            ||
| Form size    | large                                                                            ||
|              |                                                                                  ||
| Task         | VastleggenAfloscapaciteitTask                                                    ||
| Form         | aanvraag-schuldhulpverlening.vastleggen-afloscapaciteit                          ||
| Form type    | Form                                                                             ||
| Displaytype  | panel                                                                            ||
| Form size    | medium                                                                           ||



| Process link                                  | SHV: Genereer document                                                                                                                                                                     |                                                                          | 
|-----------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Document                                      | Volmacht van invordering ||
| Process                                       | SHV: Genereer document                                                                                                                                                                     |                                                                          |
| Service task                                  | Genereer document                                                                                                                                                                          |                                                                          |
| Plugin                                        | SmartDocuments                                                                                                                                                                             |                                                                          | 
| Plugin action                                 | Document genereren                                                                                                                                                                         |                                                                          |
| var: Template groep                           | GZAC sd                                                                                                                                                                                    |                                                                          |
| var: Template naam                            | pv:templateName                                                                                                                                                                            |                                                                          |
| var: Naam procesvariable voor opslag document | gegenereerdShvDocument                                                                                                                                                                     |                                                                          |
| var: Documentformaat                          | PDF                                                                                                           |                                                                          | 
| Template-data:                                |                                                                                                                                                                                            |                                                                          |
| zaaknummer             | zaak:identificatie                                      |
| datumAanvraag          | case:createdOn                                          |
| aanmaakdatumBrief      | pv:aanmaakdatumBrief                                    |
| voorvoegselClient      | pv:voorvoegselClient                                    |
| voornaamClient         | pv:voornaamClient                                       |
| achternaamClient       | pv:achternaamClient                                     |
| naamDgtConsulent       | doc:gegevensBeoordeling.consulent.volledigeNaam         |
| telefoonDgtConsulent   | doc:gegevensBeoordeling.consulent.telefoonnummer        |
| emailadresDgtConsulent | doc:gegevensBeoordeling.consulent.emailadres            |
| bsnClient              | pv:bsnClient                                            |
| prsnummerClient        | doc:/gegevensBeoordeling/prsNummer                      |

| Process link                                  | SHV: Genereer document                                                                                                                                           |                                                                          | 
|-----------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Document                                      | Overeenkomst budgetbeheer ||
| Process                                       | SHV: Genereer document                                                                                                                                           |                                                                          |
| Service task                                  | Genereer document                                                                                                                                                |                                                                          |
| Plugin                                        | SmartDocuments                                                                                                                                                   |                                                                          | 
| Plugin action                                 | Document genereren                                                                                                                                               |                                                                          |
| var: Template groep                           | GZAC sd                                                                                                                                                          |                                                                          |
| var: Template naam                            | pv:templateName                                                                                                                                                  |                                                                          |
| var: Naam procesvariable voor opslag document | gegenereerdShvDocument                                                                                                                                           |                                                                          |
| var: Documentformaat                          | PDF                                                                               |                                                                          | 
| Template-data:                                |                                                                                                                                                                  |                                                                          || zaaknummer             | zaak:identificatie                                      |
| datumAanvraag          | case:createdOn                                          |
| aanmaakdatumBrief      | pv:aanmaakdatumBrief                                    |
| voorvoegselClient      | pv:voorvoegselClient                                    |
| voornaamClient         | pv:voornaamClient                                       |
| achternaamClient       | pv:achternaamClient                                     |
| naamDgtConsulent       | doc:gegevensBeoordeling.consulent.volledigeNaam         |
| telefoonDgtConsulent   | doc:gegevensBeoordeling.consulent.telefoonnummer        |
| emailadresDgtConsulent | doc:gegevensBeoordeling.consulent.emailadres            |
| bsnClient              | pv:bsnClient                                            |
| prsnummerClient        | doc:/gegevensBeoordeling/prsNummer                      |
| afloscapaciteitClient  | pv:afloscapaciteitClient                                |

| Process link                                  | SHV: Genereer document                                                                                                                |                                                                          | 
|-----------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Document                                      | Overdrachtsdocument naar budgetbeheer ||
| Process                                       | SHV: Genereer document                                                                                                                |                                                                          |
| Service task                                  | Genereer document                                                                                                                     |                                                                          |
| Plugin                                        | SmartDocuments                                                                                                                        |                                                                          | 
| Plugin action                                 | Document genereren                                                                                                                    |                                                                          |
| var: Template groep                           | GZAC sd                                                                                                                               |                                                                          |
| var: Template naam                            | pv:templateName                                                                                                                       |                                                                          |
| var: Naam procesvariable voor opslag document | gegenereerdShvDocument                                                                                                                |                                                                          |
| var: Documentformaat                          | PDF                                                      |                                                                          | 
| Template-data:                                |                                                                                                                                       |                                                                          |
| zaaknummer             | zaak:identificatie                                      |
| datumAanvraag          | case:createdOn                                          |
| aanmaakdatumBrief      | pv:aanmaakdatumBrief                                    |
| voorvoegselClient      | pv:voorvoegselClient                                    |
| voornaamClient         | pv:voornaamClient                                       |
| achternaamClient       | pv:achternaamClient                                     |
| naamDgtConsulent       | doc:gegevensBeoordeling.consulent.volledigeNaam         |
| telefoonDgtConsulent   | doc:gegevensBeoordeling.consulent.telefoonnummer        |
| emailadresDgtConsulent | doc:gegevensBeoordeling.consulent.emailadres            |
| bsnClient              | pv:bsnClient                                            |
| prsnummerClient        | doc:/gegevensBeoordeling/prsNummer                      |
| afloscapaciteitClient  | pv:afloscapaciteitClient                                |

| Process link                                  | SHV: Genereer document                          |                                                                          | 
|-----------------------------------------------|-------------------------------------------------|--------------------------------------------------------------------------|
| Document                                      | Overdrachtsdocument naar schuldregelaar         ||
| Process                                       | SHV: Genereer document                          |                                                                          |
| Service task                                  | Genereer document                               |                                                                          |
| Plugin                                        | SmartDocuments                                  |                                                                          | 
| Plugin action                                 | Document genereren                              |                                                                          |
| var: Template groep                           | GZAC sd                                         |                                                                          |
| var: Template naam                            | pv:templateName                                 |                                                                          |
| var: Naam procesvariable voor opslag document | gegenereerdShvDocument                          |                                                                          |
| var: Documentformaat                          | PDF                                             |                                                                          | 
| Template-data:                                |                                                 |                                                                          |
| zaaknummer                | zaak:identificatie                              |
| datumAanvraag             | case:createdOn                                  |
| aanmaakdatumBrief         | pv:aanmaakdatumBrief                            |
| voorvoegselClient         | pv:voorvoegselClient                            |
| voornaamClient            | pv:voornaamClient                               |
| achternaamClient          | pv:achternaamClient                             |
| voorlettersBewindvoerder  | pv:voorlettersBewindvoerder                     |
| voorvoegselBewindvoerder  | pv:voorvoegselBewindvoerder                     |
| achternaamBewindvoerder   | pv:achternaamBewindvoerder                      |
| emailBewindvoerder        | pv:emailBewindvoerder                           |
| telefoonBewindvoerder     | pv:telefoonBewindvoerder                        |
| naamDgtConsulent          | doc:gegevensBeoordeling.consulent.volledigeNaam |
| telefoonDgtConsulent      | doc:gegevensBeoordeling.consulent.telefoonnummer |
| emailadresDgtConsulent    | doc:gegevensBeoordeling.consulent.emailadres    |
| bsnClient                 | pv:bsnClient                                    |
| prsnummerClient           | doc:/gegevensBeoordeling/prsNummer              |

| Process link                                  | SHV: Genereer document                           |                                                                          | 
|-----------------------------------------------|--------------------------------------------------|--------------------------------------------------------------------------|
| Document                                      | Plan van aanpak                                  ||
| Process                                       | SHV: Genereer document                           |                                                                          |
| Service task                                  | Genereer document                                |                                                                          |
| Plugin                                        | SmartDocuments                                   |                                                                          | 
| Plugin action                                 | Document genereren                               |                                                                          |
| var: Template groep                           | GZAC sd                                          |                                                                          |
| var: Template naam                            | pv:templateName                                  |                                                                          |
| var: Naam procesvariable voor opslag document | gegenereerdShvDocument                           |                                                                          |
| var: Documentformaat                          | DOCX                                             |                                                                          | 
| Template-data:                                |                                                  |                                                                          |
| zaaknummer                | zaak:identificatie                               |
| datumAanvraag             | case:createdOn                                   |
| aanmaakdatumBrief         | pv:aanmaakdatumBrief                             |
| voorvoegselClient         | pv:voorvoegselClient                             |
| voornaamClient            | pv:voornaamClient                                |
| achternaamClient          | pv:achternaamClient                              |
| voorlettersBewindvoerder  | pv:voorlettersBewindvoerder                      |
| voorvoegselBewindvoerder  | pv:voorvoegselBewindvoerder                      |
| achternaamBewindvoerder   | pv:achternaamBewindvoerder                       |
| emailBewindvoerder        | pv:emailBewindvoerder                            |
| telefoonBewindvoerder     | pv:telefoonBewindvoerder                         |
| naamDgtConsulent          | doc:gegevensBeoordeling.consulent.volledigeNaam  |
| telefoonDgtConsulent      | doc:gegevensBeoordeling.consulent.telefoonnummer |
| emailadresDgtConsulent    | doc:gegevensBeoordeling.consulent.emailadres     |
| bsnClient                 | pv:bsnClient                                     |
| prsnummerClient           | doc:/gegevensBeoordeling/prsNummer               |

| Process link                                  | SHV: Genereer document                                                           |                                                                          | 
|-----------------------------------------------|----------------------------------------------------------------------------------|--------------------------------------------------------------------------|
| Document                                      | Informatieverzoek                                             ||
| Process                                       | SHV: Genereer document                                                           |                                                                          |
| Service task                                  | Genereer document                                                                |                                                                          |
| Plugin                                        | SmartDocuments                                                                   |                                                                          | 
| Plugin action                                 | Document genereren                                                               |                                                                          |
| var: Template groep                           | GZAC sd                                                                          |                                                                          |
| var: Template naam                            | pv:templateName                                                                  |                                                                          |
| var: Naam procesvariable voor opslag document | gegenereerdShvDocument                                                           |                                                                          |
| var: Documentformaat                          | PDF |                                                                          | 
| Template-data:                                |                                                                                  |                                                                          |
| zaaknummer                | zaak:identificatie                                      |
| datumAanvraag             | case:createdOn                                          |
| aanmaakdatumBrief         | pv:aanmaakdatumBrief                                    |
| voorvoegselClient         | pv:voorvoegselClient                                    |
| voornaamClient            | pv:voornaamClient                                       |
| achternaamClient          | pv:achternaamClient                                     |
| voorlettersBewindvoerder  | pv:voorlettersBewindvoerder                             |
| voorvoegselBewindvoerder  | pv:voorvoegselBewindvoerder                             |
| achternaamBewindvoerder   | pv:achternaamBewindvoerder                              |
| emailBewindvoerder        | pv:emailBewindvoerder                                   |
| telefoonBewindvoerder     | pv:telefoonBewindvoerder                                |
| naamDgtConsulent          | doc:gegevensBeoordeling.consulent.volledigeNaam         |
| telefoonDgtConsulent      | doc:gegevensBeoordeling.consulent.telefoonnummer        |
| emailadresDgtConsulent    | doc:gegevensBeoordeling.consulent.emailadres            |
| berichtNaarAanvrager      | pv:berichtNaarAanvrager                                 |
| opTeVragenInformatie      | pv:opTeVragenInformatie                                 |
| deadline                  | pv:deadline                                             |
| hersteltermijn            | pv:hersteltermijn                                       |

| Process link                                  | SHV: Genereer document                           |                                                                          | 
|-----------------------------------------------|--------------------------------------------------|--------------------------------------------------------------------------|
| Document                                      | Genomen besluit                                  ||
| Process                                       | SHV: Genereer document                           |                                                                          |
| Service task                                  | Genereer document                                |                                                                          |
| Plugin                                        | SmartDocuments                                   |                                                                          | 
| Plugin action                                 | Document genereren                               |                                                                          |
| var: Template groep                           | GZAC sd                                          |                                                                          |
| var: Template naam                            | pv:templateName                                  |                                                                          |
| var: Naam procesvariable voor opslag document | gegenereerdShvDocument                           |                                                                          |
| var: Documentformaat                          | PDF                                              |                                                                          | 
| Template-data:                                |                                                  |                                                                          |
| zaaknummer             | zaak:identificatie                               |
| datumAanvraag          | case:createdOn                                   |
| aanmaakdatumBrief      | pv:aanmaakdatumBrief                             |
| voorvoegselClient      | pv:voorvoegselClient                             |
| voornaamClient         | pv:voornaamClient                                |
| achternaamClient       | pv:achternaamClient                              |
| naamDgtConsulent       | doc:gegevensBeoordeling.consulent.volledigeNaam  |
| telefoonDgtConsulent   | doc:gegevensBeoordeling.consulent.telefoonnummer |
| emailadresDgtConsulent | doc:gegevensBeoordeling.consulent.emailadres     |
| redenVanBeeindiging    | doc:beeindigingAanvraag.redenBeeindiging         |

| Process link                                  | SHV: Genereer documment                                                                                                                                                                                                                             | 
|-----------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Uitleg                                        | Deze proces koppeling moet 6 keer ingesteld worden voor: volmacht van invordering, overeenkomst budgetbeheer, overdrachtsdocument naar budgetbeheer, overdrachtsdocument naar schuldregelaar, plan van aanpak, informatieverzoek, genereren besluit ||
| Process                                       | SHV: Genereer document                                                                                                                                                                                                                              |
| Service task                                  | Opslaan document in documenten api                                                                                                                                                                                                                  |
| Plugin                                        | documenten api                                                                                                                                                                                                                                      | 
| Plugin action                                 | Opslaan document                                                                                                                                                                                                                                    | 
| Vertrouwelijkheidsaanduiding                  | zaakvertrouwelijk                                                                                                                                                                                                                                   |
| Titel                                         | pv:templateName                                                                                                                                                                                                                                     |
| Beschrijving                                  | pv:templateName                                                                                                                                                                                                                                     | 
| Naam procesvariabele met document             | gegenereerdShvDocument                                                                                                                                                                                                                              |
| Naam procesvariabele voor opslag document-URL | gegenereerdeShvDocumentUrl                                                                                                                                                                                                                          | 
| Taal                                          | Nederlands                                                                                                                                                                                                                                          |
| Status                                        | In bewerking                                                                                                                                                                                                                                        |
| URL naar het informatieobjecttype             |                                                                                                                                                                                                                                                     |
| Volmacht van invordering                      | https://openzaak-zgw.test.denhaag.nl/catalogi/api/v1/informatieobjecttypen/36231514-46c7-4543-86c8-79564b9c9db5                                                                                                                                     |
| Overeenkomst budgetbeheer                     | https://openzaak-zgw.test.denhaag.nl/catalogi/api/v1/informatieobjecttypen/7d4362b3-4146-4713-9001-20a04a1b3358                                                                                                                                     |
| Overdrachtsdocument naar budgetbeheer         | https://openzaak-zgw.test.denhaag.nl/catalogi/api/v1/informatieobjecttypen/0b1a6faa-f9bd-4e23-9058-f94e92d69c94                                                                                                                                     |
| Overdrachtsdocument naar schuldregelaar       | https://openzaak-zgw.test.denhaag.nl/catalogi/api/v1/informatieobjecttypen/0b1a6faa-f9bd-4e23-9058-f94e92d69c94                                                                                                                                     |
| Plan van aanpak                               | https://openzaak-zgw.test.denhaag.nl/catalogi/api/v1/informatieobjecttypen/220a9f7e-5193-4394-9f5e-51d9e43b9238                                                                                                                                     |
| Informatieverzoek                             | https://openzaak-zgw.test.denhaag.nl/catalogi/api/v1/informatieobjecttypen/de6d7251-9642-42d2-84b2-c63a0d659a29                                                                                                                                     |
| Genereren besluit                             | https://openzaak-zgw.test.denhaag.nl/catalogi/api/v1/informatieobjecttypen/12b23139-0fc8-46d1-8256-7ebb7a3a070b                                                                                                                                     |



| process link          | SHV: Genereer document      | 
|-----------------------|-----------------------------|
| Process               | SHV: Genereer document      | 
| Service task          | Linken document aan de zaak | 
| Plugin                | Zaken API                   | 
| Plugin action         | Koppel document aan zaak    | 
| URL naar het document | pv:gegenereerdeShvDocumentUrl      |
| DocumentTitel         | SVH Document                |
| Documentbeschrijving  | SHV Document                |

| process link          | SHV: Genereer document               | 
|-----------------------|--------------------------------------|
| Process               | SHV: Genereer document               | 
| Service task          | Add tag to document                  | 
| Plugin                | Custom Documenten API                | 
| Plugin action         | Store informatieobjecttype trefwoord | 
| URL naar het document | pv:gegenereerdeShvDocumentUrl               |


### OPAS
#### Local configuration

- Docker compose — Ensure the docker compose stack is running.
- Plugins and object management configurations are **auto-deployed**.
- Plugin actions for process connection are **auto-deployed**.
- Test 
  - Case can be created manually or with the http requests zie: [Creation of Verzoek objects in objecten API](#creation-of-verzoek-objects-in-objecten-api). 
  - Portal task flow can't be tested locally. Use instead the ad-hoc task 'Ooievaarspas: Vastleggen reactie op informatieverzoek'.
  - Replace `${customZakenApiService.linkOverigeObjectToZaak(opasProductObjectUrl, "overige", execution.businessKey)}` with `${true}` in the 'Koppelen product aan zaak' task of the 'Ooievaarspas: Vastleggen product' process to test the flow completely. 

| ...                               | Zaaktype link (Aanvraag Opas)                               |                                 |
|-----------------------------------|-------------------------------------------------------------|---------------------------------|
| Go to                             | Admin -> Dossiers -> Aanvraag Opas -> Tabblad Configuratie  |                                 |
| Uploadproces koppelen aan dossier |                                                             |                                 |
| -                                 | Kies welk proces het uploaden van bestanden moet afhandelen | Documenten API upload document  |
| OpenZaak-koppelingen              |                                                             |                                 |
| -                                 | Zaak koppeling                                              | Aanvraag stadspas behandelen v6 |
| -                                 | Zaken Api plugin                                            | Zaken API (Autodeployed)        |
| -                                 | RSIN gebruikt bij aanmaken zaak                             | 100000009                       |
| -                                 | Automatisch aanmaken voor elk dossier                       | Aan                             |
| -                                 | Dit type dossier kan een behandelaar hebben                 | Aan                             |

### Financiële trainers
#### Local configuration

- Link zaaktype and upload process in UI for Trainingsbeheer and Trainingsdeelname
- Configure roltypen in UI for FT related verzoekplugin configurations
- Fetch latest [http-client.private.env.json](examples/objects-api-requests/http-client.private.env.json) and [.env.properties](.env.properties) from KeyPass
- Docker compose — Ensure the docker compose stack is running
- Set expression to **true** on **Koppel zaakobject** service task in **Financiële trainers: Initialiseren dossier**
- Remove process connection for **Update object** service task in process **Financiële trainers: Bijwerken trainingsstatus**


- Optional:
  - Link financiele-trainers.beheren-training.dev.start.json to Financiële trainers: Beheren training start event for quick training creatie (prefilled form) 
- Test - Use [http requests](examples/objects-api-requests/financiële-trainers) to add participants to the training you made.

## Algemene bijstand and IOAW uitkering 

### Algemene bijstand (DCM)
#### Local configuration

- Plugins and object management configurations are **auto-deployed** with 1 exception.
  - You must add objects under ` Objecten -> ALO: Informatieverzoek Items -> New Object ` in order for the informatieverzoek flow to work.
- Test - Use the http requests in [examples/objects-api-requests/algemene-bijstand-dcm](examples/objects-api-requests/algemene-bijstand-dcm) to create an object in the objects-api.
    - You have to create an environment private file with the url and token of the objects-api.

| ...                               | Zaaktype link (Algemene bijstand (DCM))                              |                                |
|-----------------------------------|----------------------------------------------------------------------|--------------------------------|
| Go to                             | Admin -> Dossiers -> Algemene bijstand (DCM) -> Tabblad Configuratie |                                |
| Uploadproces koppelen aan dossier |                                                                      |                                |
| -                                 | Kies welk proces het uploaden van bestanden moet afhandelen          | Documenten API upload document |
| OpenZaak-koppelingen              |                                                                      |                                |
| -                                 | Zaak koppeling                                                       | Aanvraag bijstand behandelen   |
| -                                 | Zaken Api plugin                                                     | Zaken API (Autodeployed)       |
| -                                 | RSIN gebruikt bij aanmaken zaak                                      | 100000009                      |
| -                                 | Automatisch aanmaken voor elk dossier                                | Aan                            |
| -                                 | Dit type dossier kan een behandelaar hebben                          | Aan                            |

### IOAW uitkering (DCM)
#### Local configuration

- Plugins and object management configurations are **auto-deployed** with 1 exception.
    - You must add objects under ` Objecten -> ALO: Informatieverzoek Items -> New Object ` in order for the informatieverzoek flow to work.
- Test - Use the http requests in [examples/objects-api-requests/ioaw-dcm](examples/objects-api-requests/ioaw-dcm) to create an object in the objects-api.
    - You have to create an environment private file with the url and token of the objects-api.

| ...                               | Zaaktype link (IOAW uitkering (DCM))                              |                                |
|-----------------------------------|-------------------------------------------------------------------|--------------------------------|
| Go to                             | Admin -> Dossiers -> IOAW uitkering (DCM) -> Tabblad Configuratie |                                |
| Uploadproces koppelen aan dossier |                                                                   |                                |
| -                                 | Kies welk proces het uploaden van bestanden moet afhandelen       | Documenten API upload document |
| OpenZaak-koppelingen              |                                                                   |                                |
| -                                 | Zaak koppeling                                                    | Aanvraag IAOW behandelen       |
| -                                 | Zaken Api plugin                                                  | Zaken API (Autodeployed)       |
| -                                 | RSIN gebruikt bij aanmaken zaak                                   | 100000009                      |
| -                                 | Automatisch aanmaken voor elk dossier                             | Aan                            |
| -                                 | Dit type dossier kan een behandelaar hebben                       | Aan                            |

## Doorbraaklab

| 17                            | Plugin                                       |                                                                    |
|-------------------------------|----------------------------------------------|--------------------------------------------------------------------|
| Plugin                        | Verzoek                                      |                                                                    |
| Configuratienaam              | Verzoek Doorbraaklab                         |                                                                    |
| Notificaties API-configuratie | docker - Notificaties API - Notificaties API |                                                                    |
| Proces                        | Create Zaakdossier                           |                                                                    |
| RSIN                          | 100000009                                    |                                                                    |
| Verzoektypen                  |                                              |                                                                    |
| -                             | type                                         | Verzoek doorbraaklab                                               |
| -                             | Dossierdefinitie                             | aanmelding-doorbraaklab                                            |
| -                             | Roltype                                      | initiator                                                          |
| -                             | Rolbeschrijving                              | Initiator                                                          |
| -                             | Procesdefinitie                              | DBL: Verwerken aanmelding doorbraaklab                             |
| -                             | Kopieerstrategie                             | Gespecifieerde velden                                              |
| -                             | Mapping                                      | zie examples/verzoek-plugin/mapping-dbl.json in sd backend project |

### Creation of Verzoek objects in objecten API
For each application, example HTTP requests are provided in the examples folder (located in the root of the project) to 
create the verzoek objects in the Objecten API, so that the respective case will be created in GZAC with the help of the 
Verzoek plugin.
 * Update the latest [http-client.private.env.json](examples/objects-api-requests/http-client.private.env.json) file from KeePass.
 * Select the request and the environment (Dev, Test, or Acc) from the "Run with" dropdown option.

