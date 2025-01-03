# FSIII_Tienda_Productos_Ar
FSIII - SUMATIVA - Arquetipo - Microservicio gestión de productos

# Ejecutar app Spring Boot
mvn spring-boot:run

# Levantar contenedor Docker
docker build -t tienda_productos_backend .
docker run --name tienda_productos_backend -p 8082:8082 tienda_productos_backend

# Patrones de diseño
- Builder, para la clase Producto. Ya que esto nos ayudara a mantener los atributos y mejorara la legibilidad y mantenibilidad.
- Singleton, para manejar el cache de los productos mas consultados.

# Ejecuta los tests con JaCoCo
mvn clean test
mvn jacoco:report

# Comando SonarQube
# Modificar por comando que da la generacion del proyecto en SonarQube
mvn clean verify sonar:sonar "-Dsonar.projectKey=FSIII_TIENDA_PRODUCTOS" "-Dsonar.projectName=FSIII_TIENDA_PRODUCTOS" "-Dsonar.host.url=http://localhost:9000" "-Dsonar.token=sqp_caa3446f3046fe5a2292c2537c3e098f015fb110"

# DockerHub
1. Crear repo en https://hub.docker.com/
2. Primero, asegúrate de estar logueado en Docker Hub desde tu terminal
    docker login
3. Identifica tu imagen local. Puedes ver tus imágenes locales con:
    docker images
4. Etiqueta tu imagen local con el formato requerido por Docker Hub:
    Por ejemplo, si tu imagen local se llama "backend-app:1.0", los comandos serían:
    docker tag tienda_productos_backend espanderlof/fs3_tienda_productos_ar:latest
    docker push espanderlof/fs3_tienda_productos_ar:latest

# Play with Docker
1. ir a https://labs.play-with-docker.com/
2. copiar repo de dockerHub
    docker pull espanderlof/fs3_tienda_productos_ar:latest
3. levantar contenedor
    docker run -d --network host --name tienda_productos_backend espanderlof/fs3_tienda_productos_ar:latest
4. verificar contenedores
    docker ps