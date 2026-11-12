package org.example.screen.components;

import org.example.screen.Theme;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;

public class Rotulo extends JComponent {

    private String texto;
    private final Font fuente;
    private TextLayout cache;

    public Rotulo(String texto, Font fuente, Color color) {
        this.texto = texto;
        this.fuente = fuente;
        setForeground(color);
        setOpaque(false);
        setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public void setTexto(String nuevo) {
        this.texto = nuevo;
        this.cache = null;
        revalidate();
        repaint();
    }

    private TextLayout trazo() {
        if (cache == null && !texto.isEmpty()) {
            cache = new TextLayout(Theme.mezclar(texto, fuente).getIterator(), Theme.FRC);
        }
        return cache;
    }

    @Override
    public Dimension getPreferredSize() {
        TextLayout l = trazo();
        if (l == null) return new Dimension(0, Math.round(fuente.getSize2D()));
        return new Dimension((int) Math.ceil(l.getAdvance()),
                (int) Math.ceil(l.getAscent() + l.getDescent() + l.getLeading()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        TextLayout l = trazo();
        if (l == null) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2.setColor(getForeground());
        float alto = l.getAscent() + l.getDescent();
        float y = (getHeight() - alto) / 2f + l.getAscent();
        l.draw(g2, 0, y);
        g2.dispose();
    }
}