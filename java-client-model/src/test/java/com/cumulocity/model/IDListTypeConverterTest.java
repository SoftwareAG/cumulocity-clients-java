package com.cumulocity.model;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.LinkedList;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import com.cumulocity.model.idtype.GId;

public class IDListTypeConverterTest {

    @Test
    public void shouldKeepTheOrderOfIDs() throws Exception {
        Set<GId> ids = (Set<GId>) new IDListTypeConverter().fromJSON(asList("1", "2", "3", "4", "5"));

        assertThat(new LinkedList<GId>(ids), is(
                asList(gid("1"), gid("2"), gid("3"), gid("4"), gid("5"))));
    }

    private static GId gid(String id) {
        return new GId(id);
    }

}
