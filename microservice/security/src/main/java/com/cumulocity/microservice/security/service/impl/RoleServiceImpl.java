package com.cumulocity.microservice.security.service.impl;

import com.cumulocity.microservice.security.service.RoleService;
import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.RoleRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.SDKException;
import com.cumulocity.sdk.client.user.UserApi;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.beans.ConstructorProperties;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static com.cumulocity.rest.representation.user.UserMediaType.CURRENT_USER;

@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final UserApi userApi;

    public RoleServiceImpl(UserApi userApi) {
        this.userApi = userApi;
    }

    @Override
    public List<String> getUserRoles() {
        final List<String> result = Lists.newArrayList();
        try {
            final CurrentUserRepresentation currrentUser = userApi.getCurrentUser();
    
            final List<RoleRepresentation> effectiveRoles = currrentUser.getEffectiveRoles();
            if (effectiveRoles != null && !effectiveRoles.isEmpty()) {
                for (final Object roleObject : effectiveRoles) {
                    if (roleObject instanceof Map) {
//                        todo by default svenson parses list elements to map, it doesn't happen if you use custom ResponseMapper
                        final Map<String, String> roleMap = (Map<String, String>) roleObject;
                        result.add(roleMap.get("name"));
                    } else if (roleObject instanceof RoleRepresentation) {
                        final RoleRepresentation role = (RoleRepresentation) roleObject;
                        result.add(role.getName());
                    } else {
                        throw new IllegalStateException("user/currentUser response body is not parsable");
                    }
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
