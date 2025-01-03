import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Product } from '../../../models/product';
import { ProductService } from '../../../services/product.service';
import { AuthService } from '../../../services/auth.service';
import { ProductFormComponent } from '../product-form/product-form.component';

@Component({
  selector: 'app-product-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, ProductFormComponent],
  templateUrl: './product-admin.component.html',
  styleUrls: ['./product-admin.component.scss']
})
export class ProductAdminComponent implements OnInit {
  products: Product[] = [];
  categories: string[] = [];
  searchTerm: string = '';
  selectedCategory: string = '';
  showForm = false;
  selectedProduct: Product | null = null;

  constructor(
    private productService: ProductService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getAllProducts().subscribe(products => {
      this.products = products;
      this.categories = [...new Set(products.map(p => p.categoria))];
    });
  }

  searchProducts(): void {
    if (this.searchTerm.trim()) {
      this.productService.searchProductsByName(this.searchTerm).subscribe(products => {
        this.products = products;
      });
    } else {
      this.loadProducts();
    }
  }

  filterByCategory(): void {
    if (this.selectedCategory) {
      this.productService.getProductsByCategory(this.selectedCategory).subscribe(products => {
        this.products = products;
      });
    } else {
      this.loadProducts();
    }
  }

  deleteProduct(id: number): void {
    if (confirm('¿Está seguro de eliminar este producto?')) {
      this.productService.deleteProduct(id).subscribe(() => {
        this.products = this.products.filter(p => p.id !== id);
      });
    }
  }

  showCreateForm(): void {
    this.selectedProduct = null;
    this.showForm = true;
  }

  editProduct(product: Product): void {
    this.selectedProduct = {...product};
    this.showForm = true;
  }

  handleFormSubmit(product: Partial<Product>): void {
    if (this.selectedProduct) {
      // Actualizar producto existente
      this.productService.updateProduct(this.selectedProduct.id, {
        ...product,
        activo: true
      }).subscribe(() => {
        this.loadProducts();
        this.showForm = false;
        this.selectedProduct = null;
      });
    } else {
      // Crear nuevo producto
      this.productService.createProduct({
        ...product,
        activo: true,
        nombre: product.nombre!,
        descripcion: product.descripcion!,
        precio: product.precio!,
        stock: product.stock!,
        categoria: product.categoria!
      }).subscribe(() => {
        this.loadProducts();
        this.showForm = false;
      });
    }
  }

  handleFormCancel(): void {
    this.showForm = false;
    this.selectedProduct = null;
  }
}