import {PluginConfigurationData} from '@valtimo/plugin';

interface Config extends PluginConfigurationData {
    tokenServiceUrl: string;
    handelsregisterBaseUrl: string;
    keystorePath: string;
    keystoreSecret: string;
    truststorePath: string;
    truststoreSecret: string;
    connectionTimeout: number;
    responseTimeout: number;
}

interface ZoekenConfig {
    kvkNummer: string;
    maxVestigingen: number;
    resultProcessVariableName: string;
}

export {Config, ZoekenConfig};
