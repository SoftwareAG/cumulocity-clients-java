package com.cumulocity.model;

import com.cumulocity.model.util.CumulocityStorageUnit;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.MoreObjects.firstNonNull;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.math.BigDecimal.ROUND_DOWN;

public class DataSize implements Comparable<DataSize> {
    public static final String MEMORY_ZERO_MBYTES = "0M";

    private static final String DEFAULT_UNIT = "B";

    private final BigDecimal quantity;
    private final String unit;

    public static final String DATA_SIZE_VALUE_PATTERN = "^((\\d+)\\s*([BKMGTEP][i]?)?)$";
    private static final Pattern DATA_SIZE_PATTERN = Pattern.compile(DATA_SIZE_VALUE_PATTERN);

    @java.beans.ConstructorProperties({"quantity", "unit"})
    private DataSize(BigDecimal quantity, String unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public static Optional<DataSize> tryParse(String data) {
        if (Strings.isNullOrEmpty(data) || !DATA_SIZE_PATTERN.matcher(data).matches()) {
            return Optional.absent();
        }
        return Optional.of(parse(data));
    }

    public static DataSize parse(String data) {
        checkNotNull(data, "Data size string can't be null");
        final Matcher matcher = DATA_SIZE_PATTERN.matcher(data);
        checkArgument(matcher.matches(), "Data size needs to match " + DATA_SIZE_PATTERN.pattern());
        final String quantity = matcher.group(2);
        final String unit = matcher.group(3);
        return new DataSize(BigDecimal.valueOf(Double.parseDouble(quantity)), firstNonNull(unit, DEFAULT_UNIT));
    }

    public static DataSize of(double quantity, String unit) {
        return of(BigDecimal.valueOf(quantity), unit);
    }

    public static DataSize of(BigDecimal quantity, String unit) {
        return new DataSize(quantity, unit);
    }

    public static DataSize ofBytes(long value) {
        return of(value, "B");
    }

    public BigDecimal getQuantity() {
        return this.quantity;
    }

    public String getUnit() {
        return this.unit;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof DataSize)) return false;
        final DataSize other = (DataSize) o;
        if (this.getQuantity().compareTo(other.getQuantity()) != 0) return false;
        final Object this$unit = this.getUnit();
        final Object other$unit = other.getUnit();
        if (!Objects.equals(this$unit, other$unit)) return false;
        return true;
    }

    @Override
    public int compareTo(DataSize o) {
        if (o.quantity == quantity && o.unit == unit) {
            return 0;
        }

        long myBytes = CumulocityStorageUnit.getBytes(quantity.longValue(), unit);
        long oBytes = CumulocityStorageUnit.getBytes(o.quantity.longValue(), o.unit);

        return Long.compare(myBytes, oBytes);
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $quantity = this.getQuantity().hashCode();
        result = result * PRIME + (int) ($quantity >>> 32 ^ $quantity);
        final Object $unit = this.getUnit();
        result = result * PRIME + ($unit == null ? 43 : $unit.hashCode());
        return result;
    }

    public String toString() {
        return defaultFormat().format(this.getQuantity().setScale(2, ROUND_DOWN)) + this.getUnit();
    }

    private DecimalFormat defaultFormat() {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        format.setGroupingUsed(false);
        return format;
    }
}
