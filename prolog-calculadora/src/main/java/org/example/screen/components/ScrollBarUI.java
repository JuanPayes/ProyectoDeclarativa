package org.example.screen.components;

import org.example.screen.Theme;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

public class ScrollBarUI extends BasicScrollBarUI {

    private final java.awt.Color pista;

    public ScrollBarUI(java.awt.Color pista) {
        this.pista = pista;
    }

    @Override
    protected void configureScrollBarColors() {
        thumbColor = Theme.COPPER;
        trackColor = pista;
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return cero();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return cero();
    }

    private static JButton cero() {
        JButton b = new JButton();
        b.setPreferredSize(new Dimension(0, 0));
        b.setBorder(null);
        return b;
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(10, 10);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        g.setColor(pista);
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
        if (r.isEmpty()) return;
        g.setColor(Theme.COPPER);
        g.fillRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4);
    }
}