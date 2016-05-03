package com.cumulocity.me.agent.smartrest.model.template;

import com.cumulocity.me.util.EnumType;

public final class Method extends EnumType {
    public static final Method GET = new Method("GET", 0);
    public static final Method POST = new Method("POST", 1);
    public static final Method PUT = new Method("PUT", 2);
    public static final Method DELETE = new Method("DELETE", 3);

    private Method(String name, int ordinal) {
        super(name, ordinal);
    }
}
