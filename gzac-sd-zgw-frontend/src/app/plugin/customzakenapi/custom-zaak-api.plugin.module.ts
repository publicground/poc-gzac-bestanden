import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PluginTranslatePipeModule} from '@valtimo/plugin';
import {CarbonMultiInputModule, FormModule, InputModule, ParagraphModule, SelectModule} from '@valtimo/components';
import {
    CustomZaakApiConfigurationComponent
} from './components/custom-zaak-api-configuration/custom-zaak-api-configuration.component';
import {LinkObjectToZaakComponent} from './components/link-object-to-zaak/link-object-to-zaak.component';

@NgModule({
    declarations: [
        CustomZaakApiConfigurationComponent,
        LinkObjectToZaakComponent
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
        CustomZaakApiConfigurationComponent,
        LinkObjectToZaakComponent
    ]
})
export class CustomZaakApiPluginModule {
}
