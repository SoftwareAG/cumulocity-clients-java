package com.cumulocity.me.agent.logger;

import com.cumulocity.me.agent.plugin.AgentApi;
import com.cumulocity.me.agent.plugin.AgentInfo;
import com.cumulocity.me.smartrest.client.SmartRequest;
import com.cumulocity.me.smartrest.client.impl.SmartRequestImpl;
import com.cumulocity.me.util.Preconditions;
import net.sf.microlog.core.Formatter;
import net.sf.microlog.core.Level;
import net.sf.microlog.core.Logger;

public class EventAppender extends AbstractAppender {
    public static final String XID = "c8y_LoggerTemplate";
    public static final int MESSAGE_ID = 100;

    private final AgentApi agentApi;

    public EventAppender(AgentApi agentApi) {
        this.agentApi = agentApi;
    }

    public EventAppender(AgentApi agentApi, Formatter formatter) {
        this.agentApi = agentApi;
        this.setFormatter(formatter);
    }

    public void doLog(String clientID, String name, long time, Level level, Object message, Throwable t) {
        if (level.toInt() > Level.DEBUG_INT) {
        	final AgentInfo agentInfo = agentApi.getAgentInfo();
        	Preconditions.checkState(agentInfo != null, "Agent info is null.");

        	final String text = getFormatter().format(clientID, name, time, level, message, t);
        	final SmartRequest request = new SmartRequestImpl(MESSAGE_ID, agentInfo.getDeviceId() + "," + "cy8_LoggerEvent" + "," + text);
        	agentApi.getSmartrestService().sendRequest(request, XID, null);
		}
    }
}
