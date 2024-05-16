package org.example.ExceptionHandlers;

public class SchedulesErrorResponse {
    private String errorField;
    private String errorMessage;

    public SchedulesErrorResponse() {
    }

    public String getErrorField() {
        return errorField;
    }

    public void setErrorField(String errorField) {
        this.errorField = errorField;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
