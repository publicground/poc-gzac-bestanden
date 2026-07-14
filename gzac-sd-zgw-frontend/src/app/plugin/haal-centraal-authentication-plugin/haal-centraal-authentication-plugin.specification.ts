import {PluginSpecification} from '@valtimo/plugin';
import {
    HaalCentraalAuthenticationPluginConfigurationComponent
} from './components/haal-centraal-authentication-plugin-configuration.component';
import {HAAL_CENTRAAL_AUTHENTICATION_PLUGIN_LOGO_BASE64} from './assets';

const haalCentraalAuthenticationPluginSpecification: PluginSpecification = {
    /*
    The plugin definition key of the plugin.
    This needs to be the same as the id received from the back-end
     */
    pluginId: 'haal-centraal-authentication-plugin',
    /*
    A component of the interface PluginConfigurationComponent, used to configure the plugin itself.
     */
    pluginConfigurationComponent: HaalCentraalAuthenticationPluginConfigurationComponent,
    // Points to a Base64 encoded string, which contains the logo of the plugin.
    pluginLogoBase64: HAAL_CENTRAAL_AUTHENTICATION_PLUGIN_LOGO_BASE64,
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
                'Haal Centraal Authenticatie Plugin',
            title: 'Haal Centraal Authenticatie Plugin',
            description: 'Plugin die wordt gebruikt om authenticatie te bieden aan haal centraal',
            tokenServiceUrl: 'Url naar token service',
            keystorePath: 'Keystore certificate pad',
            keystoreSecret: 'Keystore certificate key',
            truststorePath: 'Truststore certificate pad',
            truststoreSecret: 'Truststore certificate key',
            connectionTimeout: 'Request connection timeout in milliseconden',
            responseTimeout: 'Request response timeout in milliseconden',
        },
        en: {
            configurationTitle: 'Configuration name',
            configurationTitleTooltip:
                'Haal Centraal Authentication Plugin',
            title: 'Haal Centraal Authentication Plugin',
            description: 'Plugin used to provide authentication to haal centraal',
            keystorePath: 'Keystore certificate path',
            keystoreSecret: 'Keystore certificate key',
            truststorePath: 'Truststore certificate path',
            truststoreSecret: 'Truststore certificate key',
            connectionTimeout: 'Request connection timeout in milliseconds',
            responseTimeout: 'Request response timeout in milliseconds',
            authenticationPluginConfiguration: 'Authentication plugin configuration',
        }
    }
};

export {haalCentraalAuthenticationPluginSpecification};
