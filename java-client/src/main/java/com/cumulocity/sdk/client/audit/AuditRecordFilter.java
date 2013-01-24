/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.client.audit;

/**
 * A filter to be used in audit record queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code AuditRecordFilter filter = new AuditRecordFilter().byUser(user).byType(type);}
 */
public class AuditRecordFilter {

    private String user;

    private String type;

    private String application;

    /**
     * Specifies the {@code user} query parameter.
     *
     * @param user the user associated with the audit record(s)
     * @return the audit record filter with {@code user} set
     */
    public AuditRecordFilter byUser(String user) {
        this.user = user;
        return this;
    }

    /**
     * Specifies the {@code type} query parameter.
     *
     * @param type the type of the audit record(s)
     * @return the audit record filter with {@code type} set
     */
    public AuditRecordFilter byType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Specifies the {@code application} query parameter
     *
     * @param application the application associated with the audit record(s)
     * @return the audit record filter with {@code application} set
     */
    public AuditRecordFilter byApplication(String application) {
        this.application = application;
        return this;
    }

    /**
     * @return the {@code user} parameter of the query
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the {@code type} parameter of the query
     */
    public String getType() {
        return type;
    }

    /**
     * @return the {@code application} parameter of the query
     */
    public String getApplication() {
        return application;
    }

}
