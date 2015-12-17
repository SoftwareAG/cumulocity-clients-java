package com.cumulocity.agent.server.logging;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

public class LogFileCommandBuilder {

    private static String DEFAULT_TIMESTAMP_FORMAT = "HH:mm";
    
    private static Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9_ ]*$");
    
    public static String HOURS_FORMAT = "HH";
    public static String MINUTES_FORMAT = "mm";
    
    private StringBuilder command = new StringBuilder();
    
    private List<String> filters = Lists.newArrayList();
    
    private int maximumLines = -1;
    
    public static LogFileCommandBuilder searchInFile(String file) {
        LogFileCommandBuilder builder = new LogFileCommandBuilder();
        builder.appendCatCommand(file);
        return builder;
    }
    
    public LogFileCommandBuilder withMaximumLines(int lines) {
        this.maximumLines = lines;
        return this;
    }
    
    public LogFileCommandBuilder withTenant(String tenant) throws InvalidSearchException {
        if (!PATTERN.matcher(tenant).matches()) {
            throw new InvalidSearchException("Cannot build command. Search parameters only allow the following characters [a-zA-Z0-9_]");
        }
        this.filters.add(String.format("\\- %s \\-", tenant));
        return this;
    }
    
    public LogFileCommandBuilder withDeviceUser(String deviceUser) throws InvalidSearchException {
        if (!PATTERN.matcher(deviceUser).matches()) {
            throw new InvalidSearchException("Cannot build command. Search parameters only allow the following characters [a-zA-Z0-9_]");
        }
        this.filters.add(String.format("\\- %s \\-", deviceUser));
        return this;
    }
    
    public LogFileCommandBuilder withSearchText(String text) throws InvalidSearchException {
        if (!PATTERN.matcher(text).matches()) {
            throw new InvalidSearchException("Cannot build command. Search parameters only allow the following characters [a-zA-Z0-9_]");
        }
        this.filters.add(text);
        return this;
    }
    
    public LogFileCommandBuilder withTimeRange(Date dateFrom, Date dateTo) {
        String filter = dateFilter(dateFrom, dateTo, DEFAULT_TIMESTAMP_FORMAT);
        if (filter != null) {
            this.filters.add(filter);
        }
        return this;
    }
    
    public LogFileCommandBuilder withTimeRangeAndFormat(Date dateFrom, Date dateTo, String timestampFormat) {
        String filter = dateFilter(dateFrom, dateTo, timestampFormat);
        if (filter != null) {
            this.filters.add(filter);
        }
        return this;
    }
    
    public String build() {
        
        for (String filter : filters) {
            appendPipe();
            appendEgrepCommand(filter);
        }
        if (maximumLines > 0) {
            appendPipe();
            appendTailCommand();
        }
        return command.toString();
    }
    
    @SuppressWarnings("deprecation")
    private static String dateFilter(Date dateFrom, Date dateTo, String timestampFormat) {
        if (dateTo.before(dateFrom)) {
            return null;
        }
        StringBuilder filter = new StringBuilder("^");
        int hoursFrom = dateFrom.getHours();
        int minutesFrom = dateFrom.getMinutes();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateTo);
        cal.add(Calendar.MINUTE, -1);
        dateTo = cal.getTime();
        int hoursTo = dateTo.getHours();
        int minutesTo = dateTo.getMinutes();
        if (hoursTo == hoursFrom) {
            filter.append(getFilterForSameHour(timestampFormat, hoursFrom, minutesFrom, minutesTo));
        } else if (hoursTo == hoursFrom + 1) {
            filter.append(getFilterForConnectedHours(timestampFormat, hoursFrom, minutesFrom, hoursTo, minutesTo));
        } else {
            filter.append(getFilterForHourRange(timestampFormat, hoursFrom, minutesFrom, hoursTo, minutesTo));
        }
        return filter.toString();
    }

    private static String getFilterForHourRange(String timestampFormat, int hoursFrom, int minutesFrom, int hoursTo,
            int minutesTo) {
        String firstHour = replacePlaceholders(getLeadingZeroString(hoursFrom),numberRangeFilter(minutesFrom, 59), timestampFormat);
        String lastHour = replacePlaceholders(getLeadingZeroString(hoursTo),numberRangeFilter(0, minutesTo), timestampFormat);
        String hoursBetween = replacePlaceholders(numberRangeFilter((hoursFrom + 1) % 24, (hoursTo - 1) % 24), "", timestampFormat);
        return String.format("(%s)|(%s)|(%s)", firstHour, hoursBetween, lastHour);
    }

    private static String getFilterForConnectedHours(String timestampFormat, int hoursFrom, int minutesFrom,
            int hoursTo, int minutesTo) {
        String firstHour = replacePlaceholders(getLeadingZeroString(hoursFrom),numberRangeFilter(minutesFrom, 59), timestampFormat);
        String lastHour = replacePlaceholders(getLeadingZeroString(hoursTo),numberRangeFilter(0, minutesTo), timestampFormat);
        return String.format("(%s)|(%s)", firstHour, lastHour);
    }

    private static String getFilterForSameHour(String timestampFormat, int hoursFrom, int minutesFrom, int minutesTo) {
        // We only need to care about minutes
        return replacePlaceholders(getLeadingZeroString(hoursFrom),numberRangeFilter(minutesFrom, minutesTo), timestampFormat);
    }

    private static String getLeadingZeroString(int number) {
        if (number < 10) {
            return "0" + number;
        } else {
            return String.valueOf(number);
        }
    }
    
    private static String replacePlaceholders(String hours, String minutes, String format) {
        return format.replace(HOURS_FORMAT, hours).replace(MINUTES_FORMAT, minutes);
    }
    
    private static String numberRangeFilter(int from, int to) {
        StringBuilder filter = new StringBuilder();
        filter.append("(");
        filter.append(getLeadingZeroString(from));
        for(int i = from+1; i<= to; i++) {
            filter.append("|" + getLeadingZeroString(i));
        }
        filter.append(")");
        return filter.toString();
    }
    
    private void appendCatCommand(String file) {
        command.append("cat " + file);
    }
    
    private void appendEgrepCommand(String filter) {
        command.append(String.format("egrep '%s'", filter));
    }
     
    private void appendTailCommand() {
        command.append("tail -n " + maximumLines);
    }
    
    private void appendPipe() {
        command.append(" | ");
    }
    
    public class InvalidSearchException extends Exception {
        
        public InvalidSearchException(String exception) {
            super(exception);
        }
    }
    
}
