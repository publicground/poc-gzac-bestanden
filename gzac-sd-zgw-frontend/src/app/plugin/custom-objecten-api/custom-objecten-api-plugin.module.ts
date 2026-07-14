import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {PluginTranslatePipeModule} from '@valtimo/plugin';
import {CarbonMultiInputModule, FormModule, InputModule, ParagraphModule, SelectModule} from '@valtimo/components';
import {
    CustomObjectenApiPluginConfigurationComponent
} from './components/custom-objecten-api-configuration/custom-objecten-api-plugin-configuration.component';
import {CreateObjectComponent} from './components/create-object/create-object.component';

@NgModule({
    declarations: [
        CustomObjectenApiPluginConfigurationComponent,
        CreateObjectComponent
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
        CustomObjectenApiPluginConfigurationComponent,
        CreateObjectComponent
    ]
})
export class CustomObjectsApiPluginModule {}
