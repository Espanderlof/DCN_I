import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { MsalService } from '@azure/msal-angular';
import { AuthenticationResult } from '@azure/msal-browser';
import { Observable, from, of } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AzureAuthService {
  private readonly TOKEN_KEY = 'msal_id_token';
  private readonly platformId = inject(PLATFORM_ID);

  constructor(
    private msalService: MsalService,
    private http: HttpClient
  ) {}

  saveToken(token: string): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.TOKEN_KEY, token);
    }
  }

  getToken(): string | null {
    if (isPlatformBrowser(this.platformId)) {
      return localStorage.getItem(this.TOKEN_KEY);
    }
    return null;
  }

  removeToken(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.TOKEN_KEY);
    }
  }

  isLoggedInWithAzure(): boolean {
    if (isPlatformBrowser(this.platformId)) {
      return this.msalService.instance.getAllAccounts().length > 0;
    }
    return false;
  }

  getActiveAccount() {
    if (isPlatformBrowser(this.platformId)) {
      return this.msalService.instance.getActiveAccount();
    }
    return null;
  }

  loginWithRedirect(): Observable<void> {
    if (isPlatformBrowser(this.platformId)) {
      return from(this.msalService.loginRedirect());
    }
    return of(void 0);
  }

  handleRedirectPromise(): Observable<AuthenticationResult | null> {
    if (isPlatformBrowser(this.platformId)) {
      return from(this.msalService.handleRedirectObservable());
    }
    return of(null);
  }

  logout(): Observable<boolean> {
    if (isPlatformBrowser(this.platformId)) {
      return from(this.msalService.logoutRedirect()).pipe(
        map(() => {
          this.removeToken();
          return true;
        })
      );
    }
    return of(false);
  }

  private async fetchAzureProfile(token: string | null): Promise<any> {
    if (!token) {
      throw new Error('No hay token disponible');
    }
  
    try {
      const response = await fetch(
        environment.apiConfig.uri, 
        {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }
      );
  
      if (!response.ok) {
        const errorBody = await response.text();
        console.error('Error en la respuesta:', errorBody);
        throw new Error(`HTTP error! status: ${response.status}`);
      }
  
      const data = await response.json();
      //console.log('Perfil de Azure obtenido:', data);
      return data;
    } catch (error) {
      console.error('Error al obtener perfil de Azure:', error);
      throw error;
    }
  }

  getAzureProfile(): Observable<any> {
    const token = this.getToken();
    return from(this.fetchAzureProfile(token));
  }
}