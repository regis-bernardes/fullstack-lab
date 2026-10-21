import { ApplicationConfig, provideZoneChangeDetection, APP_INITIALIZER } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { KeycloakService, KeycloakEventType } from 'keycloak-angular';

import { routes } from './app.routes';
import { authInterceptor } from './core/interceptors/auth.interceptor';

// Função para inicializar o Keycloak no start da app
function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: {
        url: 'http://localhost:8180', // Porta correta do Keycloak que está a usar
        realm: 'dbulinkedin-realm',
        clientId: 'sql-client' // Certifique-se de que este client está criado no Keycloak
      },
      initOptions: {
        onLoad: 'login-required', // Exige login logo ao abrir a app
        checkLoginIframe: false
      },
      loadUserProfileAtStartUp: true
    });
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([
      authInterceptor
    ])),
    KeycloakService,
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    }
  ]
};