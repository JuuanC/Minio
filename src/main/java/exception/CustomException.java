package exception;

import java.io.Serializable;

/**
 * @author Juan Carlos Dominguez
 * @version 1.0
 * @since 20/10/2020
 * <h1>Clase para crear una excepcion personalizada</h1>
 */
public class CustomException extends Exception implements Serializable {

    public CustomException(){
        super();
    }

    public CustomException(String msg){
        super(msg);
    }

    public CustomException(String msg, Exception e){
        super(msg, e);
    }
}
