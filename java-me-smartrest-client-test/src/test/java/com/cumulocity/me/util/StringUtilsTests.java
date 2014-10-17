package com.cumulocity.me.util;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

public class StringUtilsTests {

	@Test
	public void testSingleCharacterDelimiterSplit() {
		String multilineString="this is line one\nthis is line two";
		String singleCharacterDelimiter = "\n";
		String[] result = StringUtils.split(multilineString, singleCharacterDelimiter);

		assertThat(  result.length, is( equalTo(2) )  );
		
		assertThat(  result[0], is( equalTo("this is line one") )  );
		assertThat(  result[1], is( equalTo("this is line two") )  );
		   
	}
	
	@Test
	public void testSeveralCharactersDelimiterSplit() {
		String multilineString="this is line one\r\nthis is line two";
		String severalCharactersDelimiter = "\r\n";
		String[] result = StringUtils.split(multilineString, severalCharactersDelimiter);

		assertThat(  result.length, is( equalTo(2) )  );
		
		assertThat(  result[0], is( equalTo("this is line one") )  );
		assertThat(  result[1], is( equalTo("this is line two") )  );
		   
	}
	
}
