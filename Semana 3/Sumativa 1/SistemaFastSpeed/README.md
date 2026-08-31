SpeedFast — Sistema de Gestión de Pedidos

Proyecto de la Semana 3 — Desarrollo Orientado a Objetos II. Sistema que gestiona pedidos de comida, encomiendas y compras express, aplicando herencia, polimorfismo, abstracción e interfaces.

Descripción general

La empresa SpeedFast necesita un sistema para gestionar sus distintos tipos de pedidos, cada uno con reglas de asignación propia en los repartidores y calculos en los tiempos de entrega, junto con funcionalidades de tipo despacho, canlación y ver historial, este diseño se organiza los siguientes paquetes:

cl.fastspeed.model       → Pedido (clase abstracta) y sus subclases
cl.fastspeed.interfaces  → Despachable, Cancelable, Rastreable
cl.fastspeed.gestores    → ControladorDeEnvios
cl.fastspeed.app         → Main (simulación del sistema)
Jerarquía de clases
Pedido (abstracta)
Se implementa Despachable, Cancelable, Rastreable
 ├── PedidoComida
 ├── PedidoEncomienda
 └── PedidoExpress

Pedido concentra los atributos y comportamientos comunes para todo pedido: identificador, dirección, distancia, historial de eventos, asignación de repartidor, resumen del pedido, despacho, cancelación y consulta de historial. El único elemento que varía completamente entre los pedidos es  el cálculo del tiempo de entrega, el cual se declara como método abstracto, obligando a cada subclase a definir su propia regla de negocio.

Interfaces:

Despachable	despachar()	Marca el pedido como despachado
Cancelable	cancelar()	Marca el pedido como cancelado
Rastreable	verHistorial()	Muestra el historial de eventos del pedido

Las tres interfaces se implementan una sola vez, en Pedido. Al heredar de Pedido, las tres subclases adquieren automáticamente la capacidad de ser tratadas como Despachable, Cancelable y Rastreable, sin necesidad de repetir la implementación ni el código.

ControladorDeEnvios no implementa estas interfaces: las usa. Sus métodos reciben los pedidos como parámetros de tipo Despachable, Cancelable o Rastreable, no como Pedido concreto. Esto significa que el controlador puede operar sobre cualquier objeto que cumpla el contrato correspondiente, sin conocer ni depender de las clases concretas de pedido.

Polimorfismo:

Sobrecarga: asignarRepartidor() y asignarRepartidor(String nombreRepartidor) conviven en Pedido, ofreciendo una asignación automática o manual según el caso.
Sobrescritura: las tres subclases sobrescriben asignarRepartidor() y calcularTiempoEntrega() con su propia lógica. PedidoExpress además sobrescribe despachar(), ya que su despacho tiene una regla distinta (prioridad), mientras que PedidoComida y PedidoEncomienda reutilizan la implementación genérica definida en Pedido.
Cómo contribuye a escalabilidad

Agregar un nuevo tipo de pedido (por ejemplo, PedidoProgramado) no requiere modificar código existente: basta con crear una nueva subclase de Pedido que implemente calcularTiempoEntrega() y, si corresponde, sobrescriba los métodos que tengan una regla distinta. El resto del sistema ControladorDeEnvios, Main, el historial, las interfaces siguen funcionando sin cambios, porque todos dependen de la abstracción (Pedido y las interfaces), no de las clases concretas.

Cómo esto contribuye a reutilización:

Los atributos (idPedido, direccionEntrega, distanciaKm, historial) y los métodos concretos de Pedido (mostrarResumen(), registrarEvento(), despachar(), cancelar(), verHistorial()) se escriben una sola vez y se reutilizan en las tres subclases.

Cómo contribuye a mantenibilidad: Siendo un sistema modular, en el cual es más fácil la escalabilidad futura y corrección de errores.

Cada clase tiene una responsabilidad delimitada:

Pedido y sus subclases: representan los datos y el comportamiento de un pedido.
Las interfaces: definen contratos de comportamiento independientes de la implementación.
ControladorDeEnvios: coordina operaciones sobre los pedidos sin conocer sus clases concretas.
Main: solo simula casos de uso, sin contener lógica de negocio.

Esta separación permite modificar una parte del sistema con bajo riesgo de afectar las demás. Por ejemplo, cambiar cómo se calcula el tiempo de entrega de PedidoExpress solo requiere tocar esa clase cambiar cómo se despacha un pedido en general solo requiere tocar Pedido y agregar una nueva forma de rastrear pedidos solo requiere trabajar sobre la interfaz Rastreable y quienes la usan, sin tocar la lógica de cálculo de tiempos ni de asignación de repartidores.

Simulación (Main)

Main crea un pedido de cada tipo y demuestra:

PedidoComida: asignación automática y manual de repartidor, cálculo de tiempo, despacho y consulta de historial.
PedidoEncomienda: mismo flujo que el anterior, con su propia regla de cálculo de tiempo.
PedidoExpress: asignación de repartidor, cálculo de tiempo, despacho (con su comportamiento sobrescrito de prioridad) y posterior cancelación, mostrando que un pedido puede cambiar de estado más de una vez y que el historial registra toda la secuencia.
Cómo ejecutar
Abrir el proyecto en IntelliJ IDEA.
Ejecutar la clase Main (cl.fastspeed.app.Main).
La salida por consola muestra, para cada pedido, su resumen, tiempo estimado, despacho/cancelación y su historial de eventos.