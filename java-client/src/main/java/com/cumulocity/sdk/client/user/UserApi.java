package com.cumulocity.sdk.client.user;

import com.cumulocity.rest.representation.user.CurrentUserRepresentation;
import com.cumulocity.rest.representation.user.UserRepresentation;

public interface UserApi {
    CurrentUserRepresentation getCurrentUser();

    CurrentUserRepresentation updateCurrentUser(CurrentUserRepresentation currentUserRepresentation);

    UserRepresentation getCurrentUserWithCustomProperties();

    UserRepresentation getUser(String tenant, String user);

    UserRepresentation create(String tenant, UserRepresentation userRepresentation);

    UserRepresentation update(String tenant, UserRepresentation userRepresentation);

    void delete(String tenant, String userName);
}
