package com.cumulocity.rest.representation.user;

import com.cumulocity.model.DateTimeConverter;
import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;
import org.joda.time.DateTime;
import org.svenson.JSONProperty;
import org.svenson.JSONTypeHint;
import org.svenson.converter.JSONConverter;

import java.util.Date;
import java.util.List;

import static com.cumulocity.model.util.DateTimeUtils.newUTC;

/**
 * This class intentionally is not extending {@link UserRepresentation}.
 * Current user resource provides information required by client (application) for normal operation.
 * One example is effective list of roles. Without building this list on server side, client would have to explicitly go
 * through associated roles/groups, possibly with pagination operations. It is both difficult, and not possible if
 * ROLE_USER_MANAGEMENT_READ role is not present.
 */
public class CurrentUserRepresentation extends AbstractExtensibleRepresentation {

    private String id;

    private String userName;

    private String password;

    private Boolean shouldResetPassword;

    private DateTime lastPasswordChange;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private List<RoleRepresentation> effectiveRoles;

    @JSONProperty(ignoreIfNull = true)
    public String getId() {
        return id;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JSONProperty(ignoreIfNull = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JSONProperty(ignoreIfNull = true)
    @JSONTypeHint(value = RoleRepresentation.class)
    public List<RoleRepresentation> getEffectiveRoles() {
        return effectiveRoles;
    }

    public void setEffectiveRoles(List<RoleRepresentation> effectiveRoles) {
        this.effectiveRoles = effectiveRoles;
    }

    @JSONProperty(ignoreIfNull = true)
    public Boolean getShouldResetPassword() {
        return shouldResetPassword;
    }

    public void setShouldResetPassword(Boolean shouldResetPassword) {
        this.shouldResetPassword = shouldResetPassword;
    }

    @JSONProperty(value = "deprecated_LastPasswordChange", ignore = true)
    @Deprecated
    public Date getLastPasswordChange() {
        return lastPasswordChange == null ? null : lastPasswordChange.toDate();
    }

    @Deprecated
    public void setLastPasswordChange(Date lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange == null ? null : newUTC(lastPasswordChange);
    }

    @JSONProperty(value = "lastPasswordChange", ignoreIfNull = true)
    @JSONConverter(type = DateTimeConverter.class)
    public DateTime getLastPasswordChangeDateTime() {
        return lastPasswordChange;
    }

    public void setLastPasswordChangeDateTime(DateTime lastPasswordChange) {
        this.lastPasswordChange = lastPasswordChange;
    }
}
