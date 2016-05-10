package com.cumulocity.me.util;

public class EnumType {
    private final String name;
    private final int ordinal;

    protected EnumType(String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public final String name() {
        return name;
    }

    public final int ordinal() {
        return ordinal;
    }

    public final int compareTo(EnumType other) {
        final EnumType self = this;
        if (self.getClass() != other.getClass()) {
            throw new ClassCastException();
        }
        return self.ordinal() - other.ordinal();
    }

    public final boolean equals(Object other) {
        return this == other;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return name;
    }
}