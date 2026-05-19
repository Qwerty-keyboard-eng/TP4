public class Principal {
    public static void main(String[] args) {
        int capacidad = 5;
        
        // 1. Creamos la interfaz primero
        InterfazGrafica gui = new InterfazGrafica(capacidad);

        // 2. Le pasamos la interfaz al buffer
        Buffer bufferCompartido = new Buffer(gui);

        // 3. Lanzamos los hilos
        Thread p = new Thread(new Productor(bufferCompartido));
        Thread c = new Thread(new Consumidor(bufferCompartido));

        p.start();
        c.start();
    }
}