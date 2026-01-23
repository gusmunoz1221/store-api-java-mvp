# Store API – Admin Core MVP 🚀

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED)
![Security](https://img.shields.io/badge/Spring_Security-JWT-red)

[![Live Demo MVP](https://img.shields.io/badge/🎮_Demo_En_Vivo-MVP-2ea44f?style=for-the-badge&logo=railway)](https://store-api-mvp.railway.app/swagger-ui.html)

## Store API – Admin Core MVP 🚀

> ⚠️ **Nota:** Este repositorio contiene el **código fuente del MVP** (núcleo técnico).
> Las imágenes mostradas más abajo ilustran cómo se vería una **versión Enterprise privada**, utilizada solo como demostración de escalabilidad, auditoría, pagos y notificaciones.

---

## 📖 Sobre el Proyecto

Este proyecto es un **MVP técnico backend-first**, enfocado en:

* Arquitectura limpia y separación de responsabilidades
* Seguridad estricta del backoffice
* Buenas prácticas en **Spring Boot 4 + JPA**

El sistema soporta:

1. **Guest Checkout:** Compra rápida sin registro para reducir fricción.
2. **Backoffice Seguro:** Gestión administrativa protegida con **JWT**, accesible solo por rol `ADMIN`.

> La ausencia de usuarios finales registrados es una **decisión de alcance del MVP**, no una limitación técnica.

---

## ⚡ Alcance: MVP vs Versión Enterprise

| Módulo / Feature    | 🟢 MVP (Código en este Repo) | 🔒 Versión Enterprise (Privada / Demo)    |
| ------------------- | ---------------------------- | ----------------------------------------- |
| **Modelo de Venta** | Guest Checkout               | Checkout con usuario registrado           |
| **Autenticación**   | JWT (Admins)                 | JWT + Refresh Token (rotación automática) |
| **Roles**           | ADMIN                        | ADMIN + USER                              |
| **Catálogo**        | ABM básico (Admin)           | Inventario avanzado, variantes, precios   |
| **Usuarios**        | Solo Admins                  | Usuarios finales + direcciones            |
| **Pagos**           | ❌ Fuera de alcance           | Integración real con Mercado Pago         |
| **Notificaciones**  | ❌ Fuera de alcance           | Emails transaccionales HTML async         |
| **Auditoría**       | Timestamps básicos           | Historial completo (SQL Window Functions) |
| **Frontend**        | ❌ No incluido                | SPA React/Vue integrada                   |

---

## 🛠️ Stack Tecnológico

* **Lenguaje:** Java 21
* **Framework:** Spring Boot 4
* **Base de Datos:** PostgreSQL 15
* **Seguridad:** Spring Security 6 + JWT
* **Infraestructura:** Docker & Docker Compose
* **Documentación:** Swagger / OpenAPI

---

## 🔐 Seguridad y Accesos

### 🔓 Zona Pública (Guest)

Endpoints abiertos:

* `GET /products`
* `GET /categories`
* `POST /orders` (checkout invitado)

### 🔒 Zona Privada (Backoffice)

* Requiere **JWT Bearer Token**
* Acceso exclusivo para rol `ADMIN`

Funcionalidades:

* Dashboard y métricas del sistema
* ABM completo de productos, categorías y subcategorías
* Gestión de órdenes y auditoría
* Reportes de ventas

> Swagger UI expone los endpoints administrativos **protegidos por JWT**, incluso en el entorno productivo.

---

## 🧠 Ingeniería de Datos y Consultas Avanzadas

Este MVP implementa soluciones técnicas que escalan directamente hacia una versión Enterprise:

* **Auditoría SQL nativa:** Uso de `LAG()` y `PARTITION BY` para historial de stock y precios.
* **Búsqueda flexible:** Regex PostgreSQL (`~*`) y filtros combinados.
* **Performance:** Prevención del problema N+1 con `JOIN FETCH` y proyecciones DTO.

---

## 🚀 Instalación y Ejecución Local

### Prerrequisitos

* Docker Desktop

### Pasos

1. **Clonar el repositorio**

```bash
git clone https://github.com/gustavito1221/store-api-java-mvp.git
cd store-api-java-mvp
```

2. **Configurar variables de entorno**

Renombrar `.env.example` a `.env` y definir:

```env
DB_USERNAME=
DB_PASSWORD=
DB_URL=
JWT_SECRET=
```

3. **Levantar la aplicación**

```bash
docker-compose up -d --build
```

4. **Probar la API**

* **Swagger local:**

    * [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

* **Swagger en producción (Render):**

    * 👉 [https://store-api-java-mvp.onrender.com/swagger-ui.html](https://store-api-java-mvp.onrender.com/swagger-ui.html)

---

## 🔑 Credenciales de Prueba (Seed Data)

Para facilitar la revisión, la aplicación ejecuta un `CommandLineRunner` que precarga un usuario administrador cuando la base de datos está vacía.

Usa estas credenciales para obtener el **JWT** en `/auth/login`:

| Rol       | Email             | Contraseña | Acceso                                                   |
| --------- | ----------------- | ---------- | -------------------------------------------------------- |
| **ADMIN** | `admin@store.com` | `admin`    | Acceso total (dashboard, productos, órdenes, categorías) |
| **GUEST** | N/A               | N/A        | Lectura de catálogo y creación de órdenes                |

---

## 📸 Ejemplos Visuales (Versión Enterprise Privada)

> ⚠️ Imágenes **solo demostrativas** para ilustrar capacidades avanzadas de la versión Enterprise.

### Dashboard Admin

![Dashboard](images/dashboard.png)

### Flujo de Checkout con Usuario Registrado

![Checkout Flow](images/checkout-flow.png)

### Email Notificación Transaccional

![Email Notification](images/email-notification.png)

---

Este repositorio expone únicamente el **núcleo técnico (MVP)** con foco en:

* Arquitectura
* Seguridad
* Buenas prácticas en Spring Boot

Su objetivo es **demostrar capacidad técnica**, no representar un producto comercial final.