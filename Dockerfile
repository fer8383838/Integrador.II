# Usa una imagen oficial de Java (JDK 17)
FROM eclipse-temurin:17-jdk

# Crea un directorio para tu app dentro del contenedor
WORKDIR /app

# Copia el archivo .jar de tu aplicación al contenedor
COPY target/GestionResiduos-0.0.1-SNAPSHOT.jar app.jar

# Expón el puerto 8080 para que Render lo detecte
EXPOSE 8080

# Comando que se ejecuta cuando se inicie el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]