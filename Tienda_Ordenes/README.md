# FSIII_Tienda_Ordenes_Ar
FSIII - SUMATIVA - Arquetipo - Microservicio gestión de ordenes

# Ejecutar app Spring Boot
mvn spring-boot:run

# Levantar contenedor Docker
docker build -t tienda_ordenes_backend .
docker run --name tienda_ordenes_backend -p 8083:8083 tienda_ordenes_backend

# Ejecuta los tests con JaCoCo
mvn clean test
mvn jacoco:report

# Comando SonarQube
# Modificar por comando que da la generacion del proyecto en SonarQube
mvn clean verify sonar:sonar "-Dsonar.projectKey=FSIII_TIENDA_ORDENES" "-Dsonar.projectName=FSIII_TIENDA_ORDENES" "-Dsonar.host.url=http://localhost:9000" "-Dsonar.token=sqp_6c8085d7989a19d3b22dcb257d9808821b2968cd"

# DockerHub
1. Crear repo en https://hub.docker.com/
2. Primero, asegúrate de estar logueado en Docker Hub desde tu terminal
    docker login
3. Identifica tu imagen local. Puedes ver tus imágenes locales con:
    docker images
4. Etiqueta tu imagen local con el formato requerido por Docker Hub:
    Por ejemplo, si tu imagen local se llama "backend-app:1.0", los comandos serían:
    docker tag tienda_ordenes_backend espanderlof/fs3_tienda_ordenes_ar:latest
    docker push espanderlof/fs3_tienda_ordenes_ar:latest

# Play with Docker
1. ir a https://labs.play-with-docker.com/
2. copiar repo de dockerHub
    docker pull espanderlof/fs3_tienda_ordenes_ar:latest
3. levantar contenedor
    docker run -d --network host --name tienda_ordenes_backend espanderlof/fs3_tienda_ordenes_ar:latest
4. verificar contenedores
    docker ps