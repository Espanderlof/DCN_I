export interface Product {
    id: number;
    activo: boolean;
    categoria: string;
    descripcion: string;
    fechaCreacion: Date;
    imagenUrl?: string;  // opcional
    nombre: string;
    precio: number;
    stock: number;
    ultimaActualizacion?: Date;  // opcional
}