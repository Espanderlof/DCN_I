import { Product } from './product';

export interface CartItem {
    product: Product;
    quantity: number;
    subtotal: number;
}

export interface Cart {
    items: CartItem[];
    total: number;
    userId: number;
}