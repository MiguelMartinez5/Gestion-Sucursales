# Gestión de Franquicias

Esta API fue desarrollada en **IntelliJ IDEA** utilizando **JDK 17**. Ofrece funcionalidades para gestionar franquicias, sucursales y productos, incluyendo operaciones CRUD y consultas específicas.  

La persistencia de datos se realiza mediante **RDS de Microsoft SQL Server**, y se incluye un archivo SQL para la creación de la base de datos. La aplicación está configurada para ejecutarse en el puerto `8080` y puede ser probada utilizando herramientas como **Postman**.  

---

## Requisitos

- **Java 17**
- **Docker** (incluye un `Dockerfile` y su respectivo `.tar.gz`)
- **Postman** (opcional para pruebas)
- Base de datos configurada con las siguientes propiedades:

## Configuración del Archivo `application.properties`

```properties
spring.application.name=Gestión de Sucursales de Calzado
spring.datasource.username=sa
spring.datasource.password=Git_pel66
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=calzadoDB;encrypt=true;trustServerCertificate=true
server.port=8080
```


**General**
**Base URL:** http://localhost:8080/api/franquicia
Los datos se pueden probar mediante Postman.
Recuerde configurar correctamente la conexión a la base de datos antes de iniciar la aplicación.


## Endpoints
1. Crear o Actualizar Franquicia
Permite agregar o actualizar una franquicia mediante un código único y un nombre.

`POST`
http://localhost:8080/api/franquicia

```Body (JSON):
{
  "codigo": "FRA001",
  "nombre": "Speedo"
}

Ejemplo adicional:

{
  "codigo": "FRA002",
  "nombre": "Oneill"
}
```
2. Crear o Actualizar Sucursal
Permite agregar o actualizar una sucursal asociada a una franquicia.

`POST`
[http://localhost:8080/api/franquicia/{sucursalId}/sucursales]

```Body (JSON):
{
  "codigo": "SUC001",
  "nombre": "Sucursal Ejemplo",
  "franquicia_id": "FRA001"
}
```

3. Crear o Actualizar Producto
Permite agregar o actualizar un producto asociado a una sucursal específica.

`POST`
http://localhost:8080/api/franquicia/sucursales/{productoId}/productos

```Body (JSON):
{
  "codigo": "PROD001",
  "nombre": "Zapatos Deportivos",
  "stock": 100,
  "sucursal_id": "SUC001"
}
```
4. Eliminar Producto de una Sucursal
Elimina un producto específico de una sucursal.

`DELETE`
http://localhost:8080/api/franquicia/productos/{productoId}/sucursales/{sucursalId}


5. Modificar Stock de un Producto
Actualiza el stock de un producto en una sucursal específica.

`PUT`
http://localhost:8080/api/franquicia/productos/{productoId}/stock/sucursales/{sucursalId}

```Body (JSON):
{
  "nuevoStock": 200
}
```

6. Obtener Producto con Mayor Stock por Franquicia
Devuelve los productos con mayor stock dentro de una franquicia específica.

`GET`
http://localhost:8080/api/franquicia/{franquiciaId}/productos-mayor-stock

```Ejemplo de Respuesta (JSON):
[
  {
    "productoId": 15,
    "nombre": "Zapatos Deportivos",
    "stock": 200,
    "sucursalId": 10
  }
]
```
## Despliegue con Docker
Incluye un Dockerfile creado con la imagen de la aplicación, en archivo .tar.gz.
Ejemplo básico para ejecutar:

```
docker build -t gestion-franquicias .
```

```
docker run -p 8080:8080 gestion-franquicias
```
**Crear la Base de Datos**
``` Crear la base de datos
CREATE DATABASE calzadoDB;


```

**Crear Tablas**

``` Crear la tabla franquicia con código único
CREATE TABLE franquicia (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE, 
    nombre VARCHAR(255) NOT NULL
);

 Crear la tabla sucursal con código único
CREATE TABLE sucursal (
    id BIGINT IDENTITY(1,1) PRIMARY KEY, 
    codigo VARCHAR(50) NOT NULL UNIQUE, 
    nombre VARCHAR(255) NOT NULL,
    franquicia_id BIGINT,
    FOREIGN KEY (franquicia_id) REFERENCES franquicia(id)
);

 Crear la tabla producto con código único
CREATE TABLE producto (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE, 
    nombre VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    sucursal_id BIGINT,
    FOREIGN KEY (sucursal_id) REFERENCES sucursal(id)
);



