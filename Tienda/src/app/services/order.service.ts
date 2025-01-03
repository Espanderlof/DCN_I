import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Order } from '../models/order';
import { CartService } from './cart.service';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private http = inject(HttpClient);
  private cartService = inject(CartService);
  private authService = inject(AuthService);

  private ordersSubject = new BehaviorSubject<Order[]>([]);
  private apiUrl = environment.apis.ordenes.baseUrl;

  constructor() {
    this.loadOrders();
  }

  private loadOrders(): void {
    const user = this.authService.currentUserValue;
    if (user) {
      if (user.role === 'ADMIN') {
        this.getOrders().subscribe(orders => this.ordersSubject.next(orders));
      } else {
        this.getUserOrders(user.id).subscribe(orders => this.ordersSubject.next(orders));
      }
    }
    console.log('API Base URL:', this.apiUrl);
  }

  createOrder(): Observable<Order> {
    const currentUser = this.authService.currentUserValue;
    const cart = this.cartService.currentCartValue;

    if (!currentUser || !cart) {
      throw new Error('No hay usuario o carrito');
    }

    const orderData = {
      userId: currentUser.id,
      userName: `${currentUser.nombre} ${currentUser.apellido}`,
      items: cart.items.map(item => ({
        productId: item.product.id,
        productName: item.product.nombre,
        quantity: item.quantity,
        price: item.product.precio,
        subtotal: item.subtotal
      })),
      total: cart.total
    };

    return this.http.post<Order>(`${this.apiUrl}/${environment.apis.ordenes.endpoints.base}`, orderData)
      .pipe(
        tap(() => {
          this.cartService.clearCart(); // Limpia el carrito después de crear la orden exitosamente
        })
      );
  }

  getOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/${environment.apis.ordenes.endpoints.base}`);
  }

  getUserOrders(userId: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/${environment.apis.ordenes.endpoints.usuario}/${userId}`);
  }

  updateOrderStatus(orderId: number, status: 'PENDIENTE' | 'ENTREGADA' | 'CANCELADA'): Observable<Order> {
    return this.http.patch<Order>(
      `${this.apiUrl}/${environment.apis.ordenes.endpoints.base}/${orderId}/status`,
      { status }
    );
  }

}