package org.example.engine;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;
import alice.tuprolog.Var;
import java.nio.charset.StandardCharsets;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Connection implements Motor {

    private static final List<String> ARCHIVOS = List.of(
            "integrador", "basicas", "trig", "hiperbolicas", "evaluar", "simplificar");

    private final Prolog engine = new Prolog();

    public Connection() throws Exception {
        StringBuilder texto = new StringBuilder();
        for (String nombre : ARCHIVOS) {
            String ruta = "/prolog/" + nombre + ".pl";
            try (InputStream in = getClass().getResourceAsStream(ruta)) {
                if (in == null) throw new IllegalStateException("No se encontró " + ruta);
                texto.append(new String(in.readAllBytes(), StandardCharsets.UTF_8)).append("\n\n");
            }
        }
        engine.setTheory(new Theory(texto.toString()));
    }

    @Override
    public Resultado resolver(Solicitud s) {
        String consulta = textoConsulta(s);
        try {
            Struct params = listaParams(s.params());
            Struct goal = new Struct("resolver",
                    new Struct(s.integral().id()), params,
                    numero(s.a()), numero(s.b()), new Var("R"));

            SolveInfo info = engine.solve(goal);
            if (!info.isSuccess()) {
                return Resultado.error("Prolog no devolvió respuesta para esta consulta.", consulta);
            }
            return interpretar(s, (Struct) info.getTerm("R"), consulta);
        } catch (Exception ex) {
            return Resultado.error("Error en Prolog: " + ex.getMessage(), consulta);
        }
    }

    @Override
    public void cancelar() {
        engine.solveHalt();
    }

    private Resultado interpretar(Solicitud s, Struct r, String consulta) {
        switch (r.getName()) {
            case "ok":
                return Resultado.ok(r.getArg(0).toString(), r.getArg(1).toString(), consulta);
            case "no_implementado":
                return Resultado.pendiente("La regla " + s.integral().id() + ") "
                        + s.integral().etiqueta() + " aún no está implementada en Prolog.", consulta);
            case "error":
                return Resultado.error(r.getArg(0).toString(), consulta);
            default:
                return Resultado.error("Respuesta desconocida de Prolog: " + r, consulta);
        }
    }

    private Struct listaParams(Map<Parametro, String> params) {
        List<Term> pares = new ArrayList<>();
        for (Map.Entry<Parametro, String> e : params.entrySet()) {
            Parametro p = e.getKey();
            Term valor = (p == Parametro.U || p == Parametro.V)
                    ? Term.createTerm(e.getValue())
                    : numero(e.getValue());
            pares.add(new Struct("=", new Struct(p.nombreProlog()), valor));
        }
        return new Struct(pares.toArray(new Term[0]));
    }

    private static Term numero(String texto) {
        if (texto.contains(".")) {
            return new alice.tuprolog.Double(java.lang.Double.parseDouble(texto));
        }
        long v = Long.parseLong(texto);
        return (v >= Integer.MIN_VALUE && v <= Integer.MAX_VALUE)
                ? new alice.tuprolog.Int((int) v)
                : new alice.tuprolog.Long(v);
    }

    private static String textoConsulta(Solicitud s) {
        StringBuilder ps = new StringBuilder();
        for (Map.Entry<Parametro, String> e : s.params().entrySet()) {
            if (ps.length() > 0) ps.append(", ");
            ps.append(e.getKey().nombreProlog()).append('=').append(e.getValue());
        }
        return "resolver(" + s.integral().id() + ", [" + ps + "], " + s.a() + ", " + s.b() + ", R)";
    }
}