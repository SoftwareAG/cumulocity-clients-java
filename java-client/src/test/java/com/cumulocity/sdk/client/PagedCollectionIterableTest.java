package com.cumulocity.sdk.client;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.cumulocity.rest.representation.TestCollectionRepresentation;

@RunWith(MockitoJUnitRunner.class)
public class PagedCollectionIterableTest {

    TestCollectionRepresentation<String> firstPage = new TestCollectionRepresentation<String>()
            .withContents(asList("one", "two", "three", "four")).withNext("more");

    TestCollectionRepresentation<String> secondPage = new TestCollectionRepresentation<String>()
            .withContents(asList("five", "six", "seven", "eight")).withNext("evenMore");

    TestCollectionRepresentation<String> thirdPage = new TestCollectionRepresentation<String>()
            .withContents(asList("nine", "ten", "eleven", "twelve"));

    TestCollectionRepresentation<String> fourthPage = new TestCollectionRepresentation<String>()
            .withContents(Collections.<String>emptyList());

    @Mock
    PagedCollectionResource<String, TestCollectionRepresentation<String>> stringsResource;

    PagedCollectionIterable<String, TestCollectionRepresentation<String>> pagedStrings;

    @Before
    public void setUp() throws Exception {
        pagedStrings = new PagedCollectionIterable<String, TestCollectionRepresentation<String>>(
                stringsResource, firstPage);
    }

    @Test
    public void shouldIterateOverAllPages() throws Exception {
        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, null);

        List<String> result = new LinkedList<String>();
        for (String s : pagedStrings) {
            result.add(s);
        }

        assertThat(result).hasSize(12);
        assertThat(result).containsSequence(
                "one", "two", "three", "four",
                "five", "six", "seven", "eight",
                "nine", "ten", "eleven", "twelve");
    }

    @Test
    public void shouldIterateOverAllPagesEvenEmpty() throws Exception {
        thirdPage.withNext("empty");

        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, fourthPage, null);

        List<String> result = new LinkedList<String>();
        for (String s : pagedStrings) {
            result.add(s);
        }

        assertThat(result).hasSize(12);
        assertThat(result).containsSequence(
                "one", "two", "three", "four",
                "five", "six", "seven", "eight",
                "nine", "ten", "eleven", "twelve");
    }
}
