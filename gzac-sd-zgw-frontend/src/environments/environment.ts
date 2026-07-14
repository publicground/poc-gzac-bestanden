// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
import {NgxLoggerLevel} from 'ngx-logger';
import {authenticationKeycloak} from './auth/keycloak-config.dev';
import {customDefinitionColumns} from './definition/customDefinitionColumnsConfig';
import {menuItems} from './menu/menu';
import {defaultDefinitionColumns} from './definition/defaultDefinitionColumnsConfig';
import {UploadProvider, ValtimoConfig, DossierListTab} from '@valtimo/config';
import {LOGO_BASE_64} from './logo';

export const environment: ValtimoConfig = {
  logoSvgBase64: LOGO_BASE_64,
  applicationTitle: '',
  production: false,
  authentication: authenticationKeycloak,
  menu: {
    menuItems,
  },
  whitelistedDomains: ['localhost:4200'],
  mockApi: {
    endpointUri: window['env']['mockApiUri'] || '/mock-api/',
  },
  valtimoApi: {
    endpointUri: window['env']['apiUri'] || '/api/',
  },
  swagger: {
    endpointUri: window['env']['swaggerUri'] || '/v3/api-docs',
  },
  logger: {
    level: NgxLoggerLevel.TRACE,
  },
  definitions: {
    dossiers: [],
  },
  openZaak: {
    catalogus: window['env']['openZaakCatalogusId'] || '11a074a0-d520-44be-a87a-85c1d9aa5af9',
  },
  featureToggles: {
    showUserNameInTopBar: true,
    disableCaseCount: true,
    //enableObjectManagement: true,
    //caseListColumn: true,
    sortFilesByDate: true,
    //enableTabManagement: true,
    experimentalDmnEditing: true,
    returnToLastUrlAfterTokenExpiration: true,
    allowUserThemeSwitching: true,
    enableCompactModeToggle: true,
    enableUserNameInTopBarToggle: true,
    enableTaskPanel: true,
    enableSuppressDocumentError: true,
    enablePbacDocumentenApiDocuments: true,
  },
  visibleDossierListTabs: [DossierListTab.MINE, DossierListTab.OPEN, DossierListTab.ALL],
  caseFileSizeUploadLimitMB: 25,
  uploadProvider: UploadProvider.DOCUMENTEN_API,
  defaultDefinitionTable: defaultDefinitionColumns,
  customDefinitionTables: customDefinitionColumns,
  translationResources: ['./assets/i18n'],
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
