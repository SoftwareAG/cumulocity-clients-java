package com.cumulocity.lpwan.lns.connectivity.Service;

import com.cumulocity.lpwan.lns.connectivity.util.JsonSchema;
import com.cumulocity.lpwan.lns.connectivity.util.Session;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionAddedEvent;
import com.cumulocity.microservice.subscription.model.MicroserviceSubscriptionRemovedEvent;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.inventory.InventoryApi;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public abstract class BaseLnsCredentialsService {

    @Autowired
    InventoryApi inventoryApi;

    final String LNS_AGENT = "LNS_Agent";
    private ManagedObjectRepresentation lnsAgentMo;

    abstract public JsonSchema getMetaData();
    abstract public boolean saveCredentials(Map<String,String> connectivitySettingData);
    abstract public Map<String,String> getCredentials();
    abstract public String getUrl(Map<String,String> connectivitySettingData);
    abstract public String getRequest(Map<String,String> connectivitySettingData);
    abstract public HttpHeaders getHeader();
    abstract public String getName();
    public ResponseEntity<JSONObject> isValidSession(Map<String,String> connectivitySettingData){
        JSONObject response =  new JSONObject();
        try {
            RestTemplate restTemplate = new RestTemplate();
            Session session = restTemplate.postForObject(getUrl(connectivitySettingData),
                    new HttpEntity<String>(getRequest(connectivitySettingData), getHeader()), Session.class);
            log.info("Received session: {}", session.getSession());
            if (session.getSession() != null && saveCredentials(connectivitySettingData)) {
                response.put("Message","Credentials Saved Successfully");
//                return response;
                return ResponseEntity.ok(response);
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }
//        response.put("Error",HttpStatus.UNAUTHORIZED);
        response.put("Message","The authorization failed.");
//        return response;
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//        return new ResponseEntity<JSONObject>(response, HttpStatus.UNAUTHORIZED);
    }

    @EventListener
    private void registerLnsAgent(MicroserviceSubscriptionAddedEvent event) {
        ManagedObjectRepresentation mor = new ManagedObjectRepresentation();
        mor.setType(LNS_AGENT);
        mor.setName(getName());
        lnsAgentMo = inventoryApi.create(mor);
    }

    @EventListener
    private void deregisterLnsAgent(MicroserviceSubscriptionRemovedEvent event){
        inventoryApi.delete(lnsAgentMo.getId());
    }
}
