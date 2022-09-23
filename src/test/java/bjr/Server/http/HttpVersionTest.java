package bjr.Server.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpVersionTest {
    @Test
    void getBestCompatibleVersion_ExactMatch(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.1");
        } catch (BadHttpVersionException e) {
            Assertions.fail();
        }
        Assertions.assertNotNull(version);
        Assertions.assertEquals(version, HttpVersion.HTTP_1_1);
    }
    @Test
    void getBestCompatibleVersion_BadFormat(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("http/1.1");
            Assertions.fail();
        } catch (BadHttpVersionException e) {

        }
    }

    @Test
    void getBestCompatibleVersion_HigherVersion(){
        HttpVersion version = null;
        try {
            version = HttpVersion.getBestCompatibleVersion("HTTP/1.2");
            Assertions.assertNotNull(version);
            Assertions.assertEquals(version, HttpVersion.HTTP_1_1);
        } catch (BadHttpVersionException e) {
            Assertions.fail();
        }

    }
}
