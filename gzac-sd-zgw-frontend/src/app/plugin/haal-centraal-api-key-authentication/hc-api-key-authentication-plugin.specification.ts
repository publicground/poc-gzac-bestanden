import {PluginSpecification} from '@valtimo/plugin';
import {HAAL_CENTRAAL_API_KEY_AUTHENTICATION_PLUGIN_LOGO_BASE64} from './assets';
import {HcApiKeyAuthenticationPluginConfigurationComponent} from "./components/hc-api-key-authentication-plugin-configuration.component";

const hcApiKeyAuthenticationPluginSpecification: PluginSpecification = {
    /*
    The plugin definition key of the plugin.
    This needs to be the same as the id received from the back-end
     */
    pluginId: 'haal-centraal-api-key-authentication',
    /*
    A component of the interface PluginConfigurationComponent, used to configure the plugin itself.
     */
    pluginConfigurationComponent: HcApiKeyAuthenticationPluginConfigurationComponent,
    // Points to a Base64 encoded string, which contains the logo of the plugin.
    pluginLogoBase64: HAAL_CENTRAAL_API_KEY_AUTHENTICATION_PLUGIN_LOGO_BASE64,
    /*
    For each language key an implementation supports, translation keys with a translation are provided below.
    These can then be used in configuration components using the pluginTranslate pipe or the PluginTranslationService.
    At a minimum, the keys 'title' and 'description' need to be defined.
    Each function key also requires a translation key. In this case, the key 'sample-action' is added.
     */
    pluginTranslations: {
        nl: {
            configurationTitle: 'Configuratienaam',
            configurationTitleTooltip:
                'Haal Centraal API Key Authenticatie Plugin',
            title: 'Haal Centraal API Key Authenticatie Plugin',
            description: 'API-sleutelgebaseerde authenticatieplug-in voor toegang tot haal centraal API\'s',
            authenticationSecret: 'API key',
        },
        en: {
            configurationTitle: 'Configuration name',
            configurationTitleTooltip:
                'Haal Centraal API Key Authentication Plugin',
            title: 'Haal Centraal API Key Authentication Plugin',
            description: 'API key based authentication plugin for haal centraal APIs',
            authenticationSecret: 'API key',
        }
    }
};

export {hcApiKeyAuthenticationPluginSpecification};
