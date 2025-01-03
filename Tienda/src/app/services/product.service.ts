import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Product } from '../models/product';
import { environment } from '../../environments/environment';

@Injectable({
 providedIn: 'root'
})
export class ProductService {
 private apiUrl = environment.apis.productos.baseUrl;
 private endpoints = environment.apis.productos.endpoints;

 constructor(private http: HttpClient) {}

 private handleError(error: HttpErrorResponse) {
   let errorMessage = 'Ha ocurrido un error en el servidor';
   if (error.error instanceof ErrorEvent) {
     errorMessage = `Error: ${error.error.message}`;
   } else {
     errorMessage = `Error: ${error.status}\nMensaje: ${error.message}`;
   }
   console.error(errorMessage);
   return throwError(() => new Error(errorMessage));
 }

 getAllProducts(): Observable<Product[]> {
  console.log('API Base URL:', this.apiUrl);
   return this.http.get<Product[]>(`${this.apiUrl}/${this.endpoints.base}`)
     .pipe(catchError(this.handleError));
 }

 getProductById(id: number): Observable<Product> {
   return this.http.get<Product>(`${this.apiUrl}/${this.endpoints.base}/${id}`)
     .pipe(catchError(this.handleError));
 }

 getProductsByCategory(categoria: string): Observable<Product[]> {
   return this.http.get<Product[]>(`${this.apiUrl}/${this.endpoints.categoria}/${categoria}`)
     .pipe(catchError(this.handleError));
 }

 searchProductsByName(nombre: string): Observable<Product[]> {
   return this.http.get<Product[]>(`${this.apiUrl}/${this.endpoints.buscar}?nombre=${nombre}`)
     .pipe(catchError(this.handleError));
 }

 createProduct(product: Omit<Product, 'id' | 'fechaCreacion' | 'ultimaActualizacion'>): Observable<Product> {
   return this.http.post<Product>(`${this.apiUrl}/${this.endpoints.base}`, product)
     .pipe(catchError(this.handleError));
 }

 updateProduct(id: number, product: Partial<Product>): Observable<Product> {
   return this.http.put<Product>(`${this.apiUrl}/${this.endpoints.base}/${id}`, product)
     .pipe(catchError(this.handleError));
 }

 updateStock(id: number, cantidad: number): Observable<Product> {
   return this.http.patch<Product>(
     `${this.apiUrl}/${this.endpoints.stock}/${id}`,
     { cantidad }
   ).pipe(catchError(this.handleError));
 }

 deleteProduct(id: number): Observable<void> {
   return this.http.delete<void>(`${this.apiUrl}/${this.endpoints.base}/${id}`)
     .pipe(catchError(this.handleError));
 }
}