import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PluginTranslatePipeModule} from '@valtimo/plugin';
import {CarbonMultiInputModule, FormModule, InputModule, ParagraphModule, SelectModule} from '@valtimo/components';
import {
    WordpressMailPluginConfigurationComponent
} from './components/wordpress-mail-plugin-configuration/wordpress-mail-plugin-configuration.component';
import {SendMailComponent} from './components/send-mail/send-mail.component';
import {SendMailWithAttachmentComponent} from './components/send-mail-with-attachement/send-mail-with-attachment.component';

@NgModule({
    declarations: [
        WordpressMailPluginConfigurationComponent,
        SendMailComponent,
        SendMailWithAttachmentComponent
    ],
    imports: [
        CommonModule,
        PluginTranslatePipeModule,
        FormModule,
        InputModule,
        SelectModule,
        ParagraphModule,
        CarbonMultiInputModule
    ],
    exports: [
        WordpressMailPluginConfigurationComponent,
        SendMailComponent,
        SendMailWithAttachmentComponent
    ]
})
export class WordpressMailPluginModule {
}
