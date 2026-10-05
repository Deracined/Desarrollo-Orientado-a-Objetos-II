package main;

import config.ConexionDB;
import dao.RepartidorDAO;
import modelo.Repartidor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * PruebaConexion no tiene interfaz gráfica ya que sirve para comprobar que la conexión y el CRUD
 * funcionen antes de abrir la aplicación completa. Usa RepartidorDAO ya que es la entidad más
 * simple del sistema.
 */
public class PruebaConexion {

    public static void main(String[] args) {

        // ---- Paso 1: Connexion real ---- //
        System.out.println("Paso 1: probando conexión...");
        try (Connection con = ConexionDB.obtenerConexion()) {
            System.out.println("  OK -> Conectado a: " + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("  FALLÓ la conexión.");
            System.out.println("  Motivo: " + e.getMessage());
            return;
        }

        RepartidorDAO repartidorDAO = new RepartidorDAO();
        String nombrePrueba = "___PRUEBA_TEMPORAL___";

        // ---- Paso 2: CREATE real ---- //
        System.out.println("Paso 2: insertando un repartidor de prueba...");
        try {
            repartidorDAO.create(new Repartidor(nombrePrueba));
            System.out.println("  OK -> create() ejecutado.");
        } catch (SQLException e) {
            System.out.println("  FALLÓ create(): " + e.getMessage());
            return;
        }

        // ---- Paso 3: READ real ---- //
        System.out.println("Paso 3: leyendo todos los repartidores (readAll)...");
        List<Repartidor> lista;
        try {
            lista = repartidorDAO.readAll();
            System.out.println("  OK -> se leyeron " + lista.size() + " repartidor(es).");
        } catch (SQLException e) {
            System.out.println("  FALLÓ readAll(): " + e.getMessage());
            return;
        }

        Repartidor repartidorPrueba = lista.stream()
                .filter(r -> r.getNombre().equals(nombrePrueba))
                .findFirst().orElse(null);

        if (repartidorPrueba == null) {
            System.out.println("  FALLÓ -> el INSERT no dejó rastro en la tabla.");
            return;
        }

        // ---- Paso 4: UPDATE y DELETE real (limpia el dato de prueba) ----
        System.out.println("Paso 4: probando update() y delete()...");
        try {
            repartidorPrueba.setNombre("___PRUEBA_ACTUALIZADA___");
            repartidorDAO.update(repartidorPrueba);
            System.out.println("  OK -> update() ejecutado.");

            repartidorDAO.delete(repartidorPrueba.getId());
            System.out.println("  OK -> delete() ejecutado (dato de prueba eliminado).");
        } catch (SQLException e) {
            System.out.println("  FALLÓ update()/delete(): " + e.getMessage());
            return;
        }

        System.out.println();
        System.out.println("Los 4 pasos (CREATE, READ, UPDATE, DELETE) funcionaron.");
        System.out.println("Ahora puedes ejecutar main.Main con confianza.");
    }
}
