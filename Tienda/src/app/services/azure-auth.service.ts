import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { MsalService } from '@azure/msal-angular';
import { AuthenticationResult } from '@azure/msal-browser';
import { Observable, from, of } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AzureAuthService {
  private readonly TOKEN_KEY = 'msal_id_token';
  private readonly platformId = inject(PLATFORM_ID);

  constructor(private msalService: MsalService) {}

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
}