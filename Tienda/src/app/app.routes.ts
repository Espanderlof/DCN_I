import { Routes } from '@angular/router';
import { ProductsComponent } from './components/pages/products/products.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { ForgotPasswordComponent } from './components/auth/forgot-password/forgot-password.component';
import { ProfileComponent } from './components/auth/profile/profile.component';
import { authGuard } from './guards/auth.guard';
import { adminGuard } from './guards/admin.guard';
import { CartComponent } from './components/cart/cart.component';
import { OrderListComponent } from './components/orders/order-list/order-list.component';
import { ProductAdminComponent } from './components/pages/product-admin/product-admin.component';

export const routes: Routes = [
  { path: '', redirectTo: '/products', pathMatch: 'full' },
  { path: 'products', component: ProductsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: 'cart', component: CartComponent, canActivate: [authGuard] },
  { path: 'orders', component: OrderListComponent, canActivate: [authGuard] },
  { 
    path: 'admin/products', 
    component: ProductAdminComponent, 
    canActivate: [authGuard, adminGuard] 
  }
];