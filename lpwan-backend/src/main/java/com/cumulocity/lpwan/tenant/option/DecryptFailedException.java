package com.cumulocity.lpwan.tenant.option;

public class DecryptFailedException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4997180499970932425L;

    public DecryptFailedException(String message) {
        super(message);
    }

    public DecryptFailedException(String message, IllegalStateException cause) {
        super(message, cause);
    }
}
