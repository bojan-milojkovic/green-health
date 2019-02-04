package com.green.health.util;

import java.util.List;

public class MyBadInputResponse {
	private String cause;
    
    private String message;
    
    private List<String> validationErrors;
    
    public MyBadInputResponse(String cause, String message) {
        this.cause = cause;
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }
    
    public String toString(){
        String mess = cause + "\n" + message;
        if(validationErrors!=null){
            for(String e : validationErrors){
                mess += ("\n\t"+e);
            }
        }
        return mess;
    }
}