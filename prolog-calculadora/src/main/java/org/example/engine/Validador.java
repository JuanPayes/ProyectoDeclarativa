package org.example.engine;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

public final class Validador {

    private Validador() {
    }

    private static final Pattern NUMERO = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern ENTERO = Pattern.compile("-?\\d+");
    private static final Pattern EXPRESION = Pattern.compile("[0-9a-z+\\-*/^(). ]+");

    public static Solicitud validar(Integral integral, String a, String b,
                                    String k, String n, String base,
                                    String u, String v) throws EntradaInvalidaException {
        String lo = numero(a);
        String hi = numero(b);
        if (lo == null || hi == null) {
            throw new EntradaInvalidaException("Ingresa números válidos en los límites a y b.");
        }

        Map<Parametro, String> params = new EnumMap<>(Parametro.class);

        if (integral.requiere(Parametro.K)) {
            String val = numero(k);
            if (val == null) throw new EntradaInvalidaException("k debe ser una constante numérica.");
            params.put(Parametro.K, val);
        }
        if (integral.requiere(Parametro.N)) {
            String val = numero(n);
            if (val == null || !ENTERO.matcher(val).matches()) {
                throw new EntradaInvalidaException("n debe ser un número entero.");
            }
            params.put(Parametro.N, val);
        }
        if (integral.requiere(Parametro.BASE)) {
            String val = numero(base);
            if (val == null) throw new EntradaInvalidaException("a debe ser una constante numérica.");
            params.put(Parametro.BASE, val);
        }
        if (integral.requiere(Parametro.U)) {
            params.put(Parametro.U, expresion(u, "u(x)"));
        }
        if (integral.requiere(Parametro.V)) {
            params.put(Parametro.V, expresion(v, "v(x)"));
        }
        return new Solicitud(integral, lo, hi, params);
    }

    static String numero(String texto) {
        if (texto == null) return null;
        String t = texto.trim().replace(',', '.');
        return NUMERO.matcher(t).matches() ? t : null;
    }

    static String expresion(String texto, String nombre) throws EntradaInvalidaException {
        String t = texto == null ? "" : texto.trim().toLowerCase();
        if (t.isEmpty()) {
            throw new EntradaInvalidaException("Escribe la función " + nombre + ".");
        }
        if (!EXPRESION.matcher(t).matches()) {
            throw new EntradaInvalidaException(nombre + " tiene caracteres no permitidos. "
                    + "Usa x, números, + - * / ^, paréntesis y funciones como sin(x).");
        }
        int nivel = 0;
        for (char c : t.toCharArray()) {
            if (c == '(') nivel++;
            if (c == ')' && --nivel < 0) break;
        }
        if (nivel != 0) {
            throw new EntradaInvalidaException("Los paréntesis de " + nombre + " no están balanceados.");
        }
        return t;
    }
}