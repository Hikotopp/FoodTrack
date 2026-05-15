# FoodTrack

FoodTrack es una aplicacion para administrar mesas, pedidos, menu, inventario de platos, cuentas de usuario e historial de ventas.

## Repositorios

- Monorepo: https://github.com/Hikotopp/FoodTrack
- Frontend: https://github.com/Hikotopp/Frontend_FoodTrack
- Backend: https://github.com/Hikotopp/Backend_FoodTrack

## Requisitos

- Docker Desktop
- Java 17, solo si vas a correr el backend fuera de Docker
- Node.js 20, solo si vas a correr el frontend fuera de Docker

## Ejecutar con Docker

1. Copia el archivo de entorno:

   ```bash
   cp .env.example .env
   ```

2. Edita `.env` y cambia las contrasenas de ejemplo.

3. Levanta la aplicacion:

   ```bash
   docker compose up -d --build
   ```

4. Abre:

   ```text
   http://localhost:4200
   ```

Servicios principales:

- Frontend: `http://localhost:4200`
- Backend: `http://localhost:8080`
- MySQL desde Workbench: `127.0.0.1:3307`

## ReportService opcional

El envio de reportes por correo usa un servicio externo que no vive en este repositorio. La app funciona sin el; si no esta levantado, el backend devuelve el resumen y avisa que no pudo conectar con ReportService.

Si tienes el proyecto `ReportService` al lado de esta carpeta, puedes levantarlo asi:

```bash
docker compose --profile reports up -d --build
```

La estructura esperada es:

```text
Desktop/
  Spring/
  ReportService/ReportService/
```

## Ejecutar sin Docker

Backend:

```bash
cd springboot-application
./mvnw spring-boot:run
```

Frontend:

```bash
cd foodtrack
npm ci
npm start
```

Para desarrollo local, el backend usa por defecto MySQL en `localhost:3307/foodtrack`.

## Pruebas

Backend:

```bash
cd springboot-application
./mvnw test
```

Frontend:

```bash
cd foodtrack
npm ci
npm run test -- --watch=false --browsers=ChromeHeadless --code-coverage
npm run build
```

## Variables importantes

Revisa `.env.example` para las variables necesarias. El archivo real `.env` esta ignorado por Git y no debe subirse.
