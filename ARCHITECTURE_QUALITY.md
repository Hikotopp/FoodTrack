# Calidad arquitectonica FoodTrack

Este documento resume como el proyecto aplica 12-Factor App y SOLID, y deja reglas concretas para mantener ese nivel al agregar nuevas funcionalidades.

## 12-Factor App

| Factor | Estado | Evidencia en el proyecto |
| --- | --- | --- |
| 1. Codigo base | Cumple | Un repositorio Git versionado y sincronizado con `origin/main`. |
| 2. Dependencias | Cumple | Backend en `springboot-application/pom.xml`; frontend en `foodtrack/package.json` y `package-lock.json`. |
| 3. Configuracion | Cumple | Secretos y valores variables salen por env vars: `DB_URL`, `JWT_SECRET`, `APP_CORS_ALLOWED_ORIGINS`, `APP_SEED_ENABLED`. |
| 4. Backing services | Cumple | MySQL se conecta por URL/configuracion externa y se reemplaza por H2 en tests. |
| 5. Build, release, run | Cumple | Dockerfiles separan build/runtime; Maven y Angular producen artefactos reproducibles. |
| 6. Procesos | Cumple | Backend no guarda estado de negocio en memoria; la base de datos conserva el estado. |
| 7. Port binding | Cumple | Spring Boot expone `8080`; el frontend se sirve por Nginx en contenedor. |
| 8. Concurrencia | Cumple | La app puede escalar por replicas stateless; Hikari controla concurrencia de conexiones. |
| 9. Desechabilidad | Cumple | Contenedores con healthcheck, arranque idempotente y apagado manejado por runtime. |
| 10. Paridad dev/prod | Cumple con perfiles | Perfiles `local`, `prod` y `test`; Docker Compose acerca infraestructura local a produccion. |
| 11. Logs | Cumple | SLF4J/Logback escribe al flujo estandar del proceso, apto para Docker y CI/CD. |
| 12. Procesos admin | Cumple | Flyway versiona migraciones; `APP_SEED_ENABLED` controla datos iniciales sin cambiar codigo. |

## SOLID

| Principio | Estado | Regla aplicada |
| --- | --- | --- |
| S - Responsabilidad unica | Cumple | Controladores exponen HTTP, servicios coordinan casos de uso, repositorios adaptan persistencia, mappers transforman DTOs. |
| O - Abierto/cerrado | Cumple | Nuevas fuentes de datos o UI pueden agregarse con adapters/ports sin modificar dominio. |
| L - Sustitucion de Liskov | Cumple | Los adapters implementan puertos sin cambiar contratos de entrada/salida. |
| I - Segregacion de interfaces | Cumple | Puertos de entrada/salida estan separados por capacidad: auth, tables, menu, sales, user admin. |
| D - Inversion de dependencias | Cumple | Backend depende de puertos, no de JPA directo; frontend usa use-cases y `TABLE_PORT` en vez de servicios HTTP en componentes. |

## Reglas para nuevos cambios

- Un componente Angular no debe inyectar adapters HTTP directamente; debe usar un use-case.
- Un use-case Angular debe depender de un puerto (`TABLE_PORT`, `AuthPort`, etc.), no de una clase de infraestructura.
- Un controlador Spring debe llamar puertos de entrada o use-cases, no repositorios.
- Una regla de negocio reutilizable debe vivir en dominio o servicio de aplicacion, no en controller ni DTO.
- Toda configuracion que cambie por ambiente debe venir de env vars o perfiles Spring.
- Nuevas tareas administrativas deben ser idempotentes y controlables por configuracion.
