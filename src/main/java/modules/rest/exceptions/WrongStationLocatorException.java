package modules.rest.exceptions;

public class WrongStationLocatorException extends IllegalArgumentException {
    public WrongStationLocatorException(String s) {
        super(s);
    }
}
