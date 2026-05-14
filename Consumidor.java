public class Consumidor implements Runnable {
    private Buffer buffer;

    public Consumidor(Buffer b) { this.buffer = b; }

    @Override
    public void run() {
        try {
            while (true) {
                buffer.consumir();
                Thread.sleep(1500); 
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}