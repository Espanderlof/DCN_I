import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ForgotPasswordComponent } from './forgot-password.component';
import { AuthService } from '../../../services/auth.service';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';

describe('ForgotPasswordComponent', () => {
  let component: ForgotPasswordComponent;
  let fixture: ComponentFixture<ForgotPasswordComponent>;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    authService = jasmine.createSpyObj('AuthService', ['resetPassword']);

    await TestBed.configureTestingModule({
      imports: [
        ForgotPasswordComponent, 
        FormsModule,
        RouterTestingModule
      ],
      providers: [
        { provide: AuthService, useValue: authService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ForgotPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deberia crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('deberia validar email correcto', () => {
    component.email = 'test@test.com';
    expect(component.validateForm()).toBe(true);
    expect(component.formErrors['email']).toBeUndefined();
  });

  it('deberia mostrar error con email invalido', () => {
    component.email = 'invalid-email';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['email']).toBe('El email no es válido');
  });

  it('deberia manejar reset de password exitoso', () => {
    authService.resetPassword.and.returnValue(of(true));
    component.email = 'test@test.com';
    
    const mockForm = { valid: true } as NgForm;
    component.onSubmit(mockForm);

    expect(component.isSuccess).toBe(true);
    expect(component.message).toContain('Se ha enviado un correo');
  });

  it('deberia manejar email no encontrado', () => {
    authService.resetPassword.and.returnValue(of(false));
    component.email = 'notfound@test.com';
    
    const mockForm = { valid: true } as NgForm;
    component.onSubmit(mockForm);

    expect(component.isSuccess).toBe(false);
    expect(component.message).toContain('No se encontró');
  });

  it('deberia mostrar error cuando el email esta vacio', () => {
    component.email = '';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['email']).toBe('El email es requerido');
  });

  it('deberia manejar error al procesar la solicitud', () => {
    authService.resetPassword.and.returnValue(throwError('Error'));
    component.email = 'test@test.com';
    
    const mockForm = { valid: true } as NgForm;
    component.onSubmit(mockForm);

    expect(component.isSuccess).toBe(false);
    expect(component.message).toContain('Error al procesar la solicitud');
  });

  it('deberia mostrar mensaje de email invalido cuando el formulario no es valido', () => {
    component.email = 'invalid-email';
    
    const mockForm = { valid: false } as NgForm;
    component.onSubmit(mockForm);

    expect(component.isSuccess).toBe(false);
    expect(component.message).toBe('Por favor, ingrese un email válido');
  });
});