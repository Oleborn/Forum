package oleborn.forumservice.exception;

/**
 * Операция запрещена: актор не владеет ресурсом.
 */
public class AccessDeniedOperationException extends RuntimeException {

    public AccessDeniedOperationException(String message) {

        super(message);
    }
}
