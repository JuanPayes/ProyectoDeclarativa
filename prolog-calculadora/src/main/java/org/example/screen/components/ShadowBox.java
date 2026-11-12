package org.example.screen.components;

import org.example.screen.Theme;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeListener;

public class ShadowBox extends JPanel {

    static final int SOMBRA = 5;
    static final int BORDE = 3;

    private boolean conFoco;

    private final PropertyChangeListener alCambiarFoco = e -> {
        Object nuevo = e.getNewValue();
        boolean ahora = nuevo instanceof Component && SwingUtilities.isDescendingFrom((Component) nuevo, this);
        if (ahora != conFoco) {
            conFoco = ahora;
            repaint();
        }
    };

    protected ShadowBox(JComponent contenido) {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(BORDE, BORDE, BORDE + SOMBRA, BORDE + SOMBRA));
        contenido.setOpaque(false);
        add(contenido, BorderLayout.CENTER);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addPropertyChangeListener("permanentFocusOwner", alCambiarFoco);
    }

    @Override
    public void removeNotify() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .removePropertyChangeListener("permanentFocusOwner", alCambiarFoco);
        super.removeNotify();
    }

    @Override
    public void setEnabled(boolean activo) {
        super.setEnabled(activo);
        for (Component c : getComponents()) c.setEnabled(activo);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        int w = getWidth() - SOMBRA;
        int h = getHeight() - SOMBRA;

        if (conFoco && isEnabled()) {
            g2.setColor(Theme.COPPER);
            g2.fillRect(SOMBRA, SOMBRA, w, h);
        }
        Color borde = !isEnabled() ? Theme.COPPER : conFoco ? Theme.IMPERIAL : Theme.MAHOGANY;
        g2.setColor(borde);
        g2.fillRect(0, 0, w, h);
        g2.setColor(isEnabled() ? Theme.FIELD : Theme.LINEN);
        g2.fillRect(BORDE, BORDE, w - 2 * BORDE, h - 2 * BORDE);
        g2.dispose();
    }
}