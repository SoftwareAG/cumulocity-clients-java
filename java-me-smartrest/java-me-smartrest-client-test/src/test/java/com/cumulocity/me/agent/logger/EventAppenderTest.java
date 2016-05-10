package com.cumulocity.me.agent.logger;

import com.cumulocity.me.agent.plugin.AgentApi;
import com.cumulocity.me.agent.plugin.AgentInfo;
import com.cumulocity.me.agent.smartrest.SmartrestService;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import net.sf.microlog.core.Formatter;
import net.sf.microlog.core.format.SimpleFormatter;
import org.junit.Test;

import static net.sf.microlog.core.Level.TRACE;
import static org.mockito.Mockito.*;

public class EventAppenderTest {

    @Test
    public void shouldLog() {
        final AgentInfo givenInfo = new AgentInfo("111");
        final AgentApi givenApi = mock(AgentApi.class);
        final Formatter givenFormatter  = new SimpleFormatter();
        final EventAppender givenAppender = new EventAppender(givenApi, givenFormatter);
        final SmartrestService givenSmartrestService = mock(SmartrestService.class);

        when(givenApi.getSmartrestService()).thenReturn(givenSmartrestService);
        when(givenApi.getAgentInfo()).thenReturn(givenInfo);
        givenAppender.doLog(null, null, 222, TRACE, "Hello", null);

        verify(givenSmartrestService).sendRequest(new SmartRequestImpl("100,111,cy8_LoggerEvent,222:[TRACE]-Hello"), "c8y_LoggerTemplate", null);
    }
}
