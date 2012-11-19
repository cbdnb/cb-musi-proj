package utils;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import utils.StringUtils;


public class StringUtilsTest {

	@Test
	public final void testfindLongestPrefix() {
		String parseString = "1 23456";
		String[][] data = { { "1", "2", "1" }, { "12", "13", "14" } };
		Pair<String, String[]> slp =
			StringUtils.findLongestPrefix(parseString, data, 0);
		String[] line = slp.second;
		
		
		String[] vektor = {"a", "ab",""};
		String prefix = StringUtils.findLongestPrefix("", vektor);
		assertNull(prefix);
		
		prefix = StringUtils.findLongestPrefix("acc", vektor);
		assertNull(prefix);
		
		prefix = StringUtils.findLongestPrefix("abc", vektor);
		assertNull(prefix);
		
		prefix = StringUtils.findLongestPrefix("a cc", vektor);
		assertEquals("a", prefix);
		
		prefix = StringUtils.findLongestPrefix("ab c", vektor);
		assertEquals("ab", prefix);
		
		
		
	}

	@Test
	public final void testContainsOrdnungsgruppe() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSplitWerkteile() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testContainsOrdnungshilfe() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGnd2Pica() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testDma2gnd() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testDma2GND430() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testContainsYear() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testMatchesPrecisely() {
		String prefix = "1";
		String parseString = "12";
		assertFalse(StringUtils.matchesPrecisely(prefix, parseString));
		parseString = "1 2";
		assertTrue(StringUtils.matchesPrecisely(prefix, parseString));
		parseString = "1";
		assertTrue(StringUtils.matchesPrecisely(prefix, parseString));
		parseString = "1, 2";
		assertTrue(StringUtils.matchesPrecisely(prefix, parseString));

		prefix = "";
		parseString = " 1, 2";
		assertFalse(StringUtils.matchesPrecisely(prefix, parseString));
		parseString = "1, 2";
		assertFalse(StringUtils.matchesPrecisely(prefix, parseString));

		try {
			prefix = null;
			parseString = "1, 2";
			StringUtils.matchesPrecisely(prefix, parseString);
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
	}

	@Test
	public final void testEinruecken() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testStripBlanksAndCommas() {
		String s;
		try {
			s = StringUtils.stripLeadingBlanksAndCommas(null);
			fail();
		} catch (IllegalArgumentException e) {
			//OK
		}

		s = StringUtils.stripLeadingBlanksAndCommas("");
		assertEquals("", s);
		assertEquals("", StringUtils.getLeadingBlanksAndCommas());

		s = StringUtils.stripLeadingBlanksAndCommas(",");
		assertEquals("", s);
		assertEquals(",", StringUtils.getLeadingBlanksAndCommas());

		s = StringUtils.stripLeadingBlanksAndCommas(" ");
		assertEquals("", s);
		assertEquals(" ", StringUtils.getLeadingBlanksAndCommas());

		s = StringUtils.stripLeadingBlanksAndCommas(",,");
		assertEquals("", s);
		assertEquals(",,", StringUtils.getLeadingBlanksAndCommas());

		s = StringUtils.stripLeadingBlanksAndCommas("   ");
		assertEquals("", s);
		assertEquals("   ", StringUtils.getLeadingBlanksAndCommas());

		s = StringUtils.stripLeadingBlanksAndCommas(", ,,  a");
		assertEquals("a", s);
		assertEquals(", ,,  ", StringUtils.getLeadingBlanksAndCommas());

	}

}
