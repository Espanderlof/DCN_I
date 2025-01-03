import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { OrderService } from './order.service';
import { AuthService } from './auth.service';
import { CartService } from './cart.service';
import { environment } from '../../environments/environment';
import { User } from '../models/user';
import { Cart } from '../models/cart-item';

describe('OrderService', () => {
  let service: OrderService;
  let httpMock: HttpTestingController;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let cartServiceSpy: jasmine.SpyObj<CartService>;
  const apiUrl = environment.apis.ordenes.baseUrl;

  beforeEach(() => {
    const mockUser: User = {
      id: 1,
      nombre: 'Test',
      apellido: 'User',
      email: 'test@test.com',
      password: '',
      role: 'CLIENTE',
      telefono: '',
      direccion: '',
      fechaCreacion: new Date(),
      ultimaActualizacion: new Date()
    };

    const mockCart: Cart = {
      items: [
        {
          product: {
            id: 1,
            nombre: 'Product',
            descripcion: 'Description',
            precio: 100,
            stock: 10,
            categoria: 'Test',
            fechaCreacion: new Date(),
            ultimaActualizacion: new Date(),
            activo: true
          },
          quantity: 2,
          subtotal: 200
        }
      ],
      total: 200,
      userId: 1
    };

    authServiceSpy = jasmine.createSpyObj('AuthService', [], {
      currentUserValue: mockUser
    });

    cartServiceSpy = jasmine.createSpyObj('CartService', ['clearCart'], {
      currentCartValue: mockCart
    });

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        OrderService,
        { provide: AuthService, useValue: authServiceSpy },
        { provide: CartService, useValue: cartServiceSpy }
      ]
    });

    service = TestBed.inject(OrderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('creacion de ordenes', () => {
    it('deberia crear una nueva orden', () => {
      service.createOrder().subscribe();

      const req = httpMock.expectOne(`${apiUrl}/${environment.apis.ordenes.endpoints.base}`);
      expect(req.request.method).toBe('POST');
      // expect(cartServiceSpy.clearCart).toHaveBeenCalled();
    });
  });
});