import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { MsalService, MsalModule } from '@azure/msal-angular';
import { PublicClientApplication, InteractionStatus } from '@azure/msal-browser';
import { msalConfig } from '../../../auth/msal.config';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, MsalModule],
  providers: [MsalService],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  private msalInstance: PublicClientApplication;

  loginData = {
    email: '',
    password: ''
  };
  errorMessage = '';
  formErrors: { [key: string]: string } = {};

  constructor(
    private authService: AuthService,
    private router: Router,
    private msalService: MsalService
  ) {
    this.msalInstance = new PublicClientApplication(msalConfig);
  }

  async ngOnInit() {
    try {
      await this.msalInstance.initialize();
      console.log('MSAL Service initialized:', this.msalInstance);
      const accounts = this.msalInstance.getAllAccounts();
      console.log('Cuentas activas:', accounts);
    } catch (error) {
      console.error('Error initializing MSAL in login component:', error);
    }
  }

  async testLogin() {
    try {
      console.log('Intentando login...');
      await this.msalInstance.initialize();
      const loginRequest = {
        scopes: ["openid", "profile"]
      };
      
      await this.msalInstance.loginRedirect(loginRequest);
    } catch (error) {
      console.error('Error en login:', error);
    }
  }

  validateForm(): boolean {
    this.formErrors = {};
    let isValid = true;

    // Validación del email
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!this.loginData.email) {
      this.formErrors['email'] = 'El email es requerido';
      isValid = false;
    } else if (!emailRegex.test(this.loginData.email)) {
      this.formErrors['email'] = 'El email no es válido';
      isValid = false;
    }

    // Validación de la contraseña
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
}