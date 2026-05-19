import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Buffer {
    private final Queue<Integer> cola = new LinkedList<>();
    private final int CAPACIDAD = 5;
    private int totalProducidos = 0; // NUEVO: Contador acumulativo
    private InterfazGrafica ui; 

    private Semaphore empty = new Semaphore(CAPACIDAD);
    private Semaphore full = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);

    public Buffer(InterfazGrafica ui) {
        this.ui = ui;
    }

    public void producir(int valor) throws InterruptedException {
        empty.acquire();
        mutex.acquire();

        cola.add(valor);
        totalProducidos++; // Incrementamos el global de producción
        
        String msg = "Producido: " + valor;
        System.out.println(msg);
        
        // Pasamos el tamaño actual de la cola Y el acumulado total
        ui.actualizar(cola.size(), totalProducidos, msg); 

        mutex.release();
        full.release();
    }

    public int consumir() throws InterruptedException {
        full.acquire();
        mutex.acquire();

        int valor = cola.poll();
        String msg = "Consumido: " + valor;
        System.out.println(msg);
        
        // Al consumir, el totalProducidos no cambia, pero lo seguimos mandando para mantener la UI al día
        ui.actualizar(cola.size(), totalProducidos, msg); 

        mutex.release();
        empty.release();
        
        return valor;
    }
}