/*
 * Copyright (C) 2013 Cumulocity GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of 
 * this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.cumulocity.sdk.client.audit;

import com.cumulocity.sdk.client.Filter;
import com.cumulocity.sdk.client.Name;

/**
 * A filter to be used in audit record queries.
 * The setter (by*) methods return the filter itself to provide chaining:
 * {@code AuditRecordFilter filter = new AuditRecordFilter().byUser(user).byType(type);}
 */
public class AuditRecordFilter extends Filter {

    @Name("user")
    private String user;

    @Name("type")
    private String type;

    @Name("application")
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
