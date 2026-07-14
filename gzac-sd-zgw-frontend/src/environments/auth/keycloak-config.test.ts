import {Auth, AuthProviders} from '@valtimo/config';
// tslint:disable-next-line:max-line-length
import {KeycloakAuthGuardService, keycloakInitializer, KeycloakModule, KeycloakUserService, ValtimoKeycloakOptions} from '@valtimo/keycloak';
import {KeycloakConfig, KeycloakOnLoad} from 'keycloak-js';
import {Injector} from '@angular/core';

export const keycloakAuthenticationProviders: AuthProviders = {
  guardServiceProvider: KeycloakAuthGuardService,
  userServiceProvider: KeycloakUserService
};

export const keycloakConfigTest: KeycloakConfig = {
  url: window['env']['keycloakUrl'] || 'https://keycloak-cg.test.denhaag.nl/auth',
  realm: window['env']['keycloakRealm'] || 'zgw-ad',
  clientId: window['env']['keycloakClientId'] || 'sd-console'
};

export const keycloakOnLoad: KeycloakOnLoad = 'login-required';

export const keycloakInitOptions: any = {
  config: keycloakConfigTest,
  onLoad: keycloakOnLoad,
  checkLoginIframe: false,
  flow: 'standard',
  redirectUri: window['env']['keycloakRedirectUri'] || 'https://gzac-sd-zgw.test.denhaag.nl'
};

export const valtimoKeycloakOptions: ValtimoKeycloakOptions = {
  keycloakOptions: {
    config: keycloakConfigTest,
    initOptions: keycloakInitOptions,
    enableBearerInterceptor: true,
    bearerExcludedUrls: [
      '/assets'
    ]
  },
  logoutRedirectUri: window['env']['keycloakLogoutRedirectUri'] || 'https://ssv-cg.test.denhaag.nl'
};

export function initializerKeycloak(injector: Injector) {
  return keycloakInitializer(injector);
}

export const authenticationKeycloak: Auth = {
  module: KeycloakModule,
  initializer: initializerKeycloak,
  authProviders: keycloakAuthenticationProviders,
  options: valtimoKeycloakOptions
};
