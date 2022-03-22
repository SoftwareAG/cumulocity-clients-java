package com.cumulocity.lpwan.lns.instance.service;

import com.cumulocity.lpwan.lns.instance.model.LnsInstance;
import com.cumulocity.microservice.context.inject.TenantScope;
import com.cumulocity.model.option.OptionPK;
import com.cumulocity.rest.representation.tenant.OptionRepresentation;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.option.TenantOptionApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@TenantScope
public abstract class LnsInstanceService {

    private static final String LNS_INSTANCE_KEY = "credentials.lns.instances";
    private static final String PASSWORD = "password";

    @Autowired
    private TenantOptionApi tenantOptionApi;

    public void createLnsInstance(LnsInstance lnsInstance) throws Exception {

        List<LnsInstance> configuredLnsInstances = getLnsInstancesWithPassword();
        configuredLnsInstances.add(lnsInstance);
        saveLnsInstanceToTenantOption(configuredLnsInstances);
    }

    public List<LnsInstance> getLnsInstances() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode nodes = mapper.readTree(new ObjectMapper().writeValueAsString(getLnsInstancesWithPassword()));
        for(JsonNode node : nodes) {
            ((ObjectNode)node).remove(PASSWORD);
        }

        ObjectReader reader = mapper.readerFor(new TypeReference<List<LnsInstance>>() {});
        return reader.readValue(nodes);
    }

    public LnsInstance getAnLnsInstance(String lnsInstanceName) throws Exception {
        List<LnsInstance> lnsInstances = getLnsInstances();

        for(LnsInstance lnsInstance : lnsInstances){
            if(lnsInstanceName.equals(lnsInstance.getName())){
                return lnsInstance;
            }
        }

        throw new NotFoundException(String.format("The LNS instance with name '%s' is not found", lnsInstanceName));
    }

    public abstract void updateAnLnsInstance(String existingLnsInstanceName, LnsInstance lnsInstanceToUpdate) throws Exception;

    public void deleteAnLnsInstance(String lnsInstanceName) throws Exception {
        List<LnsInstance> lnsInstances = getLnsInstances();

        for(LnsInstance lnsInstance : lnsInstances){
            if(lnsInstanceName.equals(lnsInstance.getName())){
                lnsInstances.remove(lnsInstance);
            }
        }
        saveLnsInstanceToTenantOption(lnsInstances);
    }

    private List<LnsInstance> getLnsInstancesWithPassword() throws Exception {
        OptionPK optionPK = new OptionPK();
        optionPK.setCategory(getCategory());
        optionPK.setKey(LNS_INSTANCE_KEY);
        Optional<OptionRepresentation> fetchedOption = getOption(optionPK);
        if (!fetchedOption.isPresent()) {
            return new ArrayList<>();
        }
        String lnsInstancesToBeParsed = fetchedOption.get().getValue();

        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(lnsInstancesToBeParsed, new TypeReference<List<LnsInstance>>() {});
    }

    private Optional<OptionRepresentation> getOption(OptionPK optionPK) {
        try {
            return Optional.of(tenantOptionApi.getOption(optionPK));
        } catch (SDKException e) {
            if (e.getHttpStatus() == HttpStatus.NOT_FOUND.value()) {
                return Optional.empty();
            } else {
                throw e;
            }
        }
    }

    protected void saveLnsInstanceToTenantOption(List<? extends LnsInstance> configuredLnsInstances) {
        OptionRepresentation option = new OptionRepresentation();
        option.setCategory(getCategory());
        option.setKey(LNS_INSTANCE_KEY);
        try {
            option.setValue(new ObjectMapper().writeValueAsString(configuredLnsInstances));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        tenantOptionApi.save(option);
    }

    public abstract String getCategory();
}
