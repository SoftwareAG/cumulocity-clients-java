package com.cumulocity.rest.representation.user;

import com.cumulocity.rest.representation.CustomPropertiesMapRepresentation;
import com.cumulocity.rest.representation.application.ApplicationReferenceCollectionRepresentation;
import lombok.*;
import org.svenson.JSONProperty;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name", callSuper = false)
@ToString
public class CurrentTenantRepresentation extends CustomPropertiesMapRepresentation {

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Setter
    private String name;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Setter
    private String parent;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Setter
    private String domainName;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Setter
    private Boolean allowCreateTenants;

    @Getter(onMethod_ = @JSONProperty(ignoreIfNull = true))
    @Setter
    private ApplicationReferenceCollectionRepresentation applications;

}
