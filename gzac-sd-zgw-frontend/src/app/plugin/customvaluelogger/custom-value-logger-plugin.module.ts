import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CarbonMultiInputModule, FormModule, InputModule, SelectModule} from '@valtimo/components';
import {PluginTranslatePipeModule} from '@valtimo/plugin';
import {CustomValueLoggerPluginConfigurationComponent} from './components/custom-value-logger-plugin-configuration/custom-value-logger-plugin-configuration.component';
import {CustomValueLoggerActionConfigurationComponent} from './components/custom-value-logger-action-configuration/custom-value-logger-action-configuration.component';

@NgModule({
  declarations: [
    CustomValueLoggerPluginConfigurationComponent,
    CustomValueLoggerActionConfigurationComponent,
  ],
  imports: [
    CommonModule,
    PluginTranslatePipeModule,
    FormModule,
    InputModule,
    CarbonMultiInputModule,
    SelectModule,
  ],
  exports: [
    CustomValueLoggerPluginConfigurationComponent,
    CustomValueLoggerActionConfigurationComponent,
  ],
})
export class CustomValueLoggerPluginModule {}
