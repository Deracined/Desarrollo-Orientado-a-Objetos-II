package dao;

import config.ConexionDB;
import modelo.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos (DAO) para la tabla "pedidos", CRUD completo con PreparedStatement y ResultSet.
 */
public class PedidoDAO {

    /** CREATE: inserta un nuevo pedido. */
    public void create(Pedido pedido) throws SQLException {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, pedido.getDireccion());
            stmt.setString(2, pedido.getTipo().name());
            stmt.setString(3, pedido.getEstado().name());
            stmt.executeUpdate();
        }
    }

    /** READ: devuelve todos los pedidos, ordenados por ID. */
    public List<Pedido> readAll() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedidos ORDER BY id";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Pedido(
                        rs.getInt("id"),
                        rs.getString("direccion"),
                        Pedido.Tipo.valueOf(rs.getString("tipo")),
                        Pedido.Estado.valueOf(rs.getString("estado"))
                ));
            }
        }
        return lista;
    }

    /** UPDATE: actualiza dirección, tipo y estado de un pedido existente. */
    public void update(Pedido pedido) throws SQLException {
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, pedido.getDireccion());
            stmt.setString(2, pedido.getTipo().name());
            stmt.setString(3, pedido.getEstado().name());
            stmt.setInt(4, pedido.getId());
            stmt.executeUpdate();
        }
    }

    /** DELETE: elimina un pedido por su ID. */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection con = ConexionDB.obtenerConexion();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
