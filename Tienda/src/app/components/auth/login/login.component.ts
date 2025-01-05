import { Component, OnInit, OnDestroy, PLATFORM_ID, inject } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { AzureAuthService } from '../../../services/azure-auth.service';
import { MsalBroadcastService } from '@azure/msal-angular';
import { AuthenticationResult, EventMessage, EventType, InteractionStatus } from '@azure/msal-browser';
import { Subject } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';

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
  formErrors: { [key: string]: string } = {};
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
      // Manejar eventos de inicio de sesión exitoso
      this.msalBroadcastService.msalSubject$
        .pipe(
          filter((msg: EventMessage) => msg.eventType === EventType.LOGIN_SUCCESS),
          takeUntil(this._destroying$)
        )
        .subscribe((result: EventMessage) => {
          const payload = result.payload as AuthenticationResult;
          if (payload.idToken) {
            this.azureAuthService.saveToken(payload.idToken);
            this.router.navigate(['/products']);
          }
        });

      // Detectar si ya hay una sesión activa
      if (this.azureAuthService.isLoggedInWithAzure()) {
        this.router.navigate(['/products']);
      }
    }
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

  validateForm(): boolean {
    this.formErrors = {};
    let isValid = true;

    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!this.loginData.email) {
      this.formErrors['email'] = 'El email es requerido';
      isValid = false;
    } else if (!emailRegex.test(this.loginData.email)) {
      this.formErrors['email'] = 'El email no es válido';
      isValid = false;
    }

    if (!this.loginData.password) {
      this.formErrors['password'] = 'La contraseña es requerida';
      isValid = false;
    }

    return isValid;
  }

  onSubmit(form: NgForm): void {
    if (form.valid && this.validateForm()) {
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
    } else {
      this.errorMessage = 'Por favor, complete todos los campos correctamente';
    }
  }

  ngOnDestroy(): void {
    this._destroying$.next(undefined);
    this._destroying$.complete();
  }
}