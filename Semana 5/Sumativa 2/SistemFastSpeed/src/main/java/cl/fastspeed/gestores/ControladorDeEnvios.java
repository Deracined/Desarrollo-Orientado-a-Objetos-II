package cl.fastspeed.gestores;

import cl.fastspeed.interfaces.Despachable;
import cl.fastspeed.interfaces.Cancelable;
import cl.fastspeed.interfaces.Rastreable;
// Gestor encargado de controlar y cordinar los envios mediante las interfaces del caso
public class ControladorDeEnvios {

    public void despachar(Despachable pedido) {
        pedido.despachar();
    }

    public void cancelar(Cancelable pedido) {
        pedido.cancelar();
    }

    public void mostrarHistorial(Rastreable pedido) {
        pedido.verHistorial();
    }
}