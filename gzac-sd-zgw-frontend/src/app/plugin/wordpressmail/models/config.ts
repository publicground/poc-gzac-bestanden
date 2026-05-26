import {PluginConfigurationData} from '@valtimo/plugin';

interface Config extends PluginConfigurationData {
    baseUrl: string;
}

interface SendMailConfig {
    mailSendTaskFrom: string;
    mailSendTaskTemplate: string;
    mailSendTaskSubject: string;
    mailSendTaskTo: string;
    templateData: Array<{key: string; value: string}>;
}

interface SendMailWithAttachmentConfig extends SendMailConfig {
    documentUrls: Array<string>;
}

export {Config, SendMailConfig, SendMailWithAttachmentConfig};
