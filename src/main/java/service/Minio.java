package service;

import dto.*;
import exception.CustomException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.jboss.logging.Logger;
import util.FileUtil;
import util.Utilities;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class Minio {

    @Inject
    FileUtil fileUtil;

    @Inject
    Utilities utilities;

    org.jboss.logging.Logger logger = Logger.getLogger(Minio.class);

    private HashMap<String, HashMap<String, AccountMinioDTO>> sessions = new HashMap<>();

    private boolean newMinioServer(String host, String bucket) {
        if (sessions.containsKey(host)) {
            if (sessions.get(host).containsKey(bucket)) {
                return true;
            }
        }
        return false;
    }

    public Response startup(AccountMinioDTO accountMinioDTO) throws CustomException {
        HashMap<String, AccountMinioDTO> temp = new HashMap<>();
        MinioClient minioClient = null;
        try {
            //CREO EL OBJETO PARA ACCEDER A MINIO
            minioClient = MinioClient.builder()
                    .endpoint(accountMinioDTO.getHost(), accountMinioDTO.getPort(), false)
                    .credentials(accountMinioDTO.getUser(), accountMinioDTO.getPassword()).build();
            //COMPRUEBO QUE SE PUEDE REALIZAR LA CONEXION
            if (minioClient.bucketExists(BucketExistsArgs.builder().bucket(accountMinioDTO.getBucket()).build())) {
                accountMinioDTO.setSuccessConnection(true);
            }
        } catch (ServerException | XmlParserException | NoSuchAlgorithmException | IOException | InvalidKeyException | InternalException | InsufficientDataException | ErrorResponseException e) {
            throw new CustomException(e.getMessage());
        } catch (InvalidResponseException e) {
            throw new CustomException("Ocurrió un error, el HOST no se encontró.");
        }

        if (accountMinioDTO.isSuccessConnection()) {
            //SI LA CONEXION FUE EXITOSA YA SE CREA UNA SESION DE ESE HOST Y BUCKET
            accountMinioDTO.setMinioClient(minioClient);
            temp.put(accountMinioDTO.getBucket(), accountMinioDTO);

            if (!newMinioServer(accountMinioDTO.getHost(), accountMinioDTO.getBucket())) {
                //SI LA SESION NO EXITE LA CREA
                sessions.put(accountMinioDTO.getHost(), temp);
            } else {
                //SI LA SESION EXISTE LA REEMPLAZA
                //POR SI CAMBIARON EL USUARIO Y PASSWORD
                sessions.replace(accountMinioDTO.getHost(), temp);
            }
        } else {
            throw new CustomException("No se encontró el Bucket.");
        }

        ResponseDTO responseDTO = new ResponseDTO(true,
                HttpStatus.SC_OK, "Conexión con exitosa con Minio.");

        return Response.status(HttpStatus.SC_OK).entity(responseDTO).build();
    }


    public Response uploadDocument(FileDTO fileDTO) throws CustomException {
        MinioClient minioClient;

        if (newMinioServer(fileDTO.getHost(), fileDTO.getBucket())) {
            minioClient = sessions.get(fileDTO.getHost()).get(fileDTO.getBucket()).getMinioClient();
        } else {
            throw new CustomException("No existe una sesión con ese host o bucket");
        }
        try {
            String fileBase64 = fileDTO.getFileBase64();

            fileBase64 = fileBase64.substring(fileBase64.indexOf(',') + 1);
            byte[] byteArray = Base64.decodeBase64(fileBase64);

            InputStream inputStream = new ByteArrayInputStream(byteArray);

            if (fileDTO.getContentType() == null) {
                minioClient.putObject(
                        PutObjectArgs.builder().bucket(fileDTO.getBucket()).object(fileDTO.getObjectName()).stream(
                                inputStream, inputStream.available(), -1).contentType(fileDTO.getContentType())
                                .build());
            } else {
                minioClient.putObject(
                        PutObjectArgs.builder().bucket(fileDTO.getBucket()).object(fileDTO.getObjectName()).stream(
                                inputStream, inputStream.available(), -1)
                                .build());
            }
        } catch (Exception e) {
            throw new CustomException("Ocurrió un error", e);
        }
        return Response.status(200).entity("Se pudo").build();
    }

    public Response uploadDocument2(RequestMultiPartDTO requestMultiPartDTO) throws CustomException {
        MinioClient minioClient;

        if (newMinioServer(requestMultiPartDTO.getHost(), requestMultiPartDTO.getBucket())) {
            minioClient = sessions.get(requestMultiPartDTO.getHost()).get(requestMultiPartDTO.getBucket()).getMinioClient();
        } else {
            throw new CustomException("No existe una sesión con ese host o bucket");
        }

        ByteArrayInputStream biteArrayInputStream = fileUtil.inputStreamToByteArrayInputStream(requestMultiPartDTO.getFileBase64());
        try {
            if (requestMultiPartDTO.getContentType() == null) {

                minioClient.putObject(
                        PutObjectArgs.builder().bucket(requestMultiPartDTO.getBucket()).object(requestMultiPartDTO.getObjectName()).stream(
                                biteArrayInputStream, biteArrayInputStream.available(), -1).contentType(requestMultiPartDTO.getContentType())
                                .build());

            } else {
                minioClient.putObject(
                        PutObjectArgs.builder().bucket(requestMultiPartDTO.getBucket()).object(requestMultiPartDTO.getObjectName()).stream(
                                biteArrayInputStream, biteArrayInputStream.available(), -1)
                                .build());
            }
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            e.printStackTrace();
        }

        return Response.status(200).entity("Se pudo").build();
    }

    public Response getFileFromMinioObject(FileDTO file) throws CustomException {
        MinioClient minioClient;

        if (newMinioServer(file.getHost(), file.getBucket())) {
            minioClient = sessions.get(file.getHost()).get(file.getBucket()).getMinioClient();
        } else {
            throw new CustomException("No existe una sesión con ese host o bucket");
        }

        InputStream minioObject;
        File tempFile;
        String fileBase64;
        try {
            minioObject = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(file.getBucket())
                            .object(file.getObjectName())
                            .build());

            String[] fileName = file.getObjectName().split("/");

            tempFile = fileUtil.inputStreamToFile(fileName[fileName.length - 1], minioObject);
            tempFile.deleteOnExit();
            fileBase64 = utilities.encodeFileToBase64Binary(tempFile);
        } catch (NullPointerException e) {
            throw new CustomException("Hubo un error al iniciar sesión con minio o no se encontró el archivo con el nombre: ");
        } catch (ErrorResponseException e) {
            throw new CustomException("No se encontró el archivo", e);
        } catch (InsufficientDataException e) {
            throw new CustomException("InsufficientDataException", e);
        } catch (InternalException e) {
            throw new CustomException("InternalException", e);
        } catch (InvalidKeyException e) {
            throw new CustomException("InvalidKeyException", e);
        } catch (InvalidResponseException e) {
            throw new CustomException("InvalidResponseException", e);
        } catch (IOException e) {
            throw new CustomException("IOException", e);
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("NoSuchAlgorithmException", e);
        } catch (ServerException e) {
            throw new CustomException("ServerException", e);
        } catch (XmlParserException e) {
            throw new CustomException("XmlParserException", e);
        } catch (IllegalArgumentException e) {
            throw new CustomException("IllegalArgumentException", e);
        }

        ResponseDTO responseDTO = new ResponseDTO(
                true, HttpStatus.SC_OK, "Archivo obtenido con éxito.", fileBase64);

        return Response.status(HttpStatus.SC_OK).entity(utilities.orderJson(responseDTO)).build();
    }

    public Response getFileFromMinioObject2(FileDTO file) throws CustomException, FileNotFoundException {
        MinioClient minioClient;

        if (newMinioServer(file.getHost(), file.getBucket())) {
            minioClient = sessions.get(file.getHost()).get(file.getBucket()).getMinioClient();
        } else {
            throw new CustomException("No existe una sesión con ese host o bucket");
        }

        InputStream minioObject;
        File tempFile;
        String[] fileName;
        try {
            minioObject = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(file.getBucket())
                            .object(file.getObjectName())
                            .build());

            //TODO AQUI SE DEBE ITERAR PARA EL CASO QUE TENGAN MUCHAS CARPETAS
            fileName = file.getObjectName().split("/");

            tempFile = fileUtil.inputStreamToFile(fileName[fileName.length - 1], minioObject);
            tempFile.deleteOnExit();
        } catch (NullPointerException e) {
            throw new CustomException("Hubo un error al iniciar sesión con minio o no se encontró el archivo con el nombre: ");
        } catch (ErrorResponseException e) {
            throw new CustomException("No se encontró el archivo", e);
        } catch (InsufficientDataException e) {
            throw new CustomException("InsufficientDataException", e);
        } catch (InternalException e) {
            throw new CustomException("InternalException", e);
        } catch (InvalidKeyException e) {
            throw new CustomException("InvalidKeyException", e);
        } catch (InvalidResponseException e) {
            throw new CustomException("InvalidResponseException", e);
        } catch (IOException e) {
            throw new CustomException("IOException", e);
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException("NoSuchAlgorithmException", e);
        } catch (ServerException e) {
            throw new CustomException("ServerException", e);
        } catch (XmlParserException e) {
            throw new CustomException("XmlParserException", e);
        } catch (IllegalArgumentException e) {
            throw new CustomException("IllegalArgumentException", e);
        }

        return Response.status(HttpStatus.SC_OK).entity(tempFile).build();
    }

    public Map<String, String> getObjectListFromPath(FileDTO file) throws CustomException {
        MinioClient minioClient;

        if (newMinioServer(file.getHost(), file.getBucket())) {
            minioClient = sessions.get(file.getHost()).get(file.getBucket()).getMinioClient();
        } else {
            throw new CustomException("No existe una sesión con ese host o bucket");
        }

        Map<String, String> objects = new HashMap<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(file.getBucket())
                        .recursive(true)
                        .startAfter(file.getObjectName())
                        .build());
        for (Result<Item> result : results) {
            Item item = null;
            try {
                item = result.get();
            } catch (Exception e) {
                throw new CustomException("Ocurrió un error", e);
            }
            //String[] algo = item.objectName().split("/");
            objects.put(item.objectName(), getObject(minioClient, file.getBucket(), item.objectName()));
        }
        return objects;
    }

    private String getObject(MinioClient minioClient, String bucket, String objectName ){
        InputStream inputStream;
        String base64 = "";
        File tempFile;
        try {
            inputStream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build());
            String[] fileName = objectName.split("/");

            tempFile = fileUtil.inputStreamToFile(fileName[fileName.length - 1], inputStream);
            tempFile.deleteOnExit();
            base64 = utilities.encodeFileToBase64Binary(tempFile);
        } catch (Exception e) {
            e.getStackTrace();
        }

        return base64;
    }



}
