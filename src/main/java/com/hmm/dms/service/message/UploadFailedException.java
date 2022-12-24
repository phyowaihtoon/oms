package com.hmm.dms.service.message;

public class UploadFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UploadFailedException(String cause, String code, String message) {
        super(cause);
        this.code = code;
        this.message = message;
    }
}
