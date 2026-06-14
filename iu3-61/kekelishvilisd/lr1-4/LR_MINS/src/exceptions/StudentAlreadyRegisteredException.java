package exceptions;

public class StudentAlreadyRegisteredException extends TrainingCenterException {

    public StudentAlreadyRegisteredException(String name) {
        super("Студент с именем \"" + name + "\" уже зарегистрирован");
    }
}