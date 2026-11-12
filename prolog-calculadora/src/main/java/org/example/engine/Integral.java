package org.example.engine;

import java.util.EnumSet;
import java.util.Set;

//Solo se van a dejar las que se van a desarrollar

public enum Integral {
    A("∫ k dx", "k es una constante numérica", Parametro.K),
    B("∫ x dx", "Solo requiere los límites a y b"),
    C("∫ k·x dx", "k es una constante numérica", Parametro.K),
    D("∫ xⁿ dx", "n es un número entero", Parametro.N),
    E("∫ u dx", "u es una función de x", Parametro.U),
    F("∫ k·u dx", "k es una constante numérica y u una función de x", Parametro.K, Parametro.U),
    G("∫ (u + v) dx", "u y v son funciones de x", Parametro.U, Parametro.V),
    H("∫ (u − v) dx", "u y v son funciones de x", Parametro.U, Parametro.V),
    I("∫ aˣ dx", "a es una constante numérica", Parametro.BASE),
    J("∫ aᵘ dx", "a es una constante numérica y u una función de x", Parametro.BASE, Parametro.U),
    K("∫ sin x dx", "Solo requiere los límites a y b"),
    L("∫ cos x dx", "Solo requiere los límites a y b"),
    M("∫ tan x dx", "Solo requiere los límites a y b"),
    N("∫ arcsin x dx", "Definida para x en [−1, 1]"),
    O("∫ arccos x dx", "Definida para x en [−1, 1]"),
    P("∫ arctan x dx", "Solo requiere los límites a y b"),
    Q("∫ sinh x dx", "Solo requiere los límites a y b"),
    R("∫ cosh x dx", "Solo requiere los límites a y b"),
    S("∫ tanh x dx", "Solo requiere los límites a y b"),
    T("∫ arcsinh x dx", "Solo requiere los límites a y b"),
    U("∫ arccosh x dx", "Definida para x ≥ 1"),
    V("∫ arctanh x dx", "Definida para x en (−1, 1)");

    private final String etiqueta;
    private final String ayuda;
    private final Set<Parametro> requeridos;

    Integral(String etiqueta, String ayuda, Parametro... requeridos) {
        this.etiqueta = etiqueta;
        this.ayuda = ayuda;
        this.requeridos = requeridos.length == 0
                ? EnumSet.noneOf(Parametro.class)
                : EnumSet.copyOf(java.util.List.of(requeridos));
    }

    /** Letra del enunciado en minúscula: "a", "b", ... Es el Id que recibe Prolog. */
    public String id() {
        return name().toLowerCase();
    }

    public String etiqueta() {
        return etiqueta;
    }

    public String ayuda() {
        return ayuda;
    }

    public boolean requiere(Parametro p) {
        return requeridos.contains(p);
    }

    public Set<Parametro> requeridos() {
        return requeridos;
    }

    /** Texto para el selector: "c)  ∫ k·x dx". */
    @Override
    public String toString() {
        return id() + ")  " + etiqueta;
    }
}
