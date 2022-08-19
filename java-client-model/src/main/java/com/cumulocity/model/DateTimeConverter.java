package com.cumulocity.model;

import static com.cumulocity.model.util.DateTimeUtils.chronologyUTC;
import static java.lang.String.format;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.svenson.converter.TypeConverter;

/**
 * Provides methods to convert DateTime to/from String in UTC format.
 */
public class DateTimeConverter implements TypeConverter {

	protected static final DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser()
            .withChronology(chronologyUTC())
            .withOffsetParsed();

	public static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.dateTime()
            .withOffsetParsed();

    @Override
    public Object fromJSON(Object in) {
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
    public static DateTime string2Date(String in) {
        return parser.parseDateTime(in);
    }

    @Override
    public Object toJSON(Object in) {
        // if the value is null just return null
        // this is required to set an existing date field value to null;
        if(null == in){
            return null;
        }
        if (in instanceof DateTime) {
            return date2String((DateTime) in);
        } 
        
        throw new IllegalArgumentException(format("Parameter must be a %s, but was a %s!", DateTime.class, in.getClass()));

    }

    /**
     * Return a String representation of the given Date.
     * 
     * @param in
     * @return
     */
    public static String date2String(DateTime in) {
		return DATE_TIME_FORMATTER.print(in);
    }

}
