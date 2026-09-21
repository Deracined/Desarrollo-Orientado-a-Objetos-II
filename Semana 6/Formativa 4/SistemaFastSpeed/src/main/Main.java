package main;

import vista.VentanaPrincipal;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Las ventanas Swing deben crearse en el hilo de eventos (EDT)
        SwingUtilities.invokeLater(() -> new VentanaPrincipal());
    }
}
