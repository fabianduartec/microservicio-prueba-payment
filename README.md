# Microservicio - Payment Authorization

Arquitectura de **microservicios** con **Spring Boot 4.0.6**, **Redis** y **Docker Compose**:

## Descripción

Microservicio `payment-service` para recibir solicitudes de autorización de pagos, validar reglas de negocio, consultar un proveedor antifraude simulado y responder con aprobación o rechazo de la transacción.

- **payment-service** (`localhost:8080`)

**Funcionalidades **: 

## Funcionalidades

- Solicitud de pago.
- Validación de reglas de negocio.
- Consulta a proveedor antifraude mediante adapter.
- Cache con Redis.
- Consulta de autorización por `transactionId`.
- Manejo centralizado de excepciones.

## ️ Tecnologías

- Backend: Spring Boot 4.0.6 + Java 17.
- Cache: Redis.
- Infraestructura: Docker Compose + Git.
- Cliente API: Postman / cURL.

## Estructura Repositorio Git

microservicio-prueba-payment/
├── README.md
├── .gitignore
├── docker-compose.yml
├── payment-service/
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/...
│   └── src/main/resources/...


##  REQUISITOS PREVIOS

- Docker 24+           docker --version
- Docker Compose 2.20+  docker compose version  
- Git 2.30+            git --version
- Java 17	java --version


##  DESPLIEGUE COMPLETO (Git + Docker)

###  1. Clonar Repositorio
git clone https://github.com/fabianduartec/microservicio-prueba-payment.git
cd microservicio-prueba-payment

###  2. Docker Build y Despliegue Completo (60 segundos)
docker-compose up -d --build

### 3. Verificar todos UP
docker-compose ps

# Abra otra terminal - Terminal 1
docker logs -f payment-authorization-service

# Verificar Cliente - Terminal 1
docker logs payment-authorization-service 2>&1 | findstr /C:"HikariPool-1 - Start completed." /C:"Started ClienteServiceApplication" /C:"Tomcat started on port 8080 (http)" /C:"Completed initialization in"


## Endpoints

### Autorizar pago
POST /api/payments/authorize

### Consultar autorización
GET /api/payments/{transactionId}


## PRUEBAS UNITARIAS cuenta-service

### **Set de Pruebas JUnit 5, Mockito y Spring Boot Test (carpeta `src/test`)**

### 2. Ubicar el archivo
├──src\test\java
├──────	PaymentServiceTest.java

### 2. Dar clic derecho y Run PaymentServiceTest

Esperar alerta "✓ 4 tests passed" en verde

# PRUEBAS UNITARIAS Postman Collection

Importar 'payment.postman_collection.json' en Postman
Probar los metodos en orden

#  Parar todo - Limpia volúmenes DB
docker-compose down -v  
#  Parar todo - Conserva volumenes DB
docker-compose down

## Configuración de Redis

Redis se levanta automáticamente con Docker Compose y el servicio `payment-service` se conecta usando el hostname `redis`.

## Decisiones técnicas

- Se evitó usar base de datos porque la prueba no obliga a persistencia.
- Se usa almacenamiento en memoria para consultas por `transactionId`.
- Se implementó un adapter para el proveedor antifraude.
- Se agregó cache con TTL configurable.
- Se centralizó el manejo de excepciones con `@RestControllerAdvice`.

## Patrones usados

- Strategy o Chain of Responsibility para validaciones.
- Factory para respuestas.
- Adapter para el proveedor antifraude.