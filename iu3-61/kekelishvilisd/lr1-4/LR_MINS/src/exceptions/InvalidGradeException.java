package exceptions;

public class InvalidGradeException extends TrainingCenterException {

    public InvalidGradeException(int invalidValue, int min, int max) {
        super("Недопустимая оценка: " + invalidValue + " (допустимо " + min + "-" + max + ")");

    }

}
