import {PluginConfigurationData} from '@valtimo/plugin';

interface HcApiKeyAuthenticationPluginConfig extends PluginConfigurationData {
    authenticationSecret: string;
}

export {HcApiKeyAuthenticationPluginConfig};
