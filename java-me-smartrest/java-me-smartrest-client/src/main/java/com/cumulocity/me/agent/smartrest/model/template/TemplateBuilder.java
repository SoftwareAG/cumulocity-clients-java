package com.cumulocity.me.agent.smartrest.model.template;

import com.cumulocity.me.util.EnumType;

import java.util.Vector;

public class TemplateBuilder {

    public static final class PlaceholderType extends EnumType {

        public static final PlaceholderType STRING = new PlaceholderType("STRING", 0);
        public static final PlaceholderType UNSIGNED = new PlaceholderType("UNSIGNED", 1);
        public static final PlaceholderType INTEGER = new PlaceholderType("INTEGER", 2);
        public static final PlaceholderType NUMBER = new PlaceholderType("NUMBER", 3);
        public static final PlaceholderType DATE = new PlaceholderType("DATE", 4);
        public static final PlaceholderType NOW = new PlaceholderType("NOW", 5);

        private PlaceholderType(String name, int ordinal) {
            super(name, ordinal);
        }
    }

    public static final class Method extends EnumType{
        public static final Method GET = new Method("GET", 0);
        public static final Method POST = new Method("POST", 1);
        public static final Method PUT = new Method("PUT", 2);
        public static final Method DELETE = new Method("DELETE", 3);

        private Method(String name, int ordinal) {
            super(name, ordinal);
        }
    }

    public static final String PLACEHOLDER = "&&";

    protected final TemplateType templateType;

    protected Integer messageId;

    protected Method method;

    protected Path.PathBuilder path;

    protected String accept;

    protected String content;

    protected String placeholder = PLACEHOLDER;

    protected Vector placeholderTypes = new Vector();

    protected Json.JsonObjectBuilder json;

    protected Vector jsonPaths = new Vector();

    private TemplateBuilder(TemplateType templateType) {
        this.templateType = templateType;
    }

    public static TemplateBuilder requestTemplate() {
        return new TemplateBuilder(TemplateType.REQUEST_TEMPLATE);
    }

    public static TemplateBuilder responseTemplate() {
        return new TemplateBuilder(TemplateType.RESPONSE_TEMPLATE);
    }

    public String build() {
        return this.templateType.build(this);
    }

    public TemplateBuilder messageId(Integer messageId) {
        this.messageId = messageId;
        return this;
    }

    public TemplateBuilder method(Method method) {
        this.method = method;
        return this;
    }

    public TemplateBuilder path(Path.PathBuilder path) {
        this.path = path;
        return this;
    }

    public TemplateBuilder accept(String accept) {
        this.accept = accept;
        return this;
    }

    public TemplateBuilder content(String content) {
        this.content = content;
        return this;
    }

    public TemplateBuilder placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public TemplateBuilder json(Json.JsonObjectBuilder json) {
        this.json = json;
        return this;
    }

    public TemplateBuilder jsonPath(String path) {
        this.jsonPaths.addElement(path);
        return this;
    }

    public TemplateBuilder placeholderType(PlaceholderType type) {
        this.placeholderTypes.addElement(type);
        return this;
    }

    public TemplateBuilder placeholderType(PlaceholderType type1, PlaceholderType type2) {
        this.placeholderTypes.addElement(type1);
        this.placeholderTypes.addElement(type2);
        return this;
    }

    public TemplateBuilder placeholderType(PlaceholderType type1, PlaceholderType type2, PlaceholderType type3) {
        this.placeholderTypes.addElement(type1);
        this.placeholderTypes.addElement(type2);
        this.placeholderTypes.addElement(type3);
        return this;
    }

    public TemplateBuilder placeholderType(PlaceholderType type1, PlaceholderType type2, PlaceholderType type3, PlaceholderType type4) {
        this.placeholderTypes.addElement(type1);
        this.placeholderTypes.addElement(type2);
        this.placeholderTypes.addElement(type3);
        this.placeholderTypes.addElement(type4);
        return this;
    }
}
