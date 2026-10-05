package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase reutilizable que entrega una conexión a la base de datos speedfast_db. La contraseña se lee desde la variable
 * de entorno MYSQL_PASSWORD.
 */
public class ConexionDB {

    //Uso de useSSL=FALSe y servertimezoneutc para evitar advertencias de certificacion con la conexion y errores de zona
    // horaria en SQL, ya que tengo mi windows/region e idioma en inglés.
    private static final String URL =
            "jdbc:mysql://localhost:3306/speedfast_db?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";

    public static Connection obtenerConexion() throws SQLException {
        String password = System.getenv("MYSQL_PASSWORD");

        if (password == null || password.isEmpty()) {
            throw new SQLException(
                    "No se encontró la variable de entorno MYSQL_PASSWORD. "
            );
        }

        return DriverManager.getConnection(URL, USUARIO, password);
    }
}
