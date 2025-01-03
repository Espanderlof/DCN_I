import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Cart, CartItem } from '../models/cart-item';
import { Product } from '../models/product';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class CartService {
  private authService = inject(AuthService);
  private cartSubject = new BehaviorSubject<Cart>({
    items: [],
    total: 0,
    userId: 0
  });
  cart$ = this.cartSubject.asObservable();

  constructor() {
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        const savedCart = localStorage.getItem(`cart_${user.id}`);
        if (savedCart) {
          this.cartSubject.next(JSON.parse(savedCart));
        } else {
          this.cartSubject.next({
            items: [],
            total: 0,
            userId: user.id
          });
        }
      }
    });
  }

  private saveCart(cart: Cart): void {
    localStorage.setItem(`cart_${cart.userId}`, JSON.stringify(cart));
    this.cartSubject.next(cart);
  }

  addToCart(product: Product, quantity: number = 1): void {
    const currentCart = this.cartSubject.value;
    const existingItem = currentCart.items.find(item => item.product.id === product.id);

    if (existingItem) {
      existingItem.quantity += quantity;
      existingItem.subtotal = existingItem.quantity * existingItem.product.precio;
    } else {
      currentCart.items.push({
        product,
        quantity,
        subtotal: product.precio * quantity
      });
    }

    currentCart.total = currentCart.items.reduce((sum, item) => sum + item.subtotal, 0);
    this.saveCart(currentCart);
  }

  removeFromCart(productId: number): void {
    const currentCart = this.cartSubject.value;
    currentCart.items = currentCart.items.filter(item => item.product.id !== productId);
    currentCart.total = currentCart.items.reduce((sum, item) => sum + item.subtotal, 0);
    this.saveCart(currentCart);
  }

  updateQuantity(productId: number, quantity: number): void {
    const currentCart = this.cartSubject.value;
    const item = currentCart.items.find(item => item.product.id === productId);

    if (item) {
      item.quantity = quantity;
      item.subtotal = item.product.precio * quantity;
      currentCart.total = currentCart.items.reduce((sum, item) => sum + item.subtotal, 0);
      this.saveCart(currentCart);
    }
  }

  clearCart(): void {
    const userId = this.cartSubject.value.userId;
    const emptyCart: Cart = {
      items: [],
      total: 0,
      userId
    };
    this.saveCart(emptyCart);
  }

  getCartItemCount(): Observable<number> {
    return new Observable<number>(observer => {
      this.cart$.subscribe(cart => {
        const count = cart.items.reduce((sum, item) => sum + item.quantity, 0);
        observer.next(count);
      });
    });
  }

  public get currentCartValue(): Cart {
    return this.cartSubject.value;
  }
}