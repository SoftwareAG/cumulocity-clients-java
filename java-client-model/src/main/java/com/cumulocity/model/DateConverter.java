package com.cumulocity.model;

import static java.lang.String.format;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.svenson.converter.TypeConverter;

/**
 * Provides methods to convert Date to/from String, so that it can be used as JSON object where required.
 */
@Deprecated
public class DateConverter implements TypeConverter {

    private static final DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
    private static final DateTimeFormatter format = ISODateTimeFormat.dateTime();

    @Override
    public Object fromJSON(Object in) {
        // if the value is null just return null
        // this is required to set an existing date field value to null;
        if(null == in){
            return null;
        }
        if (in instanceof String) {
            return string2Date((String) in);
        } else {
            throw new IllegalArgumentException("Parameter must be a String, was a " + ((null != in) ? in.getClass().toString() : "null"));
        }
    }

    /**
     * Convert a String into a date using {@link ISODateTimeFormat};
     *
     * @param in
     * @return
     */
    @Deprecated
    public static Date string2Date(String in) {
        return parser.parseDateTime(in).toDate();
    }

    @Override
    public Object toJSON(Object in) {
        // if the value is null just return null
        // this is required to set an existing date field value to null;
        if(null == in){
            return null;
        }
        if (in instanceof Date) {
            return date2String((Date) in);
        }

        throw new IllegalArgumentException(format("Parameter must be a %s, but was a %s!", Date.class, in.getClass()));

    }

    /**
     * Return a String representation of the given Date.
     *
     * @param in
     * @return
     */
    @Deprecated
    public static String date2String(Date in) {
        DateTime dateTime = new DateTime(in.getTime());
        return format.print(dateTime);
    }

}