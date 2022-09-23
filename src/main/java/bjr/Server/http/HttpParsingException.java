package bjr.Server.http;

public class HttpParsingException extends Exception {
    // This Class along with the HttpStatusCodes are separate
    // such that it allows for better division of tasks
    // and local modifications with ease.

    private final HttpStatusCodes errorCode;

    public HttpParsingException(HttpStatusCodes errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatusCodes getErrorCode() {
        return errorCode;
    }
}
