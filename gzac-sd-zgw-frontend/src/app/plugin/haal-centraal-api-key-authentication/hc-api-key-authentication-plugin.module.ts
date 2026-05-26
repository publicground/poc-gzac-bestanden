import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {PluginTranslatePipeModule} from "@valtimo/plugin";
import {FormModule, InputModule} from "@valtimo/components";
import {
    HcApiKeyAuthenticationPluginConfigurationComponent
} from "./components/hc-api-key-authentication-plugin-configuration.component";

@NgModule({
    declarations: [HcApiKeyAuthenticationPluginConfigurationComponent],
    imports: [
        CommonModule,
        PluginTranslatePipeModule,
        FormModule,
        InputModule,
    ],
    exports: [HcApiKeyAuthenticationPluginConfigurationComponent]
})
export class HcApiKeyAuthenticationPluginModule {
}
