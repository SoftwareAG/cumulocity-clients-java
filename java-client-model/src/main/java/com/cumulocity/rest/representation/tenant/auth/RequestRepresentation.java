package com.cumulocity.rest.representation.tenant.auth;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import lombok.*;
import org.svenson.JSONProperty;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class RequestRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String url;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String method;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Map<String, String> requestParams;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Map<String, String> headers;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String body;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @NotNull(operation = {Command.CREATE, Command.UPDATE})
    private String operation;

    @Builder(builderMethodName = "requestRepresentation")
    public RequestRepresentation(String url, String method, @Singular(ignoreNullCollections = true) Map<String, String> requestParams, @Singular(ignoreNullCollections = true) Map<String, String> headers, String body, String operation) {
        this.url = url;
        this.method = method;
        this.requestParams = requestParams == null ? null : new HashMap<>(requestParams);
        this.headers = headers == null ? null : new HashMap<>(headers);
        this.body = body;
        this.operation = operation;
    }
}
