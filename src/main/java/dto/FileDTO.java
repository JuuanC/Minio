package dto;

public class FileDTO {
    private String host;
    private String bucket;
    private String objectName;
    private String fileBase64;
    private String contentType;

    public FileDTO() {

    }

    public FileDTO(String host, String bucket, String objectName,String fileBase64, String contentType) {
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

    public String getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(String fileName) {
        this.fileBase64 = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
