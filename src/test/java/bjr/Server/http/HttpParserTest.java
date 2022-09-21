package bjr.Server.http;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;



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
    void parseHTTPRequest() {
        // This was first run as an empty test
        // which will always return true, but we'll introduce
        // some asserts.
        httpParser.parseHTTPrequest(
                generateValidTestCase()
        ); // TODO: For the moment parseHTTPRequest is void method so this test also returns true no matter what.


    }

    private InputStream generateValidTestCase() {
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

}
