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
package com.cumulocity.sdk.client.measurement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;

import java.util.Date;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;

public class MeasurementFilterTest {

    @Test
    public void shouldHoldTypeSourceDateAndFragmentType() throws Exception {
        ManagedObjectRepresentation mo = new ManagedObjectRepresentation();

        Date fromDate = new Date(0L);
        Date toDate = new Date(1L);
        MeasurementFilter filter = new MeasurementFilter().byType("type").bySource(mo).byFragmentType(NonRelevantFragmentType.class).byDate(
                fromDate, toDate);

        assertThat(filter.getType(), is("type"));
        assertThat(filter.getSource(), sameInstance(mo));
        assertThat(filter.getFragmentType(), Matchers.<Object>equalTo(NonRelevantFragmentType.class));
        assertThat(filter.getFromDate(), is(fromDate));
        assertThat(filter.getToDate(), is(toDate));
    }

    private static class NonRelevantFragmentType {
    }
}
