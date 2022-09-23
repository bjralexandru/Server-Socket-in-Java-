package bjr.Server.http;

public enum HttpMethod {
    GET, HEAD;
    public static final int MAX_LENGTH;

    static {
        int tempMaxLength = -1;
        for(HttpMethod method: values()) {
            // We iterate over all the method defined in the enumeration
            // and assign the lengths automatically. This way we can insert new
            // methods without changing the length property.
            if(method.name().length() > tempMaxLength) {
                tempMaxLength = method.name().length();
            }
        }
        MAX_LENGTH = tempMaxLength;
    }
}
