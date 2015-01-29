package com.cumulocity.sdk.client;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class UrlProcessorTest {
	
	static final String URL = "http://abc.com?param1=abc&param2=cde";
	
	UrlProcessor processor = new UrlProcessor();
	
	@Test
    public void shouldRemoveParamFromUrl() throws Exception {
	    String actual = processor.removeQueryParam(URL, asList("param1"));
	    
	    assertThat(actual).isEqualTo("http://abc.com?param2=cde");
	    
	    actual = processor.removeQueryParam(URL, asList("param2"));
	    
	    assertThat(actual).isEqualTo("http://abc.com?param1=abc");
	    
	    actual = processor.removeQueryParam(URL, asList("param1", "param2"));
	    
	    assertThat(actual).isEqualTo("http://abc.com");
    }

}
