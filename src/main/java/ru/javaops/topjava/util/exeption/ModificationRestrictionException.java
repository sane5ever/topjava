package ru.javaops.topjava.util.exeption;

public class ModificationRestrictionException extends ApplicationException {
    public static final String EXCEPTION_MODIFICATION_RESTRICTION = "exception.user.modificationRestriction";

    public ModificationRestrictionException() {
        super(ErrorType.VALIDATION_ERROR, EXCEPTION_MODIFICATION_RESTRICTION);
    }
}
