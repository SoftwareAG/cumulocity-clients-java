package com.cumulocity.me.agent.smartrest.model.template;

import com.cumulocity.me.agent.smartrest.model.TemplateCollection;

public class TemplateSupport {
    public static final PlaceholderType STRING = PlaceholderType.STRING;
    public static final PlaceholderType UNSIGNED = PlaceholderType.UNSIGNED;
    public static final PlaceholderType INTEGER = PlaceholderType.INTEGER;
    public static final PlaceholderType NUMBER = PlaceholderType.NUMBER;
    public static final PlaceholderType DATE = PlaceholderType.DATE;
    public static final PlaceholderType NOW = PlaceholderType.NOW;

    public static final Method GET = Method.GET;
    public static final Method POST = Method.POST;
    public static final Method PUT = Method.PUT;
    public static final Method DELETE = Method.DELETE;

    public static final String PLACEHOLDER = TemplateBuilder.PLACEHOLDER;

    public static TemplateCollection.Builder templateCollection() {
        return TemplateCollection.templateCollection();
    }

    public static Json.JsonObjectBuilder json() {
        return Json.json();
    }

    public static TemplateBuilder requestTemplate() {
        return TemplateBuilder.requestTemplate();
    }

    public static TemplateBuilder responseTemplate() {
        return TemplateBuilder.responseTemplate();
    }

    public static Path.PathBuilder path() {
        return Path.path();
    }

    public static Path.PathBuilder path(String string) {
        return Path.path(string);
    }
}
