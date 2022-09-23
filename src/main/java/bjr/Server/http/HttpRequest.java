package bjr.Server.http;

public class HttpRequest extends HttpMessage {

    private HttpMethod method;
    private String requestTarget;



    private String originalHttpVersion; // literal from the request
    private HttpVersion bestCompatibleVersion; // best compatible version of http

    HttpRequest() {
        // This constructor initialized without an access modifier token
        // makes it package level accessible. All other object of this package
        // can instantiate this class.


        // RFC 7231 states: (tools.ietf.org/html/rfc7231#section-4)
        // a method is a token (and it's case-sensitive)

        // All general-purpose servers MUST support GET and HEAD methods.
        // Any not implemented methods must return an 501 Error (Not implemented)
        // When a method is known, but not allowed to the target source, the origin server
        // should return an 405 Error (Method Not Allowed).


    }


    public HttpMethod getMethod() {
        return method;
    }

    public String getRequestTarget() {
        return requestTarget;
    }


    public HttpVersion getBestCompatibleVersion() {
        return bestCompatibleVersion;
    }

    public String getOriginalHttpVersion() {
        return originalHttpVersion;
    }

    void setMethod(String methodName) throws HttpParsingException {
        // Again this method has a package-leve scope.

        for(HttpMethod method: HttpMethod.values()){
            if(methodName.equals(method.name())){
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(
                HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED
        );

    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.length() == 0) {
            throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = requestTarget;
    }

    void seHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        // Package level method
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        if(this.bestCompatibleVersion == null) {
            throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }
}

