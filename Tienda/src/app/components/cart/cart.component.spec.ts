import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CartComponent } from './cart.component';
import { CartService } from '../../services/cart.service';
import { OrderService } from '../../services/order.service';
import { RouterTestingModule } from '@angular/router/testing';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { Cart, CartItem } from '../../models/cart-item';
import { DummyComponent, TEST_ROUTES } from '../../testing/test-utils';
import { Product } from '../../models/product';
import { Order } from '../../models/order';
import { Router } from '@angular/router';

describe('CartComponent', () => {
  let component: CartComponent;
  let fixture: ComponentFixture<CartComponent>;
  let cartService: jasmine.SpyObj<CartService>;
  let orderService: jasmine.SpyObj<OrderService>;
  let router: Router;

  const mockProduct: Product = {
    id: 1,
    nombre: 'Test Product',
    descripcion: 'Test Description',
    precio: 100,
    stock: 10,
    categoria: 'Test',
    fechaCreacion: new Date(),
    ultimaActualizacion: new Date(),
    activo: true
  };

  const mockCartItem: CartItem = {
    product: mockProduct,
    quantity: 2,
    subtotal: 200
  };

  const mockCart: Cart = {
    items: [mockCartItem],
    total: 200,
    userId: 1
  };

  const mockOrder: Order = {
    id: 1,
    userId: 1,
    userName: 'Test User',
    items: [{
      productId: 1,
      productName: 'Test Product',
      quantity: 2,
      price: 100,
      subtotal: 200
    }],
    total: 200,
    status: 'PENDIENTE',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  beforeEach(async () => {
    const cartSubject = new BehaviorSubject<Cart>(mockCart);

    const cartServiceSpy = jasmine.createSpyObj('CartService', 
      ['updateQuantity', 'removeFromCart', 'clearCart'], {
      cart$: cartSubject.asObservable()
    });
    const orderServiceSpy = jasmine.createSpyObj('OrderService', ['createOrder']);

    await TestBed.configureTestingModule({
      declarations: [DummyComponent],
      imports: [
        CartComponent,
        RouterTestingModule.withRoutes(TEST_ROUTES)
      ],
      providers: [
        { provide: CartService, useValue: cartServiceSpy },
        { provide: OrderService, useValue: orderServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CartComponent);
    component = fixture.componentInstance;
    cartService = TestBed.inject(CartService) as jasmine.SpyObj<CartService>;
    orderService = TestBed.inject(OrderService) as jasmine.SpyObj<OrderService>;
    router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    fixture.detectChanges();
  });

  it('deberia crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('deberia cargar los items del carrito', () => {
    expect(component.cartItems.length).toBe(1);
    expect(component.total).toBe(200);
  });

  it('deberia incrementar la cantidad', () => {
    component.incrementQuantity(mockCartItem);
    expect(cartService.updateQuantity).toHaveBeenCalledWith(mockProduct.id, 3);
  });

  it('deberia decrementar la cantidad', () => {
    component.decrementQuantity(mockCartItem);
    expect(cartService.updateQuantity).toHaveBeenCalledWith(mockProduct.id, 1);
  });

  it('deberia remover un item', () => {
    component.removeItem(mockProduct.id);
    expect(cartService.removeFromCart).toHaveBeenCalledWith(mockProduct.id);
  });

  it('deberia limpiar el carrito', () => {
    component.clearCart();
    expect(cartService.clearCart).toHaveBeenCalled();
  });

  it('deberia procesar el checkout exitosamente', () => {
    orderService.createOrder.and.returnValue(of(mockOrder));
    spyOn(window, 'alert');
    
    component.checkout();

    expect(orderService.createOrder).toHaveBeenCalled();
    expect(window.alert).toHaveBeenCalledWith('Orden procesada correctamente');
    expect(router.navigate).toHaveBeenCalledWith(['/orders']);
  });

  it('deberia manejar error en el checkout', () => {
    orderService.createOrder.and.throwError('Error');
    spyOn(window, 'alert');
    
    component.checkout();

    expect(window.alert).toHaveBeenCalledWith('Error al procesar la orden');
  });

  it('deberia limitar la cantidad al stock disponible', () => {
    const itemWithMaxStock = {...mockCartItem, quantity: mockProduct.stock};
    component.incrementQuantity(itemWithMaxStock);
    expect(cartService.updateQuantity).not.toHaveBeenCalled();
  });

  it('deberia no permitir cantidades menores a 1', () => {
    const itemWithMinQuantity = {...mockCartItem, quantity: 1};
    component.decrementQuantity(itemWithMinQuantity);
    expect(cartService.updateQuantity).not.toHaveBeenCalled();
  });

  it('deberia ajustar la cantidad a 1 si es menor a 1', () => {
    const itemWithInvalidQuantity = {...mockCartItem, quantity: 0};
    component.onQuantityChange(itemWithInvalidQuantity);
    expect(itemWithInvalidQuantity.quantity).toBe(1);
    expect(cartService.updateQuantity).toHaveBeenCalledWith(mockProduct.id, 1);
  });

  it('deberia ajustar la cantidad al stock si es mayor al stock', () => {
    const itemWithExceededQuantity = {...mockCartItem, quantity: mockProduct.stock + 1};
    component.onQuantityChange(itemWithExceededQuantity);
    expect(itemWithExceededQuantity.quantity).toBe(mockProduct.stock);
    expect(cartService.updateQuantity).toHaveBeenCalledWith(mockProduct.id, mockProduct.stock);
  });

  it('deberia mostrar alerta cuando falla el checkout', () => {
    orderService.createOrder.and.returnValue(throwError('Error'));
    spyOn(window, 'alert');
    
    component.checkout();

    expect(window.alert).toHaveBeenCalledWith('Error al procesar la orden');
  });
});