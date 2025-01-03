import { TestBed } from '@angular/core/testing';
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { authGuard } from './auth.guard';
import { BehaviorSubject, Observable } from 'rxjs';
import { User } from '../models/user';

describe('authGuard', () => {
  let router: jasmine.SpyObj<Router>;
  let authService: jasmine.SpyObj<AuthService>;
  let userSubject: BehaviorSubject<User | null>;

  beforeEach(() => {
    router = jasmine.createSpyObj('Router', ['navigate']);
    userSubject = new BehaviorSubject<User | null>(null);

    authService = jasmine.createSpyObj('AuthService', [], {
      currentUser$: userSubject
    });

    TestBed.configureTestingModule({
      providers: [
        { provide: Router, useValue: router },
        { provide: AuthService, useValue: authService }
      ]
    });
  });

  it('deberia retornar true para usuarios autenticados', (done) => {
    const mockUser = {
      id: 1,
      email: 'user@test.com',
      role: 'CLIENTE'
    } as User;

    userSubject.next(mockUser);

    const guardResult = TestBed.runInInjectionContext(() => 
      authGuard({} as ActivatedRouteSnapshot, {} as RouterStateSnapshot)
    ) as Observable<boolean>;

    guardResult.subscribe(allowed => {
      expect(allowed).toBe(true);
      done();
    });
  });

  it('deberia redirigir y retornar false para usuarios no autenticados', (done) => {
    userSubject.next(null);

    const guardResult = TestBed.runInInjectionContext(() => 
      authGuard({} as ActivatedRouteSnapshot, {} as RouterStateSnapshot)
    ) as Observable<boolean>;

    guardResult.subscribe(allowed => {
      expect(allowed).toBe(false);
      expect(router.navigate).toHaveBeenCalledWith(['/login']);
      done();
    });
  });
});