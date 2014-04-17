package com.cumulocity.sdk.client.polling;

import static java.util.Arrays.asList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.FluentIterable;

public class PollingStrategy {

    private final List<Long> pollIntervals;
    private final boolean repeatLast;
    private final Long last;
    private int index;
    private final TimeUnit timeUnit;
    private final Long timeout;
        
    public PollingStrategy(boolean repeatLast, Long timeout, TimeUnit timeUnit, List<Long> pollIntervals) {
        this.repeatLast = repeatLast;
        this.timeout = timeout;
        this.pollIntervals = pollIntervals;
        this.timeUnit = timeUnit;
        this.index = -1;
        this.last = FluentIterable.from(pollIntervals).last().orNull();
    }
    
    public PollingStrategy(boolean repeatLast, Long timeout, TimeUnit timeUnit, Long... pollIntervals) {
        this(repeatLast, timeout, timeUnit, asList(pollIntervals));
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
     * @return interval in milliseconds
     */
    public Long popNext() {
        Long result = peakNext();
        if(result != null) {
            index++;
        }
        return result;
    }
    
    /**
     * @return timeout in niliseconds
     */
    public Long getTimeout() {
        return asMiliseconds(timeout);
    }

    private Long asMiliseconds(Long result) {        
        return result == null ? null : MILLISECONDS.convert(result, timeUnit);
    }
}
