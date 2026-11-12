package org.example.screen.components;

import org.example.screen.Theme;

import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;

public class Buttons extends JButton {

    private static final int SOMBRA = 5;   // desplazamiento de la sombra
    private static final int SUBE = 2;     // cuánto se levanta en hover
    private static final int BORDE = 3;

    private final Color fondo;
    private final Color colorTexto;
    private final Font fuente;
    private final int padH;
    private final int alto;
    private boolean hover;
    private boolean clicado;

    public Buttons(String texto, Color fondo, Color colorTexto, int padH, int alto, float tam) {
        super(texto);
        this.fondo = fondo;
        this.colorTexto = colorTexto;
        this.padH = padH;
        this.alto = alto;
        this.fuente = Theme.sans(Font.BOLD, tam, 0.12f);

        setBorder(null);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                clicado = true; // el aro de foco solo se ve con teclado
            }
        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                clicado = false;
                repaint();
            }
        });
    }

    private TextLayout trazo() {
        return new TextLayout(getText().toUpperCase(), fuente, Theme.FRC);
    }

    @Override
    public Dimension getPreferredSize() {
        int ancho = (int) Math.ceil(trazo().getAdvance()) + 2 * padH + 2 * BORDE;
        return new Dimension(ancho + SOMBRA + SUBE, alto + SOMBRA + SUBE);
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        int bw = getWidth() - SOMBRA - SUBE;
        int bh = getHeight() - SOMBRA - SUBE;
        boolean levantado = hover && !getModel().isPressed();
        int x = levantado ? 0 : SUBE;
        int y = levantado ? 0 : SUBE;

        if (levantado) {
            g2.setColor(Theme.COPPER);
            g2.fillRect(x + SOMBRA, y + SOMBRA, bw, bh);
        }
        g2.setColor(Theme.MAHOGANY);
        g2.fillRect(x, y, bw, bh);
        g2.setColor(fondo);
        g2.fillRect(x + BORDE, y + BORDE, bw - 2 * BORDE, bh - 2 * BORDE);

        TextLayout tl = trazo();
        float tx = x + (bw - tl.getAdvance()) / 2f;
        float ty = y + (bh + tl.getAscent() - tl.getDescent()) / 2f;
        g2.setColor(colorTexto);
        tl.draw(g2, tx, ty);

        if (isFocusOwner() && !clicado) {
            g2.setColor(Theme.COPPER);
            g2.setStroke(new BasicStroke(3f));
            g2.drawRect(1, 1, getWidth() - SOMBRA - 3, getHeight() - SOMBRA - 3);
        }
        g2.dispose();
    }
}