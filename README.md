Aplicación de escritorio en **Java (Swing)** con base de datos **MySQL** para administrar un gimnasio con varias sucursales: clientes, empleados, membresías, planes, clases, pagos, ventas, inventario, proveedores, equipos y asistencia.

Proyecto del curso de Curso Integrador I: Sistemas Software – Universidad Tecnológica del Perú (UTP).
Integrantes:Edgar Tello Saravia, Luis Cuya Cuya, 

## Módulos principales
- Inicio de sesión (administrador, empleado y cliente)
- Sucursales y empleados (con asistencia y reportes de incidencia)
- Clientes, membresías y planes
- Clases y servicios
- Ventas, facturas y pagos
- Productos, inventario, proveedores y equipos

## Arquitectura
El sistema separa las responsabilidades en capas:

| Capa | Paquete | Qué hace |
|---|---|---|
| Vista | `funciones` | Pantallas Swing (`Frm*`, `DlgReportes`). Solo muestran datos y reciben acciones del usuario. |
| Servicio | `servicio` | Reglas de negocio. Devuelven un `Resultado` (éxito o error con mensaje). |
| Acceso a datos (DAO) | `repositorio` | Interfaces `*Repository` y sus implementaciones `*RepositoryJdbc`, que son las únicas que hacen consultas SQL. |
| Modelo | `modelo` | Datos del dominio. |
| Fábrica | `fabrica` | `AppFactory` crea los servicios y les entrega su repositorio. |

Principios aplicados:
- **DAO:** cada tabla tiene una interfaz de repositorio y una implementación JDBC.
- **SOLID:**
  - *S:* cada servicio atiende una sola entidad.
  - *D:* los servicios dependen de interfaces (`ClienteRepository`), no de la clase JDBC concreta.
  - *O:* se puede cambiar la base de datos creando otra implementación sin modificar los servicios.
- **TDD:** pruebas unitarias en la carpeta `test`, con repositorios falsos en memoria (sin MySQL).
- **Seguridad:** consultas con `PreparedStatement` (evita inyección SQL) y contraseñas de empleado guardadas con hash SHA-256 y sal aleatoria.

## Librerías de apoyo
| Librería | Para qué se usa |
|---|---|
| **Logback (SLF4J)** | Registro de eventos y errores en consola y en `logs/leongym.log`. |
| **Apache POI** | Exportar a Excel (.xlsx) los reportes de incidencia (`ExcelExportService`). |
| **Apache Commons Lang** | Limpiar los textos de entrada (`StringUtils.trimToEmpty`). |
| **Google Guava** | Validar argumentos (`Preconditions`) y devolver listas inmutables (`ImmutableList`). |
| **MySQL Connector/J** | Conexión JDBC con MySQL. |
| **JUnit 5** | Pruebas unitarias. |

## Cómo ejecutarlo
Requisitos: JDK 25 o superior, MySQL, NetBeans y Maven.

1. Crear la base de datos `UTPsports7` en MySQL (usuario y clave configurados en `UTPsports/UTPsports.java`).
2. Descargar las librerías:
   ```
   mvn dependency:copy-dependencies -DoutputDirectory=lib
   ```
3. En NetBeans: clic derecho en el proyecto → *Properties → Libraries → Compile → Add JAR/Folder* y agregar los `.jar` de `lib`.
4. Ejecutar el proyecto (F6).

## Pruebas
```
mvn test
```
Ejecuta las pruebas de `ClienteService`, `ReporteService`, `PasswordUtil` y `Resultado`.

## Control de versiones
El repositorio parte del sistema base y registra cada mejora en commits separados (librerías, pruebas, documentación, etc.).
