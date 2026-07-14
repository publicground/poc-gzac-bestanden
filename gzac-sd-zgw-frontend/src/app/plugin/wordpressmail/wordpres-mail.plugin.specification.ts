import {PluginSpecification} from '@valtimo/plugin';
import {
    WordpressMailPluginConfigurationComponent
} from './components/wordpress-mail-plugin-configuration/wordpress-mail-plugin-configuration.component';
import {WORDPRESS_MAIL_PLUGIN_LOGO_BASE64} from './assets';
import {SendMailComponent} from './components/send-mail/send-mail.component';
import {SendMailWithAttachmentComponent} from './components/send-mail-with-attachement/send-mail-with-attachment.component';

const wordpresMailPluginSpecification: PluginSpecification = {
    /*
    The plugin definition key of the plugin.
    This needs to be the same as the id received from the back-endÏÏ
     */
    pluginId: 'wordpressmail',
    /*
    A component of the interface PluginConfigurationComponent, used to configure the plugin itself.
     */
    pluginConfigurationComponent: WordpressMailPluginConfigurationComponent,
    // Points to a Base64 encoded string, which contains the logo of the plugin.
    pluginLogoBase64: WORDPRESS_MAIL_PLUGIN_LOGO_BASE64,
    functionConfigurationComponents: {
        'send-mail': SendMailComponent,
        'send-mail-with-attachment': SendMailWithAttachmentComponent
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
                'Wordpress mail plugin',
            title: 'Wordpress Mail',
            description: 'Met deze plugin kan GZAC mails versturen mbv de DenHaag Wordpress Mail service.',
            templateDataTooltip:
                'De rechter value-kolom ondersteunt ook het gebruik van procesvariabelen zoals pv: en doc:',
            templateData: 'Email template data',
            mailSendTaskFrom: 'E-mailadres afzender',
            mailSendTaskTemplate: 'E-mail template',
            mailSendTaskSubject: 'E-mail onderwerp',
            mailSendTaskTo: 'E-mailadres ontvanger',
            documentUrls: 'Document url lijst',
            url: 'URL',
            'send-mail': 'Versturen  e-mail',
            'send-mail-with-attachment': 'Versturen  e-mail met bijlage'
        },
        en: {
            configurationTitle: 'Configuration name',
            configurationTitleTooltip:
                'Wordpress mail plugin',
            title: 'Wordpress Mail',
            description: 'With this plugin GZAC can send emails with the DenHaag Wordpress Mail service',
            templateData: 'E-mail template data',
            mailSendTaskFrom: 'E-mail address from',
            mailSendTaskTemplate: 'E-mail template',
            mailSendTaskSubject: 'E-mail subject',
            mailSendTaskTo: 'E-mail address to',
            documentUrls: 'Document url list',
            templateDataTooltip:
                'The right value-column also supports the use of process variables such as pv: and doc:',
            url: 'URL',
            'send-mail': 'Send  e-mail',
            'send-mail-with-attachment': 'Send  e-mail with attachment'
        }
    }
};

export {wordpresMailPluginSpecification};
