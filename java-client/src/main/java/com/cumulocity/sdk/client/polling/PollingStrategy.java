package com.cumulocity.sdk.client.polling;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PollingStrategy {
    
    private static final Long POLL_CREDENTIALS_TIMEOUT = 3600L * 24;
    private static final Long[] POLL_INTERVALS = new Long[] { 5L, 10L, 20L, 40L, 80L, 160L, 320L, 640L, 1280L, 2560L, 3600L };

    private final List<Long> pollIntervals;
    private boolean repeatLast = true;
    private final Long last;
    private int index;
    private final TimeUnit timeUnit;
    private final Long timeout;
        
    public PollingStrategy() {
        this(POLL_CREDENTIALS_TIMEOUT, SECONDS, POLL_INTERVALS);
    }
    public PollingStrategy(Long timeout, TimeUnit timeUnit, List<Long> pollIntervals) {
        this.timeout = timeout;
        this.pollIntervals = pollIntervals;
        this.timeUnit = timeUnit;
        this.index = -1;
        this.last = pollIntervals.isEmpty() ? null : pollIntervals.get(pollIntervals.size() - 1);
    }
    
    public PollingStrategy(Long timeout, TimeUnit timeUnit, Long... pollIntervals) {
        this(timeout, timeUnit, asList(pollIntervals));
    }
    
    private Long forIndex(int index) {
        return index < pollIntervals.size() ? pollIntervals.get(index) : null;
    }

    public boolean isEmpty() {
        return peakNext() == null;
    }

    /**      
     * get next polling interval 
     * @return interval in milliseconds
     */
    public Long peakNext() {
        Long result = forIndex(index + 1);
        if(result == null && repeatLast) {
            result = last;
        }
        return asMiliseconds(result);
    }
    
    /**
     * get and remove next polling interval 
     * @return interval
     */
    public Long popNext() {
        Long result = peakNext();
        if(result != null) {
            index++;
        }
        return result;
    }
    
    /**
     * @return timeout
     */
    public Long getTimeout() {
        return asMiliseconds(timeout);
    }
    

    public boolean isRepeatLast() {
        return repeatLast;
    }

    public void setRepeatLast(boolean repeatLast) {
        this.repeatLast = repeatLast;
    }

    private Long asMiliseconds(Long result) {        
        return result == null ? null : MILLISECONDS.convert(result, timeUnit);
    }
}
