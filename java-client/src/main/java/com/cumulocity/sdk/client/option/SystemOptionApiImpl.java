package com.cumulocity.sdk.client.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.*;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;

public class SystemOptionApiImpl implements SystemOptionApi {

    private final RestConnector restConnector;

    private TenantApiRepresentation tenantApiRepresentation;

    public SystemOptionApiImpl(RestConnector restConnector, TenantApiRepresentation tenantApiRepresentation) {
        this.restConnector = restConnector;
        this.tenantApiRepresentation = tenantApiRepresentation;
    }

    @Override
    public OptionRepresentation getOption(OptionPK optionPK) throws SDKException {
        String url = getTenantApiRepresentation().getTenantSystemOptionsForCategoryAndKey()
                .replace("{category}", optionPK.getCategory())
                .replace("{key}", optionPK.getKey());
        return restConnector.get(url, OptionMediaType.OPTION, OptionRepresentation.class);
    }

    private TenantApiRepresentation getTenantApiRepresentation() {
        if (tenantApiRepresentation == null) {
            tenantApiRepresentation = buildTenantApiRepresentation();
        }
        return tenantApiRepresentation;
    }

    private TenantApiRepresentation buildTenantApiRepresentation() {
        final OptionCollectionRepresentation optionCollectionRepresentation = new OptionCollectionRepresentation();
        optionCollectionRepresentation.setSelf("/tenant/options");

        final TenantCollectionRepresentation tenants = new TenantCollectionRepresentation();
        tenants.setSelf("/tenant/tenants");

        final TenantApiRepresentation tenantApiRepresentation = new TenantApiRepresentation();
        tenantApiRepresentation.setOptions(optionCollectionRepresentation);
        tenantApiRepresentation.setTenants(tenants);
        tenantApiRepresentation.setTenantApplicationForId("/tenant/tenants/{tenantId}/applications/{applicationId}");
        tenantApiRepresentation.setTenantApplications("/tenant/tenants/{tenantId}/applications");
        tenantApiRepresentation.setTenantForId("/tenant/tenants/{tenantId}");
        tenantApiRepresentation.setTenantOptionForCategoryAndKey("/tenant/options/{category}/{key}");
        tenantApiRepresentation.setTenantOptionsForCategory("/tenant/options/{category}");
        tenantApiRepresentation.setTenantSystemOptions("/tenant/system/options");
        tenantApiRepresentation.setTenantSystemOptionsForCategoryAndKey("/tenant/system/options/{category}/{key}");
        return tenantApiRepresentation;
    }
}
