package com.cumulocity.sdk.client;

import com.cumulocity.rest.representation.TestCollectionRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PagedCollectionIterableTest {

    TestCollectionRepresentation<String> firstPage = new TestCollectionRepresentation<String>()
            .withContents(asList("one", "two", "three", "four")).withNext("more");

    TestCollectionRepresentation<String> secondPage = new TestCollectionRepresentation<String>()
            .withContents(asList("five", "six", "seven", "eight")).withNext("evenMore");

    TestCollectionRepresentation<String> thirdPage = new TestCollectionRepresentation<String>()
            .withContents(asList("nine", "ten", "eleven", "twelve"));

    TestCollectionRepresentation<String> fourthPage = new TestCollectionRepresentation<String>()
            .withContents(emptyList());

    @Mock
    PagedCollectionResource<String, TestCollectionRepresentation<String>> stringsResource;

    Iterable<String> pagedStrings;

    @Test
    public void shouldIterateOverAllPages() {
        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, null);

        pagedStrings = new PagedCollectionIterable<>(stringsResource, firstPage);

        assertThat(pagedStrings).containsExactly(
                "one", "two", "three", "four",
                "five", "six", "seven", "eight",
                "nine", "ten", "eleven", "twelve");
    }

    @Test
    public void shouldIterateOverAllPagesEvenEmpty() {
        thirdPage.withNext("empty");
        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, fourthPage, null);

        pagedStrings = new PagedCollectionIterable<>(stringsResource, firstPage);

        assertThat(pagedStrings).containsExactly(
                "one", "two", "three", "four",
                "five", "six", "seven", "eight",
                "nine", "ten", "eleven", "twelve");
    }

    @Test
    public void shouldIterateOverPagesWithLimitInMiddleOfSecondPage() {
        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, null);

        pagedStrings = new PagedCollectionIterable<>(stringsResource, firstPage, 6);

        assertThat(pagedStrings).containsExactly(
                "one", "two", "three", "four",
                "five", "six");
    }

    @Test
    public void shouldIterateOverPagesWithLimitAtTheEndOfSecondPage() {
        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, null);

        pagedStrings = new PagedCollectionIterable<>(stringsResource, firstPage, 8);

        assertThat(pagedStrings).containsExactly(
                "one", "two", "three", "four",
                "five", "six", "seven", "eight");
    }
}
