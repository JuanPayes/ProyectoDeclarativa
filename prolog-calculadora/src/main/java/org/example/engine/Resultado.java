package org.example.engine;

/**
 * Respuesta de Prolog ya interpretada.
 *
 * @param estado       OK, PENDIENTE (regla aún no escrita) o ERROR
 * @param antiderivada texto de F(x) (solo si OK)
 * @param area         texto del área F(b) - F(a) (solo si OK)
 * @param mensaje      explicación para el usuario (PENDIENTE / ERROR)
 * @param consulta     consulta enviada a Prolog, útil para depurar
 */

public record Resultado(Estado estado, String antiderivada, String area, String mensaje, String consulta) {

    public enum Estado { OK, PENDIENTE, ERROR }

    public static Resultado ok(String antiderivada, String area, String consulta) {
        return new Resultado(Estado.OK, antiderivada, area, "", consulta);
    }

    public static Resultado pendiente(String mensaje, String consulta) {
        return new Resultado(Estado.PENDIENTE, "", "", mensaje, consulta);
    }

    public static Resultado error(String mensaje, String consulta) {
        return new Resultado(Estado.ERROR, "", "", mensaje, consulta);
    }

}
