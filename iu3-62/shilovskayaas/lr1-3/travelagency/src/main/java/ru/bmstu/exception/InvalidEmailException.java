package ru.bmstu.exception;

public class InvalidEmailException extends TourAgencyException {
  public InvalidEmailException(String message) {
    super(message);
  }
}
