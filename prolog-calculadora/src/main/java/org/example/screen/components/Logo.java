package org.example.screen.components;

import org.example.screen.Theme;

import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Logo extends JComponent {

    public Logo() {
        setOpaque(false);
        Dimension d = new Dimension(72, 72);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Theme.IMPERIAL);
        g2.fillRect(0, 0, 72, 72);
        g2.setColor(Theme.COPPER);
        g2.fillOval(8, 8, 36, 36);
        g2.setColor(Theme.LINEN);
        g2.fillPolygon(new int[]{30, 52, 70}, new int[]{70, 34, 70}, 3);
        g2.dispose();
    }
}