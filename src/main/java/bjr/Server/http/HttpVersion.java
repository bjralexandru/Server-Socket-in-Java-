package bjr.Server.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {
    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;


    HttpVersion(String LITERAL, int MAJOR, int MINOR) {
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }


    private static final Pattern httpVersionRegexPattern = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");
    // To retrieve some part of the regex and use it elsewhere, you can see that we wrapped the MAJOR and MINOR
    // versions inside ?<major> and ?<minor> tags.

    // Util function to get the next best version for the provided one
    public static HttpVersion getBestCompatibleVersion(String literalVersion) throws BadHttpVersionException {
        Matcher matcher = httpVersionRegexPattern.matcher(literalVersion);
        if(!matcher.find() || matcher.groupCount() != 2) {
            // TODO change it to a 505 Error.

            throw new BadHttpVersionException();
        }

        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));

        HttpVersion tempBestCompatible = null;
        for (HttpVersion version: HttpVersion.values()) {
            if(version.LITERAL.equals(literalVersion)) {
                // If it matches perfectly, return just that.
                return version;
            } else {
                // But if it doesn't we start comparing the major and minor
                if(version.MAJOR == major) {
                    if(version.MINOR < minor){
                        tempBestCompatible = version;
                    }
                }
            }
        }
        return tempBestCompatible;
    }
}
