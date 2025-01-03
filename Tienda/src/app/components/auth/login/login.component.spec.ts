import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { RouterTestingModule } from '@angular/router/testing';
import { DummyComponent, TEST_ROUTES } from '../../../testing/test-utils';
import { User } from '../../../models/user';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: Router;

  const mockUser: User = {
    id: 1,
    email: 'test@test.com',
    nombre: 'Test',
    apellido: 'User',
    password: '',
    role: 'CLIENTE',
    telefono: '123456789',
    direccion: 'Test Address',
    fechaCreacion: new Date(),
    ultimaActualizacion: new Date()
  };

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);

    await TestBed.configureTestingModule({
      declarations: [DummyComponent],
      imports: [
        LoginComponent,
        FormsModule,
        RouterTestingModule.withRoutes(TEST_ROUTES)
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    fixture.detectChanges();
  });

  it('deberia crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('deberia validar formulario correctamente', () => {
    component.loginData = {
      email: 'test@test.com',
      password: 'password123'
    };
    expect(component.validateForm()).toBe(true);
  });

  it('deberia detectar email invalido', () => {
    component.loginData = {
      email: 'invalid-email',
      password: 'password123'
    };
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['email']).toBeTruthy();
  });

  it('deberia manejar login exitoso', () => {
    authService.login.and.returnValue(of(mockUser));
    
    component.loginData = {
      email: 'test@test.com',
      password: 'password123'
    };
    
    const mockForm = { valid: true, form: {} };
    component.onSubmit(mockForm as any);

    expect(router.navigate).toHaveBeenCalledWith(['/products']);
  });

  it('deberia detectar email vacio', () => {
    component.loginData = {
      email: '',
      password: 'password123'
    };
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['email']).toBe('El email es requerido');
  });

  it('deberia detectar contraseña vacia', () => {
    component.loginData = {
      email: 'test@test.com',
      password: ''
    };
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['password']).toBe('La contraseña es requerida');
  });

  it('deberia manejar error al iniciar sesión', () => {
    authService.login.and.returnValue(throwError('Error'));
    
    component.loginData = {
      email: 'test@test.com',
      password: 'password123'
    };
    
    const mockForm = { valid: true, form: {} };
    component.onSubmit(mockForm as any);

    expect(component.errorMessage).toBe('Error al iniciar sesión');
  });

  it('deberia mostrar mensaje si el formulario no es valido', () => {
    const mockForm = { valid: false, form: {} };
    component.onSubmit(mockForm as any);

    expect(component.errorMessage).toBe('Por favor, complete todos los campos correctamente');
  });

});