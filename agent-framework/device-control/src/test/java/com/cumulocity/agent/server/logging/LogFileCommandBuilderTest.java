package com.cumulocity.agent.server.logging;

import static com.cumulocity.agent.server.logging.LogFileCommandBuilder.searchInFile;
import static org.fest.assertions.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class LogFileCommandBuilderTest {

    private static String filename = "defaultFileName";
    
    private Calendar cal = Calendar.getInstance();
    
    @Test
    public void shouldHandleSearchTextCorrectly() throws Exception {
        LogFileCommandBuilder builder = searchInFile(filename);
        builder.withSearchText("text1");
        builder.withSearchText("text2");
        
        assertThat(builder.build()).isEqualTo("cat defaultFileName | egrep 'text1' | egrep 'text2'");
    }
    
    @Test
    public void shouldHandleTimeRangeSameHour() throws Exception {
        cal.set(Calendar.HOUR_OF_DAY,17);
        cal.set(Calendar.MINUTE,30);
        Date from = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY,17);
        cal.set(Calendar.MINUTE,45);
        Date to = cal.getTime();
        
        LogFileCommandBuilder builder = searchInFile(filename);
        builder.withTimeRange(from, to);
        
        assertThat(builder.build()).isEqualTo("cat defaultFileName | egrep '^17:(30|31|32|33|34|35|36|37|38|39|40|41|42|43|44)'");
    }
    
    @Test
    public void shouldHandleTimeRange1HourLater() throws Exception {
        cal.set(Calendar.HOUR_OF_DAY,17);
        cal.set(Calendar.MINUTE,50);
        Date from = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY,18);
        cal.set(Calendar.MINUTE,5);
        Date to = cal.getTime();
        
        LogFileCommandBuilder builder = searchInFile(filename);
        builder.withTimeRange(from, to);
        
        assertThat(builder.build()).isEqualTo("cat defaultFileName | egrep '^(17:(50|51|52|53|54|55|56|57|58|59))|(18:(00|01|02|03|04))'");
    }
    
    @Test
    public void shouldHandleComplexTimeRange() throws Exception {
        cal.set(Calendar.HOUR_OF_DAY,14);
        cal.set(Calendar.MINUTE,55);
        Date from = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY,19);
        cal.set(Calendar.MINUTE,5);
        Date to = cal.getTime();
        
        LogFileCommandBuilder builder = searchInFile(filename);
        builder.withTimeRange(from, to);
        
        assertThat(builder.build()).isEqualTo("cat defaultFileName | egrep '^(14:(55|56|57|58|59))|((15|16|17|18):)|(19:(00|01|02|03|04))'");
    }
}
