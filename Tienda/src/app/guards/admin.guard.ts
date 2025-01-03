import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { map } from 'rxjs';

export const adminGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const authService = inject(AuthService);

  return authService.currentUser$.pipe(
    map(user => {
      if (user?.role === 'ADMIN') {
        return true;
      } else {
        router.navigate(['/login']);
        return false;
      }
    })
  );
};