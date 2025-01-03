import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfileComponent } from './profile.component';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { User } from '../../../models/user';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;
  let userSubject: BehaviorSubject<User | null>;

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
    userSubject = new BehaviorSubject<User | null>(mockUser);
    authService = jasmine.createSpyObj('AuthService', ['updateProfile'], {
      currentUser$: userSubject
    });
    router = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        ProfileComponent, 
        FormsModule,
        RouterTestingModule
      ],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deberia crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('deberia cargar datos del usuario', () => {
    expect(component.user).toEqual(mockUser);
    expect(component.editableUser).toEqual(mockUser);
  });

  it('deberia redirigir si no hay usuario', () => {
    userSubject.next(null);
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('deberia manejar actualizacion exitosa', () => {
    authService.updateProfile.and.returnValue(of(mockUser));
    component.editMode = true;
    component.onSubmit();
    
    expect(component.successMessage).toBeTruthy();
    expect(component.errorMessage).toBe('');
  });

  it('deberia alternar el modo de edicion', () => {
    component.toggleEditMode();
    expect(component.editMode).toBe(true);
    expect(component.successMessage).toBe('');
    expect(component.errorMessage).toBe('');

    component.toggleEditMode();
    expect(component.editMode).toBe(false);
    expect(component.editableUser).toEqual(mockUser);
  });

  it('deberia manejar error al actualizar perfil', () => {
    authService.updateProfile.and.returnValue(throwError('Error'));
    component.onSubmit();
    
    expect(component.errorMessage).toBe('Error al actualizar el perfil');
    expect(component.successMessage).toBe('');
  });
});