# Etapa de construcción
FROM node:18 AS builder

WORKDIR /app

# Copiar archivos de configuración
COPY package*.json ./

# Instalar dependencias
RUN npm install

# Copiar todo el código fuente
COPY . .

# Construir la aplicación
RUN npm run build --prod

# Etapa de producción
FROM nginx:alpine

# Remover la configuración por defecto de nginx
RUN rm -rf /usr/share/nginx/html/*

# Copiar los archivos construidos y assets
COPY --from=builder /app/dist/tienda-web/browser/* /usr/share/nginx/html/
COPY --from=builder /app/public /usr/share/nginx/html

# Crear directorio para assets si es necesario
RUN mkdir -p /usr/share/nginx/html/assets

# Copiar la configuración de nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]