import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Product } from '../../../models/product';
import { CartService } from '../../../services/cart.service';
import { AuthService } from '../../../services/auth.service';
import { ProductService } from '../../../services/product.service';
import { map, Observable } from 'rxjs';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  categories: string[] = [];
  searchTerm: string = '';
  selectedCategory: string = '';
  loading: boolean = true;
  error: string | null = null;

  constructor(
    private cartService: CartService,
    private authService: AuthService,
    private productService: ProductService
  ) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.loading = true;
    this.error = null;
    this.productService.getAllProducts().subscribe({
      next: (products) => {
        this.products = products.filter(p => p.activo);
        this.categories = [...new Set(products.map(p => p.categoria))];
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Error al cargar los productos';
        this.loading = false;
        console.error('Error cargando productos:', error);
      }
    });
  }

  searchProducts(): void {
    if (this.searchTerm.trim()) {
      this.productService.searchProductsByName(this.searchTerm).subscribe({
        next: (products) => {
          this.products = products.filter(p => p.activo);
        },
        error: (error) => {
          this.error = 'Error en la búsqueda';
          console.error('Error buscando productos:', error);
        }
      });
    } else {
      this.loadProducts();
    }
  }

  filterByCategory(): void {
    if (this.selectedCategory) {
      this.productService.getProductsByCategory(this.selectedCategory).subscribe({
        next: (products) => {
          this.products = products.filter(p => p.activo);
        },
        error: (error) => {
          this.error = 'Error al filtrar por categoría';
          console.error('Error filtrando productos:', error);
        }
      });
    } else {
      this.loadProducts();
    }
  }

  addToCart(product: Product): void {
    this.cartService.addToCart(product);
  }

  isClient(): Observable<boolean> {
    return this.authService.currentUser$.pipe(
      map(user => user?.role === 'CLIENTE')
    );
  }
}