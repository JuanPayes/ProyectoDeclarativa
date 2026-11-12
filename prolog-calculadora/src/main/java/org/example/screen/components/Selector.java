package org.example.screen.components;

import org.example.engine.Integral;
import org.example.screen.Theme;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

public class Selector extends ShadowBox {

    private final JComboBox<Integral> combo;

    public Selector(Integral[] items) {
        this(crear(items));
    }

    private Selector(JComboBox<Integral> combo) {
        super(combo);
        this.combo = combo;
    }

    public Integral getSeleccion() {
        return (Integral) combo.getSelectedItem();
    }

    public void setSeleccion(Integral i) {
        combo.setSelectedItem(i);
    }

    public void onCambio(Runnable r) {
        combo.addActionListener(e -> r.run());
    }

    // ------------------------------------------------------------------
    private static JComboBox<Integral> crear(Integral[] items) {
        JComboBox<Integral> c = new JComboBox<>(items);
        c.setUI(new EstiloCombo());
        c.setBorder(null);
        c.setBackground(Theme.FIELD);
        c.setMaximumRowCount(10);
        c.setPreferredSize(new Dimension(200, 46));
        c.setRenderer((lista, valor, indice, seleccionado, foco) ->
                new Item(valor == null ? "" : valor.toString(), indice, seleccionado));
        return c;
    }

    private static final class Item extends JPanel {
        private final boolean enLista;
        private final boolean seleccionado;

        Item(String texto, int indice, boolean seleccionado) {
            super(new BorderLayout());
            this.enLista = indice >= 0;
            this.seleccionado = seleccionado;
            Color fg = (enLista && seleccionado) ? Theme.LINEN : Theme.IMPERIAL;
            Rotulo r = new Rotulo(texto, Theme.sans(Font.BOLD, 20), fg);
            setBorder(new EmptyBorder(0, 12, 0, 12));
            add(r, BorderLayout.CENTER);
            setOpaque(false);
            setPreferredSize(new Dimension(100, enLista ? 40 : 46));
        }

        // El selector asigna colores por su cuenta; aquí los ignoramos.
        @Override
        public void setForeground(Color c) {
        }

        @Override
        public void setBackground(Color c) {
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (enLista) {
                g.setColor(seleccionado ? Theme.IMPERIAL : Theme.FIELD);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    private static final class EstiloCombo extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            return new Flecha();
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    JScrollPane sp = super.createScroller();
                    sp.setBorder(null);
                    sp.getVerticalScrollBar().setUI(new ScrollBarUI(Theme.FIELD));
                    return sp;
                }
            };
            popup.setBorder(new LineBorder(Theme.MAHOGANY, 3));
            return popup;
        }
    }

    private static final class Flecha extends JButton {
        Flecha() {
            setBorder(null);
            setContentAreaFilled(false);
            setFocusable(false);
            setOpaque(false);
            setPreferredSize(new Dimension(44, 0));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isEnabled() ? Theme.MAHOGANY : Theme.COPPER);
            g2.setStroke(new BasicStroke(2.6f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            g2.drawPolyline(new int[]{cx - 6, cx, cx + 6}, new int[]{cy - 3, cy + 3, cy - 3}, 3);
            g2.dispose();
        }
    }
}