package com.cumulocity.rest.representation.support;

import com.cumulocity.model.JSONBase;
import com.cumulocity.rest.representation.BaseResourceRepresentation;
import lombok.Getter;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.junit.jupiter.api.AfterEach;
import org.springframework.core.GenericTypeResolver;
import org.svenson.JSON;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class BDDJsonRepresentationTestBase<R extends BaseResourceRepresentation> {

    protected JSON svenson = JSONBase.getJSONGenerator();

    @Getter
    private R representation;
    //svenson serialized json, or json to deserialize
    private String svensonJson;

    @AfterEach
    void tearDown() {
        representation = null;
        svensonJson = null;
    }

    protected void givenJsonString(String givenJson) {
        this.svensonJson = givenJson;
    }

    protected void whenDeserializedUsing(TestJsonProvider provider) {
        representation = deserializer(provider).deserialize(svensonJson, resolveRepresentationClass());
    }

    protected Class<R> resolveRepresentationClass() {
        return (Class<R>) GenericTypeResolver.resolveTypeArguments(getClass(), BDDJsonRepresentationTestBase.class)[0];
    }

    protected TestJsonDeserializer deserializer(TestJsonProvider provider) {
        return new TestJsonDeserializer.SvensonTestJsonDeserializer();
    }

}
