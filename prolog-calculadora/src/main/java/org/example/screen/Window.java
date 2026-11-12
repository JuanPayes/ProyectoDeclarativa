package org.example.screen;

import org.example.engine.EntradaInvalidaException;
import org.example.engine.Integral;
import org.example.engine.Motor;
import org.example.engine.Parametro;
import org.example.engine.Resultado;
import org.example.engine.Solicitud;
import org.example.engine.Validador;
import org.example.screen.components.Buttons;
import org.example.screen.components.Field;
import org.example.screen.components.Logo;
import org.example.screen.components.PanelResult;
import org.example.screen.components.Rotulo;
import org.example.screen.components.Selector;
import org.example.screen.components.TitleBar;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

public class Window extends JFrame {

    private static final String TITULO = "Calculadora de integrales";
    private static final String AYUDA_INICIAL = "Elige una integral, escribe los límites y presiona Calcular.";
    private static final int TIMEOUT_MS = 10_000;
    private static final int ALTO_BASE = 740;
    private static final int ALTO_MIN_RESULTADO = 170;

    private final Motor motor;

    private final Selector selector = new Selector(Integral.values());
    private final JLabel lblAyuda = new JLabel(" ");
    private final Field fLo = new Field("Límite a", "Ej: 0");
    private final Field fHi = new Field("Límite b", "Ej: 5");
    private final Field fK = new Field("Constante k", "Ej: 3");
    private final Field fN = new Field("Exponente n (entero)", "Ej: 2");
    private final Field fBase = new Field("Constante a (base)", "Ej: 2");
    private final Field fU = new Field("Función u(x)", "Ej: x^2 + 1");
    private final Field fV = new Field("Función v(x)", "Ej: sin(x)");
    private final JPanel rejilla = new JPanel(new GridLayout(0, 2, 18, 9));
    private final Buttons btnCalcular = new Buttons("Calcular", Theme.IMPERIAL, Theme.LINEN, 32, 52, 17);
    private final Buttons btnLimpiar = new Buttons("Limpiar", Theme.LINEN, Theme.MAHOGANY, 28, 52, 17);
    private final PanelResult panelResultado = new PanelResult();

    public Window(Motor motor) {
        super(TITULO);
        this.motor = motor;

        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(Theme.LINEN);
        raiz.setBorder(new LineBorder(Theme.MAHOGANY, 3));
        raiz.add(new TitleBar(this, TITULO), BorderLayout.NORTH);
        raiz.add(crearPrincipal(), BorderLayout.CENTER);
        setContentPane(raiz);

        conectarEventos();
        valoresIniciales();

        setMinimumSize(new Dimension(640, 640));
        setSize(640, ALTO_BASE);
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------------
    // Construcción de la pantalla
    // ------------------------------------------------------------------
    private JPanel bloqueSuperior;

    private int alturaBloqueSuperior() {
        return bloqueSuperior.getPreferredSize().height;
    }

    private JPanel crearPrincipal() {
        JPanel arriba = new JPanel();
        bloqueSuperior = arriba;
        arriba.setOpaque(false);
        arriba.setLayout(new BoxLayout(arriba, BoxLayout.Y_AXIS));
        agregar(arriba, crearEncabezado(), 16);
        agregar(arriba, crearSelector(), 16);
        agregar(arriba, rejilla, 11);
        agregar(arriba, crearAcciones(), 0);

        rejilla.setOpaque(false);

        panelResultado.setTexto(AYUDA_INICIAL);

        JPanel principal = new JPanel(new BorderLayout(0, 11));
        principal.setOpaque(false);
        principal.setBorder(new EmptyBorder(24, 32, 24, 32));
        principal.add(arriba, BorderLayout.NORTH);
        principal.add(panelResultado, BorderLayout.CENTER);
        return principal;
    }

    private JPanel crearEncabezado() {
        Rotulo titulo = new Rotulo("INTEGRALES", Theme.sans(Font.BOLD, 34, 0.02f), Theme.IMPERIAL);
        JLabel sub = new JLabel("Resueltas con reglas en Prolog");
        sub.setFont(Theme.sans(Font.PLAIN, 15));
        sub.setForeground(Theme.MAHOGANY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel texto = new JPanel();
        texto.setOpaque(false);
        texto.setLayout(new BoxLayout(texto, BoxLayout.Y_AXIS));
        texto.add(titulo);
        texto.add(Box.createVerticalStrut(4));
        texto.add(sub);

        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(new Logo());
        p.add(Box.createHorizontalStrut(18));
        p.add(texto);
        p.add(Box.createHorizontalGlue());
        return p;
    }

    private JPanel crearSelector() {
        lblAyuda.setFont(Theme.sans(Font.PLAIN, 14));
        lblAyuda.setForeground(Theme.MAHOGANY);
        lblAyuda.setAlignmentX(Component.LEFT_ALIGNMENT);
        selector.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(new Rotulo("INTEGRAL", Theme.sans(Font.BOLD, 12, 0.14f), Theme.MAHOGANY));
        p.add(Box.createVerticalStrut(6));
        p.add(selector);
        p.add(Box.createVerticalStrut(1));
        p.add(lblAyuda);
        return p;
    }

    private JPanel crearAcciones() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(btnCalcular);
        p.add(Box.createHorizontalStrut(7)); // los botones ya reservan 7 px para la sombra
        p.add(btnLimpiar);
        p.add(Box.createHorizontalGlue());
        return p;
    }

    /**
     * Límites a y b, y k y n siempre visibles (k y n se atenúan si la integral no los usa).
     * base, u y v aparecen solo cuando la integral elegida los necesita.
     */
    private void refrescarParametros() {
        Integral i = selector.getSeleccion();
        lblAyuda.setText(i.ayuda());

        fK.setEnabled(i.requiere(Parametro.K));
        fN.setEnabled(i.requiere(Parametro.N));

        rejilla.removeAll();
        rejilla.add(fLo);
        rejilla.add(fHi);
        rejilla.add(fK);
        rejilla.add(fN);
        if (i.requiere(Parametro.BASE)) rejilla.add(fBase);
        if (i.requiere(Parametro.U)) rejilla.add(fU);
        if (i.requiere(Parametro.V)) rejilla.add(fV);
        rejilla.revalidate();
        rejilla.repaint();
        SwingUtilities.invokeLater(this::ajustarAltura);
    }

    private void ajustarAltura() {
        if ((getExtendedState() & MAXIMIZED_BOTH) != 0) return;
        int fijo = 46 + 6 + 48 + 11;                      // barra + borde + márgenes + separación
        int necesaria = fijo + alturaBloqueSuperior() + ALTO_MIN_RESULTADO;
        int tope = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
        int alto = Math.min(Math.max(ALTO_BASE, necesaria), tope);
        if (alto != getHeight()) setSize(getWidth(), alto);
    }

    // ------------------------------------------------------------------
    // Eventos
    // ------------------------------------------------------------------
    private void conectarEventos() {
        selector.onCambio(this::refrescarParametros);
        btnCalcular.addActionListener(e -> calcular());
        btnLimpiar.addActionListener(e -> limpiar());
        panelResultado.onCopiar(e -> copiar());
        for (Field f : new Field[]{fLo, fHi, fK, fN, fBase, fU, fV}) {
            f.addActionListener(e -> calcular()); // Enter calcula
        }
    }

    private void valoresIniciales() {
        fLo.setText("0");
        fHi.setText("2");
        fK.setText("3");
        fN.setText("2");
        fBase.setText("2");
        selector.setSeleccion(Integral.C);
        refrescarParametros();
    }

    private void limpiar() {
        for (Field f : new Field[]{fLo, fHi, fK, fN, fBase, fU, fV}) f.setText("");
        panelResultado.setTexto(AYUDA_INICIAL);
    }

    private void copiar() {
        String t = panelResultado.getTexto();
        if (t.isBlank() || t.equals(AYUDA_INICIAL)) return;
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(t), null);
        panelResultado.setTextoBoton("Copiado");
        Timer volver = new Timer(1200, e -> panelResultado.setTextoBoton("Copiar"));
        volver.setRepeats(false);
        volver.start();
    }

    // ------------------------------------------------------------------
    // Cálculo
    // ------------------------------------------------------------------
    private void calcular() {
        Solicitud s;
        try {
            s = Validador.validar(selector.getSeleccion(), fLo.getText(), fHi.getText(),
                    fK.getText(), fN.getText(), fBase.getText(), fU.getText(), fV.getText());
        } catch (EntradaInvalidaException ex) {
            panelResultado.setTexto(ex.getMessage());
            return;
        }

        btnCalcular.setEnabled(false);
        panelResultado.setTexto("Calculando…");

        // Prolog corre fuera del hilo de Swing; si tarda demasiado se cancela.
        boolean[] vencio = {false};
        Timer limite = new Timer(TIMEOUT_MS, e -> {
            vencio[0] = true;
            motor.cancelar();
        });
        limite.setRepeats(false);
        limite.start();

        new SwingWorker<Resultado, Void>() {
            @Override
            protected Resultado doInBackground() {
                return motor.resolver(s);
            }

            @Override
            protected void done() {
                limite.stop();
                btnCalcular.setEnabled(true);
                if (vencio[0]) {
                    panelResultado.setTexto("La consulta tardó más de " + TIMEOUT_MS / 1000 + " s y se canceló.");
                    return;
                }
                try {
                    panelResultado.setTexto(formatear(s, get()));
                } catch (Exception ex) {
                    panelResultado.setTexto("Error inesperado: " + ex.getMessage());
                }
            }
        }.execute();
    }

    /** Arma el texto que se muestra en el recuadro de resultado. */
    private static String formatear(Solicitud s, Resultado r) {
        String cabecera = s.integral().etiqueta() + "   en [" + s.a() + ", " + s.b() + "]";
        switch (r.estado()) {
            case OK:
                return cabecera + "\nF(x) = " + r.antiderivada()
                        + "\nF(" + s.b() + ") − F(" + s.a() + ") = " + r.area();
            case PENDIENTE:
                return cabecera + "\n" + r.mensaje() + "\nProlog recibió: " + r.consulta();
            default:
                return r.mensaje();
        }
    }

    private static void agregar(JPanel panel, JComponent c, int espacioDespues) {
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(c);
        if (espacioDespues > 0) panel.add(Box.createVerticalStrut(espacioDespues));
    }
}