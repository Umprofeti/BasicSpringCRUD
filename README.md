# BASIC SPRING API CRUD

### Configuracion del ambiente

Primero que todo debemos tener la base de datos configurada, para ello se recomienda utilizar docker

Aquí está el archivo de docker-compose para levantar el servicio:

```services:
postgres:
image: postgres:latest
container_name: postgres-container
environment:
POSTGRES_USER: root
POSTGRES_PASSWORD: tucontraseñaaqui
ports:
- "5432:5432"
networks:
- database-network

pgadmin:
image: dpage/pgadmin4
container_name: pgadmin-container
environment:
PGADMIN_DEFAULT_EMAIL: example@example.com
PGADMIN_DEFAULT_PASSWORD: tucontraseña
ports:
- "80:80"
networks:
- database-network

networks:
database-network:
driver: bridge`
````
Docker levanda dos servicios, uno es la base de datos y el otro es el cliente para poder interactuar con ello.

Adicionalmente a la hora de crear la base de datos utilizaremos el siguiente script:

```
-- Crear la base de datos
CREATE DATABASE api_database_reto_1;


-- Crear la tabla Usuario
CREATE TABLE Usuario (
id SERIAL PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
email VARCHAR(100) NOT NULL UNIQUE,
fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Función para crear un nuevo usuario
CREATE OR REPLACE FUNCTION CrearUsuario(
p_nombre VARCHAR(100),
p_email VARCHAR(100)
) RETURNS VOID AS $$
BEGIN
INSERT INTO Usuario (nombre, email) VALUES (p_nombre, p_email);
END;
$$ LANGUAGE plpgsql;

-- Función para obtener un usuario por ID
CREATE OR REPLACE FUNCTION ObtenerUsuarioPorID(
p_id INT
) RETURNS TABLE(id INT, nombre VARCHAR, email VARCHAR, fecha_creacion TIMESTAMP) AS $$
BEGIN
RETURN QUERY
SELECT u.id, u.nombre, u.email, u.fecha_creacion
FROM Usuario u
WHERE u.id = p_id;
END;
$$ LANGUAGE plpgsql;

-- Función para obtener todos los usuarios
CREATE OR REPLACE FUNCTION ObtenerTodosLosUsuarios()
RETURNS TABLE(id INT, nombre VARCHAR, email VARCHAR, fecha_creacion TIMESTAMP) AS $$
BEGIN
RETURN QUERY
SELECT * FROM Usuario;
END;
$$ LANGUAGE plpgsql;

-- Función para actualizar un usuario
CREATE OR REPLACE FUNCTION ActualizarUsuario(
p_id INT,
p_nombre VARCHAR(100),
p_email VARCHAR(100)
) RETURNS VOID AS $$
BEGIN
UPDATE Usuario
SET nombre = p_nombre, email = p_email
WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;

-- Función para eliminar un usuario
CREATE OR REPLACE FUNCTION EliminarUsuario(
p_id INT
) RETURNS VOID AS $$
BEGIN
DELETE FROM Usuario WHERE id = p_id;
END;
$$ LANGUAGE plpgsql;
```
Este script levanta una base de datos con una tabla para realizar las operaciones básicas de un CRUD

### Documentacion de la API
Para ver la documentación completa de la API una vez que el servicio esté ejecutandose, este se monstrará en
http://localhost:8080/swagger-ui/index.htm

### Contenerización de la aplicación

En los archivos Dockerfile y docker-compose.yml se encuentra la configuración necesaria para contenerizar la aplicación.

El archivo Dockerfile contiene las instrucciones necesarias para construir la imagen de la aplicación, mientras que el archivo docker-compose.yml contiene la configuración para levantar el servicio.

### Ejecución de la aplicación

Si se prefiere ejecutar el servicio de forma local simplemente se tiene abrir el archivo dentro de:

```Build/libs/BASIC_SPRING_API_CRUD-0.0.1-SNAPSHOT.jar``` y ejecutarlo con el comando:

```java -jar BASIC_SPRING_API_CRUD-0.0.1-SNAPSHOT.jar```