package com.cumulocity.me.agent.smartrest.model.template;


import com.cumulocity.me.util.EnumType;
import com.cumulocity.me.util.Preconditions;

import java.util.Vector;

public abstract class TemplateType extends EnumType {

    public static final TemplateType REQUEST_TEMPLATE = new TemplateType("10", 0) {

        public String build(TemplateBuilder templateBuilder) {
            Preconditions.checkState(templateBuilder.messageId != null, "Message id cannot be null");
            Preconditions.checkState(templateBuilder.method != null, "Method cannot be null");
            Preconditions.checkState(templateBuilder.path != null, "Path cannot be null");

            final StringBuffer sb = new StringBuffer();
            addEntry(templateBuilder.templateType, sb);
            addEntry(templateBuilder.messageId, sb);
            addEntry(templateBuilder.method, sb);
            addEntry(templateBuilder.path.get(), sb);
            addEntry(templateBuilder.content, sb);
            addEntry(templateBuilder.accept, sb);
            addEntry(templateBuilder.placeholder, sb);
            addEntry(templateBuilder.placeholderTypes, sb, ' ');
            addEntry(templateBuilder.json, sb);
            addNewLine(sb);
            return sb.toString();
        }
    };

    public static final TemplateType RESPONSE_TEMPLATE = new TemplateType("11", 1) {
        public String build(TemplateBuilder templateBuilder) {
            Preconditions.checkState(templateBuilder.messageId != null, "Message id cannot be null");

            final StringBuffer sb = new StringBuffer();
            addEntry(templateBuilder.templateType, sb);
            addEntry(templateBuilder.messageId, sb);
            addEntry(templateBuilder.json, sb);
            addEntry(templateBuilder.jsonPaths, sb, ',');
            addNewLine(sb);
            return sb.toString();
        }
    };

    private TemplateType(String name, int ordinal) {
        super(name, ordinal);
    }

    public abstract String build(TemplateBuilder templateBuilder);

    protected void addEntry(Json.JsonObjectBuilder json, StringBuffer sb) {
        if (json != null) {
            sb.append("\"" + json.get() + "\"").append(",");
        } else {
            sb.append(",");
        }
    }

    protected void addEntry(EnumType e, StringBuffer sb) {
        String value;
        if (e == null) {
            value = "";
        } else {
            value = e.name();
        }
        addEntry(value, sb);
    }

    protected void addEntry(Vector value, StringBuffer sb, char separator) {
        final StringBuffer result = new StringBuffer();
        for (int i = 0; i < value.size(); i++) {
            final Object entry = value.elementAt(i);
            if (entry instanceof EnumType) {
                result.append(((EnumType) entry).name());
            } else {
                result.append(entry.toString());
            }
            result.append(separator);
        }
        replaceLastComa(result);
        addEntry(result.toString(), sb);
    }

    protected void addEntry(Integer value, StringBuffer sb) {
        if (value == null) {
            sb.append(",");
        } else {
            sb.append(value).append(",");
        }
    }

    protected void addEntry(String value, StringBuffer sb) {
        if (value == null) {
            sb.append(",");
        } else {
            sb.append(value).append(",");
        }
    }

    protected void addNewLine(StringBuffer sb) {
        replaceLastComa(sb);
        sb.append("\r\n");
    }

    protected void replaceLastComa(StringBuffer types) {
        if (types.length() > 0) {
            types.deleteCharAt(types.length() - 1);
        }
    }
}
