/*
 * AUTHOR: CALEB PRINCEWILL NWOKOCHA
 * SCHOOL: THE UNIVERSITY OF MANITOBA
 * DEPARTMENT: COMPUTER SCIENCE
 */

public class Exception {
    public Exception(String errorMessage) {}
}

class WrongInitialization extends java.lang.Exception {
    public WrongInitialization(String errorMessage) {
        super(errorMessage);
    }
}