package com.cumulocity.sdk.client;

public enum ProcessingMode {
    
    TRANSIENT,

    PERSISTENT,

    /**
     * Persistent with real time broadcast disabled
     */
    QUIESCENT,

    /**
     * CEP only. Persistence & real time broadcast are disabled.
     */
    CEP;

    public static final ProcessingMode DEFAULT = ProcessingMode.PERSISTENT;

    public static ProcessingMode parse(String value) {
        if (value == null) {
            return DEFAULT;
        }
        value = value.trim();
        if (value.length() == 0) {
            return DEFAULT;
        }
        return valueOf(value.toUpperCase());
    }

    public static boolean isPersistent(ProcessingMode other) {
        return other == null || other == PERSISTENT || other == QUIESCENT;
    }

    public static boolean isQuiescent(ProcessingMode other) {
        return other == QUIESCENT || other == CEP;
    }
}
