package com.cumulocity.model;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.svenson.JSONParseException;
import org.svenson.converter.TypeConverter;

import com.cumulocity.model.idtype.GId;

public class IDTypeConverter implements TypeConverter {
    private static Logger logger = LoggerFactory.getLogger(IDTypeConverter.class);

    private static final String VALUENAME = "value";

    private static final String TYPENAME = "type";
    
    private static final String NAMENAME = "name";

    @Override
    public Object fromJSON(Object in) {
        if (in == null) {
            return in;
        }
        else if (in instanceof ID) {
            // Svenson may already have guessed at the type as ID,
            // but we must check that Svenson's guess matches the type parameter
            ID inId = (ID) in;
            String clazz = inId.getType().replace('_', '.');

            if (clazz != in.getClass().getCanonicalName()) {
                // the requested type and the actual type do not match, 
                // so try and create the requested type
                try {
                    ID typeInstance = (ID) Class.forName(clazz).newInstance();
                    typeInstance.setValue(inId.getValue());
                    typeInstance.setName(inId.getName());
                    return typeInstance;
                } catch (Exception e) {
                    logger.debug("Parameter type of " + inId.getType() + " is not a known class. ID type returned.");
                    return new ID(inId);
                }
            }
            // classes match so just return what we were given
            return in;

        } else if (in instanceof String) {
            // assume a simple string is an id of type GID
            return new GId((String) in);
        } else if (in instanceof Map) {
            // assume a Map indicates an object
            // there should be a "type" key that indicates the type of
            // object to create
            ID thisId;
            @SuppressWarnings("unchecked")
            Map<String, String> idMap = (Map<String, String>) in;
            try {
                if (idMap.containsKey(TYPENAME) && idMap.containsKey(VALUENAME)) {
                    thisId = new ID();
                    
                    // remove and store the type and value
                    thisId.setType(idMap.remove(TYPENAME));
                    thisId.setValue(idMap.remove(VALUENAME));
                    thisId.setName(idMap.remove(NAMENAME));
                    
                    // all remaining properties are set as dynamic properties
                    for (String key : idMap.keySet()) {
                        thisId.setProperty(key, idMap.get(key));
                    }

                    return fromJSON(thisId);
                }
            } catch (ClassCastException e) {
                throw new JSONParseException("Incompatible types creating ID class: " + e.getLocalizedMessage());
            }
        } else {
            // this is a malformed list of IDs
            throw new JSONParseException("Malformed ID list, embedded type found: " + in.getClass().getCanonicalName());
        }
        return in;
    }

    @Override
    public Object toJSON(Object in) {
        return in;
    }

}
