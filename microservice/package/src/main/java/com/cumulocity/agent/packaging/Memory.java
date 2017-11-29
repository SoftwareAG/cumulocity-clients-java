package com.cumulocity.agent.packaging;

import static com.google.common.base.Strings.isNullOrEmpty;

public class Memory {
    private String min;

    private String max;

    public Memory() {
    }

    public Memory(String min, String max) {
        this.min = min;
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public Memory or(Memory other) {
        return new Memory(nvl(min, other.min), nvl(max, other.max));
    }

    public String nvl(String value, String defaultValue) {
        if (!isNullOrEmpty(value)) {
            return value;
        }
        return defaultValue;
    }

}
