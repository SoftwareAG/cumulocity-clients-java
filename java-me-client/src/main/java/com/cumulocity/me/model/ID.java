/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.cumulocity.me.model;

import com.cumulocity.me.model.util.ExtensibilityConverter;

public class ID {
    
    private String type;

    private String value;

    private String name;

    public ID() {
        setType(ExtensibilityConverter.classToStringRepresentation(this.getClass()));
    }

    public ID(String id) {
        this();
        this.setValue(id);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //@JSONProperty(ignore = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ID other = (ID) obj;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    /**
     * This method compares if two ids have the same content (type, value) even if they have different classes. This method is useful for the identity service.
     * @param otherId the ID to compare with this instance
     * @return true if both ID have the same contents (type, value) regardless of the class, false otherwise
     */
    public boolean equalsIgnoreClass(ID otherId) {
        if (this == otherId)
            return true;
        if (otherId == null)
            return false;
        if (type == null) {
            if (otherId.type != null)
                return false;
        } else if (!type.equals(otherId.type))
            return false;
        if (value == null) {
            if (otherId.value != null)
                return false;
        } else if (!value.equals(otherId.value))
            return false;
        return true;
    }

    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("ID [type=").append(type).append(", value=").append(value).append("]");
        return builder.toString();
    }
}
