package org.example.screen.components;

import org.example.screen.Theme;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class TitleBar extends JPanel {

    private enum Tipo { MINIMIZAR, MAXIMIZAR, CERRAR }

    private Point origen;

    public TitleBar(JFrame ventana, String titulo) {
        super(new GridBagLayout());
        setBackground(Theme.MAHOGANY);
        setPreferredSize(new Dimension(100, 46));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.setOpaque(false);
        botones.add(boton(Tipo.MINIMIZAR, Theme.GLAUCOUS, "Minimizar",
                () -> ventana.setState(JFrame.ICONIFIED)));
        botones.add(boton(Tipo.MAXIMIZAR, Theme.LINEN, "Maximizar", () -> alternarMaximo(ventana)));
        botones.add(boton(Tipo.CERRAR, Theme.COPPER, "Cerrar", ventana::dispose));

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        add(new JPanel() {{ setOpaque(false); }}, c);

        c.gridx = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        add(new Rotulo(titulo, Theme.sans(Font.BOLD, 15, 0.06f), Theme.LINEN), c);

        c.gridx = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(botones, c);

        MouseAdapter arrastre = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                origen = e.getPoint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (origen == null || (ventana.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0) return;
                ventana.setLocation(e.getXOnScreen() - origen.x, e.getYOnScreen() - origen.y);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) alternarMaximo(ventana);
            }
        };
        addMouseListener(arrastre);
        addMouseMotionListener(arrastre);
    }

    private static void alternarMaximo(JFrame v) {
        boolean max = (v.getExtendedState() & JFrame.MAXIMIZED_BOTH) != 0;
        v.setExtendedState(max ? JFrame.NORMAL : JFrame.MAXIMIZED_BOTH);
    }

    private static JButton boton(Tipo tipo, Color fondo, String descripcion, Runnable accion) {
        JButton b = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(fondo);
                g2.fillRect(0, 0, getWidth(), getHeight());
                Color trazo = tipo == Tipo.MAXIMIZAR ? Theme.MAHOGANY : Theme.LINEN;
                g2.setColor(trazo);
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                switch (tipo) {
                    case MINIMIZAR -> {
                        g2.setStroke(new BasicStroke(2.4f));
                        g2.drawLine(cx - 5, cy + 3, cx + 5, cy + 3);
                    }
                    case MAXIMIZAR -> {
                        g2.setStroke(new BasicStroke(2.2f));
                        g2.drawRect(cx - 4, cy - 4, 9, 9);
                    }
                    case CERRAR -> {
                        g2.setStroke(new BasicStroke(2.4f));
                        g2.drawLine(cx - 4, cy - 4, cx + 4, cy + 4);
                        g2.drawLine(cx + 4, cy - 4, cx - 4, cy + 4);
                    }
                }
                g2.dispose();
            }
        };
        b.setPreferredSize(new Dimension(28, 28));
        b.setBorder(null);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setFocusable(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.getAccessibleContext().setAccessibleName(descripcion);
        b.setToolTipText(descripcion);
        b.addActionListener(e -> accion.run());
        return b;
    }
}