package exception;

import dto.ResponseDTO;
import org.apache.http.HttpStatus;
import org.jboss.logging.Logger;
import util.Utilities;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


/**
 * @author Juan Carlos Dominguez
 * @version 3.0
 * @since 03/11/2020
 * <h1>Clase para interceptar las excepciones de la clase CustomException</h1>
 */
@Provider
public class ExceptionInterceptor implements ExceptionMapper<CustomException> {

    @Inject
    Utilities utilities;

    @Override
    public Response toResponse(CustomException exception) {
        Logger logger = Logger.getLogger(ExceptionInterceptor.class);

        ResponseDTO responseDTO = new ResponseDTO(
                false, HttpStatus.SC_BAD_REQUEST, exception.getMessage());
        try {
            return Response.status(Response.Status.BAD_REQUEST).entity(utilities.orderJson(responseDTO)).build();
        } catch (CustomException e) {
            logger.error(e.getMessage() + " : Error en el interceptor.");
        }
        return null;
    }
}