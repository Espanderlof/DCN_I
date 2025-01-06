import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth.service';
import { AzureAuthService } from '../../../services/azure-auth.service';
import { CartService } from '../../../services/cart.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  cartItemCount$: Observable<number>;

  constructor(
    public authService: AuthService,
    private azureAuthService: AzureAuthService,
    private cartService: CartService
  ) {
    this.cartItemCount$ = this.cartService.getCartItemCount();
  }

  logout(): void {
    // Cerrar sesión local
    this.authService.logout();
    
    // Verificar si hay sesión de Azure activa
    if (this.azureAuthService.isLoggedInWithAzure()) {
      // Cerrar sesión de Azure y limpiar tokens
      this.azureAuthService.logout().subscribe({
        next: () => {
          console.log('Sesión de Azure cerrada exitosamente');
        },
        error: (error) => {
          console.error('Error al cerrar sesión de Azure:', error);
        }
      });
    }
  }
}