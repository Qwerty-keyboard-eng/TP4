public class Principal {
    public static void main(String[] args) {
        Buffer bufferCompartido = new Buffer();

        Thread p = new Thread(new Productor(bufferCompartido));
        Thread c = new Thread(new Consumidor(bufferCompartido));

        p.start();
        c.start();
    }
}