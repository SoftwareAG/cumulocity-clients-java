package com.cumulocity.rest.representation.devicebootstrap;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.rest.representation.CustomPropertiesMapRepresentation;
import com.cumulocity.rest.representation.annotation.NotNull;
import com.cumulocity.rest.representation.annotation.Null;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.svenson.JSONProperty;
import org.svenson.converter.JSONConverter;

import static com.cumulocity.rest.representation.annotation.Command.CREATE;
import static com.cumulocity.rest.representation.annotation.Command.UPDATE;

@Getter
@Setter
public class NewDeviceRequestRepresentation extends CustomPropertiesMapRepresentation {

    @NotNull(operation = {CREATE})
    @Null(operation = {UPDATE})
    private String id;

    @Null(operation = {CREATE})
    @NotNull(operation = {UPDATE})
    private String status;

    @Null(operation = {UPDATE})
    private String tenantId;
    
    @Null(operation = {UPDATE})
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String groupId;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String type;

    @Null(operation = {CREATE, UPDATE})
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String owner;

    @Null(operation = {CREATE, UPDATE})
    @Getter(onMethod_ = {@JSONProperty(ignoreIfNull = true), @JSONConverter(type = DateTimeConverter.class)})
    private DateTime creationTime;

    @Null(operation = {CREATE})
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String securityToken;

}
