package exceptions;

public class InvalidNameException extends TrainingCenterException {

    public InvalidNameException(String invalidName, String message) {
        super("Недопустимое имя: [" + invalidName + "], Причина: " + message);
    }

}
