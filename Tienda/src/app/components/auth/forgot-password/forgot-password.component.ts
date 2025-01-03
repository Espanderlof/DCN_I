import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent {
  email = '';
  message = '';
  isSuccess = false;
  formErrors: { [key: string]: string } = {};

  constructor(private authService: AuthService) {}

  validateForm(): boolean {
    this.formErrors = {};
    let isValid = true;

    // Validación del email
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!this.email) {
      this.formErrors['email'] = 'El email es requerido';
      isValid = false;
    } else if (!emailRegex.test(this.email)) {
      this.formErrors['email'] = 'El email no es válido';
      isValid = false;
    }

    return isValid;
  }

  onSubmit(form: NgForm): void {
    if (form.valid && this.validateForm()) {
      this.authService.resetPassword(this.email)
        .subscribe({
          next: (success) => {
            if (success) {
              this.message = 'Se ha enviado un correo con las instrucciones para restablecer tu contraseña. Tu nueva contraseña temporal es: 123456';
              this.isSuccess = true;
            } else {
              this.message = 'No se encontró ninguna cuenta con este email';
              this.isSuccess = false;
            }
          },
          error: () => {
            this.message = 'Error al procesar la solicitud';
            this.isSuccess = false;
          }
        });
    } else {
      this.message = 'Por favor, ingrese un email válido';
      this.isSuccess = false;
    }
  }
}