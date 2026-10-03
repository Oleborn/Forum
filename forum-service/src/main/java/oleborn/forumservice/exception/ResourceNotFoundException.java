package oleborn.forumservice.exception;

/**
 * Запрашиваемый ресурс не найден.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {

        super(message);
    }
}
