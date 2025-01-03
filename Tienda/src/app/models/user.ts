export interface User {
    id: number;
    apellido: string;
    direccion: string;
    email: string;
    fechaCreacion: Date;
    nombre: string;
    password: string;
    role: 'ADMIN' | 'CLIENTE';
    telefono?: string;
    ultimaActualizacion?: Date;
}