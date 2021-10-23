package com.rps.exceptions;

public class UseException extends Exception {
    UseExceptionType useExceptionType;
    public UseException(UseExceptionType useExceptionType) {
        super(" failed because "+ useExceptionType);
        this.useExceptionType = useExceptionType;
    }
    public UseExceptionType getUseExceptionType() {
        return useExceptionType;
    }

}
