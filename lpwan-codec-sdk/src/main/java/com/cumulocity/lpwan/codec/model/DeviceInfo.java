/*
 * Copyright (c) 2012-2020 Cumulocity GmbH
 * Copyright (c) 2020-2021 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 *
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */

package com.cumulocity.lpwan.codec.model;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * The <b>DeviceInfo</b> class uniquely represents one device with the device manufacturer name, the device model.
 *
 */
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInfo {

    /**
     * This represents the name of the device manufacturer.
     */
    @NotBlank
    private String manufacturer;

    /**
     * This represents the name of the device model.
     */
    @NotBlank
    private String model;

    /**
     * This method validates the object field.
     *
     * @throws IllegalArgumentException if the field marked with <b>@NotBlank</b> are either null or blank.
     * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/IllegalArgumentException.html">IllegalArgumentException</a>
     */
    public void validate() {
        List<String> missingParameters = new ArrayList<>(2);

        if (Strings.isNullOrEmpty(manufacturer)) {
            missingParameters.add("'manufacturer'");
        }

        if (Strings.isNullOrEmpty(model)) {
            missingParameters.add("'model'");
        }

        if(!missingParameters.isEmpty()) {
            throw new IllegalArgumentException("DeviceInfo is missing mandatory parameters: " + String.join(", ", missingParameters));
        }
    }

    public @NotBlank String getManufacturer() {
        return this.manufacturer;
    }

    public @NotBlank String getModel() {
        return this.model;
    }

    public void setManufacturer(@NotBlank String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setModel(@NotBlank String model) {
        this.model = model;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof DeviceInfo)) return false;
        final DeviceInfo other = (DeviceInfo) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$manufacturer = this.getManufacturer();
        final Object other$manufacturer = other.getManufacturer();
        if (this$manufacturer == null ? other$manufacturer != null : !this$manufacturer.equals(other$manufacturer))
            return false;
        final Object this$model = this.getModel();
        final Object other$model = other.getModel();
        if (this$model == null ? other$model != null : !this$model.equals(other$model)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof DeviceInfo;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $manufacturer = this.getManufacturer();
        result = result * PRIME + ($manufacturer == null ? 43 : $manufacturer.hashCode());
        final Object $model = this.getModel();
        result = result * PRIME + ($model == null ? 43 : $model.hashCode());
        return result;
    }

    public String toString() {
        return "DeviceInfo(manufacturer=" + this.getManufacturer() + ", model=" + this.getModel() + ")";
    }
}
