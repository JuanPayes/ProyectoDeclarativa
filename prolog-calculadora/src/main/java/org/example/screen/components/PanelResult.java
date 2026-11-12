package org.example.screen.components;

import org.example.screen.Theme;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

public class PanelResult extends JPanel {

    private final JTextPane texto = new JTextPane();
    private final Buttons btnCopiar = new Buttons("Copiar", Theme.LINEN, Theme.MAHOGANY, 22, 44, 14);
    private final Font fuente = Theme.mono(Font.PLAIN, 16);

    public PanelResult() {
        super(new BorderLayout(0, 4));
        setOpaque(false);

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(new Rotulo("RESULTADO", Theme.sans(Font.BOLD, 12, 0.14f), Theme.MAHOGANY), BorderLayout.WEST);
        cabecera.add(btnCopiar, BorderLayout.EAST);

        texto.setEditable(false);
        texto.setOpaque(true);
        texto.setBackground(Theme.MAHOGANY);
        texto.setForeground(Theme.LINEN);
        texto.setFont(fuente);
        texto.setSelectionColor(Theme.GLAUCOUS);
        texto.setSelectedTextColor(Theme.LINEN);
        texto.setBorder(new EmptyBorder(14, 20, 14, 20));

        JScrollPane scroll = new JScrollPane(texto);
        scroll.setBorder(BorderFactory.createMatteBorder(8, 0, 0, 0, Theme.COPPER));
        scroll.getViewport().setBackground(Theme.MAHOGANY);
        scroll.getVerticalScrollBar().setUI(new ScrollBarUI(Theme.MAHOGANY));
        scroll.getHorizontalScrollBar().setUI(new ScrollBarUI(Theme.MAHOGANY));

        add(cabecera, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public void onCopiar(ActionListener l) {
        btnCopiar.addActionListener(l);
    }

    public void setTextoBoton(String t) {
        btnCopiar.setText(t);
    }

    public String getTexto() {
        return texto.getText();
    }

    public void setTexto(String contenido) {
        StyledDocument doc = texto.getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            int i = 0;
            while (i < contenido.length()) {
                boolean propia = fuente.canDisplay(contenido.charAt(i));
                int j = i + 1;
                while (j < contenido.length() && fuente.canDisplay(contenido.charAt(j)) == propia) j++;
                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setFontFamily(attrs, propia ? fuente.getFamily() : Font.DIALOG);
                StyleConstants.setFontSize(attrs, 16);
                StyleConstants.setForeground(attrs, Theme.LINEN);
                doc.insertString(doc.getLength(), contenido.substring(i, j), attrs);
                i = j;
            }
            SimpleAttributeSet parrafo = new SimpleAttributeSet();
            StyleConstants.setLineSpacing(parrafo, 0.55f);
            doc.setParagraphAttributes(0, doc.getLength(), parrafo, false);
            texto.setCaretPosition(0);
        } catch (BadLocationException e) {
            texto.setText(contenido);
        }
    }

}