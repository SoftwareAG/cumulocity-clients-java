package com.cumulocity.me.util;

import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import junit.framework.Assert;

import org.fest.assertions.Assertions;
import org.junit.Test;

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
	
	@Test
	public void testSplitQuotedWithoutQuotes() {
		String multilineString="this is line one\nthis is line two";
		String delimiter = "\n";
		String[] result = StringUtils.splitQuoted(multilineString, delimiter);

		assertThat(  result.length, is( equalTo(2) )  );
		
		assertThat(  result[0], is( equalTo("this is line one") )  );
		assertThat(  result[1], is( equalTo("this is line two") )  );
		   
	}
	
	@Test
	public void testSplitQuotedWithQuotes() {
		String multilineString="this is line one\n\"this is a quoted line with a line\nbreak\"\nthis is line 3";
		String delimiter = "\n";
		String[] result = StringUtils.splitQuoted(multilineString, delimiter);

		assertThat(  result.length, is( equalTo(3) )  );
		
		assertThat(  result[0], is( equalTo("this is line one") )  );
		assertThat(  result[1], is( equalTo("\"this is a quoted line with a line\nbreak\"") )  );
		assertThat(  result[2], is( equalTo("this is line 3") )  );
	}
	
	@Test
	public void testSplitQuotedWithUnevenQuotes() {
		String multilineString="this is line one\n\"this is a badly quoted line with a line\nbreak\nthis is line 3";
		String delimiter = "\n";
		String[] result = StringUtils.splitQuoted(multilineString, delimiter);

		assertThat(  result.length, is( equalTo(4) )  );
		
		assertThat(  result[0], is( equalTo("this is line one") )  );
		assertThat(  result[1], is( equalTo("\"this is a badly quoted line with a line") )  );
		assertThat(  result[2], is( equalTo("break") )  );
		assertThat(  result[3], is( equalTo("this is line 3") )  );
	}
	
	@Test
	public void testStringWithoutAnythingToSplit() {
		String withoutANewlineString = "this is a plain line";
		String delimiter = "§$%&/(";
		String[] result = StringUtils.split(withoutANewlineString, delimiter);
		
		assertThat(  result.length, is( equalTo(1) )  );
		assertThat(  result[0], is( equalTo(withoutANewlineString) )  );
	}
	
	@Test
	public void testRelacement() {
	    String origString = "my1my1my";
	    String newString = StringUtils.replaceAll(origString, "1", "2");
	    
	    Assert.assertEquals("my2my2my", newString);
	}
	
	@Test
	public void testTailEnsurementTrue() {
	    String origString = "my.cumulocity.com";
	    String newString = StringUtils.ensureTail(origString, ".com");
	    
	    Assert.assertEquals(origString, newString);
	}
	
	@Test
	public void testTailEnsurementFalse() {
	    String origString = "my.cumulocity.co";
        String newString = StringUtils.ensureTail(origString, ".com");
        
        Assert.assertEquals(origString + ".com", newString);
	}
	
	@Test
	public void testStringInsertion() {
	    String origString = "myString";
	    String newString = StringUtils.insert(origString, 2, "New");
	    
	    Assert.assertEquals("myNewString", newString);
	}
	
	@Test
	public void testGetQuoteIndexesWithoutQuotes() {
	    String origString = "myString,myOtherString";
	    Integer[] result = StringUtils.getQuoteIndexes(origString);
	    
	    Assert.assertEquals(0, result.length);
	}
	
	@Test
	public void testGetQuoteIndexesWithQuotes() {
	    String origString = "myString,\"myOtherString\",\"something quoted\"";
	    Integer[] result = StringUtils.getQuoteIndexes(origString);
	    
	    Assert.assertEquals(4, result.length);
	    Assert.assertEquals(9, result[0].intValue());
	    Assert.assertEquals(23, result[1].intValue());
	    Assert.assertEquals(25, result[2].intValue());
	    Assert.assertEquals(42, result[3].intValue());
	}
	
	@Test
	public void testIsInsideQuotesWithoutQuotes() {
	    
		boolean insideQuotes = StringUtils.isInsideQuotes(11, new Integer[0]);
	    
	    Assert.assertEquals(false, insideQuotes);
	}
	
	@Test
	public void testIsInsideQuotesInside() {
	    
		boolean insideQuotes = StringUtils.isInsideQuotes(11, new Integer[]{9, 12, 22, 40});
	    
	    Assert.assertEquals(true, insideQuotes);
	}
	
	@Test
	public void testIsInsideQuotesOutside() {
	    
		boolean insideQuotes = StringUtils.isInsideQuotes(15, new Integer[]{9, 12, 22, 40});
	    
	    Assert.assertEquals(false, insideQuotes);
	}
	
	@Test
	public void testIsInsideQuotesEnd() {
	    
		boolean insideQuotes = StringUtils.isInsideQuotes(44, new Integer[]{9, 12, 22, 40});
	    
	    Assert.assertEquals(false, insideQuotes);
	}
	
	@Test
	public void testIsInsideQuotesUnevenEnd() {
	    
		boolean insideQuotes = StringUtils.isInsideQuotes(44, new Integer[]{9, 12, 22});
	    
	    Assert.assertEquals(false, insideQuotes);
	}

	@Test
	public void shouldPadStartCorrectly() throws Exception {
		//given
		String input = "1234567890";

		//when
		String padded = StringUtils.padStart(input, '0', 20);

		//then
		assertThat(padded).hasSize(20);
		assertThat(padded).startsWith("0000000000");
		assertThat(padded).endsWith(input);
	}

	@Test
	public void shouldNotPadStartIfLengthAlreadyReached() throws Exception {
		//given
		String input = "1234567890";

		//when
		String padded = StringUtils.padStart(input, '0', 10);

		//then
		assertThat(padded).hasSize(10);
		assertThat(padded).isEqualTo(input);
	}

	@Test
	public void shouldNotPadStartIfLengthAlreadySurpassed() throws Exception {
		//given
		String input = "1234567890";

		//when
		String padded = StringUtils.padStart(input, '0', 5);

		//then
		assertThat(padded).hasSize(10);
		assertThat(padded).isEqualTo(input);
	}


}
