package dto;

import io.minio.MinioClient;

public class AccountMinioDTO {
    private String host;
    private int port;
    private String bucket;
    private String user;
    private String password;
    private boolean successConnection = false;
    private MinioClient minioClient;

    public AccountMinioDTO() {
    }

    public AccountMinioDTO(String host, int port, String bucket, String user, String password) {
        this.host = host;
        this.port = port;
        this.bucket = bucket;
        this.user = user;
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSuccessConnection() {
        return successConnection;
    }

    public void setSuccessConnection(boolean successConnection) {
        this.successConnection = successConnection;
    }

    public MinioClient getMinioClient() {
        return minioClient;
    }

    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }
}
