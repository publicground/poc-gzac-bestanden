import {PluginSpecification} from '@valtimo/plugin';
import {CustomValueLoggerPluginConfigurationComponent} from './components/custom-value-logger-plugin-configuration/custom-value-logger-plugin-configuration.component';
import {CUSTOM_VALUE_LOGGER_PLUGIN_LOGO_BASE64} from './assets/custom-value-logger-plugin-logo';
import {CustomValueLoggerActionConfigurationComponent} from './components/custom-value-logger-action-configuration/custom-value-logger-action-configuration.component';

const customValueLoggerPluginSpecification: PluginSpecification = {
  /*
  The plugin definition key of the plugin.
  This needs to be the same as the id received from the back-end
   */
  pluginId: 'valueLogger',
  /*
  A component of the interface PluginConfigurationComponent, used to configure the plugin itself.
   */
  pluginConfigurationComponent: CustomValueLoggerPluginConfigurationComponent,
  // Points to a Base64 encoded string, which contains the logo of the plugin.
  pluginLogoBase64: CUSTOM_VALUE_LOGGER_PLUGIN_LOGO_BASE64,
  functionConfigurationComponents: {
    /*
     For each plugin action id received from the back-end, a component is provided of the interface FunctionConfigurationComponent.
     These are used to configure each plugin action.
     */
    'log-values': CustomValueLoggerActionConfigurationComponent,
  },
  /*
  For each language key an implementation supports, translation keys with a translation are provided below.
  These can then be used in configuration components using the pluginTranslate pipe or the PluginTranslationService.
  At a minumum, the keys 'title' and 'description' need to be defined.
  Each function key also requires a translation key. In this case, the key 'sample-action' is added.
   */
  pluginTranslations: {
    nl: {
      title: 'Value Logger Plugin',
      description: 'Plugin voor het loggen van variabelen.',
      configurationTitle: 'Configuratienaam',
      'log-values': 'Logwaarden in de IDEA-console'
    },
    en: {
      title: 'Value logger plugin',
      description: 'A great plugin to get started with.',
      configurationTitle: 'Configuration name',
      url: 'URL',
      'log-values': 'Log values in the IDEA console'
    },
  },
};

export {customValueLoggerPluginSpecification};
