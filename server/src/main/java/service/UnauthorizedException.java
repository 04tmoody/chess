package service;

/**
 * Indicates data was attempted to be accessed without authorization
 */
public class UnauthorizedException extends Exception{
    public UnauthorizedException(String message) {
        super(message);
    }
}
