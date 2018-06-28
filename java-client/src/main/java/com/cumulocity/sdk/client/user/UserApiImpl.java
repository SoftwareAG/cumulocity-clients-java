package com.cumulocity.sdk.client.user;

import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.rest.representation.user.UserRepresentation;
import com.cumulocity.rest.representation.user.UsersApiRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.TemplateUrlParser;

import java.util.HashMap;
import java.util.Map;

public class UserApiImpl implements UserApi {

    private static final String REALM = "realm";
    private static final String USER_NAME = "userName";

    private final RestConnector restConnector;
    private final TemplateUrlParser templateUrlParser;
    private final UsersApiRepresentation usersApiRepresentation;

    public UserApiImpl(RestConnector restConnector, TemplateUrlParser templateUrlParser, UsersApiRepresentation usersApiRepresentation) {
        this.restConnector = restConnector;
        this.templateUrlParser = templateUrlParser;
        this.usersApiRepresentation = usersApiRepresentation;
    }

    @Override
    public CurrentUserRepresentation getCurrentUser() {
        String url = usersApiRepresentation.getCurrentUser();
        return restConnector.get(url, UserMediaType.CURRENT_USER, CurrentUserRepresentation.class);
    }

    @Override
    public CurrentUserRepresentation updateCurrentUser(CurrentUserRepresentation currentUserRepresentation) {
        String url = usersApiRepresentation.getCurrentUser();
        return restConnector.put(url, UserMediaType.CURRENT_USER, currentUserRepresentation);
    }

    @Override
    public UserRepresentation getCurrentUserWithCustomProperties() {
        String url = usersApiRepresentation.getCurrentUser();
        return restConnector.get(url, UserMediaType.USER, UserRepresentation.class);
    }

    @Override
    public UserRepresentation getUser(String tenant, String user) {
        Map<String, String> params = new HashMap<>();
        params.put(REALM, tenant);
        params.put(USER_NAME, user);
        String url = templateUrlParser.replacePlaceholdersWithParams(usersApiRepresentation.getUserByName(), params);
        System.out.println(url);
        return restConnector.get(url, UserMediaType.USER, UserRepresentation.class);
    }

    @Override
    public UserRepresentation create(String tenant, UserRepresentation userRepresentation) {
        Map<String, String> params = new HashMap<>();
        params.put(REALM, tenant);
        String url = templateUrlParser.replacePlaceholdersWithParams(usersApiRepresentation.getUsers(), params);
        return restConnector.post(url, UserMediaType.USER, userRepresentation);
    }

    @Override
    public UserRepresentation update(String tenant, UserRepresentation userRepresentation) {
        Map<String, String> params = new HashMap<>();
        params.put(REALM, tenant);
        String url = templateUrlParser.replacePlaceholdersWithParams(usersApiRepresentation.getUsers() + '/' + userRepresentation.getUserName(), params);
        userRepresentation.setUserName(null);
        return restConnector.put(url, UserMediaType.USER, userRepresentation);
    }

    @Override
    public void delete(String tenant, String userName) {
        Map<String, String> params = new HashMap<>();
        params.put(REALM, tenant);
        String url = templateUrlParser.replacePlaceholdersWithParams(usersApiRepresentation.getUsers() + '/' + userName, params);
        restConnector.delete(url);
    }
}
