package services.auth;

/**
 * Created by Kris on 2015-05-15.
 */
public class TokenPayloadException extends Exception {
    public TokenPayloadException() {
    }

    public TokenPayloadException(String message) {
        super(message);
    }
}