package com.cumulocity.rest.representation.user;

import com.cumulocity.rest.representation.AbstractExtensibleRepresentation;

public class UsersApiRepresentation extends AbstractExtensibleRepresentation {

    private String users;

    private String userByName;

    private String currentUser;

    private String groups;

    private String groupByName;

    private String roles;

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups (String groups) {
        this.groups = groups;
    }

    /**
     * Contains a placeholder name and point to a resource of Type User.
     * @return userByName api url
     */
    public String getUserByName() {
        return userByName;
    }

    public void setUserByName(String userByName) {
        this.userByName = userByName;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public void setGroupByName(String groupByName) {
        this.groupByName = groupByName;
    }

    public String getGroupByName() {
        return groupByName;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }
}
