package com.cumulocity.sdk.client;

import com.cumulocity.rest.representation.TestCollectionRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Test
    public void shouldIterateOverAllPagesUsingIteratorDirectly() {
        thirdPage.withNext("empty");
        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, fourthPage, null);
        pagedStrings = new PagedCollectionIterable<>(stringsResource, firstPage);
        Iterator<String> iterator = pagedStrings.iterator();
        List<String> result = new LinkedList<>();

        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        assertThat(result).containsExactly(
                "one", "two", "three", "four",
                "five", "six", "seven", "eight",
                "nine", "ten", "eleven", "twelve");
    }

    @Test
    public void shouldIterateOverAllPagesUsingIteratorNextWithoutHasNext() {
        thirdPage.withNext("empty");
        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, fourthPage);
        pagedStrings = new PagedCollectionIterable<>(stringsResource, firstPage);
        Iterator<String> iterator = pagedStrings.iterator();
        List<String> result = new LinkedList<>();

        for (int i = 0; i < 12; i++) {
            result.add(iterator.next());
        }

        assertThat(result).containsExactly(
                "one", "two", "three", "four",
                "five", "six", "seven", "eight",
                "nine", "ten", "eleven", "twelve");
    }

    @Test
    public void shouldThrowWhenNextExceedsTotalSize() {
        thirdPage.withNext("empty");
        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, fourthPage);
        pagedStrings = new PagedCollectionIterable<>(stringsResource, firstPage);
        Iterator<String> iterator = pagedStrings.iterator();
        for (int i = 0; i < 12; i++) {
            iterator.next();
        }

        assertThatThrownBy(() -> iterator.next())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldIterateOverAllElementsWhenMultipleCallsToHasNext() {
        thirdPage.withNext("empty");
        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
                .thenReturn(secondPage, thirdPage, fourthPage);
        pagedStrings = new PagedCollectionIterable<>(stringsResource, firstPage);
        Iterator<String> iterator = pagedStrings.iterator();
        List<String> result = new LinkedList<>();

        while (iterator.hasNext()) {
            iterator.hasNext();
            iterator.hasNext();
            result.add(iterator.next());
        }

        assertThat(result).containsExactly(
                "one", "two", "three", "four",
                "five", "six", "seven", "eight",
                "nine", "ten", "eleven", "twelve");
    }

}
