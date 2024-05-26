package ssg.nodemanager.exception;

public class NotLoggedInException extends RuntimeException {

    public NotLoggedInException() {
        super("User is not logged in.");
    }

    public NotLoggedInException(String message) {
        super(message);
    }
}