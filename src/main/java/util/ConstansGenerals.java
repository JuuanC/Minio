package util;

/**
 * @author Juan Carlos Dominguez
 * @version 1.0
 * @since 25/11/2020
 * <h1>Clase de constantes generales</h1>
 * <p>Aqui se encuentran frases que se utilizan en varias ocasiones y en varios lugares.</p>
 */
public class ConstansGenerals {

    private ConstansGenerals() {
        throw new IllegalStateException("Clase de constantes para excepciones");
    }

    public static final String REG_EXP_DATE = "^\\d{4}\\-\\d{2}\\-\\d{2}$";
    public static final String INF_SUCCESS = "Informacion obtenida con exito.";
    public static final int ANIO_BASE = 2020;
}