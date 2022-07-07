package com.cumulocity.model.idtype;


import com.cumulocity.model.ID;

/**
 * Representation of a weak-type ID, ie an ID whose type does not match
 * a known fully qualified class name.
 * All such weak-typed IDs are defined to be kinds of XtId.
 * 
 * @author pitchfor
 *
 */
public class WeakTypedID extends XtId {

    /** 
     * Copy constructor for converting base ID types to WeakTypedID
     * @param in
     */
    public WeakTypedID(ID in) {
        this.setType(in.getType());
        this.setValue(in.getValue());
        this.setName(in.getName());
        for (String name : propertyNames()) {
            this.setProperty(name, in.getProperty(name));     
        }
    }
}
