package com.cumulocity.model;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.primitives.Doubles;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@EqualsAndHashCode
public class Cpu implements Comparable<Cpu> {
    public static final String CPU_ZERO_MILLIS = "0m";

    public static final String CPU_VALUE_PATTERN = "\\d+(\\.\\d+)?m?";
    private static final Pattern CPU_PATTERN = Pattern.compile(CPU_VALUE_PATTERN);
    private final BigDecimal value;

    private Cpu(BigDecimal value) {
        this.value = checkNotNull(value, "value can't be null");
    }

    public BigDecimal asMillis() {
        return value;
    }

    @Override
    public int compareTo(Cpu o) {
        return value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return value.longValue() + "m";
    }

    public static Optional<Cpu> tryParse(String value) {
        if (Strings.isNullOrEmpty(value) || !CPU_PATTERN.matcher(value).matches()) {
            return Optional.absent();
        }
        return Optional.of(parse(value));
    }

    public static Cpu parse(String value) {
        checkNotNull(value, "Cpu string can't be null");
        final Matcher matcher = CPU_PATTERN.matcher(value);
        checkArgument(matcher.matches(), "Cpu needs to match " + CPU_PATTERN.pattern());
        BigDecimal millis = parseMillis(value);
        return new Cpu(millis);
    }

    public static Cpu valueOf(BigDecimal value) {
        return new Cpu(value);
    }

    private static BigDecimal parseMillis(String value) {
        if (value.endsWith("m")) {
            return BigDecimal.valueOf(Double.valueOf(removeLastChar(value))).setScale(0);
        } else {
            return BigDecimal.valueOf(Doubles.tryParse(value)).multiply(BigDecimal.valueOf(1000)).setScale(0);
        }
    }

    private static String removeLastChar(String string) {
        return string.substring(0, string.length() - 1);
    }
}
