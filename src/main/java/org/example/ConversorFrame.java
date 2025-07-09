package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// --- IMPORTACIONES NUEVAS ---
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
// --- FIN DE IMPORTACIONES NUEVAS ---
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConversorFrame extends JFrame {

    // Componentes de la interfaz gráfica
    private JComboBox<String> comboDivisaOrigen;
    private JComboBox<String> comboDivisaDestino;
    private JTextField campoCantidad;
    private JButton botonConvertir;
    private JLabel etiquetaResultado;
    private JTextArea areaHistorial;

    public ConversorFrame() {
        // --- 1. Configuración de la ventana principal (JFrame) ---
        setTitle("Conversor de Monedas");
        setSize(500, 600); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setLayout(new BorderLayout(10, 10)); // Diseño principal con espaciado

        // --- 2. Creación del panel de controles (parte superior) ---
        JPanel panelControles = new JPanel(new GridLayout(4, 2, 5, 5)); // Rejilla para organizar
        panelControles.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Etiquetas y campos
        panelControles.add(new JLabel("Moneda de Origen:"));
        comboDivisaOrigen = new JComboBox<>();
        panelControles.add(comboDivisaOrigen);

        panelControles.add(new JLabel("Moneda de Destino:"));
        comboDivisaDestino = new JComboBox<>();
        panelControles.add(comboDivisaDestino);

        panelControles.add(new JLabel("Cantidad a Convertir:"));
        campoCantidad = new JTextField();
        panelControles.add(campoCantidad);

        // Botón
        botonConvertir = new JButton("Convertir");
        panelControles.add(new JLabel()); // Celda vacía para alinear el botón
        panelControles.add(botonConvertir);

        // --- 3. Creación del panel de resultado (centro) ---
        JPanel panelResultado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelResultado.setBorder(BorderFactory.createTitledBorder("Resultado"));
        etiquetaResultado = new JLabel("0.00");
        etiquetaResultado.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente más grande para el resultado
        panelResultado.add(etiquetaResultado);

        // --- 4. Creación del panel de historial (abajo) ---
        areaHistorial = new JTextArea(10, 40);
        areaHistorial.setEditable(false); // El usuario no puede escribir aquí
        JScrollPane panelScrollHistorial = new JScrollPane(areaHistorial); // Panel con barra de scroll
        panelScrollHistorial.setBorder(BorderFactory.createTitledBorder("Historial de Conversiones"));
        panelScrollHistorial.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 5. Añadir todos los paneles a la ventana principal ---
        add(panelControles, BorderLayout.NORTH);
        add(panelResultado, BorderLayout.CENTER);
        add(panelScrollHistorial, BorderLayout.SOUTH);

        // --- 6. Cargar las divisas en los menús desplegables ---
        cargarDivisas();

        // --- 7. Añadir la lógica al botón "Convertir" ---
        botonConvertir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertirMoneda();
            }
        });
    }

    private void cargarDivisas() {
        try {
            // Llamamos a nuestro método de la API para obtener las divisas
            List<String> divisas = ClienteHTTPDivisas.obtenerDivisasSoportadas();
            for (String divisa : divisas) {
                comboDivisaOrigen.addItem(divisa);
                comboDivisaDestino.addItem(divisa);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar las divisas: " + e.getMessage(), "Error de Red", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void convertirMoneda() {
        // Obtener los datos de la interfaz
        String origen = (String) comboDivisaOrigen.getSelectedItem();
        String destino = (String) comboDivisaDestino.getSelectedItem();
        String textoCantidad = campoCantidad.getText();

        // Validar que la cantidad no esté vacía
        if (textoCantidad.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese una cantidad.", "Error de Entrada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double cantidad = Double.parseDouble(textoCantidad);

            // Llamar a la lógica de conversión
            double tasa = ClienteHTTPDivisas.obtenerTasaCambio(origen, destino);
            double resultado = cantidad * tasa;

            // Actualizar la etiqueta del resultado
            etiquetaResultado.setText(String.format("%.2f %s", resultado, destino));

            // Registrar en el historial
            registrarEnHistorial(cantidad, origen, resultado, destino);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad ingresada no es un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al realizar la conversión: " + ex.getMessage(), "Error de Conversión", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarEnHistorial(double cantidadOrigen, String divisaOrigen, double cantidadDestino, String divisaDestino) {
        // Formatear la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHora = ahora.format(formato);

        // Crear la línea de texto para el historial
        String registro = String.format("[%s] - %.2f %s -> %.2f %s%n",
                fechaHora, cantidadOrigen, divisaOrigen, cantidadDestino, divisaDestino);

        // Añadir el registro al área de texto de la ventana
        areaHistorial.append(registro);

        // --- NUEVA LÍNEA ---
        // Llamamos al nuevo método para guardar este mismo registro en un archivo
        guardarRegistroEnArchivo(registro);
    }

    // --- MÉTODO COMPLETAMENTE NUEVO ---
    // Este método se encarga de escribir en el archivo
    private void guardarRegistroEnArchivo(String registro) {
        // El 'true' en FileWriter hace que se añada al final del archivo en vez de sobreescribirlo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("historial_conversiones.txt", true))) {
            writer.write(registro);
        } catch (IOException e) {
            // Mostramos un popup si hay un error al guardar el archivo
            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar la conversión en el archivo de historial.",
                    "Error de Archivo",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Imprime el error detallado en la consola para depuración
        }
    }
}