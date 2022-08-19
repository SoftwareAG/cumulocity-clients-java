package com.cumulocity.rest.representation.application.microservice;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProbeRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private ExecActionRepresentation exec;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private TCPSocketActionRepresentation tcpSocket;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private HTTPGetActionRepresentation httpGet;

    @Builder.Default
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Integer initialDelaySeconds = 0;

    @Builder.Default
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Integer periodSeconds = 10;

    @Builder.Default
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Integer successThreshold = 1;

    @Builder.Default
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Integer timeoutSeconds = 1;

    @Builder.Default
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private Integer failureThreshold = 3;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TCPSocketActionRepresentation extends AbstractExtensibleRepresentation {

        @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
        private Integer port = 80;

        @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
        private String host;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HTTPGetActionRepresentation extends AbstractExtensibleRepresentation {

        @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
        private String host;

        @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
        private String path;

        @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
        private Integer port = 80;

        @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
        private String scheme;

        @Singular
        @Getter(onMethod_ = {
                @JSONProperty(ignoreIfNull = true)
                , @JSONTypeHint(HttpHeaderRepresentation.class)
        })
        private List<HttpHeaderRepresentation> headers;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class HttpHeaderRepresentation extends AbstractExtensibleRepresentation {

        @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
        private String name;

        @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
        private String value;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExecActionRepresentation extends AbstractExtensibleRepresentation {

        @Singular("command")
        @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
        private List<String> command;

    }

}
