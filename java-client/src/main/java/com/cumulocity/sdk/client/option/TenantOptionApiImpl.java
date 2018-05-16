package com.cumulocity.sdk.client.option;

import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.CumulocityMediaType;
import com.cumulocity.rest.representation.tenant.*;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.buffering.Future;

import java.util.ArrayList;
import java.util.List;

import static com.cumulocity.rest.representation.tenant.OptionRepresentation.asOptionRepresetation;

public class TenantOptionApiImpl implements TenantOptionApi {

    private final RestConnector restConnector;

    private final int pageSize;

    private TenantApiRepresentation tenantApiRepresentation;

    public TenantOptionApiImpl(RestConnector restConnector, TenantApiRepresentation tenantApiRepresentation, int pageSize) {
        this.restConnector = restConnector;
        this.tenantApiRepresentation = tenantApiRepresentation;
        this.pageSize = pageSize;
    }


    @Override
    public OptionRepresentation getOption(OptionPK optionPK) throws SDKException {
        String url = getTenantApiRepresentation().getTenantOptionForCategoryAndKey()
                .replace("{category}", optionPK.getCategory())
                .replace("{key}", optionPK.getKey());
        return restConnector.get(url, OptionMediaType.OPTION, OptionRepresentation.class);
    }

    @Override
    public TenantOptionCollection getOptions() throws SDKException {
        String url = getSelfUri();
        return new TenantOptionCollectionImpl(restConnector, url, pageSize);
    }

    private String getSelfUri() throws SDKException {
        return getTenantApiRepresentation().getOptions().getSelf();
    }

    @Override
    public OptionRepresentation save(OptionRepresentation representation) throws SDKException {
        return restConnector.post(getSelfUri(), OptionMediaType.OPTION, representation);
    }

    @Override
    public Future saveAsync(OptionRepresentation representation) throws SDKException {
        return restConnector.postAsync(getSelfUri(), OptionMediaType.OPTION, representation);
    }

    @Override
    public void delete(OptionPK optionPK) throws SDKException {
        String url = getTenantApiRepresentation().getTenantOptionForCategoryAndKey()
                .replace("{category}", optionPK.getCategory())
                .replace("{key}", optionPK.getKey());
        restConnector.delete(url);
    }

    @Override
    public List<OptionRepresentation> getAllOptionsForCategory(String category) throws SDKException {
        String url = getTenantApiRepresentation().getTenantOptionsForCategory()
                .replace("{category}", category);
        OptionsRepresentation optionsRepresentation = restConnector.get(
                url,
                CumulocityMediaType.APPLICATION_JSON_TYPE,
                OptionsRepresentation.class);

        return transformOptions(category, optionsRepresentation);
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
        return tenantApiRepresentation;
    }

    private List<OptionRepresentation> transformOptions(String category, OptionsRepresentation optionsRepresentation) {
        List<OptionRepresentation> options = new ArrayList<>();
        for (String key : optionsRepresentation.propertyNames()) {
            options.add(
                    asOptionRepresetation(
                            category,
                            key,
                            optionsRepresentation.getProperty(key)));
        }
        return options;
    }

}
