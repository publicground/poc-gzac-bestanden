import {PluginSpecification} from '@valtimo/plugin';
import {
    CustomZaakApiConfigurationComponent
} from './components/custom-zaak-api-configuration/custom-zaak-api-configuration.component';
import {ZAKEN_API_PLUGIN_LOGO_BASE64} from './assets';
import {LinkObjectToZaakComponent} from './components/link-object-to-zaak/link-object-to-zaak.component';

const customZaakApiPluginSpecification: PluginSpecification = {
    /*
    The plugin definition key of the plugin.
    This needs to be the same as the id received from the back-endÏÏ
     */
    pluginId: 'customzakenapi',
    /*
    A component of the interface PluginConfigurationComponent, used to configure the plugin itself.
     */
    pluginConfigurationComponent: CustomZaakApiConfigurationComponent,
    // Points to a Base64 encoded string, which contains the logo of the plugin.
    pluginLogoBase64: ZAKEN_API_PLUGIN_LOGO_BASE64,
    functionConfigurationComponents: {
        'link-object-to-zaak': LinkObjectToZaakComponent
    },
    /*
    For each language key an implementation supports, translation keys with a translation are provided below.
    These can then be used in configuration components using the pluginTranslate pipe or the PluginTranslationService.
    At a minumum, the keys 'title' and 'description' need to be defined.
    Each function key also requires a translation key. In this case, the key 'sample-action' is added.
     */
    pluginTranslations: {
        nl: {
            configurationTitle: 'Configuratienaam',
            configurationTitleTooltip:
                'Custom Zaken API plugin',
            title: 'Custom Zaken API',
            description: 'Custom plugin voor het ontsluiten van extra open zaak API Endpoints.',
            url: 'URL',
            urlTooltip:
                'In dit veld moet de verwijzing komen naar de REST api van Open zaak. Deze url moet dus eindigen op /zaken/api/v1/',
            objectUrl: 'URL naar het document',
            objectUrlTooltip:
                'Dit veld ondersteunt URLs en proces variabelen. Gebruik pv:variable om een proces variabele uit te lezen',
            objectType: 'Objecttype (default overige)',
            objectTypeTooltip: 'Objecttype overige wanneer niet standaard type',
            objectTypeOverige: 'Alternatief objecttype (default overige)',
            objectTypeOverigeTooltip: 'Alternatief objecttype wanneer niet standaard type',
            objectenApiAuthentication: 'Configuratie object API authenticatie',
            objectenApiAuthenticationTooltip:
                'Selecteer de plugin die de authenticatie voor de object API kan afhandelen. Wanneer de selectiebox leeg blijft zal de authenticatie plugin eerst aangemaakt moeten worden',
            zakenApiAuthentication: 'Configuratie zaken API authenticatie',
            zakenApiAuthenticationTooltip:
                'Selecteer de plugin die de authenticatie voor openzaak kan afhandelen. Wanneer de selectiebox leeg blijft zal de authenticatie plugin eerst aangemaakt moeten worden',
            'link-object-to-zaak': 'Link object aan zaak',
        },
        en: {
            configurationTitle: 'Configuration name',
            configurationTitleTooltip:
                'Custom Zaken API plugin',
            title: 'Custom Zaken API',
            description: 'Custom plugin to unlock more open zaak API Endpoints.',
            url: 'URL',
            urlTooltip:
                'In this field, the reference to the REST API of Open Zaak must be provided. This URL should therefore end with /zaken/api/v1/.',
            objectUrl: 'URL to the object',
            objectUrlTooltip:
                'This field supports URLs and process variables. Use pv:variable to read a use variable.',
            objectType: 'Objecttype (default overige)',
            objectTypeTooltip: 'Objecttype overige when not predefined',
            objectTypeOverige: 'Alternative objecttype (default overige)',
            objectTypeOverigeTooltip: 'Alternative objecttype when not predefined',
            objectenApiAuthentication: 'Configuration object API authentication',
            objectenApiAuthenticationTooltip:
                'Select the plugin that can handle authentication for the Object API. If the selection box remains empty, the authentication plugin will need to be created first.',
            zakenApiAuthentication: 'Configuration zaken API authentication',
            zakenApiAuthenticationTooltip:
                'Select the plugin that can handle Open zaak the authentication. If the selection box remains empty, the authentication plugin will need to be created first.',
            'link-object-to-zaak': 'Link object to case',
        }
    }
};

export {customZaakApiPluginSpecification};
