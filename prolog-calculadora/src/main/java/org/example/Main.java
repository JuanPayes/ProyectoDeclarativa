package org.example;

import org.example.engine.Connection;
import org.example.screen.Window;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Texto suavizado en todos los sistemas
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            Connection motor = new Connection();
            SwingUtilities.invokeLater(() -> new Window(motor).setVisible(true));
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "No se pudo iniciar el motor de Prolog:\n" + ex.getMessage(),
                    "Calculadora de integrales", JOptionPane.ERROR_MESSAGE);
        }
    }
}