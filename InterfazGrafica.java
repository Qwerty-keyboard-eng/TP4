import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InterfazGrafica extends JFrame {
    private JProgressBar barraBuffer;
    private JTextArea areaLog;
    private JLabel etiquetaEstado;
    private JLabel etiquetaPorcentaje;
    private JLabel etiquetaTotalProducidos; // Nueva etiqueta

    // Colores Dashboard Moderno
    private final Color COLOR_FONDO = new Color(15, 23, 42);      
    private final Color COLOR_TARJETA = new Color(30, 41, 59);    
    private final Color COLOR_ACENTO = new Color(239, 64, 64);    // Rojo UNRN
    private final Color COLOR_TEXTO = new Color(248, 250, 252);   
    private final Color COLOR_LOG = new Color(2, 6, 23);          

    public InterfazGrafica(int capacidadMaxima) {
        setTitle("UNRN - Monitor de Concurrencia | Facundo Molina");
        setSize(800, 650); // Subimos un toque el alto
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout(20, 20));

        // 1. PANEL SUPERIOR (Header)
        JPanel panelHeader = new JPanel(new GridLayout(3, 1, 5, 5)); // 3 filas ahora
        panelHeader.setBackground(COLOR_FONDO);
        panelHeader.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel titulo = new JLabel("SISTEMA PRODUCTOR-CONSUMIDOR", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(COLOR_TEXTO);

        etiquetaEstado = new JLabel("Estado: Inicializando...", SwingConstants.CENTER);
        etiquetaEstado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        etiquetaEstado.setForeground(new Color(148, 163, 184));

        // Configuración del nuevo contador total
        etiquetaTotalProducidos = new JLabel("Total Elementos Producidos: 0", SwingConstants.CENTER);
        etiquetaTotalProducidos.setFont(new Font("Segoe UI", Font.BOLD, 15));
        etiquetaTotalProducidos.setForeground(new Color(56, 189, 248)); // Un celeste fachero para diferenciarlo

        panelHeader.add(titulo);
        panelHeader.add(etiquetaEstado);
        panelHeader.add(etiquetaTotalProducidos); // Lo sumamos al panel
        add(panelHeader, BorderLayout.NORTH);

        // 2. PANEL CENTRAL (Monitor Visual)
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(COLOR_FONDO);
        
        JPanel tarjetaBuffer = new JPanel();
        tarjetaBuffer.setBackground(COLOR_TARJETA);
        tarjetaBuffer.setLayout(new BoxLayout(tarjetaBuffer, BoxLayout.Y_AXIS));
        tarjetaBuffer.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        JLabel lblBuffer = new JLabel("Capacidad Actual del Buffer");
        lblBuffer.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblBuffer.setForeground(COLOR_TEXTO);
        lblBuffer.setFont(new Font("Segoe UI", Font.BOLD, 16));

        barraBuffer = new JProgressBar(0, capacidadMaxima);
        barraBuffer.setPreferredSize(new Dimension(400, 40));
        barraBuffer.setMaximumSize(new Dimension(400, 40));
        barraBuffer.setForeground(COLOR_ACENTO);
        barraBuffer.setBackground(COLOR_FONDO);
        barraBuffer.setBorderPainted(false);
        barraBuffer.setStringPainted(false);

        etiquetaPorcentaje = new JLabel("0%");
        etiquetaPorcentaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        etiquetaPorcentaje.setForeground(COLOR_ACENTO);
        etiquetaPorcentaje.setFont(new Font("Segoe UI", Font.BOLD, 30));

        tarjetaBuffer.add(lblBuffer);
        tarjetaBuffer.add(Box.createRigidArea(new Dimension(0, 15)));
        tarjetaBuffer.add(barraBuffer);
        tarjetaBuffer.add(Box.createRigidArea(new Dimension(0, 10)));
        tarjetaBuffer.add(etiquetaPorcentaje);

        panelCentral.add(tarjetaBuffer);
        add(panelCentral, BorderLayout.CENTER);

        // 3. PANEL INFERIOR (Log de Consola)
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(COLOR_FONDO);
        panelInferior.setBorder(new EmptyBorder(0, 25, 25, 25));

        areaLog = new JTextArea();
        areaLog.setBackground(COLOR_LOG);
        areaLog.setForeground(new Color(16, 185, 129)); 
        areaLog.setFont(new Font("Consolas", Font.PLAIN, 13));
        areaLog.setMargin(new Insets(10, 10, 10, 10));
        areaLog.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaLog);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_TARJETA));
        scroll.setPreferredSize(new Dimension(0, 180));
        
        panelInferior.add(scroll, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setLocationRelativeTo(null); 
        setVisible(true);
    }

    // Modificamos el método para que acepte el totalProducido
    public void actualizar(int cantidadActual, int totalProducido, String mensaje) {
        SwingUtilities.invokeLater(() -> {
            barraBuffer.setValue(cantidadActual);
            int porcentaje = (int) ((cantidadActual / (double) barraBuffer.getMaximum()) * 100);
            etiquetaPorcentaje.setText(porcentaje + "%");
            
            // Actualizamos el contador total en la UI
            etiquetaTotalProducidos.setText("Total Elementos Producidos: " + totalProducido);
            
            if (porcentaje >= 100) {
                etiquetaPorcentaje.setForeground(Color.ORANGE);
                etiquetaEstado.setText("Estado: BUFFER LLENO - Productor en espera");
            } else if (porcentaje == 0) {
                etiquetaEstado.setText("Estado: BUFFER VACÍO - Consumidor en espera");
            } else {
                etiquetaPorcentaje.setForeground(COLOR_ACENTO);
                etiquetaEstado.setText("Estado: Procesando hilos...");
            }

            areaLog.append(" > " + mensaje + "\n");
            areaLog.setCaretPosition(areaLog.getDocument().getLength());
        });
    }
}