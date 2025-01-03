import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductAdminComponent } from './product-admin.component';
import { ProductService } from '../../../services/product.service';
import { AuthService } from '../../../services/auth.service';
import { of } from 'rxjs';
import { Product } from '../../../models/product';

describe('ProductAdminComponent', () => {
  let component: ProductAdminComponent;
  let fixture: ComponentFixture<ProductAdminComponent>;
  let productService: jasmine.SpyObj<ProductService>;
  let authService: jasmine.SpyObj<AuthService>;

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
    }
  ];

  beforeEach(async () => {
    productService = jasmine.createSpyObj('ProductService', [
      'getAllProducts',
      'searchProductsByName',
      'getProductsByCategory',
      'deleteProduct',
      'updateProduct',
      'createProduct'
    ]);
    authService = jasmine.createSpyObj('AuthService', ['currentUser$']);

    await TestBed.configureTestingModule({
      imports: [ProductAdminComponent],
      providers: [
        { provide: ProductService, useValue: productService },
        { provide: AuthService, useValue: authService }
      ]
    }).compileComponents();

    productService.getAllProducts.and.returnValue(of(mockProducts));
    productService.searchProductsByName.and.returnValue(of(mockProducts));
    productService.getProductsByCategory.and.returnValue(of(mockProducts));
    productService.deleteProduct.and.returnValue(of(void 0));
    productService.updateProduct.and.returnValue(of(mockProducts[0]));
    productService.createProduct.and.returnValue(of(mockProducts[0]));

    fixture = TestBed.createComponent(ProductAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deberia crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('deberia cargar productos al inicializar', () => {
    expect(productService.getAllProducts).toHaveBeenCalled();
    expect(component.products).toEqual(mockProducts);
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

  it('deberia eliminar un producto', () => {
    spyOn(window, 'confirm').and.returnValue(true);
    component.deleteProduct(1);
    expect(productService.deleteProduct).toHaveBeenCalledWith(1);
  });

  it('deberia mostrar formulario para crear producto', () => {
    component.showCreateForm();
    expect(component.selectedProduct).toBeNull();
    expect(component.showForm).toBe(true);
  });

  it('deberia mostrar formulario para editar producto', () => {
    component.editProduct(mockProducts[0]);
    expect(component.selectedProduct).toEqual(mockProducts[0]);
    expect(component.showForm).toBe(true);
  });

  it('deberia manejar el envío del formulario para actualizar', () => {
    const updatedProduct = { ...mockProducts[0], nombre: 'Updated' };
    component.selectedProduct = mockProducts[0];
    component.handleFormSubmit(updatedProduct);
    expect(productService.updateProduct).toHaveBeenCalled();
  });

  it('deberia manejar el envío del formulario para crear', () => {
    const newProduct = {
      nombre: 'New Product',
      descripcion: 'New Description',
      precio: 100,
      stock: 10,
      categoria: 'New Category',
      activo: true
    };
    component.handleFormSubmit(newProduct);
    expect(productService.createProduct).toHaveBeenCalled();
  });

  it('deberia manejar la cancelación del formulario', () => {
    component.handleFormCancel();
    expect(component.showForm).toBe(false);
    expect(component.selectedProduct).toBeNull();
  });
});