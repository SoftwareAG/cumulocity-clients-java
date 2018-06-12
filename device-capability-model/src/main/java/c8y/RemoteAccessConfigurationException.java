package c8y;

public class RemoteAccessConfigurationException extends RuntimeException {


    public RemoteAccessConfigurationException(String message, Object... params) {
        super(String.format(message, params));
    }
}
