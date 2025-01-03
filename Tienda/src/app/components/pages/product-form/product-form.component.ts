import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Product } from '../../../models/product';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.scss']
})
export class ProductFormComponent implements OnInit {
  @Input() product: Partial<Product> = {
    nombre: '',
    descripcion: '',
    precio: 0,
    stock: 0,
    categoria: '',
    imagenUrl: '',
    activo: true
  };
  @Input() isEditing = false;
  @Output() submitForm = new EventEmitter<Partial<Product>>();
  @Output() cancelForm = new EventEmitter<void>();

  constructor() {}

  ngOnInit(): void {}

  onSubmit(): void {
    this.submitForm.emit({
      ...this.product,
      activo:true
    });
  }

  onCancel(): void {
    this.cancelForm.emit();
  }
}