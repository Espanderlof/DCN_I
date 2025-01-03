import { Component } from '@angular/core';
import { Routes } from '@angular/router';

@Component({
  template: ''
})
export class DummyComponent {}

export const TEST_ROUTES: Routes = [
  { path: '', component: DummyComponent },
  { path: 'login', component: DummyComponent },
  { path: 'register', component: DummyComponent },
  { path: 'products', component: DummyComponent },
  { path: 'cart', component: DummyComponent },
  { path: 'orders', component: DummyComponent }
];