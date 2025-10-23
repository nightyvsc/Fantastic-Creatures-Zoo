# 🦄 Fantastic Creatures Zoo — Backend (Guía del Proyecto)

API REST para gestionar **criaturas fantásticas**. Proyecto académico de *Desarrollo de Software* construido con **Java 17 + Spring Boot 3.5.x + Maven + MySQL + JPA/Hibernate + Validation + Lombok**.

---

## 🎯 Alcance del laboratorio
- CRUD completo de **Creature**: crear, listar, obtener por id, actualizar y eliminar.
- **Regla de negocio**: **NO** se puede eliminar una criatura si `healthStatus = "critical"` → responde **400 Bad Request**.
- Validaciones de datos de entrada (Bean Validation).
- Manejo de errores consistente (404, 400) con **GlobalExceptionHandler**.
- **Pruebas manuales** con **Postman** (lo principal para el informe).

---

## 🧩 Arquitectura del código (capas y clases)
**Paquete base**: `com.example.zoo_fantastico`

- **model/**
  - `Creature` → Entidad JPA con validaciones.
  - `Zone` → Entidad JPA con relación a criaturas.
- **repository/**
  - `CreatureRepository` → `JpaRepository<Creature, Long>` (CRUD básico).
  - `ZoneRepository` → `JpaRepository<Zone, Long>` (CRUD básico).
- **service/**
  - `CreatureService` → Lógica de negocio (incluye la regla de borrado “critical”).  
  - `ZoneService` → Lógica de negocio (incluye la regla de zonas con criaturas).
- **controller/**
  - `CreatureController` → Endpoints REST bajo `/api/creatures`.
  - `ZoneController` → Endpoints REST bajo `/api/zones`.
- **exception/**
  - `ResourceNotFoundException` → para 404.  
  - `GlobalExceptionHandler` → traduce excepciones en respuestas claras (400/404).

> Con estas capas mantenemos separación de responsabilidades y un flujo claro: **Controller → Service → Repository**.

---

## ✅ Requisitos
- **JDK 17** (Temurin/Adoptium recomendado).
- **Maven 3.9+** (o wrapper si el repo lo incluye).
- **MySQL 8+** activo en `localhost:3306`.
- **IntelliJ IDEA** (Community o Ultimate).
- **Postman** (para las pruebas del informe).

---

## ⚙️ Instalación y configuración

### 1) Clonar el repositorio
```bash
git clone https://github.com/nightyvsc/Fantastic-Creatures-Zoo.git
cd Fantastic-Creatures-Zoo
```

### 2) Base de datos (MySQL)
Ejecuta en tu servidor MySQL (CLI o Workbench):
```sql
CREATE DATABASE IF NOT EXISTS zoo_fantastico CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'zoo_user'@'localhost' IDENTIFIED BY 'TU_PASSWORD';
GRANT ALL PRIVILEGES ON zoo_fantastico.* TO 'zoo_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3) Configuración de la app
Edita `src/main/resources/application.properties` (ajusta usuario/clave):
```properties
spring.application.name=zoo-fantastico

# --- MySQL ---
spring.datasource.url=jdbc:mysql://localhost:3306/zoo_fantastico?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Bogota
spring.datasource.username=zoo_user
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# --- JPA/Hibernate (desarrollo) ---
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> `ddl-auto=update` crea/actualiza tablas automáticamente **solo para desarrollo**.

---

## ▶️ Cómo levantar el servidor

**Opción A — IntelliJ**  
1. Abrir el proyecto y esperar la importación de Maven.  
2. Ejecutar la clase `ZooFantasticoApplication`.  
3. En consola debes ver: `Tomcat started on port 8080`.

**Opción B — Maven CLI**  
```bash
mvn spring-boot:run
# o empaquetar y ejecutar:
mvn -DskipTests package
java -jar target/zoo-fantastico-0.0.1-SNAPSHOT.jar
```

La API quedará disponible en: **http://localhost:8080**

---

## 🌐 API REST — Creature

**Base path:** `/api/creatures`

- **POST** `/api/creatures` → **201 Created**  
  Crea una criatura. *Body ejemplo*:
  ```json
  { "name":"Fenix", "species":"Ave", "size":1.2, "dangerLevel":4, "healthStatus":"stable" }
  ```

- **GET** `/api/creatures` → **200 OK**  
  Lista todas las criaturas.

- **GET** `/api/creatures/{id}` → **200 OK** | **404 Not Found**  
  Retorna una criatura por id.

- **PUT** `/api/creatures/{id}` → **200 OK** | **404 Not Found**  
  Actualiza una criatura. *Body ejemplo*:
  ```json
  { "name":"Fenix", "species":"Ave", "size":1.8, "dangerLevel":5, "healthStatus":"stable" }
  ```

- **DELETE** `/api/creatures/{id}` → **204 No Content**  
  **Regla**: si `healthStatus = "critical"` → **400 Bad Request** (no se permite eliminar).

### Validaciones relevantes (Bean Validation)
- `name`, `species`, `healthStatus` → no vacíos.
- `size` → `>= 0`.
- `dangerLevel` → entre `1` y `10`.

### Errores manejados
- **400 Bad Request** → JSON mal formado o validaciones incumplidas (detalle en el body).  
- **404 Not Found** → recurso inexistente.  
- **400 en DELETE** → regla de negocio (estado “critical”).

---

## 🧪 Pruebas con Postman (lo usado en el lab)

### A) Preparar entorno
1. Crear **Environment** → nombre: `Local`.
2. Variable: `baseUrl = http://localhost:8080` (selecciónala arriba a la derecha).

### B) Colección “Zoo Fantastico” (requests principales)

1. **Listar**  
   - GET `{{baseUrl}}/api/creatures`  
   - Tests (opcional):
     ```js
     pm.test("200 OK", () => pm.response.to.have.status(200));
     pm.test("Es un array", () => Array.isArray(pm.response.json()));
     ```

2. **Crear (válido)**  
   - POST `{{baseUrl}}/api/creatures`  
   - Body (raw → JSON):
     ```json
     { "name":"Fenix", "species":"Ave", "size":1.2, "dangerLevel":4, "healthStatus":"stable" }
     ```
   - Tests (guardar id para siguientes llamadas):
     ```js
     pm.test("201 Created", () => pm.response.to.have.status(201));
     pm.collectionVariables.set("creatureId", pm.response.json().id);
     ```

3. **Obtener por id**  
   - GET `{{baseUrl}}/api/creatures/{{creatureId}}`  
   - Tests:
     ```js
     pm.test("200 OK", () => pm.response.to.have.status(200));
     ```

4. **Actualizar**  
   - PUT `{{baseUrl}}/api/creatures/{{creatureId}}`  
   - Body:
     ```json
     { "name":"Fenix", "species":"Ave", "size":1.8, "dangerLevel":5, "healthStatus":"stable" }
     ```
   - Tests:
     ```js
     pm.test("200 OK", () => pm.response.to.have.status(200));
     ```

5. **Eliminar (caso normal)**  
   - DELETE `{{baseUrl}}/api/creatures/{{creatureId}}`  
   - Tests:
     ```js
     pm.test("204 No Content", () => pm.response.to.have.status(204));
     ```

6. **Casos negativos (para el informe)**
   - POST con `dangerLevel = 11` → **400**.  
   - POST con `size = -1` → **400**.  
   - DELETE de criatura con `healthStatus = "critical"` → **400**.  
     - Para esto: crea antes una criatura con `critical`, guarda su id como `criticalId` y prueba delete:
       ```js
       pm.collectionVariables.set("criticalId", pm.response.json().id);
       ```

---

## 🔎 🧪 Pruebas automatizadas con JUnit y Mockito

Además de Postman, el proyecto cuenta con **tests unitarios** para los servicios principales:

### 📂 Ubicación de las clases de prueba
```
src/test/java/com/example/zoo_fantastico/service/CreatureServiceTest.java
src/test/java/com/example/zoo_fantastico/service/ZoneServiceTest.java
```

### ✔️ CreatureServiceTest
- `findById` retorna criatura cuando existe, lanza `ResourceNotFoundException` si no.  
- `create` guarda y retorna criatura.  
- `findAll` devuelve lista completa.  
- `update` copia campos nuevos y guarda; lanza excepción si no existe.  
- `delete` elimina criatura solo si `healthStatus != "critical"`; lanza `IllegalStateException` en caso contrario.

### ✔️ ZoneServiceTest
- `findById` retorna zona o lanza `ResourceNotFoundException`.  
- `create` guarda y retorna zona.  
- `findAll` devuelve todas las zonas.  
- `update` modifica atributos (`name`, `zoneType`, `areaMeters`) y guarda.  
- `delete` elimina zona solo si no tiene criaturas asignadas; lanza `IllegalStateException` si la lista no está vacía.

### ▶️ Ejecutar las pruebas
```bash
mvn test
```
En IntelliJ también se puede: clic derecho sobre la clase → **Run 'CreatureServiceTest'** o **Run 'ZoneServiceTest'**.

---

## 🔁 Flujo de trabajo con Git (resumen del equipo)
- Ramas: `main` (estable), `develop` (integración), `feature/<apellido>/<tarea>` (trabajo individual).  
- Ciclo: crear rama desde `develop` → implementar → `commit/push` → **Pull Request a `develop`** → revisión y merge.

---

## 🛟 Troubleshooting rápido
- **No conecta a MySQL**: verifica servicio activo, credenciales y URL en `application.properties`.  
- **Puerto 8080 ocupado**: agrega `server.port=8081` en `application.properties` o libera el puerto.  
- **Errores con OneDrive/`target` bloqueado**: usa una ruta local (ej. `C:\dev\...`), cierra IntelliJ y borra `target/`.  
- **Lombok**: instala el plugin y habilita *Annotation Processing* en IntelliJ.  
- **cURL en PowerShell**: usa Postman o `Invoke-RestMethod` para evitar problemas de comillas/escape.

---

## 👥 Autores
- Juan Manuel Diaz · Santiago Beltran · Sebastián Perez · Sebastián Basto  

---
