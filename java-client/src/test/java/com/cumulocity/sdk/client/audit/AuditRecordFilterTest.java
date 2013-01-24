package com.cumulocity.sdk.client.audit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class AuditRecordFilterTest {

    @Test
    public void shouldHoldUserAndTypeAndApplication() {
        AuditRecordFilter filter = new AuditRecordFilter().byUser("user").byType("type").byApplication("application");

        assertThat(filter.getUser(), is("user"));
        assertThat(filter.getType(), is("type"));
        assertThat(filter.getApplication(), is("application"));
    }

}
