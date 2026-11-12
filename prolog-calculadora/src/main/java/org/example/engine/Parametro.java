package org.example.engine;

public enum Parametro {
    K("k"), N("n"), BASE("base"), U("u"), V("v");

    private final String nombreProlog;

    Parametro(String nombreProlog) {
        this.nombreProlog = nombreProlog;
    }

    /** Nombre con el que se envía a Prolog en la lista Params (k=3, n=2, ...). */
    public String nombreProlog() {
        return nombreProlog;
    }
}
