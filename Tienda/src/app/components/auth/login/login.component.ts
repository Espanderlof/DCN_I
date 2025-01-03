import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {
  loginData = {
    email: '',
    password: ''
  };
  errorMessage = '';
  formErrors: { [key: string]: string } = {};

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

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