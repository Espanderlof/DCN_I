import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ProductService } from './product.service';
import { environment } from '../../environments/environment';

describe('ProductService', () => {
  let service: ProductService;
  let httpMock: HttpTestingController;
  const apiUrl = environment.apis.productos.baseUrl;
  const endpoints = environment.apis.productos.endpoints;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ProductService]
    });
    service = TestBed.inject(ProductService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe('manejo de errores', () => {
    it('deberia manejar errores de red', (done) => {
      service.getAllProducts().subscribe({
        error: (error) => {
          expect(error.message).toContain('Error:');
          done();
        }
      });

      const req = httpMock.expectOne(`${apiUrl}/${endpoints.base}`);
      req.error(new ErrorEvent('Network error'));
    });
  });

  describe('busqueda de productos', () => {
    it('deberia buscar productos por categoria', () => {
      const categoria = 'electronica';
      service.getProductsByCategory(categoria).subscribe();
      const req = httpMock.expectOne(`${apiUrl}/${endpoints.categoria}/${categoria}`);
      expect(req.request.method).toBe('GET');
    });

    it('deberia buscar productos por nombre', () => {
      const nombre = 'laptop';
      service.searchProductsByName(nombre).subscribe();
      const req = httpMock.expectOne(`${apiUrl}/${endpoints.buscar}?nombre=${nombre}`);
      expect(req.request.method).toBe('GET');
    });
  });

  describe('gestion de stock', () => {
    it('deberia actualizar el stock de un producto', () => {
      const productId = 1;
      const cantidad = 5;
      
      service.updateStock(productId, cantidad).subscribe();
      
      const req = httpMock.expectOne(`${apiUrl}/${endpoints.stock}/${productId}`);
      expect(req.request.method).toBe('PATCH');
      expect(req.request.body).toEqual({ cantidad });
    });
  });
});