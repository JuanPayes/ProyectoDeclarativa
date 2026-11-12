package org.example.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.io.InputStream;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

public final class Theme {

    private Theme() {
    }

    // Paleta
    public static final Color IMPERIAL = new Color(0x072768);   // imperial blue
    public static final Color GLAUCOUS = new Color(0x5079B2);   // glaucous
    public static final Color LINEN = new Color(0xF7EFE8);      // linen (fondo)
    public static final Color COPPER = new Color(0x996F51);     // faded copper
    public static final Color MAHOGANY = new Color(0x351303);   // rich mahogany (texto y bordes)
    public static final Color FIELD = new Color(0xFBF7F3);       // fondo de campos
    public static final Color PLACEHOLDER = new Color(0x6B5648); // texto de ayuda

    public static final FontRenderContext FRC = new FontRenderContext(null, true, true);

    private static final Font JOST = cargar("/fonts/Jost.ttf");
    private static final Font MONO = cargar("/fonts/JetBrainsMono-Regular.ttf");
    private static final Font MONO_BOLD = cargar("/fonts/JetBrainsMono-Bold.ttf");

    private static Font cargar(String ruta) {
        try (InputStream in = Theme.class.getResourceAsStream(ruta)) {
            return in == null ? null : Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (Exception e) {
            return null; // si falla, se usa la fuente del sistema
        }
    }

    public static Font sans(int estilo, float tam) {
        Font base = JOST != null ? JOST : new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        return base.deriveFont(estilo, tam);
    }

    public static Font sans(int estilo, float tam, float tracking) {
        Font f = sans(estilo, tam);
        Map<TextAttribute, Object> attrs = new HashMap<>(f.getAttributes());
        attrs.put(TextAttribute.TRACKING, tracking);
        return f.deriveFont(attrs);
    }

    public static Font mono(int estilo, float tam) {
        Font base = (estilo & Font.BOLD) != 0 ? MONO_BOLD : MONO;
        if (base == null) return new Font(Font.MONOSPACED, estilo, Math.round(tam));
        return base.deriveFont(Font.PLAIN, tam);
    }

    public static boolean tiene(Font f, char c) {
        return f.canDisplay(c);
    }

    public static AttributedString mezclar(String texto, Font base) {
        AttributedString as = new AttributedString(texto);
        as.addAttribute(TextAttribute.FONT, base);
        Font alterna = new Font(Font.DIALOG, base.getStyle(), Math.round(base.getSize2D()));
        for (int i = 0; i < texto.length(); i++) {
            if (!base.canDisplay(texto.charAt(i))) {
                as.addAttribute(TextAttribute.FONT, alterna, i, i + 1);
            }
        }
        return as;
    }
}