package org.example.engine;

import java.util.Map;

public record Solicitud(Integral integral, String a, String b, Map<Parametro, String> params) {
}
