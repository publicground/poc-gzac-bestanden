import {PluginConfigurationData} from '@valtimo/plugin';

interface HaalCentraalAuthenticationPluginConfig extends PluginConfigurationData {
    tokenServiceUrl: string;
    keystorePath: string;
    keystoreSecret: string;
    truststorePath: string;
    truststoreSecret: string;
    connectionTimeout: number;
    responseTimeout: number;
}

export {HaalCentraalAuthenticationPluginConfig};
