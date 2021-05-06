package recource;

import dto.AccountMinioDTO;
import dto.FileDTO;
import dto.RequestMultiPartDTO;
import exception.CustomException;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import service.Minio;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

@ApplicationScoped
@Path("/v1/minio")

public class MinioResource {

    @Inject
    Minio minio;

    /**
     * <h1>Metodo para la grafica de solicitantes</h1>
     * <p>Lo unico que hace este metodo es pasar los datos enviados por el cliente
     * y se los pasa al service para ser validados y procesados.</p>
     *
     * @return Lista de Charts donde van todos los valores obtenidos.
     * @throws CustomException Excepciones que pueden ocurrir en el proceso de este metodo.
     */
    @Path("/start")
    @POST
    public Response start(@RequestBody AccountMinioDTO accountMinioDTO) throws CustomException {
        return minio.startup(accountMinioDTO);
    }

    @Path("/file")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFile(@RequestBody FileDTO file) throws CustomException {
        return minio.getFileFromMinioObject(file);
    }

    @Path("/file2")
    @POST
    @Produces(MediaType.MULTIPART_FORM_DATA)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getFile2(@RequestBody FileDTO file) throws CustomException, FileNotFoundException {
        return minio.getFileFromMinioObject2(file);
    }

    @Path("/upload")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response upload(FileDTO file) throws CustomException {
        return minio.uploadDocument(file);
    }

    @Path("/upload2")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload2(@MultipartForm RequestMultiPartDTO requestMultiPartDTO) throws CustomException {
        return minio.uploadDocument2(requestMultiPartDTO);
    }

    @Path("/list")
    @POST
    //@Produces(MediaType.MULTIPART_FORM_DATA)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, String> list(@RequestBody FileDTO file) throws CustomException {
        return minio.getObjectListFromPath(file);
    }

}
