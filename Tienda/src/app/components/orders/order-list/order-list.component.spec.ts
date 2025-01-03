import { ComponentFixture, TestBed } from '@angular/core/testing';
import { OrderListComponent } from './order-list.component';
import { OrderService } from '../../../services/order.service';
import { AuthService } from '../../../services/auth.service';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { Order } from '../../../models/order';
import { User } from '../../../models/user';

describe('OrderListComponent', () => {
  let component: OrderListComponent;
  let fixture: ComponentFixture<OrderListComponent>;
  let orderService: jasmine.SpyObj<OrderService>;
  let authService: jasmine.SpyObj<AuthService>;
  let userSubject: BehaviorSubject<User | null>;

  const mockOrders: Order[] = [
    {
      id: 2,
      userId: 1,
      userName: 'Test User',
      items: [
        {
          productId: 1,
          productName: 'Product 1',
          quantity: 2,
          price: 100,
          subtotal: 200
        }
      ],
      total: 200,
      status: 'PENDIENTE',
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 1,
      userId: 1,
      userName: 'Test User',
      items: [
        {
          productId: 2,
          productName: 'Product 2',
          quantity: 1,
          price: 150,
          subtotal: 150
        }
      ],
      total: 150,
      status: 'ENTREGADA',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  const mockAdminUser: User = {
    id: 1,
    email: 'admin@test.com',
    password: '',
    nombre: 'Admin',
    apellido: 'User',
    role: 'ADMIN',
    telefono: '',
    direccion: '',
    fechaCreacion: new Date(),
    ultimaActualizacion: new Date()
  };

  const mockClientUser: User = {
    ...mockAdminUser,
    role: 'CLIENTE'
  };

  beforeEach(async () => {
    userSubject = new BehaviorSubject<User | null>(null);

    orderService = jasmine.createSpyObj('OrderService', [
      'getOrders',
      'getUserOrders',
      'updateOrderStatus'
    ]);
    orderService.getOrders.and.returnValue(of(mockOrders));
    orderService.getUserOrders.and.returnValue(of(mockOrders));
    orderService.updateOrderStatus.and.returnValue(of({ ...mockOrders[0], status: 'ENTREGADA' }));

    authService = jasmine.createSpyObj('AuthService', [], {
      currentUser$: userSubject.asObservable()
    });

    await TestBed.configureTestingModule({
      imports: [OrderListComponent],
      providers: [
        { provide: OrderService, useValue: orderService },
        { provide: AuthService, useValue: authService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(OrderListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deberia crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('deberia cargar todas las ordenes para admin', () => {
    userSubject.next(mockAdminUser);
    fixture.detectChanges();

    expect(orderService.getOrders).toHaveBeenCalled();
    expect(component.orders.length).toBe(2);
    expect(component.isAdmin).toBeTrue();
  });

  it('deberia cargar solo ordenes del usuario para cliente', () => {
    userSubject.next(mockClientUser);
    fixture.detectChanges();

    expect(orderService.getUserOrders).toHaveBeenCalledWith(mockClientUser.id);
    expect(component.orders.length).toBe(2);
    expect(component.isAdmin).toBeFalse();
  });

  it('deberia actualizar el estado de una orden', () => {
    spyOn(window, 'alert');
    userSubject.next(mockAdminUser);
    fixture.detectChanges();

    component.updateStatus(1, 'ENTREGADA');

    expect(orderService.updateOrderStatus).toHaveBeenCalledWith(1, 'ENTREGADA');
    expect(window.alert).toHaveBeenCalledWith('Orden entregada correctamente');
    expect(component.processingOrderId).toBeNull();
  });

  it('deberia manejar error al actualizar estado', () => {
    spyOn(window, 'alert');
    spyOn(console, 'error');
    orderService.updateOrderStatus.and.returnValue(
      throwError(() => new Error('Error de actualización'))
    );
    userSubject.next(mockAdminUser);
    fixture.detectChanges();

    component.updateStatus(1, 'CANCELADA');

    expect(window.alert).toHaveBeenCalledWith('Error al cancelar la orden');
    expect(console.error).toHaveBeenCalled();
    expect(component.processingOrderId).toBeNull();
  });

  it('deberia alternar los detalles de la orden', () => {
    component.toggleDetails(mockOrders[0]);
    expect(component.expandedOrderId).toBe(mockOrders[0].id);

    component.toggleDetails(mockOrders[0]);
    expect(component.expandedOrderId).toBeNull();
  });

  it('deberia retornar la clase correcta según el estado', () => {
    expect(component.getBadgeClass('PENDIENTE')).toBe('bg-warning');
    expect(component.getBadgeClass('ENTREGADA')).toBe('bg-success');
    expect(component.getBadgeClass('CANCELADA')).toBe('bg-danger');
    expect(component.getBadgeClass('OTRO')).toBe('bg-secondary');
  });

  it('deberia formatear la fecha correctamente', () => {
    const date = new Date('2024-01-01T12:00:00');
    const formattedDate = component.formatDate(date);
    expect(formattedDate).toBe(date.toLocaleString());
  });

});