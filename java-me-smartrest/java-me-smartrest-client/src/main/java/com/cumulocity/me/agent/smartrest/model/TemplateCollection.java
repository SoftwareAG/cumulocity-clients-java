package com.cumulocity.me.agent.smartrest.model;

import com.cumulocity.me.agent.smartrest.model.template.Method;
import com.cumulocity.me.agent.smartrest.model.template.Path;
import com.cumulocity.me.agent.smartrest.model.template.PlaceholderType;
import com.cumulocity.me.agent.smartrest.model.template.TemplateBuilder;
import com.cumulocity.me.util.Preconditions;
import com.cumulocity.me.util.StringUtils;

import java.util.Vector;

public class TemplateCollection {

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

	public static class Builder {
		private String xid;
		private Vector templates = new Vector();

		public Builder xid(String xid) {
			this.xid = xid;
			return this;
		}

		public Builder template(TemplateBuilder templateBuilder) {
			templates.addElement(templateBuilder);
			return this;
		}

		public TemplateCollection build() {
			Preconditions.checkState(templates.size() != 0, "Templates size cannot be 0");
			Preconditions.checkState(StringUtils.isNotBlank(xid), "XID cannot be blank");

			final StringBuffer sb = new StringBuffer();
			for (int i = 0; i < templates.size(); i++) {
				sb.append(((TemplateBuilder) templates.elementAt(i)).build());
			}
			return new TemplateCollection(sb.toString().trim(), xid);
		}
	}

	public static Builder templateCollection() {
		return new Builder();
	}

	private final String templates;
	private final String xid;

	public TemplateCollection() {
		this(null, null);
	}

	public TemplateCollection(String templates, String xid) {
		this.templates = templates;
		this.xid = xid;
	}
	
	public final String buildTemplates(){
		StringBuffer buffer = new StringBuffer(MANDATORY_TEMPLATES);
		buffer.append(getTemplates());
		return buffer.toString();
	}
			
	public String getTemplates() {
		return templates;
	}

	public String getXid() {
		return xid;
	}
}
