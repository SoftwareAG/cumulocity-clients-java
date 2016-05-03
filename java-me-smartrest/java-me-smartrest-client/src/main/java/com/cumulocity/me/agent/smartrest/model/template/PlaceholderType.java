package com.cumulocity.me.agent.smartrest.model.template;

import com.cumulocity.me.util.EnumType;

public final class PlaceholderType extends EnumType {

    public static final PlaceholderType STRING = new PlaceholderType("STRING", 0);
    public static final PlaceholderType UNSIGNED = new PlaceholderType("UNSIGNED", 1);
    public static final PlaceholderType INTEGER = new PlaceholderType("INTEGER", 2);
    public static final PlaceholderType NUMBER = new PlaceholderType("NUMBER", 3);
    public static final PlaceholderType DATE = new PlaceholderType("DATE", 4);
    public static final PlaceholderType NOW = new PlaceholderType("NOW", 5);

    private PlaceholderType(String name, int ordinal) {
        super(name, ordinal);
    }
}
