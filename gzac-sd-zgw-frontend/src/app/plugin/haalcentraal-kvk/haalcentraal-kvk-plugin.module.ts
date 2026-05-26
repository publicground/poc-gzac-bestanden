import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PluginTranslatePipeModule} from '@valtimo/plugin';
import {CarbonMultiInputModule, FormModule, InputModule, ParagraphModule, SelectModule} from '@valtimo/components';
import {
    HaalcentraalKvkPluginConfigurationComponent
} from './components/haalcentraal-kvk-plugin-configuration/haalcentraal-kvk-plugin-configuration.component';
import {HaalcentraalZoekenOpKvkNummerComponent} from './components/haalcentraal-zoeken-op-kvk-nummer/haalcentraal-zoeken-op-kvk-nummer.component';

@NgModule({
    declarations: [
        HaalcentraalKvkPluginConfigurationComponent,
        HaalcentraalZoekenOpKvkNummerComponent
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
        HaalcentraalKvkPluginConfigurationComponent,
        HaalcentraalZoekenOpKvkNummerComponent
    ]
})
export class HaalcentraalKvkPluginModule {
}
