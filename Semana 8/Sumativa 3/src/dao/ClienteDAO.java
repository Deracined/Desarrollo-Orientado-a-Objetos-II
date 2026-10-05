package dao;

import config.ConexionDB;
import modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos (DAO) para la tabla "clientes".
 * Implementa las operaciones CRUD mediante JDBC.
 */
public class ClienteDAO {

    /**
     * CREATE: inserta un nuevo cliente.
     */
    public void create(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nombre) VALUES (?)";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.executeUpdate();
        }
    }

    /**
     * READ: obtiene todos los clientes.
     */
    public List<Cliente> readAll() throws SQLException {
        List<Cliente> lista = new ArrayList<>();

        String sql = "SELECT id, nombre FROM clientes ORDER BY id";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre")
                ));
            }
        }

        return lista;
    }

    /**
     * UPDATE: actualiza un cliente existente.
     */
    public void update(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ? WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setInt(2, cliente.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * DELETE: elimina un cliente por su ID.
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}