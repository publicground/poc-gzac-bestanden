import {NgModule} from "@angular/core";
import {
    HaalCentraalAuthenticationPluginConfigurationComponent
} from "./components/haal-centraal-authentication-plugin-configuration.component";
import {CommonModule} from "@angular/common";
import {PluginTranslatePipeModule} from "@valtimo/plugin";
import {FormModule, InputModule} from "@valtimo/components";

@NgModule({
    declarations: [HaalCentraalAuthenticationPluginConfigurationComponent],
    imports: [
        CommonModule,
        PluginTranslatePipeModule,
        FormModule,
        InputModule,
    ],
    exports: [HaalCentraalAuthenticationPluginConfigurationComponent]
})
export class HaalCentraalAuthenticationPluginModule {
}
