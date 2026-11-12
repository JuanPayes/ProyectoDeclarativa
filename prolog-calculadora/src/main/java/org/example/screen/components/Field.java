package org.example.screen.components;

import org.example.screen.Theme;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;

public class Field extends JPanel {

    private final Rotulo etiqueta;
    private final TextField campo;

    public Field(String etiqueta, String ayuda) {
        this.etiqueta = new Rotulo(etiqueta.toUpperCase(), Theme.sans(Font.BOLD, 12, 0.14f), Theme.MAHOGANY);
        this.campo = new TextField(ayuda);

        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(this.etiqueta);
        add(javax.swing.Box.createVerticalStrut(6));
        add(this.campo);
    }

    public String getText() {
        return campo.getText();
    }

    public void setText(String t) {
        campo.setText(t);
    }

    public void addActionListener(ActionListener l) {
        campo.addActionListener(l);
    }

    @Override
    public void setEnabled(boolean activo) {
        super.setEnabled(activo);
        campo.setEnabled(activo);
        etiqueta.setForeground(activo ? Theme.MAHOGANY : Theme.COPPER);
    }
}