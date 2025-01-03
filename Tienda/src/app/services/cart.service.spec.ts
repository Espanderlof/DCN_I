// cart.service.spec.ts
import { TestBed } from '@angular/core/testing';
import { CartService } from './cart.service';
import { AuthService } from './auth.service';
import { BehaviorSubject } from 'rxjs';
import { Product } from '../models/product';
import { User } from '../models/user';

describe('CartService', () => {
  let service: CartService;
  let authService: jasmine.SpyObj<AuthService>;
  const mockUser: User = { 
    id: 1, 
    email: '', 
    nombre: '', 
    apellido: '', 
    role: 'CLIENTE', 
    password: '', 
    fechaCreacion: new Date(), 
    ultimaActualizacion: new Date(), 
    telefono: '', 
    direccion: '' 
  };

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

  beforeEach(() => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', [], {
      currentUser$: new BehaviorSubject<User>(mockUser)
    });

    TestBed.configureTestingModule({
      providers: [
        CartService,
        { provide: AuthService, useValue: authServiceSpy }
      ]
    });
    service = TestBed.inject(CartService);
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;

    localStorage.clear();
  });

  describe('operaciones del carrito', () => {
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

    it('deberia agregar un producto al carrito', () => {
      service.addToCart(mockProduct, 2);
      expect(service.currentCartValue.items.length).toBe(1);
      expect(service.currentCartValue.total).toBe(200);
    });
  });

  // it('deberia cargar el carrito desde el localStorage', () => {
  //   const mockProduct: Product = {
  //     id: 1,
  //     nombre: 'Test Product',
  //     descripcion: 'Test Description',
  //     precio: 100,
  //     stock: 10,
  //     categoria: 'Test',
  //     fechaCreacion: new Date(),
  //     ultimaActualizacion: new Date(),
  //     activo: true
  //   };
    
  //   const mockCart = {
  //     items: [
  //       {
  //         product: mockProduct,
  //         quantity: 2,
  //         subtotal: 200
  //       }
  //     ],
  //     total: 200,
  //     userId: mockUser.id
  //   };
  //   localStorage.setItem(`cart_${mockUser.id}`, JSON.stringify(mockCart));

  //   const service = new CartService();
  //   expect(service.currentCartValue).toEqual(mockCart);
  // });

  it('deberia encontrar un producto existente en el carrito', () => {
    service.addToCart(mockProduct, 2);
    const existingItem = service.currentCartValue.items.find(item => item.product.id === mockProduct.id);
    expect(existingItem).toBeDefined();
  });

  it('deberia remover un producto del carrito', () => {
    service.addToCart(mockProduct, 2);
    service.removeFromCart(mockProduct.id);
    expect(service.currentCartValue.items.length).toBe(0);
    expect(service.currentCartValue.total).toBe(0);
  });

  it('deberia actualizar la cantidad de un producto', () => {
    service.addToCart(mockProduct, 2);
    service.updateQuantity(mockProduct.id, 3);
    const updatedItem = service.currentCartValue.items.find(item => item.product.id === mockProduct.id);
    expect(updatedItem?.quantity).toBe(3);
    expect(updatedItem?.subtotal).toBe(300);
    expect(service.currentCartValue.total).toBe(300);
  });

  it('deberia limpiar el carrito', () => {
    service.addToCart(mockProduct, 2);
    service.clearCart();
    expect(service.currentCartValue.items.length).toBe(0);
    expect(service.currentCartValue.total).toBe(0);
    expect(service.currentCartValue.userId).toBe(mockUser.id);
  });

  // it('deberia retornar el conteo de items del carrito', done => {
  //   service.addToCart(mockProduct, 2);
  //   service.getCartItemCount().subscribe(count => {
  //     expect(count).toBe(2);
  //     done();
  //   });
  // });
});