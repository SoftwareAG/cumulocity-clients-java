package com.cumulocity.me.logger;

public class Log {
    
    private static final Log INSTANCE = new Log();
    
    private static boolean showDebug;

    public static final Log getInstance() {
        return INSTANCE;
    }
    
    public void debug(final String message) {
      if (showDebug) {
        System.out.println(message);
      }
    }

    public static void setShowDebug(final boolean show) {
      showDebug = show;
    }
}
