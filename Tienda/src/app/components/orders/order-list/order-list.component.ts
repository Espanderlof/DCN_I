import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Order } from '../../../models/order';
import { OrderService } from '../../../services/order.service';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-order-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-list.component.html',
  styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit {
  orders: Order[] = [];
  isAdmin = false;
  expandedOrderId: number | null = null;
  processingOrderId: number | null = null

  constructor(
    private OrderService: OrderService,
    private AuthService: AuthService
  ) { }

  ngOnInit(): void {
    this.AuthService.currentUser$.subscribe(user => {
      if (user) {
        this.isAdmin = user.role === 'ADMIN';
        if (this.isAdmin) {
          this.OrderService.getOrders().subscribe(orders => {
            this.orders = orders.sort((a, b) => b.id - a.id);
          });
        } else {
          this.OrderService.getUserOrders(user.id).subscribe(orders => {
            this.orders = orders.sort((a, b) => b.id - a.id);
          });
        }
      }
    });
  }

  updateStatus(orderId: number, status: 'PENDIENTE' | 'ENTREGADA' | 'CANCELADA'): void {
    this.processingOrderId = orderId; // Indicar que esta orden está siendo procesada

    this.OrderService.updateOrderStatus(orderId, status).subscribe({
      next: (updatedOrder) => {
        const index = this.orders.findIndex(o => o.id === orderId);
        if (index !== -1) {
          this.orders[index] = updatedOrder;
          this.orders = [...this.orders];
          const action = status === 'ENTREGADA' ? 'entregada' : 'cancelada';
          alert(`Orden ${action} correctamente`);
        }
        this.processingOrderId = null; // Limpiar el indicador
      },
      error: (error) => {
        console.error('Error al actualizar el estado de la orden:', error);
        alert(`Error al ${status === 'ENTREGADA' ? 'entregar' : 'cancelar'} la orden`);
        this.processingOrderId = null; // Limpiar el indicador en caso de error
      }
    });
  }

  toggleDetails(order: Order): void {
    this.expandedOrderId = this.expandedOrderId === order.id ? null : order.id;
  }

  getBadgeClass(status: string): string {
    switch (status) {
      case 'PENDIENTE':
        return 'bg-warning';
      case 'ENTREGADA':
        return 'bg-success';
      case 'CANCELADA':
        return 'bg-danger';
      default:
        return 'bg-secondary';
    }
  }

  formatDate(date: Date): string {
    return new Date(date).toLocaleString();
  }
}