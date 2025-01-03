import { TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';
import { User } from '../models/user';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  const apiUrl = environment.apis.usuarios.baseUrl;
  const endpoints = environment.apis.usuarios.endpoints;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  describe('registro', () => {
    it('deberia registrar un nuevo usuario', fakeAsync(() => {
      const mockUser = {
        email: 'test@test.com',
        password: 'Test123!',
        nombre: 'Test',
        apellido: 'User',
        telefono: '123456789',
        direccion: 'Test Address'
      };

      service.register(mockUser).subscribe(user => {
        expect(user).toBeTruthy();
        expect(user.role).toBe('CLIENTE');
      });

      const req = httpMock.expectOne(`${apiUrl}/${endpoints.registro}`);
      expect(req.request.method).toBe('POST');
      req.flush({ ...mockUser, role: 'CLIENTE', id: 1 });
    }));
  });

  describe('login', () => {
    it('deberia iniciar sesión y almacenar el usuario actual', () => {
      const mockUser: User = {
        id: 1,
        email: 'test@test.com',
        password: 'Test123!',
        nombre: 'Test',
        apellido: 'User',
        role: 'CLIENTE',
        telefono: '123456789',
        direccion: 'Test Address',
        fechaCreacion: new Date(),
        ultimaActualizacion: new Date()
      };
      
      service.login('test@test.com', 'Test123!').subscribe(user => {
        expect(user).toEqual(mockUser);
        expect(localStorage.getItem('currentUser')).toBe(JSON.stringify(mockUser));
        expect(service.currentUserValue).toEqual(mockUser);
      });

      const req = httpMock.expectOne(`${apiUrl}/${endpoints.login}`);
      expect(req.request.method).toBe('POST');
      req.flush(mockUser);
    });
  });

  describe('logout', () => {
    it('deberia cerrar sesión y eliminar el usuario actual', () => {
      localStorage.setItem('currentUser', JSON.stringify({ id: 1 }));
      service.logout();
      expect(localStorage.getItem('currentUser')).toBeNull();
      expect(service.currentUserValue).toBeNull();
    });
  });

  describe('updateProfile', () => {
    it('deberia actualizar el perfil del usuario', () => {
      const mockUser: User = {
        id: 1,
        email: 'test@test.com',
        password: 'Test123!',
        nombre: 'Test',
        apellido: 'User',
        role: 'CLIENTE',
        telefono: '123456789',
        direccion: 'Test Address',
        fechaCreacion: new Date(),
        ultimaActualizacion: new Date()
      };
      localStorage.setItem('currentUser', JSON.stringify(mockUser));
      
      const updatedUser = { ...mockUser, nombre: 'Updated', apellido: 'Name' };
      service.updateProfile(updatedUser).subscribe(user => {
        expect(user).toEqual(updatedUser);
        expect(localStorage.getItem('currentUser')).toBe(JSON.stringify(updatedUser));
        expect(service.currentUserValue).toEqual(updatedUser);
      });

      const req = httpMock.expectOne(`${apiUrl}/${endpoints.actualizar}/${mockUser.id}`);
      expect(req.request.method).toBe('PUT');
      req.flush(updatedUser);
    });
  });

  describe('resetPassword', () => {
    it('deberia restablecer la contraseña del usuario', () => {
      const mockUsers: User[] = [
        {
          id: 1,
          email: 'test@test.com',
          password: 'Test123!',
          nombre: 'Test',
          apellido: 'User',
          role: 'CLIENTE',
          telefono: '123456789',
          direccion: 'Test Address',
          fechaCreacion: new Date(),
          ultimaActualizacion: new Date()
        }
      ];
      
      service.resetPassword('test@test.com').subscribe(success => {
        expect(success).toBe(true);
      });

      const req1 = httpMock.expectOne(`${apiUrl}/${endpoints.base}`);
      expect(req1.request.method).toBe('GET');
      req1.flush(mockUsers);

      const req2 = httpMock.expectOne(`${apiUrl}/${endpoints.resetPassword}/${mockUsers[0].id}/reset-password`);
      expect(req2.request.method).toBe('PUT');
      req2.flush(null);
    });

    it('deberia manejar usuario no encontrado', () => {
      service.resetPassword('notfound@test.com').subscribe(success => {
        expect(success).toBe(false);
      });

      const req = httpMock.expectOne(`${apiUrl}/${endpoints.base}`);
      expect(req.request.method).toBe('GET');
      req.flush([]);
    });
  });

  it('deberia retornar el usuario actual', () => {
    const mockUser: User = {
      id: 1,
      email: 'test@test.com',
      password: 'Test123!',
      nombre: 'Test',
      apellido: 'User',
      role: 'CLIENTE',
      telefono: '123456789',
      direccion: 'Test Address',
      fechaCreacion: new Date(),
      ultimaActualizacion: new Date()
    };
    (service as any).currentUserSubject.next(mockUser);
    expect(service.currentUserValue).toEqual(mockUser);
  });

  // it('deberia cargar el usuario actual desde el localStorage', () => {
  //   const mockUser: User = {
  //     id: 1,
  //     email: 'test@test.com',
  //     password: 'Test123!',
  //     nombre: 'Test',
  //     apellido: 'User',
  //     role: 'CLIENTE',
  //     telefono: '123456789',
  //     direccion: 'Test Address',
  //     fechaCreacion: new Date(),
  //     ultimaActualizacion: new Date()
  //   };
  //   localStorage.setItem('currentUser', JSON.stringify(mockUser));
  //   const service = new AuthService(TestBed.inject(HttpClient), 'browser');
  //   expect(service.currentUserValue).toEqual(mockUser);
  // });

  // it('deberia manejar errores de HTTP', () => {
  //   const errorResponse = new HttpErrorResponse({
  //     error: 'Test 404 error',
  //     status: 404, statusText: 'Not Found'
  //   });
    
  //   (service as any).handleError(errorResponse).subscribe({
  //     error: (error) => {
  //       expect(error.message).toContain('Error: 404');
  //       expect(error.message).toContain('Mensaje: Test 404 error');
  //     }
  //   });
  // });

});