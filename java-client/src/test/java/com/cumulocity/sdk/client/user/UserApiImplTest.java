package com.cumulocity.sdk.client.user;

import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.UserMediaType;
import com.cumulocity.rest.representation.user.UserRepresentation;
import com.cumulocity.rest.representation.user.UsersApiRepresentation;
import com.cumulocity.sdk.client.RestConnector;
import com.cumulocity.sdk.client.TemplateUrlParser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserApiImplTest {
    private static final String USER_BY_NAME_URL = "user_by_name";
    private static final String CURRENT_USER_URL = "current_user";
    private static final String USERS_URL = "users";

    @Mock
    private RestConnector restConnector;

    @Mock
    private TemplateUrlParser templateUrlParser;

    private UsersApiRepresentation usersApiRepresentation;

    private UserApi userApi;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        usersApiRepresentation = new UsersApiRepresentation();
        usersApiRepresentation.setUserByName(USER_BY_NAME_URL);
        usersApiRepresentation.setCurrentUser(CURRENT_USER_URL);
        usersApiRepresentation.setUsers(USERS_URL);

        userApi = new UserApiImpl(restConnector, templateUrlParser, usersApiRepresentation);
    }

    @Test
    public void shouldGetCurrentUser() throws Exception {
        //given
        CurrentUserRepresentation retreived = new CurrentUserRepresentation();
        when(restConnector.get(eq(CURRENT_USER_URL), eq(UserMediaType.CURRENT_USER), eq(CurrentUserRepresentation.class)))
                .thenReturn(retreived);

        //when
        CurrentUserRepresentation user = userApi.getCurrentUser();

        //then
        assertThat(user).isSameAs(retreived);
    }

    @Test
    public void shouldUpdateCurrentUser() throws Exception {
        //given
        CurrentUserRepresentation toUpdate = new CurrentUserRepresentation();
        CurrentUserRepresentation retreived = new CurrentUserRepresentation();
        when(restConnector.put(eq(CURRENT_USER_URL), eq(UserMediaType.CURRENT_USER), same(toUpdate)))
                .thenReturn(retreived);

        //when
        CurrentUserRepresentation updated = userApi.updateCurrentUser(toUpdate);

        //then
        assertThat(updated).isSameAs(retreived);
    }

    @Test
    public void shouldGetCurrentUserWithCustomProperties() throws Exception {
        //given
        UserRepresentation retreived = new UserRepresentation();
        when(restConnector.get(eq(CURRENT_USER_URL), eq(UserMediaType.USER), eq(UserRepresentation.class)))
                .thenReturn(retreived);

        //when
        UserRepresentation user = userApi.getCurrentUserWithCustomProperties();

        //then
        assertThat(user).isSameAs(retreived);
    }

    @Test
    public void shouldGetUser() throws Exception {
        //given
        String tenant = "myTenant";
        String userName = "myUser";
        String parsedUrl = "parsed_url";
        Map<String, String> params = asParams(tenant, userName);

        when(templateUrlParser.replacePlaceholdersWithParams(eq(USER_BY_NAME_URL), eq(params)))
                .thenReturn(parsedUrl);
        UserRepresentation retreived = new UserRepresentation();
        when(restConnector.get(eq(parsedUrl), eq(UserMediaType.USER), eq(UserRepresentation.class)))
                .thenReturn(retreived);

        //when
        UserRepresentation user = userApi.getUser(tenant, userName);

        //then
        assertThat(user).isSameAs(retreived);
    }

    @Test
    public void shouldCreate() throws Exception {
        //given
        String tenant = "myTenant";
        String parsedUrl = "parsed_url";
        Map<String, String> params = asParams(tenant, null);

        when(templateUrlParser.replacePlaceholdersWithParams(eq(USERS_URL), eq(params)))
                .thenReturn(parsedUrl);
        UserRepresentation toCreate = new UserRepresentation();
        UserRepresentation retreived = new UserRepresentation();
        when(restConnector.post(eq(parsedUrl), eq(UserMediaType.USER), same(toCreate)))
                .thenReturn(retreived);

        //when
        UserRepresentation user = userApi.create(tenant, toCreate);

        //then
        assertThat(user).isSameAs(retreived);
    }

    @Test
    public void shouldUpdate() throws Exception {
        //given
        String tenant = "myTenant";
        String userName = "myUser";
        String parsedUrl = "parsed_url";
        Map<String, String> params = asParams(tenant, null);

        when(templateUrlParser.replacePlaceholdersWithParams(eq(USERS_URL + '/' + userName), eq(params)))
                .thenReturn(parsedUrl);
        UserRepresentation toUpdate = new UserRepresentation();
        toUpdate.setUserName(userName);
        UserRepresentation retreived = new UserRepresentation();
        when(restConnector.put(eq(parsedUrl), eq(UserMediaType.USER), same(toUpdate)))
                .thenReturn(retreived);

        //when
        UserRepresentation user = userApi.update(tenant, toUpdate);

        //then
        assertThat(user).isSameAs(retreived);
    }

    @Test
    public void shouldDelete() throws Exception {
        //given
        String tenant = "myTenant";
        String userName = "myUser";
        String parsedUrl = "parsed_url";
        Map<String, String> params = asParams(tenant, null);

        when(templateUrlParser.replacePlaceholdersWithParams(eq(USERS_URL + '/' + userName), eq(params)))
                .thenReturn(parsedUrl);

        //when
        userApi.delete(tenant, userName);

        //then
        verify(restConnector).delete(parsedUrl);
    }

    private Map<String, String> asParams(String tenant, String userName) {
        Map<String, String> params = new HashMap<>();
        if (tenant != null) {
            params.put("realm", tenant);
        }
        if (userName != null) {
            params.put("userName", userName);
        }
        return params;
    }
}