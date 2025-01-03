import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError, of } from 'rxjs';
import { catchError, map, tap, mergeMap } from 'rxjs/operators';
import { User } from '../models/user';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  private apiUrl = environment.apis.usuarios.baseUrl;
  private endpoints = environment.apis.usuarios.endpoints;

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    if (isPlatformBrowser(this.platformId)) {
      const storedUser = localStorage.getItem('currentUser');
      if (storedUser) {
        this.currentUserSubject.next(JSON.parse(storedUser));
      }
    }
    console.log('API Base URL:', this.apiUrl);
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ha ocurrido un error en el servidor';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = `Error: ${error.status}\nMensaje: ${error.message}`;
    }
    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }

  register(user: Omit<User, 'id' | 'fechaCreacion' | 'ultimaActualizacion' | 'role'>): Observable<User> {
    const url = `${this.apiUrl}/${this.endpoints.registro}`;
    return this.http.post<User>(url, { ...user, role: 'CLIENTE' })
      .pipe(catchError(this.handleError));
  }

  login(email: string, password: string): Observable<User> {
    const url = `${this.apiUrl}/${this.endpoints.login}`;
    return this.http.post<User>(url, { email, password }).pipe(
      tap(user => {
        if (isPlatformBrowser(this.platformId)) {
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);
        }
      }),
      catchError(this.handleError)
    );
  }

  logout(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('currentUser');
      this.currentUserSubject.next(null);
    }
  }

  updateProfile(user: User): Observable<User> {
    const url = `${this.apiUrl}/${this.endpoints.actualizar}/${user.id}`;
    return this.http.put<User>(url, {
      nombre: user.nombre,
      apellido: user.apellido,
      telefono: user.telefono,
      direccion: user.direccion
    }).pipe(
      tap(updatedUser => {
        if (isPlatformBrowser(this.platformId)) {
          const currentUser = { ...this.currentUserValue, ...updatedUser };
          localStorage.setItem('currentUser', JSON.stringify(currentUser));
          this.currentUserSubject.next(currentUser);
        }
      }),
      catchError(this.handleError)
    );
  }

  resetPassword(email: string): Observable<boolean> {
    const defaultPassword = '123456';
    const url = `${this.apiUrl}/${this.endpoints.base}`;
    
    return this.http.get<User[]>(url).pipe(
      mergeMap((users: User[]) => {
        const user = users.find((u: User) => u.email === email);
        if (!user) {
          return of(false);
        }
        
        const resetUrl = `${this.apiUrl}/${this.endpoints.resetPassword}/${user.id}/reset-password`;
        return this.http.put<void>(resetUrl, { newPassword: defaultPassword }).pipe(
          map(() => true),
          catchError(() => of(false))
        );
      }),
      catchError(() => of(false))
    );
  }

  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }
}