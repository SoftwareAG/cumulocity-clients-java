package com.cumulocity.rest.representation.connector;

import java.util.List;

import com.cumulocity.model.DateTimeConverter;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;
import org.svenson.converter.JSONConverter;

import com.cumulocity.model.IDTypeConverter;
import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import com.cumulocity.rest.representation.annotation.Command;
import com.cumulocity.rest.representation.annotation.NotNull;
import com.cumulocity.rest.representation.annotation.Null;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ConnectorRepresentation extends AbstractExtensibleRepresentation {

    @Null(operation = { Command.CREATE })
    @Getter(onMethod_ = {
            @JSONProperty(ignoreIfNull = true),
            @JSONConverter(type = IDTypeConverter.class)
    })
    private GId id;

    @NotNull(operation = { Command.CREATE })
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String name;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String description;

    @Null(operation = { Command.CREATE })
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String status;

    @Null(operation = { Command.UPDATE, Command.CREATE })
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String suspendReason;

    @Null(operation = { Command.CREATE })
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String destinationStatus;

    @Null(operation = { Command.UPDATE, Command.CREATE })
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String tenantId;

    @NotNull(operation = { Command.CREATE })
    @Null(operation = { Command.UPDATE })
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String instanceUrl;

    @Null(operation = { Command.CREATE })
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String username;

    @Null(operation = { Command.CREATE })
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String password;

    @Null(operation = { Command.UPDATE, Command.CREATE })
    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    private String code;

    @Getter(onMethod_ = {
            @JSONProperty(value="lastSynchronizationTime", ignoreIfNull = true),
            @JSONConverter(type = DateTimeConverter.class)
    })
    private DateTime lastSync;

    @Getter(onMethod_ = {
            @JSONProperty(ignoreIfNull = true),
            @JSONTypeHint(ConnectorFilterRepresentation.class)
    })
    private List<ConnectorFilterRepresentation> filters;
}
