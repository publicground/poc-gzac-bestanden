import {PluginSpecification} from '@valtimo/plugin';
import {
    CustomObjectenApiPluginConfigurationComponent
} from './components/custom-objecten-api-configuration/custom-objecten-api-plugin-configuration.component';
import {CUSTOM_OBJECTEN_API_PLUGIN_LOGO_BASE64} from './assets';
import {CreateObjectComponent} from './components/create-object/create-object.component';

const customObjectenApiPluginSpecification: PluginSpecification = {
    /*
    The plugin definition key of the plugin.
    This needs to be the same as the id received from the back-endÏÏ
     */
    pluginId: 'custom-objecten-api-plugin',
    /*
    A component of the interface PluginConfigurationComponent, used to configure the plugin itself.
     */
    pluginConfigurationComponent: CustomObjectenApiPluginConfigurationComponent,
    // Points to a Base64 encoded string, which contains the logo of the plugin.
    pluginLogoBase64: CUSTOM_OBJECTEN_API_PLUGIN_LOGO_BASE64,
    functionConfigurationComponents: {
        'custom-create-object': CreateObjectComponent
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
                'Custom Objecten API plugin',
            title: 'Custom Objecten API',
            description: 'Met deze plugin kan GZAC objecten genereren.',
            waarderingObject: 'waardering object pv',
            resultObjectUrl: 'resultaat object URL process variable',
            objectManagementTitle: 'Object Management titel',
            'create-object': 'Creëer object',
        },
        en: {
            configurationTitle: 'Configuration name',
            configurationTitleTooltip:
                'Custom Objects API plugin',
            title: 'Custom Objects API',
            description: 'This Plugin extends the regular ObjectsAPI with etra featrues like creating objects.',
            waarderingObject: 'waardering object pv',
            resultObjectUrl: 'Result Object URL',
            objectManagementTitle: 'Object Management title',
            'create-object': 'create object',
        }
    }
};

export {customObjectenApiPluginSpecification};
