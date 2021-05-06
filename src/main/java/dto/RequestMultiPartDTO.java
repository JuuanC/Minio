package dto;

import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

public class RequestMultiPartDTO {

    @FormParam("host")
    @PartType(MediaType.TEXT_PLAIN)
    private String host;
    @FormParam("bucket")
    @PartType(MediaType.TEXT_PLAIN)
    private String bucket;
    @FormParam("objectName")
    @PartType(MediaType.TEXT_PLAIN)
    private String objectName;
    @FormParam("fileBase64")
    @PartType(MediaType.MULTIPART_FORM_DATA)
    private InputStream fileBase64;
    @FormParam("contentType")
    @PartType(MediaType.TEXT_PLAIN)
    private String contentType;

    public RequestMultiPartDTO() {

    }

    public RequestMultiPartDTO(String host, String bucket, String objectName, InputStream fileBase64, String contentType) {
        this.host = host;
        this.bucket = bucket;
        this.objectName = objectName;
        this.fileBase64 = fileBase64;
        this.contentType = contentType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public InputStream getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(InputStream fileName) {
        this.fileBase64 = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
