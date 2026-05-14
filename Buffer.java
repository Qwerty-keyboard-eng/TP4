import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Buffer {
    private final Queue<Integer> cola = new LinkedList<>();
    private final int CAPACIDAD = 5;

    // Semáforo para espacios vacíos (arranca en CAPACIDAD porque todo está vacío)
    private Semaphore empty = new Semaphore(CAPACIDAD);
    // Semáforo para productos listos (arranca en 0 porque no hay nada)
    private Semaphore full = new Semaphore(0);
    // Semáforo para exclusión mutua (solo 1 a la vez toca la cola)
    private Semaphore mutex = new Semaphore(1);

    public void producir(int valor) throws InterruptedException {
        empty.acquire(); // Decrementa espacios vacíos. Si es 0, espera.
        mutex.acquire(); // Entra a la zona crítica

        cola.add(valor);
        System.out.println("Producido: " + valor + " | En buffer: " + cola.size());

        mutex.release(); // Sale de la zona crítica
        full.release();  // Incrementa productos listos. Avisa al consumidor.
    }

    public int consumir() throws InterruptedException {
        full.acquire();  // Espera a que haya un producto listo.
        mutex.acquire(); // Entra a la zona crítica

        int valor = cola.poll();
        System.out.println("Consumido: " + valor + " | En buffer: " + cola.size());

        mutex.release(); // Sale de la zona crítica
        empty.release(); // Incrementa espacios vacíos. Avisa al productor.
        
        return valor;
    }
}