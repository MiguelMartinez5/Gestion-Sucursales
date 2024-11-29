Gestión de Franquicias
Esta Api esta desarrollada en Intellij con jdk 17 comparte archivo Dockerfile con su respetivo .tar.gz y con el archivo SQL en este caso se trabajó la persistencia de datos con RDS de Microsoft SQL Server. Para poder levantar la aplicación esta configurada por defecto por el puerto 8080, la puede probar con la aplicación de Postman teniendo en cuenta la configuración de la base de datos que esta especificada de la siguiente manera en el application.properties:
spring.application.name=Gestión de Sucursales de Calzado
spring.datasource.username=sa
spring.datasource.password=Git_pel66
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=calzadoDB;encrypt=true;trustServerCertificate=true;
server.port=8080

 
General: http://localhost:8080/api/franquicia
Se puede crear y si ya existe se puede actualizar la franquicia mediante el codigo que tenga registrado, esto en caso de que tengamos una o más franquicias, ingresamos un código que la identifica y un nombre.
1- Agregar ó Actualizar franquicia     http://localhost:8080/api/franquicia
{
"codigo": "FRA001",
"nombre": "Speedo"
}
{
"codigo": "FRA002",
"nombre": "Oneill"
}

2- Agregar ó Actualizar sucursal a una sucursal:    
Se puede crear y si ya existe se puede actualizar la sucursal mediante el codigo que tenga registrado, esto en caso de que tengamos una o mas sucursales, ingresamos el codigo que la identifica y un nombre. En {sucursalId} ingresamos el Id de la sucursal que deseamos agregar o eliminar.
	  
  POST:    http://localhost:8080/api/franquicia/{sucursalId}/sucursales 
{
    "codigo": "SUC001", 
    "nombre": "Sucursal Ejemplo",
    "franquicia_id": “FRA001”
}


3- Agregar ó Actualizar producto a un producto:  
Se puede crear y si ya existe se puede actualizar un producto mediante el codigo que tenga registrado, esto en caso de que tengamos una o más productos, ingresamos el codigo que lo identifica y un nombre. En {productoId} ingresamos el Id del producto que deseamos agregar o eliminar.


POST:	http://localhost:8080/api/franquicia/sucursales/{productoId}/productos 
{
    "codigo": "PROD001", 
    "nombre": "Zapatos Deportivos",
    "stock": 100,
    "sucursal_id": SUC001
}
4- Eliminar producto de una sucursal especifica:    
Con el endpoind de Delete podemos eliminar un producto de una sucursale especifica	   
 DELETE :  http://localhost:8080/api/franquicia /productos/{productoId}/sucursales/{sucursalId}  



5- Modificar stock de un producto:       	    
Este endpoint modifica la cantidad de stock de un producto en una sucursal específica. Recibe el identificador del producto {productoId}, el identificador de la sucursal {sucursalId}, y el nuevo valor de stock en el cuerpo de la solicitud. Si el producto y la sucursal existen, el stock se actualizará en la base de datos.
PUT:  http://localhost:8080/api/franquicia/productos/{productoId}/stock/sucursales/{sucursalId} 

{
  "nuevoStock": 200
}

6- Obtener producto con mayor stock por sucursal:  
Este endpoint permite obtener una lista de los productos con el mayor stock dentro de una franquicia. Recibe el identificador de la franquicia {franquiciaId} como parámetro, y devuelve una lista de productos con su información, ordenada según el stock disponible en cada uno. La lista incluye los productos con los mayores niveles de stock en la franquicia solicitada.
 GET: 	 http://localhost:8080/api/franquicia/4/productos-mayor-stock 
[
  {
    "productoId": 15,
    "nombre": "Zapatos Deportivos",
    "stock": 200,
    "sucursalId": 10
  }
]







SQL de la base de datos:
-- Crear la base de datos
CREATE DATABASE calzadoDB;

-- Crear la tabla franquicia con código único
CREATE TABLE franquicia (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE, 
    nombre VARCHAR(255) NOT NULL
);

-- Crear la tabla sucursal con código único
CREATE TABLE sucursal (
    id BIGINT IDENTITY(1,1) PRIMARY KEY, 
    codigo VARCHAR(50) NOT NULL UNIQUE, 
    nombre VARCHAR(255) NOT NULL,
    franquicia_id BIGINT,
    FOREIGN KEY (franquicia_id) REFERENCES franquicia(id)
);

-- Crear la tabla producto con código único
CREATE TABLE producto (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE, 
    nombre VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    sucursal_id BIGINT,
    FOREIGN KEY (sucursal_id) REFERENCES sucursal(id)
);
