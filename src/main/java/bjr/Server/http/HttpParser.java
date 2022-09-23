package bjr.Server.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    // Next we will define the constants that make up the Message structure
    // we're talking about CR = carriage return , SP = space and LF = line feed.

    // These characters will be defined as constants and implemented using an ASCII Table
    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10

    public HttpRequest parseHTTPRequest(InputStream inputStream) throws HttpParsingException{
        // We don't make it static because of a DESIGN argument
        // It will allow us to do some configuration on the HTTP-Parser
        // Also, doing so - makes it available to each thread which we are working with.
        // Making it static would imply that any new thread will have its own instance of this
        // method.

        // A request is made up of:
                // 1) Start line
                // 2) Header
                // 3) Message Body
        InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest request = new HttpRequest();

        try {
            parseRequestLine(reader, request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseHeaders(reader, request);
        parseBody(reader, request);
        
        return request;

    }
    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        StringBuilder processingDataBuffer = new StringBuilder(); //This stores data until we reach an "SP" token

        // Next we'll use a boolean to diff each of the items we're looking at

        boolean methodParsed = false;
        boolean requestTargetParsed = false;

        // We're looping to search for the CR in the Line Feed
        int _byte;
        while ((_byte = reader.read()) >= 0){
            if(_byte == CR){
                _byte = reader.read();
                if (_byte == LF) {
                    LOGGER.debug("Request Line VERSION to Process: {}", processingDataBuffer.toString());
                    if(!methodParsed || !requestTargetParsed) {
                        throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    // After we find the line feed, we'll update the HTTP Version

                    try {
                        request.seHttpVersion(processingDataBuffer.toString());
                    } catch (BadHttpVersionException e) {
                        throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                    }

                    return;
                } else {
                    throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            // We look for spaces in the feed line to use them as delimiters
            // for parsing the data before and after them to their corresponding
            // variables.
            if(_byte == SP) {
                if (!methodParsed){
                    // Following the pattern of a HTTP Request, first thing to process would be the MethodParsed
                    // GET/POST/DELETE/etc.
                    // Get that and change the status to true, so we know we've got it covered
                    LOGGER.debug("Request Line METHOD to Process: {}", processingDataBuffer.toString());
                    // Set the request variable to the extracted Method, such that
                    // it can be tested by our HttpParserTest
                    request.setMethod(processingDataBuffer.toString());
                    methodParsed = true;
                } else if (!requestTargetParsed) {
                    // Next in line would be the REQUEST target, but we get there only if the Method
                    // was assigned correctly.
                    LOGGER.debug("Request Line REQ TARGET to Process: {}", processingDataBuffer.toString());
                    request.setRequestTarget(processingDataBuffer.toString());
                    requestTargetParsed = true;
                } else {
                    throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                }
                // Delete and start storing the next set of tokens
                processingDataBuffer.delete(0, processingDataBuffer.length());
            } else {
                // Store all the data which will be processed when  SP shows up
                processingDataBuffer.append((char)_byte);
                if (!methodParsed){
                    // Make sure that we don't save in memory a malicious/bad request
                    // with thousands upon thousands of words/lines.
                    if (processingDataBuffer.length() > HttpMethod.MAX_LENGTH){
                        throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) {
    }

    private void parseBody(InputStreamReader reader , HttpRequest request) {
    }





}
