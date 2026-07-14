import {PluginConfigurationData} from '@valtimo/plugin';

interface Config extends PluginConfigurationData {
}

interface LinkObjectToZaakConfig {
    objectUrl: string;
    objectType: string;
    objectTypeOverige: string;
}

export {Config, LinkObjectToZaakConfig};
