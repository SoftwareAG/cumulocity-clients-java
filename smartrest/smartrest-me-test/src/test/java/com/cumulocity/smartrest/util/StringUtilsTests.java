package com.cumulocity.smartrest.util;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Hashtable;

import junit.framework.Assert;

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
	public void testStringEscaping() {
		Assert.assertEquals("\"\"", StringUtils.escape(""));
		Assert.assertEquals("\"abc\"", StringUtils.escape("abc"));
		Assert.assertEquals("\"\"\"\"", StringUtils.escape("\""));
		Assert.assertEquals("\"A \"\"quote\"\"!\"", StringUtils.escape("A \"quote\"!"));		
	}
	
	@Test
	public void testArrayEscaping() {
		Assert.assertEquals("\"\"", StringUtils.escape(new String[] { } ));
		Assert.assertEquals("\"\"\"Test\"\"\"", StringUtils.escape(new String[] { "Test" } ));
		Assert.assertEquals("\"\"\"Test\"\",\"\"Test2\"\"\"", StringUtils.escape(new String[] { "Test", "Test2" } ));
	}
	
	@Test
	public void testTableEscaping() {
		Hashtable table = new Hashtable();
		Assert.assertEquals("\"\"", StringUtils.escape(table));

		table.put("key1", "value1");
		Assert.assertEquals("\"\"\"key1\"\":\"\"value1\"\"\"", StringUtils.escape(table));

		table.put("key2", "value2");
		// Some speculation on hash ordering here ...
		Assert.assertEquals("\"\"\"key2\"\":\"\"value2\"\",\"\"key1\"\":\"\"value1\"\"\"", StringUtils.escape(table));	
	}
}
