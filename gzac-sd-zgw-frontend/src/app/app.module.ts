import {BrowserModule} from '@angular/platform-browser';
import {Injector, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {HttpBackend, HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LayoutModule, TranslationManagementModule} from '@valtimo/layout';
import {TaskModule} from '@valtimo/task';
import {environment} from '../environments/environment';
import {SecurityModule} from '@valtimo/security';
import {
  BpmnJsDiagramModule,
  CardModule,
  MenuModule,
  registerFormioUploadComponent,
  WidgetModule,
  FormIoModule,
  registerFormioFileSelectorComponent,
  registerFormioIbanComponent,
  registerFormioCurrencyComponent,
} from '@valtimo/components';
import {
  CASE_TAB_TOKEN,
  DefaultTabs,
  DossierDetailTabAuditComponent,
  DossierDetailTabDocumentsComponent,
  DossierDetailTabNotesComponent,
  DossierDetailTabProgressComponent,
  DossierDetailTabSummaryComponent,
  DossierModule,
} from '@valtimo/dossier';
import {ProcessModule} from '@valtimo/process';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {
  BigNumberModule,
  CaseCountDataSourceModule,
  CaseCountsDataSourceModule,
  CaseGroupByDataSourceModule,
  DashboardModule,
  DonutModule,
  GaugeModule,
  MeterModule,
} from '@valtimo/dashboard';
import {DashboardManagementModule} from '@valtimo/dashboard-management';
import {DocumentModule} from '@valtimo/document';
import {AccountModule} from '@valtimo/account';
import {ChoiceFieldModule} from '@valtimo/choice-field';
import {ResourceModule} from '@valtimo/resource';
import {FormModule} from '@valtimo/form';
import {SwaggerModule} from '@valtimo/swagger';
import {AnalyseModule} from '@valtimo/analyse';
import {ProcessManagementModule} from '@valtimo/process-management';
import {DecisionModule} from '@valtimo/decision';
import {MilestoneModule} from '@valtimo/milestone';
import {LoggerModule} from 'ngx-logger';
import {FormManagementModule} from '@valtimo/form-management';
import {ProcessLinkModule} from '@valtimo/process-link';
import {MigrationModule} from '@valtimo/migration';
import {DossierManagementModule} from '@valtimo/dossier-management';
import {BootstrapModule} from '@valtimo/bootstrap';
import {ConfigModule, ConfigService, MultiTranslateHttpLoaderFactory} from '@valtimo/config';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {FormFlowManagementModule} from '@valtimo/form-flow-management';
import {
  PLUGINS_TOKEN,
  OpenZaakPluginModule,
  openZaakPluginSpecification,
  SmartDocumentsPluginModule,
  smartDocumentsPluginSpecification,
  ZakenApiPluginModule,
  zakenApiPluginSpecification,
  DocumentenApiPluginModule,
  documentenApiPluginSpecification,
  CatalogiApiPluginModule,
  catalogiApiPluginSpecification,
  notificatiesApiPluginSpecification,
  NotificatiesApiPluginModule,
  openNotificatiesPluginSpecification,
  OpenNotificatiesPluginModule,
  ObjectenApiPluginModule,
  objectenApiPluginSpecification,
  ObjecttypenApiPluginModule,
  objecttypenApiPluginSpecification,
  ObjectTokenAuthenticationPluginModule,
  objectTokenAuthenticationPluginSpecification,
  portaaltaakPluginSpecification,
  PortaaltaakPluginModule,
  verzoekPluginSpecification,
  VerzoekPluginModule,
  besluitenApiPluginSpecification,
  BesluitenApiPluginModule,
} from '@valtimo/plugin';
import {PluginManagementModule} from '@valtimo/plugin-management';
import {ObjectManagementModule} from '@valtimo/object-management';
import {ConnectorManagementModule} from '@valtimo/connector-management';
import {CustomObjectsApiPluginModule} from './plugin/custom-objecten-api/custom-objecten-api-plugin.module';
import {customObjectenApiPluginSpecification} from './plugin/custom-objecten-api/custom-objecten-api.plugin.specification';
import {WordpressMailPluginModule} from './plugin/wordpressmail/wordpress-mail-plugin.module';
import {wordpresMailPluginSpecification} from './plugin/wordpressmail/wordpres-mail.plugin.specification';
import {HaalCentraalBrpPluginModule} from './plugin/haal-centraal/brp/haal-centraal-brp.plugin.module';
import {haalCentraalBrpPluginSpecification} from './plugin/haal-centraal/brp/haal-centraal-brp.plugin.specification';
import {HaalcentraalKvkPluginModule} from './plugin/haalcentraal-kvk/haalcentraal-kvk-plugin.module';
import {haalcentraalKvkPluginSpecification} from './plugin/haalcentraal-kvk/haalcentraal-kvk.plugin.specification';
import {CustomZaakApiPluginModule} from './plugin/customzakenapi/custom-zaak-api.plugin.module';
import {customZaakApiPluginSpecification} from './plugin/customzakenapi/custom-zaak-api.plugin.specification';
import {AccessControlManagementModule} from '@valtimo/access-control-management';
import {CustomSummaryComponent} from './custom-components/custom-summary/summary.component';
import {TaskManagementModule} from '@valtimo/task-management';
import {CaseMigrationModule} from '@valtimo/case-migration';
import {registerDocumentenApiFormioUploadComponent, ZgwModule} from '@valtimo/zgw';
import {ObjectModule} from '@valtimo/object';
import {InputModule} from 'carbon-components-angular';
import {CustomDocumentenApiPluginModule} from './plugin/customdocumentenapi/custom-documenten-api.module';
import {customDocumentenApiPluginSpecification} from './plugin/customdocumentenapi/custom-documenten-api.plugin.specification';
import {CustomValueLoggerPluginModule} from './plugin/customvaluelogger/custom-value-logger-plugin.module';
import {customValueLoggerPluginSpecification} from './plugin/customvaluelogger/custom-value-logger.plugin.specification';
import {LoggingModule} from '@valtimo/logging';
import {HaalCentraalAuthenticationPluginModule} from './plugin/haal-centraal-authentication-plugin/haal-centraal-authentication-plugin.module';
import {haalCentraalAuthenticationPluginSpecification} from './plugin/haal-centraal-authentication-plugin/haal-centraal-authentication-plugin.specification';
import {
  DocumentGeneratorPluginModule,
  documentGeneratorPluginSpecification,
  MailTemplatePluginModule,
  mailTemplatePluginSpecification,
  TextTemplatePluginModule,
  textTemplatePluginSpecification,
} from '@valtimo-plugins/freemarker';
import {
    HaalCentraalBagPluginModule
} from "./plugin/haal-centraal/bag/haal-centraal-bag-plugin.module";
import {
    haalCentraalBagPluginSpecification
} from "./plugin/haal-centraal/bag/haal-centraal-bag-plugin.specification";
import {HcApiKeyAuthenticationPluginModule} from "./plugin/haal-centraal-api-key-authentication/hc-api-key-authentication-plugin.module";
import {hcApiKeyAuthenticationPluginSpecification} from "./plugin/haal-centraal-api-key-authentication/hc-api-key-authentication-plugin.specification";
import {ObjectManagementPluginModule, objectManagementPluginSpecification} from "@valtimo-plugins/object-management";
import {ExterneKlanttaakPluginModule, externeKlanttaakPluginSpecification} from "@valtimo-plugins/externe-klanttaak";
import {DocumentSearchPluginModule, documentSearchPluginSpecification} from "@valtimo-plugins/document-search";
import {SuwinetAuthPluginModule, suwinetAuthPluginSpecification} from "@valtimo-plugins/suwinet-auth";
import {SuwinetPluginModule, suwinetPluginSpecification} from "@valtimo-plugins/suwinet";
import {SocratesPluginModule, socratesPluginSpecification} from "@valtimo-plugins/socrates";
import {ValueMapperPluginModule, valueMapperPluginSpecification} from "@valtimo-plugins/value-mapper";
import {
  HttpClientAuthenticationPluginModule,
  httpClientAuthenticationPluginSpecification
} from "@valtimo-plugins/http-client-authentication";

export function tabsFactory() {
  return new Map<string, object>([
    [DefaultTabs.summary, DossierDetailTabSummaryComponent],
    [DefaultTabs.progress, DossierDetailTabProgressComponent],
    [DefaultTabs.audit, DossierDetailTabAuditComponent],
    [DefaultTabs.documents, DossierDetailTabDocumentsComponent],
    [DefaultTabs.notes, DossierDetailTabNotesComponent],
  ]);
}

@NgModule({
  declarations: [AppComponent, CustomSummaryComponent],
  imports: [
    HttpClientModule,
    CommonModule,
    BrowserModule,
    AppRoutingModule,
    LayoutModule,
    CardModule,
    WidgetModule,
    BootstrapModule,
    ConfigModule.forRoot(environment),
    LoggerModule.forRoot(environment.logger),
    environment.authentication.module,
    SecurityModule,
    MenuModule,
    TaskModule,
    CaseMigrationModule,
    DossierModule.forRoot(tabsFactory),
    ProcessModule,
    BpmnJsDiagramModule,
    FormsModule,
    ReactiveFormsModule,
    DashboardModule,
    DocumentModule,
    AccountModule,
    ChoiceFieldModule,
    ResourceModule,
    FormModule,
    AnalyseModule,
    SwaggerModule,
    FormFlowManagementModule,
    ProcessManagementModule,
    DecisionModule,
    MilestoneModule,
    FormManagementModule,
    ProcessLinkModule,
    MigrationModule,
    DossierManagementModule,
    OpenZaakPluginModule,
    PluginManagementModule,
    SmartDocumentsPluginModule,
    ZakenApiPluginModule,
    BesluitenApiPluginModule,
    DocumentenApiPluginModule,
    CatalogiApiPluginModule,
    NotificatiesApiPluginModule,
    OpenNotificatiesPluginModule,
    PortaaltaakPluginModule,
    ObjectenApiPluginModule,
    ObjecttypenApiPluginModule,
    ObjectTokenAuthenticationPluginModule,
    ObjectManagementModule,
    ObjectModule,
    VerzoekPluginModule,
    HttpClientModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: MultiTranslateHttpLoaderFactory,
        deps: [HttpBackend, ConfigService],
      },
    }),
    FormIoModule,
    SuwinetPluginModule,
    SuwinetAuthPluginModule,
    WordpressMailPluginModule,
    HaalCentraalBrpPluginModule,
    HaalcentraalKvkPluginModule,
    CustomZaakApiPluginModule,
    CustomObjectsApiPluginModule,
    DashboardManagementModule,
    BigNumberModule,
    MeterModule,
    CaseCountDataSourceModule,
    BesluitenApiPluginModule,
    AccessControlManagementModule,
    TranslationManagementModule,
    TaskManagementModule,
    ZgwModule,
    InputModule,
    CaseCountsDataSourceModule,
    CaseGroupByDataSourceModule,
    DonutModule,
    GaugeModule,
    ConnectorManagementModule,
    CustomDocumentenApiPluginModule,
    CustomValueLoggerPluginModule,
    LoggingModule,
    HaalCentraalAuthenticationPluginModule,
    HaalCentraalBagPluginModule,
    HcApiKeyAuthenticationPluginModule,
    DocumentGeneratorPluginModule,
    MailTemplatePluginModule,
    TextTemplatePluginModule,
    ObjectManagementPluginModule,
    ExterneKlanttaakPluginModule,
    DocumentSearchPluginModule,
    SocratesPluginModule,
    ValueMapperPluginModule,
    HttpClientAuthenticationPluginModule,
  ],
  providers: [
    {
      provide: PLUGINS_TOKEN,
      useValue: [
        smartDocumentsPluginSpecification,
        documentenApiPluginSpecification,
        zakenApiPluginSpecification,
        openZaakPluginSpecification,
        catalogiApiPluginSpecification,
        objectenApiPluginSpecification,
        objecttypenApiPluginSpecification,
        objectTokenAuthenticationPluginSpecification,
        notificatiesApiPluginSpecification,
        openNotificatiesPluginSpecification,
        portaaltaakPluginSpecification,
        verzoekPluginSpecification,
        suwinetPluginSpecification,
        suwinetAuthPluginSpecification,
        wordpresMailPluginSpecification,
        haalcentraalKvkPluginSpecification,
        customZaakApiPluginSpecification,
        customObjectenApiPluginSpecification,
        wordpresMailPluginSpecification,
        besluitenApiPluginSpecification,
        customDocumentenApiPluginSpecification,
        customValueLoggerPluginSpecification,
        haalCentraalAuthenticationPluginSpecification,
        haalCentraalBagPluginSpecification,
        documentGeneratorPluginSpecification,
        mailTemplatePluginSpecification,
        textTemplatePluginSpecification,
        haalCentraalBrpPluginSpecification,
        hcApiKeyAuthenticationPluginSpecification,
        objectManagementPluginSpecification,
        externeKlanttaakPluginSpecification,
        documentSearchPluginSpecification,
        socratesPluginSpecification,
        valueMapperPluginSpecification,
        httpClientAuthenticationPluginSpecification,
      ],
    },
    {
      provide: CASE_TAB_TOKEN,
      useValue: {
        'custom-summary': CustomSummaryComponent,
      },
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
  constructor(injector: Injector) {
    registerFormioUploadComponent(injector);
    registerFormioFileSelectorComponent(injector);
    registerDocumentenApiFormioUploadComponent(injector);
    registerFormioIbanComponent(injector);
    registerFormioCurrencyComponent(injector);
  }
}
