🦄 Fantastic Creatures Zoo — Backend

API REST para gestionar criaturas fantásticas.
Stack: Java 17, Spring Boot 3.5.x, Maven, MySQL 8, JPA/Hibernate, Validation, Lombok.

Requisitos

JDK 17 (Temurin/Adoptium recomendado).

Maven 3.9+ (o el wrapper del repo si está).

MySQL 8+ en el puerto 3306.

IntelliJ IDEA (Community/Ultimate).

Estructura (resumen)

controller (endpoints), service (reglas de negocio), repository (JPA), model (entidades), exception (errores), resources/application.properties.

Base de datos (local)

Inicia MySQL.

Crea base y usuario:

CREATE DATABASE IF NOT EXISTS zoo_fantastico CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'zoo_user'@'localhost' IDENTIFIED BY 'TU_PASSWORD';
GRANT ALL PRIVILEGES ON zoo_fantastico.* TO 'zoo_user'@'localhost';
FLUSH PRIVILEGES;


En src/main/resources/application.properties ajusta:

spring.datasource.url=jdbc:mysql://localhost:3306/zoo_fantastico?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota

spring.datasource.username=zoo_user

spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true

ddl-auto=update es solo para desarrollo.

Cómo levantar el servidor

Opción IntelliJ: abrir el proyecto y ejecutar ZooFantasticoApplication.
Opción Maven (terminal en la raíz del repo):

Ejecutar: mvn spring-boot:run

Empaquetar: mvn -DskipTests package y luego java -jar target/zoo-fantastico-0.0.1-SNAPSHOT.jar

Si todo va bien, verás “Tomcat started on port 8080”.

Endpoints (CRUD Creature)

Base URL: http://localhost:8080/api/creatures

GET /api/creatures → lista todas

GET /api/creatures/{id} → una por id

POST /api/creatures → crea (campos: name, species, size ≥ 0, dangerLevel 1..10, healthStatus)

PUT /api/creatures/{id} → actualiza

DELETE /api/creatures/{id} → elimina
Regla de negocio: si healthStatus = "critical", no se permite borrar (responde 400).

Probar rápido

Postman: crea colección “Zoo Fantastico”, variable baseUrl = http://localhost:8080, añade los 5 requests anteriores.
PowerShell (ejemplo POST):

$body = @{ name="Fenix"; species="Ave"; size=1.2; dangerLevel=4; healthStatus="stable" } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/creatures" -ContentType "application/json" -Body $body

Tests

Ejecutar todos: mvn test

Un test específico: mvn -Dtest=CreatureServiceTest test

Flujo de trabajo con Git (sugerido)

Ramas: main (estable), develop (integración), feature/<apellido>/<tarea> (trabajo individual).

Ciclo: crear rama feature/... desde develop, trabajar, commit/push, abrir PR hacia develop, code review y merge.

Troubleshooting rápido

No conecta a MySQL: revisa usuario/contraseña/URL y que el servicio esté arriba.

Puerto 8080 ocupado: agrega server.port=8081 en application.properties o libera el 8080.

Problemas con Maven/target bloqueado: no uses OneDrive; trabaja en C:\dev\..., cierra IntelliJ y borra target/ si es necesario.

Lombok: activa Annotation Processing en IntelliJ y ten el plugin instalado.