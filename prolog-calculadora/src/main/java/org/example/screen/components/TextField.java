package org.example.screen.components;

import org.example.screen.Theme;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;

public class TextField extends ShadowBox {

    private final Campo campo;

    public TextField(String ayuda) {
        this(new Campo(ayuda));
    }

    private TextField(Campo campo) {
        super(campo);
        this.campo = campo;
    }

    public String getText() {
        return campo.getText();
    }

    public void setText(String texto) {
        campo.setText(texto);
    }

    public void addActionListener(ActionListener l) {
        campo.addActionListener(l);
    }

    @Override
    public boolean requestFocusInWindow() {
        return campo.requestFocusInWindow();
    }

    private static final class Campo extends JTextField {
        private final String ayuda;

        Campo(String ayuda) {
            this.ayuda = ayuda;
            setFont(Theme.mono(java.awt.Font.PLAIN, 18));
            setForeground(Theme.IMPERIAL);
            setDisabledTextColor(Theme.COPPER);
            setCaretColor(Theme.IMPERIAL);
            setSelectionColor(Theme.GLAUCOUS);
            setSelectedTextColor(Theme.LINEN);
            setBorder(new EmptyBorder(0, 14, 0, 14));
            setPreferredSize(new Dimension(120, 42));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? Theme.PLACEHOLDER : Theme.COPPER);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(ayuda, getInsets().left, y);
                g2.dispose();
            }
        }
    }
}