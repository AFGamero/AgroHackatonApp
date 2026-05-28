# Agro Trace Backend

Backend de la plataforma **AgroTrace Magdalena** - Trazabilidad agrícola, turismo rural y comercialización internacional para productores del Magdalena, Colombia.

## 📋 Prerrequisitos

- Java 21 LTS
- Docker y Docker Compose
- IntelliJ IDEA (recomendado)

## 🚀 Levantar el Proyecto

### 1. Iniciar base de datos

```bash
docker-compose up -d
```

Esto levantará:
- **PostgreSQL 17** en `localhost:5432`
- **pgAdmin 4** en `http://localhost:5050` (admin@agrotrace.com / admin)

### 2. Compilar el proyecto

```bash
./gradlew build
```

### 3. Ejecutar la aplicación

```bash
./gradlew bootRun
```

## 📡 Acceso

| Servicio | URL | Credenciales |
|----------|-----|--------------|
| **API Base** | http://localhost:8080/api/v1 | - |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | - |
| **pgAdmin** | http://localhost:5050 | admin@agrotrace.com / admin |
| **PostgreSQL** | localhost:5432 | postgres / postgres |

## 🛠️ Comandos Útiles

```bash
# Ejecutar tests
./gradlew test

# Ejecutar con perfil específico
./gradlew bootRun -Dspring-boot.run.profiles=dev

# Limpiar build
./gradlew clean

# Ver dependencias
./gradlew dependencies

# Generar JAR
./gradlew bootJar

# Detener Docker
docker-compose down
```

## 📁 Estructura del Proyecto

```
src/main/java/com/agrotrace/agrotrace/
├── config/                    # Configuraciones Spring
│   ├── OpenApiConfig.java     # Swagger UI
│   ├── MapperConfig.java      # MapStruct
│   ├── CorsConfig.java        # CORS global
│   └── AuditConfig.java       # Auditoría
├── modules/                   # Módulos de negocio
│   ├── users/                 # Sprint 1 - Usuarios y autenticación
│   ├── producers/             # Sprint 2 - Productores
│   ├── farms/                 # Sprint 3 - Fincas
│   ├── lots/                  # Sprint 4 - Lotes y trazabilidad
│   ├── evidence/              # Sprint 4 - Evidencias
│   ├── certifications/        # Sprint 5 - Certificaciones
│   ├── passports/             # Sprint 5 - Pasaporte digital
│   ├── qr/                    # Sprint 5 - Códigos QR
│   ├── tourism/               # Sprint 6 - Turismo
│   └── purchase-requests/     # Sprint 6 - Solicitudes de compra
└── shared/                    # Kernel compartido
    ├── audit/                 # Entidades auditables
    ├── exceptions/            # Manejo de errores
    └── validation/            # Validaciones
```

## 🗄️ Base de Datos

Las migraciones están en `src/main/resources/db/migration/`:

```
changelogs/
├── V1__users_schema.yaml           # Users, Sessions
├── V2__producers_schema.yaml       # Producers
├── V3__farms_schema.yaml           # Farms, FarmPhotos
├── V4__lots_schema.yaml            # Lots, CropStatusEvents
├── V5__evidence_schema.yaml        # Evidence
├── V6__certifications_schema.yaml  # Certifications
├── V7__passports_qr_schema.yaml    # DigitalPassports, QRCodes
└── V8__tourism_purchase_schema.yaml # TourismExperiences, PurchaseRequests
```

## 🔧 Stack Tecnológico

| Categoría | Tecnología |
|-----------|-----------|
| **Framework** | Spring Boot 3.4.1 |
| **Build Tool** | Gradle Kotlin DSL |
| **Java** | 21 LTS |
| **Base de Datos** | PostgreSQL 17 |
| **Migraciones** | Liquibase (YAML) |
| **ORM** | Hibernate 6.6 + JPA |
| **Utilidades** | Lombok, MapStruct |
| **Documentación** | OpenAPI 3.0 + Swagger UI |
| **Caché** | Caffeine |
| **QR** | ZXing 3.5.3 |
| **Storage** | AWS S3 SDK (o local) |
| **Resiliencia** | Resilience4j |
| **Tests** | Testcontainers, AssertJ |

## 🔐 Configuración de Seguridad (Pendiente)

La seguridad con JWT está pendiente de implementar. Cuando se agregue:

```yaml
# application-local.yml (no commitear)
app:
  jwt:
    secret: tu-secreto-muy-seguro
```

## 🌍 CORS

Orígenes permitidos (configurables en `application.yml`):

```yaml
app:
  cors:
    allowed-origins: http://localhost:5173,http://localhost:3000
```

Para producción, actualizar en `application-prod.yml`.

## 📝 Roadmap

| Sprint | Módulo | Estado |
|--------|--------|--------|
| 0 | Base técnica | ✅ Completado |
| 1 | Usuarios y autenticación | ⏳ Pendiente |
| 2 | Productores | ⏳ Pendiente |
| 3 | Fincas | ⏳ Pendiente |
| 4 | Lotes y trazabilidad | ⏳ Pendiente |
| 5 | Certificaciones, Pasaporte y QR | ⏳ Pendiente |
| 6 | Turismo y Solicitudes de Compra | ⏳ Pendiente |

## ⚙️ Configuración IntelliJ IDEA

1. **Habilitar Annotation Processing**:
   ```
   Settings → Build → Compiler → Annotation Processors
   → Marcar "Enable annotation processing"
   ```

2. **Rebuild Project**:
   ```
   Build → Rebuild Project
   ```

3. **Marcar Fuentes Generadas**:
   ```
   build/generated/sources/annotationProcessor/java/main
   → Right-click → Mark Directory as → Generated Sources Root
   ```

## 🤝 Contribución

1. Crear rama feature (`feature/nueva-funcionalidad`)
2. Commitear cambios
3. Push a la rama
4. Abrir Pull Request

## 📄 Licencia

Propietario - Equipo AgroTrace

---

**Equipo AgroTrace Magdalena** | 2026
