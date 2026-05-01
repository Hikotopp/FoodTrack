# 🧪 Testing & CI/CD Strategy

## Overview

Este documento describe la estrategia completa de testing automatizado y CI/CD para el proyecto Foodtrack.

## 1. Testing Strategy

### Backend Testing

#### Unit Tests
- **Ubicación**: `src/test/java/com/foodtrack/spring/springboot_application/`
- **Framework**: JUnit 5 + Mockito
- **Cobertura objetivo**: >= 80%

**Tests implementados:**
- `AuthenticationApplicationServiceTest`: Pruebas de lógica de negocio de autenticación
  - Register con email válido ✅
  - Register con email existente ✅
  - Login con credenciales correctas ✅
  - Login con credenciales incorrectas ✅
  - Profile retrieval ✅

- `AuthControllerTest`: Pruebas de endpoints
  - POST `/api/auth/register` con datos válidos ✅
  - POST `/api/auth/register` con email duplicado ✅
  - POST `/api/auth/login` exitoso ✅
  - POST `/api/auth/login` con credenciales incorrectas ✅
  - GET `/api/auth/me` autenticado ✅
  - GET `/api/auth/me` sin autenticación ✅

- `JwtServiceTest`: Pruebas de generación y validación de JWT
  - Token generation ✅
  - Token extraction ✅
  - Token expiration ✅
  - Claims validation ✅

#### Integration Tests
```bash
cd springboot-application
mvn clean test
```

**Próximamente:**
- Pruebas de integración con base de datos (TestContainers)
- Pruebas de transaccionalidad
- Pruebas end-to-end del flujo de autenticación

### Frontend Testing

#### Unit Tests
- **Ubicación**: `src/app/**/*.spec.ts`
- **Framework**: Jasmine + Karma
- **Cobertura objetivo**: >= 75%

**Tests implementados:**
- `SessionService.spec.ts`: Pruebas de gestión de sesión
  - Save session ✅
  - Get stored user ✅
  - Get token ✅
  - Check authentication status ✅
  - Check user role ✅
  - Logout ✅

- `LoginUseCase.spec.ts`: Pruebas del caso de uso de login
  - Login exitoso ✅
  - Login fallido ✅
  - Error propagation ✅
  - Admin user handling ✅
  - Session persistence ✅

- `RegisterUseCase.spec.ts`: Pruebas del caso de uso de registro
  - Register exitoso ✅
  - Email duplicado ✅
  - Validación de campos requeridos ✅
  - Validación de email ✅
  - Validación de contraseña ✅

#### E2E Tests
**Próximamente:** Playwright para flujos completos
```bash
cd foodtrack
npm run e2e
```

### Contract Tests (Pact)
**Próximamente:** Validar contrato entre frontend-backend
```bash
# Backend provider tests
mvn test -Ppact-provider

# Frontend consumer tests
npm run test:pact-consumer
```

## 2. Ejecutar Tests Localmente

### Backend
```bash
cd springboot-application

# Todos los tests
mvn clean test

# Tests específicos
mvn test -Dtest=AuthenticationApplicationServiceTest

# Con reporte de cobertura
mvn clean test jacoco:report
# Reportes en: target/site/jacoco/index.html
```

### Frontend
```bash
cd foodtrack

# Unit tests (headless)
npm run test -- --watch=false --browsers=ChromeHeadless

# Unit tests (watch mode)
npm run test

# Con cobertura
npm run test -- --code-coverage
# Reportes en: coverage/index.html
```

## 3. CI/CD Pipeline

### GitHub Actions Workflow

El archivo `.github/workflows/ci-cd-pipeline.yml` define el siguiente pipeline:

```
┌─────────────────────────────────────────────────────────┐
│                    PULL REQUEST / PUSH                   │
├─────────────────────────────────────────────────────────┤
│                                                          │
│  ┌──────────────────────┐  ┌──────────────────────┐    │
│  │  Backend Tests       │  │  Frontend Tests      │    │
│  │  (JUnit + Mockito)   │  │  (Jasmine + Karma)   │    │
│  └──────────────────────┘  └──────────────────────┘    │
│           ↓                         ↓                    │
│  ┌──────────────────────┐  ┌──────────────────────┐    │
│  │  Backend Build       │  │  Frontend Build      │    │
│  │  (Maven package)     │  │  (ng build)          │    │
│  │  Security Scan       │  │  Bundle Analysis     │    │
│  │  (OWASP Dependency)  │  │                      │    │
│  └──────────────────────┘  └──────────────────────┘    │
│           ↓                         ↓                    │
│  ┌──────────────────────────────────────────────────┐  │
│  │        Docker Build & Push to Registry           │  │
│  │        (Si la rama es main o develop)           │  │
│  └──────────────────────────────────────────────────┘  │
│                           ↓                             │
│              ┌────────────────────────┐                │
│              │   Deploy to Staging    │ (si develop)  │
│              └────────────────────────┘                │
│                    o                                    │
│              ┌────────────────────────┐                │
│              │ Deploy to Production   │ (si main)     │
│              └────────────────────────┘                │
└─────────────────────────────────────────────────────────┘
```

### Jobs del Pipeline

#### 1. **backend-tests**
- Ejecuta `mvn clean test` en el backend
- Subir reportes de test a GitHub Artifacts

#### 2. **frontend-tests**
- Ejecuta `npm test` con Karma en Chrome headless
- Recolecta cobertura con Istanbul
- Subir reportes de cobertura

#### 3. **backend-build**
- Build Maven sin tests (ya pasaron)
- Ejecuta OWASP Dependency Check
- Subir reporte de vulnerabilidades

#### 4. **frontend-build**
- Build de producción Angular
- Análisis de tamaño de bundle
- Subir artifacts de build

#### 5. **docker-build**
- Build de imágenes Docker para backend y frontend
- Push a GitHub Container Registry (GHCR)
- Solo en push a `main` o `develop`

#### 6. **deploy-staging**
- Deploy automático a ambiente staging
- Solo en push a `develop`
- Requiere configuración de secretos

#### 7. **deploy-production**
- Deploy automático a producción
- Solo en push a `main`
- Requiere aprobación manual (opcional)

#### 8. **quality-gate**
- Verifica que todos los tests pasaron
- Bloquea merge si fallan tests

## 4. Configuración Necesaria

### GitHub Secrets
Para que el pipeline CI/CD funcione, configura estos secretos en GitHub:

```
REGISTRY_USERNAME = tu-usuario-github
REGISTRY_PASSWORD = tu-token-github
STAGING_DEPLOY_KEY = tu-clave-staging
PRODUCTION_DEPLOY_KEY = tu-clave-produccion
```

### Requirements

**Backend:**
- Java 17+
- Maven 3.8+
- Docker

**Frontend:**
- Node.js 18+
- npm 9+
- Chrome (para tests headless)

## 5. Próximas Mejoras

### Testing
- [ ] Integration tests con TestContainers
- [ ] E2E tests con Playwright
- [ ] Contract tests con Pact
- [ ] Performance tests (k6 o JMeter)
- [ ] Security tests (OWASP ZAP)

### CI/CD
- [ ] Automatic changelog generation
- [ ] Semantic versioning
- [ ] Release notes automation
- [ ] Deployment notifications (Slack)
- [ ] Rollback automation

### Observabilidad
- [ ] ELK Stack para logs
- [ ] Prometheus + Grafana para métricas
- [ ] Distributed tracing (Jaeger)
- [ ] Sentry para error tracking

## 6. Referencia Rápida

### Ejecutar tests localmente
```bash
# Backend
cd springboot-application && mvn test

# Frontend
cd foodtrack && npm test

# Con cobertura
# Backend
mvn clean test jacoco:report

# Frontend
npm test -- --code-coverage
```

### Build local
```bash
# Backend
cd springboot-application && mvn clean package

# Frontend
cd foodtrack && npm run build
```

### Docker local
```bash
docker-compose -f docker-compose.yml up -d
docker-compose -f docker-compose.yml logs -f
```

---

**Last Updated**: 2026-04-30
**Status**: 🚀 Ready for Production
