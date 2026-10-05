package dao;

import config.ConexionDB;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos (DAO) para la tabla "repartidores", CRUD completo con PreparedStatement (evita inyección SQL) y
 * ResultSet (para leer filas). Cada metodo abre y cierra su propia conexión con try-with-resources.
 */
public class RepartidorDAO {

    /** CREATE: inserta un nuevo repartidor. */
    public void create(Repartidor repartidor) throws SQLException {
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, repartidor.getNombre());
            stmt.executeUpdate();
        }
    }

    /** READ: devuelve todos los repartidores, ordenados por ID. */
    public List<Repartidor> readAll() throws SQLException {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidores ORDER BY id";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Repartidor(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return lista;
    }

    /** UPDATE: actualiza el nombre de un repartidor existente. */
    public void update(Repartidor repartidor) throws SQLException {
        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, repartidor.getNombre());
            stmt.setInt(2, repartidor.getId());
            stmt.executeUpdate();
        }
    }

    /** DELETE: elimina un repartidor por su ID. */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM repartidores WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
