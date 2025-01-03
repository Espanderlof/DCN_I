import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { PasswordValidator } from '../../../utils/password-validator';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {
  registerData = {
    nombre: '',
    apellido: '',
    email: '',
    password: '',
    confirmPassword: '',
    direccion: '',
    telefono: ''
  };
  errorMessage = '';
  formErrors: { [key: string]: string } = {};

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  validateForm(): boolean {
    this.formErrors = {};
    let isValid = true;

    // Validación del nombre
    if (!this.registerData.nombre.trim()) {
      this.formErrors['nombre'] = 'El nombre es requerido';
      isValid = false;
    } else if (this.registerData.nombre.length < 2) {
      this.formErrors['nombre'] = 'El nombre debe tener al menos 2 caracteres';
      isValid = false;
    }

    // Validación del apellido
    if (!this.registerData.apellido.trim()) {
      this.formErrors['apellido'] = 'El apellido es requerido';
      isValid = false;
    } else if (this.registerData.apellido.length < 2) {
      this.formErrors['apellido'] = 'El apellido debe tener al menos 2 caracteres';
      isValid = false;
    }

    // Validación del email
    const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
    if (!this.registerData.email) {
      this.formErrors['email'] = 'El email es requerido';
      isValid = false;
    } else if (!emailRegex.test(this.registerData.email)) {
      this.formErrors['email'] = 'El email no es válido';
      isValid = false;
    }

    // Validación de la dirección
    if (!this.registerData.direccion.trim()) {
      this.formErrors['direccion'] = 'La dirección es requerida';
      isValid = false;
    }

    // Validación del teléfono
    const phoneRegex = /^\d{9,}$/;
    if (this.registerData.telefono && !phoneRegex.test(this.registerData.telefono)) {
      this.formErrors['telefono'] = 'El teléfono debe tener al menos 9 dígitos';
      isValid = false;
    }

    // Validación de la contraseña
    if (!this.registerData.password) {
      this.formErrors['password'] = 'La contraseña es requerida';
      isValid = false;
    } else {
      const passwordValidation = PasswordValidator.validate(this.registerData.password);
      if (!passwordValidation.isValid) {
        this.formErrors['password'] = passwordValidation.errors.join('\n');
        isValid = false;
      }
    }

    // Validación de confirmación de contraseña
    if (!this.registerData.confirmPassword) {
      this.formErrors['confirmPassword'] = 'Debe confirmar la contraseña';
      isValid = false;
    } else if (this.registerData.password !== this.registerData.confirmPassword) {
      this.formErrors['confirmPassword'] = 'Las contraseñas no coinciden';
      isValid = false;
    }

    return isValid;
  }

  onSubmit(form: NgForm): void {
    if (form.valid && this.validateForm()) {
      const { confirmPassword, ...userData } = this.registerData;
      
      this.authService.register(userData)
        .subscribe({
          next: () => {
            this.router.navigate(['/login'], { 
              queryParams: { registered: 'true' }
            });
          },
          error: (error) => {
            this.errorMessage = 'Error al registrar usuario';
          }
        });
    } else {
      this.errorMessage = 'Por favor, complete todos los campos requeridos correctamente';
    }
  }
}