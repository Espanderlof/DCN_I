export interface OrderItem {
    productId: number;
    productName: string;
    quantity: number;
    price: number;
    subtotal: number;
}

export interface Order {
    id: number;
    userId: number;
    userName: string;
    items: OrderItem[];
    total: number;
    status: 'PENDIENTE' | 'ENTREGADA' | 'CANCELADA';
    createdAt: Date;
    updatedAt: Date;
}