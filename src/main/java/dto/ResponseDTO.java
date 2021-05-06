package dto;

public class ResponseDTO {

    private boolean success;
    private int status;
    private String message;
    private Object data;

    public ResponseDTO() {
        super();
    }

    public ResponseDTO(boolean success, int status, String message) {
        this.success = success;
        this.status = status;
        this.message = message;
    }

    public ResponseDTO(boolean success, int status, String message, Object data) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object  getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}



