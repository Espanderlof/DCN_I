# FSIII_Tienda
FSIII - SUMATIVA - Angular

# Angular
ng serve

# Dockerfile
docker build -t tienda-web .
docker run --name tienda-web -p 8080:80 tienda-web

# Revisar covertura de testing
ng test --code-coverage

# Usar Sonarqube
1. Crear contenedor de SonarQube.
2. Configurar session y optener token para proyecto.
3. Intalar  scanner de Sonarqube globalmente.
    - npm install -g sonar-scanner
4. Crea un archivo en la raiz del proyecto llamado sonar-project.properties.
5. Genera el reporte de cobertura.
    - ng test --code-coverage --no-watch
6. Ejecuta el analisis
    - sonar-scanner
7. El comando completo seria.
    - sonar-scanner.bat -D"sonar.projectKey=FSIII_TIENDA" -D"sonar.sources=." -D"sonar.host.url=http://localhost:9000" -D"sonar.token=sqp_2e0880e03ea5a21159be1567afe63e82690490e5"

# DockerHub
1. Crear repo en https://hub.docker.com/
2. Primero, asegúrate de estar logueado en Docker Hub desde tu terminal
    docker login
3. Identifica tu imagen local. Puedes ver tus imágenes locales con:
    docker images
4. Etiqueta tu imagen local con el formato requerido por Docker Hub:
    Por ejemplo, si tu imagen local se llama "backend-app:1.0", los comandos serían:
    docker tag tienda-web espanderlof/fs3_tienda:latest
    docker push espanderlof/fs3_tienda:latest

# Play with Docker
1. ir a https://labs.play-with-docker.com/
2. copiar repo de dockerHub
    docker pull espanderlof/fs3_tienda:latest
3. levantar contenedor
    docker run -d --network host --name tienda-web espanderlof/fs3_tienda:latest
4. verificar contenedores
    docker ps