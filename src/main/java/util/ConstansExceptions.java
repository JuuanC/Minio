

package util;

/**
 * @author Juan Carlos Dominguez
 * @version 1.0
 * @since 25/11/2020
 * <h1>Clase de constantes para excepciones</h1>
 * <p>Aqui se encuentran frases que se utilizan para el manejo de excepciones.</p>
 */
public class ConstansExceptions {

    private ConstansExceptions() {
        throw new IllegalStateException("Clase de constantes para excepciones");
    }

    public static final String ERROR_INESPERADO = "Ocurrio un error inesperado, contacte a soporte.";
    public static final String ERROR_VALORES_NULOS = "No puedes enviar datos vacios.";
    public static final String ERROR_SIN_INFO = "No se encontro la informacion solicitada.";
    public static final String ERROR_SUB_SIN_INFO = "No se encontraron datos relacionados con el SubCentro.";
    public static final String ERROR_CREACION_JSON = "Ocurrio un error al crear el JSON de respuesta.";
    public static final String ERROR_FORMATO_FECHA = "El formato de la fecha es incorrecto.";
    public static final String ERROR_DIA_MES = "El mes o el dia son incorrectos.";
    public static final String ERROR_ANIO = "El año es muy pequeño o superior al año actual.";
}

