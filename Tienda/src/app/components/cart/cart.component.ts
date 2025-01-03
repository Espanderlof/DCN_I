import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { CartItem } from '../../models/cart-item';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  total: number = 0;

  constructor(
    private cartService: CartService,
    private OrderService: OrderService,
    private Router: Router
  ) { }

  ngOnInit(): void {
    this.cartService.cart$.subscribe(cart => {
      this.cartItems = cart.items;
      this.total = cart.total;
    });
  }

  incrementQuantity(item: CartItem): void {
    if (item.quantity < item.product.stock) {
      this.cartService.updateQuantity(item.product.id, item.quantity + 1);
    }
  }

  decrementQuantity(item: CartItem): void {
    if (item.quantity > 1) {
      this.cartService.updateQuantity(item.product.id, item.quantity - 1);
    }
  }

  onQuantityChange(item: CartItem): void {
    if (item.quantity < 1) {
      item.quantity = 1;
    } else if (item.quantity > item.product.stock) {
      item.quantity = item.product.stock;
    }
    this.cartService.updateQuantity(item.product.id, item.quantity);
  }

  removeItem(productId: number): void {
    this.cartService.removeFromCart(productId);
  }

  clearCart(): void {
    this.cartService.clearCart();
  }

  checkout(): void {
    try {
      this.OrderService.createOrder().subscribe({
        next: (order) => {
          alert('Orden procesada correctamente');
          this.Router.navigate(['/orders']);
        },
        error: (error) => {
          alert('Error al procesar la orden');
        }
      });
    } catch (error) {
      alert('Error al procesar la orden');
    }
  }
}