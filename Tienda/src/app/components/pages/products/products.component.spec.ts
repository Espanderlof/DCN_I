import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductsComponent } from './products.component';
import { ProductService } from '../../../services/product.service';
import { CartService } from '../../../services/cart.service';
import { AuthService } from '../../../services/auth.service';
import { BehaviorSubject, of, throwError } from 'rxjs';
import { User } from '../../../models/user';
import { Product } from '../../../models/product';

describe('ProductsComponent', () => {
  let component: ProductsComponent;
  let fixture: ComponentFixture<ProductsComponent>;
  let productService: jasmine.SpyObj<ProductService>;
  let cartService: jasmine.SpyObj<CartService>;
  let authService: jasmine.SpyObj<AuthService>;
  let userSubject: BehaviorSubject<User | null>;

  const mockProducts: Product[] = [
    {
      id: 1,
      nombre: 'Producto 1',
      descripcion: 'Descripción 1',
      precio: 100,
      stock: 10,
      categoria: 'Categoría 1',
      fechaCreacion: new Date(),
      ultimaActualizacion: new Date(),
      activo: true
    },
    {
      id: 2,
      nombre: 'Producto 2',
      descripcion: 'Descripción 2',
      precio: 200,
      stock: 20,
      categoria: 'Categoría 2',
      fechaCreacion: new Date(),
      ultimaActualizacion: new Date(),
      activo: false
    }
  ];

  beforeEach(async () => {
    productService = jasmine.createSpyObj('ProductService', [
      'getAllProducts',
      'searchProductsByName',
      'getProductsByCategory'
    ]);
    cartService = jasmine.createSpyObj('CartService', ['addToCart']);
    userSubject = new BehaviorSubject<User | null>(null);
    authService = jasmine.createSpyObj('AuthService', [], {
      currentUser$: userSubject
    });

    await TestBed.configureTestingModule({
      imports: [ProductsComponent],
      providers: [
        { provide: ProductService, useValue: productService },
        { provide: CartService, useValue: cartService },
        { provide: AuthService, useValue: authService }
      ]
    }).compileComponents();

    productService.getAllProducts.and.returnValue(of(mockProducts));
    productService.searchProductsByName.and.returnValue(of(mockProducts));
    productService.getProductsByCategory.and.returnValue(of(mockProducts));

    fixture = TestBed.createComponent(ProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deberia crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('deberia cargar productos activos al inicializar', () => {
    expect(productService.getAllProducts).toHaveBeenCalled();
    expect(component.products.length).toBe(1);
    expect(component.products.every(p => p.activo)).toBe(true);
  });

  it('deberia buscar productos por nombre', () => {
    component.searchTerm = 'Producto';
    component.searchProducts();
    expect(productService.searchProductsByName).toHaveBeenCalledWith('Producto');
  });

  it('deberia filtrar productos por categoria', () => {
    component.selectedCategory = 'Categoría 1';
    component.filterByCategory();
    expect(productService.getProductsByCategory).toHaveBeenCalledWith('Categoría 1');
  });

  it('deberia agregar producto al carrito', () => {
    component.addToCart(mockProducts[0]);
    expect(cartService.addToCart).toHaveBeenCalledWith(mockProducts[0]);
  });

  it('deberia verificar si el usuario es cliente', (done) => {
    userSubject.next({ role: 'CLIENTE' } as User);
    component.isClient().subscribe(isClient => {
      expect(isClient).toBe(true);
      done();
    });
  });

  it('deberia manejar error al cargar productos', () => {
    productService.getAllProducts.and.returnValue(throwError('Error'));
    component.loadProducts();
    expect(component.error).toBe('Error al cargar los productos');
    expect(component.loading).toBe(false);
  });

  it('deberia manejar error en la búsqueda de productos', () => {
    productService.searchProductsByName.and.returnValue(throwError('Error'));
    component.searchTerm = 'test';
    component.searchProducts();
    expect(component.error).toBe('Error en la búsqueda');
  });

  it('deberia cargar todos los productos si el término de búsqueda está vacío', () => {
    component.searchTerm = '';
    component.searchProducts();
    expect(productService.getAllProducts).toHaveBeenCalled();
  });
});