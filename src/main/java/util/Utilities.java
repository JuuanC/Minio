package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ResponseDTO;
import exception.CustomException;
import org.apache.commons.codec.binary.Base64;
import org.jboss.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Juan Carlos Dominguez
 * @version 1.0
 * @since 08/12/2020
 * <h1>Clase de utilidades</h1>
 * <p>Aqui se encuentran metodos genericos, es decir, varias clases
 * hacen uso uso de ellos</p>
 */
@ApplicationScoped
public class Utilities {

    Logger logger = Logger.getLogger(Utilities.class);

    /**
     * <h1>Metodo para crear un JSON con un orden especifico</h1>
     * @param responseDTO datos que se convertiran en json
     * @return un String con el formato deseado
     * @throws CustomException
     */
    public String orderJson(ResponseDTO responseDTO) throws CustomException {
        String jsonString;
        ObjectMapper om = new ObjectMapper();
        try {
            jsonString = om.writeValueAsString(responseDTO);
        } catch (JsonProcessingException e) {
            throw new CustomException(ConstansExceptions.ERROR_CREACION_JSON, e);
        }
        return jsonString;
    }

    /**
     * Método para codificar una imagen en base64
     * @param file imagen que se va a codificar
     * @return imagen en base64
     * @throws CustomException
     * @throws IOException
     */
    public String encodeFileToBase64Binary(File file) throws CustomException, IOException {

        try (FileInputStream fileInputStreamReader = new FileInputStream(file)){
            byte[] bytes = new byte[(int)file.length()];
            int count = fileInputStreamReader.read(bytes);
            if (count < 0 ){
                throw new CustomException("Error al codificar la imagen");
            }
            return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            throw new CustomException("No se pudo encontrar la imagen.", e);
        }catch (UnsupportedEncodingException e) {
            throw new CustomException("Hubo un error al decodificar la imagen.", e);
        }catch (IOException e) {
            throw new CustomException("Hubo un error al encontrar la imagen.", e);
        }
    }

}

