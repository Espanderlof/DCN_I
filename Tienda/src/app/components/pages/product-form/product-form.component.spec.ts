import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductFormComponent } from './product-form.component';
import { FormsModule } from '@angular/forms';

describe('ProductFormComponent', () => {
  let component: ProductFormComponent;
  let fixture: ComponentFixture<ProductFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductFormComponent, FormsModule]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deberia crear el componente', () => {
    expect(component).toBeTruthy();
  });

  it('deberia emitir el producto al enviar el formulario', () => {
    spyOn(component.submitForm, 'emit');
    const mockProduct = {
      nombre: 'Test Product',
      descripcion: 'Test Description',
      precio: 100,
      stock: 10,
      categoria: 'Test Category',
      activo: true
    };
    
    component.product = mockProduct;
    component.onSubmit();
    
    expect(component.submitForm.emit).toHaveBeenCalledWith({
      ...mockProduct,
      activo: true
    });
  });

  it('deberia emitir evento al cancelar', () => {
    spyOn(component.cancelForm, 'emit');
    component.onCancel();
    expect(component.cancelForm.emit).toHaveBeenCalled();
  });
});