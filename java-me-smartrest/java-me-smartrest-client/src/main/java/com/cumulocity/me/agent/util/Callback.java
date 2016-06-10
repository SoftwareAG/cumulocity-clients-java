package com.cumulocity.me.agent.util;

public abstract class Callback {

    public static void execute(Callback callback) {
        if (callback != null) {
            callback.execute();
        }
    }

    public abstract void execute();
}
