package com.cumulocity.me.agent.smartrest.model;

import com.cumulocity.me.agent.smartrest.model.template.Method;
import com.cumulocity.me.agent.smartrest.model.template.Path;
import com.cumulocity.me.agent.smartrest.model.template.PlaceholderType;
import com.cumulocity.me.agent.smartrest.model.template.TemplateBuilder;

public abstract class TemplateCollection {

	public static final int GET_PENDING_OPERATIONS_MESSAGE_ID = 98;
	public static final int GET_EXECUTING_OPERATIONS_MESSAGE_ID = 99;

	
	public static final String MANDATORY_TEMPLATES
		= TemplateBuilder.requestTemplate()
			.messageId(GET_PENDING_OPERATIONS_MESSAGE_ID)
			.method(Method.GET)
			.path(Path.path("/devicecontrol/operations?agentId=%%&status=PENDING&pageSize=100"))
			.placeholder("%%")
			.placeholderType(PlaceholderType.UNSIGNED)
			.build()
		+ TemplateBuilder.requestTemplate()
			.messageId(GET_EXECUTING_OPERATIONS_MESSAGE_ID)
			.method(Method.GET)
			.path(Path.path("/devicecontrol/operations?agentId=%%&status=EXECUTING&pageSize=100"))
			.placeholder("%%")
			.placeholderType(PlaceholderType.UNSIGNED)
			.build();
	
	public final String buildTemplates(){
		StringBuffer buffer = new StringBuffer(MANDATORY_TEMPLATES);
		buffer.append(getTemplates());
		return buffer.toString();
	}
			
	public abstract String getTemplates();
	public abstract String getXid();
	
}
