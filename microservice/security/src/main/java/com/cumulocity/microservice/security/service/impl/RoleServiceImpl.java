package com.cumulocity.microservice.security.service.impl;

import com.cumulocity.microservice.security.service.RoleService;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.RoleRepresentation;
import com.cumulocity.sdk.client.PlatformParameters;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.cumulocity.rest.representation.user.UserMediaType.CURRENT_USER;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class RoleServiceImpl implements RoleService {
    private final PlatformParameters platformParameters;
    private final RestConnector restConnector;

    @java.beans.ConstructorProperties({"platformParameters", "restConnector"})
    @Autowired
    public RoleServiceImpl(PlatformParameters platformParameters, RestConnector restConnector) {
        this.platformParameters = platformParameters;
        this.restConnector = restConnector;
    }

    @Override
    public List<String> getUserRoles() {
        final List<String> result = Lists.newArrayList();
        try {
            final URL url = new URL(new URL(platformParameters.getHost()), "user/currentUser");
            final CurrentUserRepresentation currrentUser = restConnector.get(url.toString(), CURRENT_USER, CurrentUserRepresentation.class);
    
            final List<RoleRepresentation> effectiveRoles = currrentUser.getEffectiveRoles();
            if (!isEmpty(effectiveRoles)) {
                for (final Object roleRepresentation : effectiveRoles) {
                    // todo: why rest connector parses role representation to map?
                    final Map<String, String> map = (Map<String, String>) roleRepresentation;
                    result.add(map.get("name"));
                }
            }
        } catch(SDKException e) {
            //!FIXME: This is a temporary workaround as long device users are not permitted to read their own user.
            //
            // If 403 Forbidden, user is authenticated but not permitted to read own user.
            // This is true for device users. So just return empty roles
            if (e.getHttpStatus() != HttpStatus.FORBIDDEN.value()) {
                throw e;
            }
        } catch (Exception ex) {
            throw Throwables.propagate(ex);
        }
        return result;
    }
}
