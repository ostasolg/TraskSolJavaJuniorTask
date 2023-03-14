package TraskSolTask.payload.response;

import lombok.Data;


@Data
public class MessageResponse {

    private String message;
    private String type;
    private String requestUri;


    public MessageResponse(String message, String type, String requestUri) {
        this.message = message;
        this.type = type;
        this.requestUri = requestUri;
    }

    public MessageResponse(String message, String requestUri) {
        this.message = message;
        this.requestUri = requestUri;
        this.type = "error";
    }

    @Override
    public String toString() {
        return "ErrorInfo{" + requestUri + ", message = " + message + "type = " + type + "}";
    }
}
