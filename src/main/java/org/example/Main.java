package org.example;

import javax.swing.SwingUtilities;
public class Main {
    public static void main(String[] args) {
        // Se asegura de que la interfaz gráfica se cree y se muestre
        // en el hilo de despacho de eventos (EDT), que es la práctica recomendada para Swing.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Crea una instancia de nuestra ventana y la hace visible
                new ConversorFrame().setVisible(true);
            }
        });
    }
}