package com.cumulocity.sdk.client;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PagedCollectionIterableTest {

//    List<String> strings = asList("one", "two", "three", "four");
//
//    List<String> moreStrings = asList("five", "six", "seven", "eight");
//
//    List<String> evenMoreStrings = asList("nine", "ten", "eleven", "twelve");
//
//    @Mock
//    PagedCollectionResource<String, TestCollectionRepresentation<String>> stringsResource;
//
//    PagedCollectionIterable<String, TestCollectionRepresentation<String>> pagedStrings;
//
//    @Before
//    public void setUp() throws Exception {
//
//    }
//
//    @Test
//    public void shouldIterateOverAllPages() throws Exception {
//        TestCollectionRepresentation<String> firstPage = new TestCollectionRepresentation<String>()
//                .withContents(strings).withNext("more");
//        TestCollectionRepresentation<String> secondPage = new TestCollectionRepresentation<String>()
//                .withContents(moreStrings).withNext("evenMore");
//        TestCollectionRepresentation<String> thirdPage = new TestCollectionRepresentation<String>()
//                .withContents(evenMoreStrings);
//
//        pagedStrings = new PagedCollectionIterable<String, TestCollectionRepresentation<String>>(
//                stringsResource, firstPage);
//
//        when(stringsResource.getNextPage(any(TestCollectionRepresentation.class)))
//                .thenReturn(secondPage, thirdPage, null);
//
//
//        List<String> result = new LinkedList<String>();
//        for (String s : pagedStrings) {
//            result.add(s);
//        }
//
//        assertThat(result).hasSize(12);
//        assertThat(result).containsSequence(
//                "one", "two", "three", "four",
//                "five", "six", "seven", "eight",
//                "nine", "ten", "eleven", "twelve");
//    }
}
