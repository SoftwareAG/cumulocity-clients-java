package com.cumulocity.model.audit;

import org.apache.commons.lang3.ClassUtils;
import org.svenson.converter.TypeConverter;

/**
 * Converter is used to ensure backward compatibility with Dozer mapper for previousValue and newValue of Audit Change
 */
public class AuditChangeValueConverter implements TypeConverter {

    @Override
    public Object fromJSON(Object in) {
        return in;
    }

    @Override
    public Object toJSON(Object in) {
        if (in != null && ClassUtils.isPrimitiveOrWrapper(in.getClass())) {
            return in.toString();
        }
        return in;
    }
}
