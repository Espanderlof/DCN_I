import { Component, OnInit, OnDestroy, PLATFORM_ID, inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { AzureAuthService } from '../../../services/azure-auth.service';
import { MsalBroadcastService } from '@azure/msal-angular';
import { AuthenticationResult, EventMessage, EventType } from '@azure/msal-browser';
import { from, Observable, Subject } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  loginData = {
    email: '',
    password: ''
  };
  errorMessage = '';
  emailError = '';
  passwordError = '';
  private readonly _destroying$ = new Subject<void>();
  private readonly platformId = inject(PLATFORM_ID);

  constructor(
    private authService: AuthService,
    private azureAuthService: AzureAuthService,
    private router: Router,
    private msalBroadcastService: MsalBroadcastService
  ) { }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.msalBroadcastService.msalSubject$
        .pipe(
          filter((msg: EventMessage) => msg.eventType === EventType.LOGIN_SUCCESS),
          takeUntil(this._destroying$)
        )
        .subscribe((result: EventMessage) => {
          const payload = result.payload as AuthenticationResult;
          if (payload.idToken) {
            console.log('Login exitoso, guardando token...');
            this.azureAuthService.saveToken(payload.idToken);
          }
        });
    }
  }

  validateLogin(): boolean {
    this.emailError = '';
    this.passwordError = '';
    let isValid = true;

    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    
    if (!this.loginData.email) {
      this.emailError = 'El email es requerido';
      isValid = false;
    } else if (!emailRegex.test(this.loginData.email)) {
      this.emailError = 'El email no es válido';
      isValid = false;
    }

    if (!this.loginData.password) {
      this.passwordError = 'La contraseña es requerida';
      isValid = false;
    }

    return isValid;
  }

  login(): void {
    if (this.validateLogin()) {
      this.authService.login(this.loginData.email, this.loginData.password)
        .subscribe({
          next: (user) => {
            if (user) {
              this.router.navigate(['/products']);
            } else {
              this.errorMessage = 'Credenciales incorrectas.';
            }
          },
          error: (error) => {
            this.errorMessage = 'Error al iniciar sesión';
          }
        });
    }
  }

  getAzureProfile(): void {
    this.azureAuthService.getAzureProfile().subscribe({
      next: (profile) => {
        console.log('Perfil de Azure obtenido:', profile);
      },
      error: (error) => {
        console.error('Error al obtener perfil de Azure:', error);
      }
    });
  }

  loginWithAzure(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.azureAuthService.loginWithRedirect().subscribe({
        error: (error) => {
          console.error('Error iniciando sesión con Azure:', error);
          this.errorMessage = 'Error al iniciar sesión con Azure';
        }
      });
    }
  }

  ngOnDestroy(): void {
    this._destroying$.next(undefined);
    this._destroying$.complete();
  }
}