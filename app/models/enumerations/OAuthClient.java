package models.enumerations;

import java.util.IllegalFormatCodePointException;

/**
 * Created by teo on 4/30/17.
 */
public enum OAuthClient {
    FACEBOOK("facebook")
    ;

    private final String text;

    /**
     * @param text
     */
    private OAuthClient(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    public static OAuthClient convertStringToClass(String client) throws IllegalArgumentException{
        OAuthClient[] clients = OAuthClient.values();
        for (OAuthClient currentClient : clients) {
            if (client.toLowerCase().contains(currentClient.toString().toLowerCase())) {
                return currentClient;
            }
        }
        throw new IllegalArgumentException("Cannot find matching enum for given string!");
    }

}
