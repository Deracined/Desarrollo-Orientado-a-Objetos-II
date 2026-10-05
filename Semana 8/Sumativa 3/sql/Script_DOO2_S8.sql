/* Script de la creación de la Base de Datos para esta semana 8, no incluye datos de ejemplo,
*  para así demostrar que campos/datos se registran desde 0 a apartir de IntelliJ.
*/
CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

/* Se eliminan las tablas anteriores (si es que existen) mediante DROP TABLE para demostrar en limpio el funcionamiento.
*/
DROP TABLE IF EXISTS entregas;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS repartidores;
DROP TABLE IF EXISTS clientes;

/*Creamos las "entidades" (tablas)
*/
CREATE TABLE repartidores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(100) NOT NULL,
    tipo ENUM('COMIDA','ENCOMIENDA','EXPRESS'),
    estado ENUM('PENDIENTE','EN_REPARTO','ENTREGADO')
);

CREATE TABLE entregas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_repartidor INT,
    fecha DATE,
    hora TIME,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id),
    FOREIGN KEY (id_repartidor) REFERENCES repartidores(id)
);

CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

-- Recuperación de datos para ejecutar de forma individual y visualizar las tablas --
SELECT * FROM repartidores;
SELECT * FROM pedidos;
SELECT * FROM entregas;
SELECT * FROM clientes;

