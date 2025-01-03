export const environment = {
    production: false,
    apis: {
        usuarios: {
            baseUrl: 'http://localhost:8081/api',
            endpoints: {
                base: 'usuarios',
                login: 'usuarios/login',
                registro: 'usuarios',
                actualizar: 'usuarios',
                eliminar: 'usuarios',
                obtenerTodos: 'usuarios',
                obtenerPorId: 'usuarios',
                resetPassword: 'usuarios'
            }
        },
        productos: {
            baseUrl: 'http://localhost:8082/api',
            endpoints: {
                base: 'productos',
                categoria: 'productos/categoria',
                buscar: 'productos/buscar',
                stock: 'productos/stock'
            }
        },
        ordenes: {
            baseUrl: 'http://localhost:8083/api',
            endpoints: {
                base: 'orders',
                usuario: 'orders/user',
                estado: 'orders/status'
            }
        }
    }
};