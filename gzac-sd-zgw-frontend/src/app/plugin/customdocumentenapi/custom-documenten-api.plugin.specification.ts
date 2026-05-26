/*
 * Copyright 2015-2024 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {PluginSpecification} from '@valtimo/plugin';
import {
    CustomDocumentenApiConfigurationComponent
} from './components/custom-documenten-api-configuration/custom-documenten-api-configuration.component';
import {CUSTOM_DOCUMENTEN_API_PLUGIN_LOGO_BASE64} from './assets';
import {StoreTrefwoordenConfigurationComponent} from './components/store-trefwoorden/store-trefwoorden-configuration.component';

const customDocumentenApiPluginSpecification: PluginSpecification = {
    pluginId: 'customdocumentenapi',
    pluginConfigurationComponent: CustomDocumentenApiConfigurationComponent,
    pluginLogoBase64: CUSTOM_DOCUMENTEN_API_PLUGIN_LOGO_BASE64,
    functionConfigurationComponents: {
        'store-informatieobjecttype-trefwoord': StoreTrefwoordenConfigurationComponent
    },
    pluginTranslations: {
        nl: {
            title: 'Custom Documenten API',
            configurationTitle: 'Configuratienaam',
            description: 'API voor opslag en ontsluiting van documenten en daarbij behorende metadata.',
            'store-informatieobjecttype-trefwoord': 'Store informatieobjecttype trefwoord',
            configurationTitleTooltip:
                'Hier kunt je een eigen naam verzinnen. Onder deze naam zal de plugin te herkennen zijn in de rest van de applicatie',
            url: 'Documenten API URL',
            urlTooltip:
                'In dit veld moet de verwijzing komen naar de REST API van Documenten. Deze url moet dus eindigen op /documenten/api/v1/',
            authenticationPluginConfiguration: 'Configuratie authenticatie-plug-in',
            authenticationPluginConfigurationTooltip:
                'Selecteer de plugin die de authenticatie kan afhandelen. Wanneer de selectiebox leeg blijft zal de authenticatie plugin (bv. OpenZaak) eerst aangemaakt moeten worden',
            documentUrl: 'URL naar het document',
            documentUrlTooltip:
                'Dit veld ondersteunt URLs en proces variabelen. Gebruik pv:variable om een proces variabele uit te lezen',
        },
        en: {
            title: 'Documenten API',
            configurationTitle: 'Configuration name',
            description: 'API for storing and accessing documents and associated metadata.',
            'store-informationobjecttype-trefwoord': 'Store informatiebjecttype trefwoorden',
            configurationTitleTooltip:
                'Here you can enter a name for the plugin. This name will be used to recognize the plugin throughout the rest of the application',
            url: 'Documenten API URL',
            urlTooltip:
                'This field must contain the URL to the REST API of Documenten, therefore this URL should end with /documenten/api/v1/',
            authenticationPluginConfiguration: 'Authentication plugin configuration',
            authenticationPluginConfigurationTooltip:
                'Select the plugin that can handle the authentication. If the selection box remains empty, the authentication plugin (e.g. OpenZaak) will have to be created first',
            documentUrl: 'URL to the document',
            documentUrlTooltip:
                'This field supports URLs and process variables. Use pv:variable to read a process variable',
        },
        de: {
            title: 'Documenten API',
            description: 'API zum Speichern und Zugreifen auf Dokumente und zugehörige Metadaten.',
            'store-informatieobjecttype-trefwoord': 'Store informatieobjecttype trefwoord',
            storeUploadedDocumentMessage:
                'Das Speichern eines hochgeladenen Dokuments erfordert keine Konfiguration.',
            configurationTitle: 'Konfigurationsname',
            configurationTitleTooltip:
                'Hier können Sie einen Namen für das Plugin eingeben. Dieser Name wird verwendet, um das Plugin im Rest der Anwendung zu erkennen',
            url: 'Documenten API URL',
            urlTooltip:
                'Dieses Feld muss die URL zur REST API von Documenten enthalten, daher sollte diese URL mit enden /documenten/api/v1/',
            authenticationPluginConfiguration: 'Authentifizierungs-Plugin-Konfiguration',
            authenticationPluginConfigurationTooltip:
                'Wählen Sie das Plugin aus, das die Authentifizierung verarbeiten kann. Bleibt das Auswahlfeld leer, muss zunächst das Authentifizierungs-Plugin (z. B. OpenZaak) erstellt werden',
            documentUrl: 'URL zum Dokument',
            documentUrlTooltip:
                'Dieses Feld unterstützt URLs und Prozessvariablen. Verwenden Sie pv:Variablen, um eine Prozessvariable zu lesen',
            titel: 'Dokumenttitel',
        },
    }

};

export {customDocumentenApiPluginSpecification};
