package bjr.Server.http;



import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;



/* -----  THIS IS HOW YOU WRITE TESTS USING JUNIT ----- */


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HttpParserTest {
// This is how you build a unit test in Java using JUnit
    // The annotation above the class was thrown in an error, all I did was copy-pasting
    // it above as indicated.

    private HttpParser httpParser; // Object to perform tests upon

    @BeforeAll
    public void beforeClass() {
        httpParser = new HttpParser(); // Create it before running the tests
    }

    @Test
    void parseHttpRequest() {
        HttpRequest request = null;
        try {
            request = httpParser.parseHTTPRequest(
                    generateValidGETTestCase()
            );
        } catch (HttpParsingException e) {
            Assertions.fail(e);
        }
        Assertions.assertNotNull(request);
        Assertions.assertEquals(request.getMethod(), HttpMethod.GET);
        Assertions.assertEquals(request.getRequestTarget(), "/");
        Assertions.assertEquals(request.getOriginalHttpVersion(), "HTTP/1.1");
        Assertions.assertEquals(request.getBestCompatibleVersion(), HttpVersion.HTTP_1_1);
    }

    @Test
    void parseHttpRequestBadMethod1() {
        // This test checks that given an invalid GeT request
        // our code throws a 501 NOT IMPLEMENTED SERVER ERROR.

        try {
            HttpRequest request  = httpParser.parseHTTPRequest(
                    generateBadTestCaseMethodName1()
            );
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseHttpRequestBadMethod2() {
        // This test checks that given an invalid GeT request
        // our code throws a 501 NOT IMPLEMENTED SERVER ERROR.

        try {
            HttpRequest request  = httpParser.parseHTTPRequest(
                    generateBadTestCaseMethodName2()
            );
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED);
        }
    }

    @Test
    void parseHttpRequestInvalidNumItems1() {
        // Request line with more than 3 items.

        try {
            HttpRequest request  = httpParser.parseHTTPRequest(
                    generateBadTestCaseInvalidNumItems3()
            );
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestEmptyRequestLine() {
        // This test checks that given an empty  request line
        // our code throws a 400 BAD REQUEST client error.

        try {
            HttpRequest request  = httpParser.parseHTTPRequest(
                    generateBadTestCaseEmptyRequestLine()
            );
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequestCRButNoLF() {
        // This test checks that if there is no LF
        // it should return an error.

        try {
            HttpRequest request  = httpParser.parseHTTPRequest(
                    generateBadTestCaseOnlyCRButNoLF()
            );
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequest_BadHTTPVersion() {
        // This test checks that if there is a bad HTTP version format
        // our code should return an error.

        try {
            HttpRequest request  = httpParser.parseHTTPRequest(
                    generateBadHttpVersionTestCase()
            );
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    @Test
    void parseHttpRequest_UnsupportedHTTPVersion() {
        // This test checks that if there is a bad HTTP version format
        // our code should return an error.

        try {
            HttpRequest request  = httpParser.parseHTTPRequest(
                    generateUnsupportedHttpVersionTestCase()
            );
            Assertions.fail();
        } catch (HttpParsingException e) {
            Assertions.assertEquals(e.getErrorCode(), HttpStatusCodes.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    @Test
    void parseHttpRequest_HigherHTTPVersion() {
        // This test checks that if there is a bad HTTP version format
        // our code should return an error.

        try {
            HttpRequest request  = httpParser.parseHTTPRequest(
                    generateHigherHttpVersionTestCase()
            );
            Assertions.assertNotNull(request);
            Assertions.assertEquals(request.getBestCompatibleVersion(), HttpVersion.HTTP_1_1);
            Assertions.assertEquals(request.getOriginalHttpVersion(), "HTTP/1.5");
        } catch (HttpParsingException e) {
            Assertions.fail();
        }
    }

    private InputStream generateValidGETTestCase(){
        String rawData = "GET / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:104.0) Gecko/20100101 Firefox/104.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "DNT: 1\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-GPC: 1" + "\r\n";    // Valid HTTP Request
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII // Go to "tools.ietf.org/html/rfc7230 to see that all messages
                        // are encoded in US ASCII.
                )
        );
        return inputStream;
    }

    private InputStream generateBadTestCaseMethodName1() {
        String rawData = "GeT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadTestCaseMethodName2() {
        // This test case will check for longer method size than expected.

        String rawData = "GETTTTTT / HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadTestCaseInvalidNumItems3() {
        // More than 3 items in the Request Line which is also invalid.

        String rawData = "GET / AAAA  HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadTestCaseEmptyRequestLine() {
        // More than 3 items in the Request Line which is also invalid.

        String rawData = "\r\n" +
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadTestCaseOnlyCRButNoLF() {
        // There is a CR but no LF which is also an error.

        String rawData =  "GET / HTTP/1.1\r" +  // <----- No LF
                "Host: localhost:8080\r\n" +
                "Accept-Language: en-US,en;q=0.9,es;q=0.8,pt;q=0.7,de-DE;q=0.6,de;q=0.5,la;q=0.4\r\n" +
                "\r\n";

        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII
                )
        );

        return inputStream;
    }

    private InputStream generateBadHttpVersionTestCase(){
        String rawData = "GET / HTP/1.1\r\n" + // <---- Wrong HTTP Format
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:104.0) Gecko/20100101 Firefox/104.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "DNT: 1\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-GPC: 1" + "\r\n";    // Valid HTTP Request
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII // Go to "tools.ietf.org/html/rfc7230 to see that all messages
                        // are encoded in US ASCII.
                )
        );
        return inputStream;
    }

    private InputStream generateUnsupportedHttpVersionTestCase(){
        String rawData = "GET / HTTP/2.1\r\n" + // <---- Unsupported HTTP Format
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:104.0) Gecko/20100101 Firefox/104.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "DNT: 1\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-GPC: 1" + "\r\n";    // Valid HTTP Request
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII // Go to "tools.ietf.org/html/rfc7230 to see that all messages
                        // are encoded in US ASCII.
                )
        );
        return inputStream;
    }

    private InputStream generateHigherHttpVersionTestCase(){
        String rawData = "GET / HTTP/1.5\r\n" + // <---- Higher HTTP Format
                "Host: localhost:8080\r\n" +
                "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:104.0) Gecko/20100101 Firefox/104.0\r\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8\r\n" +
                "Accept-Language: en-US,en;q=0.5\r\n" +
                "Accept-Encoding: gzip, deflate, br\r\n" +
                "DNT: 1\r\n" +
                "Connection: keep-alive\r\n" +
                "Upgrade-Insecure-Requests: 1\r\n" +
                "Sec-Fetch-Dest: document\r\n" +
                "Sec-Fetch-Mode: navigate\r\n" +
                "Sec-Fetch-Site: none\r\n" +
                "Sec-Fetch-User: ?1\r\n" +
                "Sec-GPC: 1" + "\r\n";    // Valid HTTP Request
        InputStream inputStream = new ByteArrayInputStream(
                rawData.getBytes(
                        StandardCharsets.US_ASCII // Go to "tools.ietf.org/html/rfc7230 to see that all messages
                        // are encoded in US ASCII.
                )
        );
        return inputStream;
    }
}
