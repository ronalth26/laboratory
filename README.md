markdown
# Proyecto Banca - Arquitectura de Microservicios

Este proyecto es una arquitectura de microservicios desarrollada con Java, Spring Boot y Maven. Incluye varios servicios independientes que se comunican entre sí y pueden ser orquestados mediante Docker Compose.

## Estructura de directorios

- `api-gateway/`: Puerta de entrada para las peticiones, gestiona el enrutamiento hacia los microservicios.
- `ServicioClientes/`: Microservicio para la gestión de clientes.
- `ServicioCuentas/`: Microservicio para la gestión de cuentas bancarias.
- `ServicioEureka/`: Servicio de descubrimiento Eureka para registro y localización de microservicios.
- `ServicioPrestamos/`: Microservicio para la gestión de préstamos.
- `ServicioTransacciones/`: Microservicio para la gestión de transacciones.

## Tecnologías utilizadas

- Java 173. **Acceso a Eureka**  
   Visita [http://localhost:8761](http://localhost:8761) para ver el panel de Eureka.

## Base de datos

Cada microservicio que lo requiera incluye un archivo `data.sql` para inicializar datos de ejemplo.

## Pruebas

Ejecuta las pruebas unitarias con:+
- Spring Boot
- Spring Cloud (Eureka)
- Maven
- SQL
- Docker & Docker Compose

## Ejecución local

1. **Compilar los servicios**  
   Ejecuta en cada ## Datos de ejemplo por microservicio
   
## Valores Por Defecto
   
### Microservio Clientes

   **Tabla: clientes**

   | id | nombre       | dni      | email                 | estado   |
   |----|--------------|----------|-----------------------|----------|
   | 1  | Juan Perez   | 12345678 | juanperez@gmail.com   | ACTIVO   |
   | 2  | Maria Gomez  | 87654321 | mariagomez@gmail.com  | ACTIVO   |
   | 3  | Carlos Lopez | 11223344 | carloslopez@gmail.com | INACTIVO |
   
   
### Microservicio Cuentas
   
   **Tabla: tipo-cuentas**
   

   | id | valor        |
   |----|--------------|
   | 1  | Ahorro       |
   | 2  | Corriente    |
   | 3  | Plazo Fijo   |
   | 4  | Empresarial  |
   
   **Tabla: estado-cuentas**
   
   | id | valor                  |
   |----|------------------------|
   | 1  | ACTIVA                 |
   | 2  | INACTIVA               |
   | 3  | BLOQUEADA              |
   | 4  | PENDIENTE_VERIFICACION |
   
   **Tabla: cuentas**
   
   | id | cliente_id | tipo_cuenta_id | estado_cuenta_id | saldo    |
   |----|------------|----------------|------------------|----------|
   | 1  | 1          | 1              | 1                | 1500.75  |
   | 2  | 2          | 1              | 1                | 5000.00  |
   | 3  | 3          | 2              | 3                | 1200.00  |
   | 4  | 4          | 3              | 4                | 10000.00 |
   | 5  | 5          | 4              | 2                | 25000.50 |

### Microservicio Prestamos
   
   **Tabla: estado_prestamos**
   
   | id | nombre       |
   |----|--------------|
   | 1  | PENDIENTE    |
   | 2  | APROBADO     |
   | 3  | RECHAZADO    |
   | 4  | CANCELADO    |
   | 5  | FINALIZADO   |
   | 6  | EN ESPERA    |
   | 7  | EN PROCESO   |
   | 8  | SUSPENDIDO   |
   | 9  | REPROGRAMADO |
   | 10 | PAGADO       |
   
   **Tabla: prestamos**
   
   | id | cliente_id | cuenta_id | monto    | plazo_meses | tasa_interes | estado_prestamo_id | fecha_desembolso |
   |----|------------|-----------|----------|-------------|--------------|--------------------|------------------|
   | 1  | 1          | 1         | 10000.00 | 12          | 5.5          | 2                  | 2023-01-15       |
   | 2  | 2          | 2         | 5000.00  | 6           | 4.5          | 1                  | 2023-02-20       |
   | 3  | 3          | 3         | 15000.00 | 24          | 6.0          | 3                  | 2023-03-10       |
   | 4  | 4          | 4         | 20000.00 | 36          | 7.0          | 2                  | 2023-04-05       |
   | 5  | 5          | 5         | 8000.00  | 18          | 5.0          | 4                  | 2023-05-12       |
   
   **Tabla: estado_cuotas**
   
   | id | nombre    |
   |----|-----------|
   | 1  | PENDIENTE |
   | 2  | PAGADA    |
   | 3  | ATRASADA  |
   | 4  | CANCELADA |
   
   **Tabla: cuotas**
   
   | id | prestamo_id | numero | fecha_vencimiento | monto  | estado_cuota_id |
   |----|-------------|--------|-------------------|--------|-----------------|
   | 1  | 1           | 1      | 2023-02-15        | 833.33 | 2               |
   | 2  | 1           | 2      | 2023-03-15        | 833.33 | 2               |
   | 3  | 1           | 3      | 2023-04-15        | 833.33 | 1               |
   | 4  | 2           | 1      | 2023-03-20        | 833.33 | 2               |
   | 5  | 2           | 2      | 2023-04-20        | 833.33 | 1               |
   | 6  | 3           | 1      | 2023-04-10        | 625.00 | 1               |
   | 7  | 3           | 2      | 2023-05-10        | 625.00 | 1               |
   | 8  | 4           | 1      | 2023-05-05        | 555.56 | 1               |
   | 9  | 4           | 2      | 2023-06-05        | 555.56 | 1               |
   | 10 | 5           | 1      | 2023-06-12        | 444.44 | 1               |
   | 11 | 5           | 2      | 2023-07-12        | 444.44 | 1               |


### Microservicio Transacciones

   **Tabla: tipo_transacciones**

   | id | nombre             |
   |----|--------------------|
   | 1  | DEPÓSITO           |
   | 2  | RETIRO             |
   | 3  | TRANSFERENCIA      |
   | 4  | PAGO DE SERVICIO   |

   **Tabla: transacciones**

   | id | cuenta_id | tipo_transaccion_id | monto   | fecha      | referencia                  |
   |----|-----------|---------------------|---------|------------|-----------------------------|
   | 1  | 1001      | 1                   | 1500.75 | 2025-09-12 | Depósito en efectivo        |
   | 2  | 1002      | 2                   | 500.00  | 2025-09-12 | Retiro por cajero           |
   | 3  | 1001      | 3                   | 300.50  | 2025-09-11 | Transferencia a cuenta 1003 |
   | 4  | 1003      | 4                   | 120.00  | 2025-09-10 | Pago de luz                 |
   