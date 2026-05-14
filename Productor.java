public class Productor implements Runnable {
    private Buffer buffer;

    public Productor(Buffer b) { this.buffer = b; }

    @Override
    public void run() {
        int i = 0;
        try {
            while (true) {
                buffer.producir(i++);
                Thread.sleep(500); 
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}