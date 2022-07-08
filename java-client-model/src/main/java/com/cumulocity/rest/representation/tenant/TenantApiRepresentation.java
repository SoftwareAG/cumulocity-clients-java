package com.cumulocity.rest.representation.tenant;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import lombok.Data;
import org.svenson.JSONProperty;

import java.util.ArrayList;
import java.util.List;

@Data
public class TenantApiRepresentation extends AbstractExtensibleRepresentation {

    private TenantCollectionRepresentation tenants;
    private OptionCollectionRepresentation options;

    private String tenantForId;

    private String tenantApplications;
    private String tenantApplicationForId;

    private String tenantOptionForCategoryAndKey;
    private String tenantOptionsForCategory;

    private String tenantSystemOptions;
    private String tenantSystemOptionsForCategoryAndKey;


    @JSONProperty(ignore = true)
    public List<String> getURITemplates() {
        List<String> uriTemplates = new ArrayList<String>();
        uriTemplates.add(this.getTenantForId());
        uriTemplates.add(this.getTenantApplications());
        uriTemplates.add(this.getTenantApplicationForId());
        uriTemplates.add(this.getTenantOptionsForCategory());
        uriTemplates.add(this.getTenantOptionForCategoryAndKey());
        uriTemplates.add(this.getTenantOptionsForCategory());
        return uriTemplates;
    }

}
