package org.example.engine;

public interface Motor {

    Resultado resolver(Solicitud solicitud);

    void cancelar();
}
