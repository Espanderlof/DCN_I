import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { RouterTestingModule } from '@angular/router/testing';
import { DummyComponent, TEST_ROUTES } from '../../../testing/test-utils';
import { User } from '../../../models/user';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
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
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['register']);

    await TestBed.configureTestingModule({
      declarations: [DummyComponent],
      imports: [
        RegisterComponent,
        FormsModule,
        RouterTestingModule.withRoutes(TEST_ROUTES)
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    fixture.detectChanges();
  });

  it('deberia crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('deberia validar formulario completo', () => {
    component.registerData = {
      nombre: 'Test',
      apellido: 'User',
      email: 'test@test.com',
      password: 'Test123!@',
      confirmPassword: 'Test123!@',
      direccion: 'Test Address',
      telefono: '123456789'
    };
    
    expect(component.validateForm()).toBe(true);
  });

  it('deberia detectar contraseñas que no coinciden', () => {
    component.registerData = {
      nombre: 'Test',
      apellido: 'User',
      email: 'test@test.com',
      password: 'Test123!@',
      confirmPassword: 'Different123!@',
      direccion: 'Test Address',
      telefono: '123456789'
    };
    
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['confirmPassword']).toBe('Las contraseñas no coinciden');
  });

  it('deberia manejar registro exitoso', () => {
    authService.register.and.returnValue(of({ id: 1 } as User));
    
    component.registerData = {
      nombre: 'Test',
      apellido: 'User',
      email: 'test@test.com',
      password: 'Test123!@',
      confirmPassword: 'Test123!@',
      direccion: 'Test Address',
      telefono: '123456789'
    };
    
    const mockForm = { valid: true, form: {} };
    component.onSubmit(mockForm as any);

    expect(router.navigate).toHaveBeenCalledWith(['/login'], { 
      queryParams: { registered: 'true' }
    });
  });

  it('deberia detectar nombre vacio', () => {
    component.registerData.nombre = '';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['nombre']).toBe('El nombre es requerido');
  });

  it('deberia detectar nombre demasiado corto', () => {
    component.registerData.nombre = 'A';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['nombre']).toBe('El nombre debe tener al menos 2 caracteres');
  });

  it('deberia detectar apellido vacio', () => {
    component.registerData.apellido = '';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['apellido']).toBe('El apellido es requerido');
  });

  it('deberia detectar apellido demasiado corto', () => {
    component.registerData.apellido = 'B';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['apellido']).toBe('El apellido debe tener al menos 2 caracteres');
  });

  it('deberia detectar email vacio', () => {
    component.registerData.email = '';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['email']).toBe('El email es requerido');
  });

  it('deberia detectar email invalido', () => {
    component.registerData.email = 'invalid-email';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['email']).toBe('El email no es válido');
  });

  it('deberia detectar direccion vacia', () => {
    component.registerData.direccion = '';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['direccion']).toBe('La dirección es requerida');
  });

  it('deberia detectar telefono con formato incorrecto', () => {
    component.registerData.telefono = '123';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['telefono']).toBe('El teléfono debe tener al menos 9 dígitos');
  });

  it('deberia detectar contraseña vacia', () => {
    component.registerData.password = '';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['password']).toBe('La contraseña es requerida');
  });

  it('deberia detectar contraseña invalida', () => {
    component.registerData.password = 'weak';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['password']).toContain('La contraseña debe tener al menos 8 caracteres');
  });

  it('deberia detectar confirmacion de contraseña vacia', () => {
    component.registerData.confirmPassword = '';
    expect(component.validateForm()).toBe(false);
    expect(component.formErrors['confirmPassword']).toBe('Debe confirmar la contraseña');
  });

  it('deberia manejar error al registrar usuario', () => {
    authService.register.and.returnValue(throwError('Error'));
    
    component.registerData = {
      nombre: 'Test',
      apellido: 'User',
      email: 'test@test.com',
      password: 'Test123!@',
      confirmPassword: 'Test123!@',
      direccion: 'Test Address',
      telefono: '123456789'
    };
    
    const mockForm = { valid: true, form: {} };
    component.onSubmit(mockForm as any);

    expect(component.errorMessage).toBe('Error al registrar usuario');
  });

  it('deberia mostrar mensaje si el formulario no es valido', () => {
    const mockForm = { valid: false, form: {} };
    component.onSubmit(mockForm as any);

    expect(component.errorMessage).toBe('Por favor, complete todos los campos requeridos correctamente');
  });
});