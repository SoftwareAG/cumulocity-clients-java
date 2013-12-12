package com.cumulocity.sdk.client;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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

    List<String> strings = asList("one", "two", "three", "four");

    List<String> moreStrings = asList("five", "six", "seven", "eight");

    List<String> evenMoreStrings = asList("nine", "ten", "eleven", "twelve");

    @Mock
    PagedCollectionResource<String, TestCollectionRepresentation<String>> stringsResource;

    PagedCollectionIterable<String, TestCollectionRepresentation<String>> pagedStrings;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldIterateOverAllPages() throws Exception {
        TestCollectionRepresentation<String> firstPage = new TestCollectionRepresentation<String>()
                .withContents(strings).withNext("more");
        TestCollectionRepresentation<String> secondPage = new TestCollectionRepresentation<String>()
                .withContents(moreStrings).withNext("evenMore");
        TestCollectionRepresentation<String> thirdPage = new TestCollectionRepresentation<String>()
                .withContents(evenMoreStrings);

        pagedStrings = new PagedCollectionIterable<String, TestCollectionRepresentation<String>>(
                stringsResource, firstPage);

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
}
