package com.cumulocity.rest.representation.application.microservice;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfigurationOptionRepresentation extends AbstractExtensibleRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
	private String key;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
	private String defaultValue;

	@Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
	private boolean editable;

	@Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
	private ConfigurationOptionSchemaRepresentation valueSchema;
	
}
