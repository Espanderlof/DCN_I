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
        resetPassword: 'usuarios',
        perfilAzure: 'usuario_azure/perfil',
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
  },
  msalConfig: {
    auth: {
      clientId: '817eaf26-9869-4d26-8e71-d2713298ddd7',
      authority: 'https://DCNGP6.b2clogin.com/DCNGP6.onmicrosoft.com/B2C_1_DCNGP6_LOGIN/v2.0',
      knownAuthorities: ['DCNGP6.b2clogin.com'],
      redirectUri: 'http://localhost:4200/login',
      postLogoutRedirectUri: 'http://localhost:4200/',
      navigateToLoginRequestUrl: true
    }
  },
  apiConfig: {
    scopes: ['https://DCNGP6.onmicrosoft.com/817eaf26-9869-4d26-8e71-d2713298ddd7/access_as_user'],
    uri: 'http://localhost:8081/api/usuario_azure/perfil'
  }
};