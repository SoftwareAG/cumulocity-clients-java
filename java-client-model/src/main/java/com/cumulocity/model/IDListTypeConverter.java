package com.cumulocity.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.JSONParseException;
import org.svenson.converter.TypeConverter;

public class IDListTypeConverter implements TypeConverter {

    private static Logger logger = LoggerFactory.getLogger(IDListTypeConverter.class);

    @Override
    public Object fromJSON(Object in) {
        if (in == null) {
            return null;
        }
        if(logger.isDebugEnabled()) {
            logger.debug(in.getClass().getCanonicalName());
        }
        if (in instanceof List) {
            List<?> idListIn = (List<?>) in;

            logger.debug("A list");
            // For ID Types, the list will be of type <ID>
            Set<ID> idSetOut = new LinkedHashSet<ID>();

            for (Object id : idListIn) {
                idSetOut.add((ID) new IDTypeConverter().fromJSON(id));
            }
            return idSetOut;
        }
        // this is not list of IDs
        throw new JSONParseException("Not an ID list. Type is: " + in.getClass().getCanonicalName());
    }

    @Override
    public Object toJSON(Object in) {
        return in;
    }
}
