import {PluginSpecification} from '@valtimo/plugin';
import {
    HaalcentraalKvkPluginConfigurationComponent
} from './components/haalcentraal-kvk-plugin-configuration/haalcentraal-kvk-plugin-configuration.component';
import {KVK_HAALCENTRAAL_PLUGIN_LOGO_BASE64} from './assets';
import {HaalcentraalZoekenOpKvkNummerComponent} from './components/haalcentraal-zoeken-op-kvk-nummer/haalcentraal-zoeken-op-kvk-nummer.component';

const haalcentraalKvkPluginSpecification: PluginSpecification = {
    /*
    The plugin definition key of the plugin.
    This needs to be the same as the id received from the back-endÏÏ
     */
    pluginId: 'haalcentraalkvkhandelsregister',
    /*
    A component of the interface PluginConfigurationComponent, used to configure the plugin itself.
     */
    pluginConfigurationComponent: HaalcentraalKvkPluginConfigurationComponent,
    // Points to a Base64 encoded string, which contains the logo of the plugin.
    pluginLogoBase64: KVK_HAALCENTRAAL_PLUGIN_LOGO_BASE64,
    functionConfigurationComponents: {
        'hc-zoeken-op-kvk-nummer': HaalcentraalZoekenOpKvkNummerComponent
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
                'Haalcentraal KvK Handelsregister plugin',
            title: 'Haalcentraal Kvk Handelsregister API',
            description: 'Haalcentraal KvK Handelsregister API bevragen.',
            kvkNummer: 'Kvk nummer',
            handelsregisterBaseUrl: 'base url naar handelsregister kvk service',
            tokenServiceUrl: 'Url naar token service',
            keystorePath: 'Keystore certificate pad',
            keystoreSecret: 'Keystore certificate key',
            truststorePath: 'Truststore certificate pad',
            truststoreSecret: 'Truststore certificate key',
            connectionTimeout: 'Request connection timeout in milliseconden',
            responseTimeout: 'Request response timeout in milliseconden',
            'hc-zoeken-op-kvk-nummer': 'Zoeken op Kvk nummer'
        },
        en: {
            configurationTitle: 'Configuration name',
            configurationTitleTooltip:
                'Haalcentraal KvK Handelsregister API plugin',
            title: 'Haalcentraal KVK Handelsregister API',
            description: 'With this plugin GZAC can retrieve Haalcentraal KvK Handelsregister API requests',
            handelsregisterBaseUrl: 'base url to handelsregister kvk service',
            kvkNummer: 'KvK Number',
            keystorePath: 'Keystore certificate path',
            keystoreSecret: 'Keystore certificate key',
            truststorePath: 'Truststore certificate path',
            truststoreSecret: 'Truststore certificate key',
            connectionTimeout: 'Request connection timeout in milliseconds',
            responseTimeout: 'Request response timeout in milliseconds',
            'hc-zoeken-op-kvk-nummer': 'find Kvk nummer'
        }
    }
};

export {haalcentraalKvkPluginSpecification};
