package utils;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import utils.StringUtils;

public class StringUtilsTest {

	@Test
	public final void testRemoveExpansion() {
		String oldRecord =
			"\n130 Humoresken$mVl 1 2$mVa$mVc$f2012"
				+ "\n380 !042974747!Humoreske$gMusik"
				+ "\n382 !040197913!Violine$n2" + "\n382 !041883640!Viola"
				+ "\n382 !041847180!Tenor$gStimmlage"
				+ "\n382 !041847180!Tenor$gStimmlage$pAlt"
				+ "\n500 !118508288!Beethoven, Ludwig$cvan$4kom1"
				+ "\n500 !118524895!Josquin$lDesprez$4kom1"
				+ "\n530 !300189591!Beethoven, Ludwig$cvan$aBundeslied$4werk"
				+ "\n680 MGG\n";
		String newRecord = StringUtils.removeExpansion(oldRecord);
		String expected =
			"130 Humoresken$mVl 1 2$mVa$mVc$f2012" + "\n380 !042974747!"
				+ "\n382 !040197913!$n2" + "\n382 !041883640!"
				+ "\n382 !041847180!" + "\n382 !041847180!$pAlt"
				+ "\n500 !118508288!$4kom1" + "\n500 !118524895!$4kom1"
				+ "\n530 !300189591!$4werk" + "\n680 MGG";
		assertEquals(expected, newRecord);
		oldRecord =
			"\n130 Humoresken$mVl 1 2$mVa$mVc$f2012" + "\n380 Humoreske$gMusik"
				+ "\n382 Violine$n2" + "\n382 Viola" + "\n382 Tenor$gStimmlage"
				+ "\n382 Tenor$gStimmlage$pAlt"
				+ "\n500 Beethoven, Ludwig$cvan$4kom1"
				+ "\n500 Josquin$lDesprez$4kom1" + "\n680 MGG\n";
		newRecord = StringUtils.removeExpansion(oldRecord);
		expected =
			"130 Humoresken$mVl 1 2$mVa$mVc$f2012" + "\n380 Humoreske$gMusik"
				+ "\n382 Violine$n2" + "\n382 Viola" + "\n382 Tenor$gStimmlage"
				+ "\n382 Tenor$gStimmlage$pAlt"
				+ "\n500 Beethoven, Ludwig$cvan$4kom1"
				+ "\n500 Josquin$lDesprez$4kom1" + "\n680 MGG";
		assertEquals(expected, newRecord);
		oldRecord = "382 !041847180!Tenor$gStimmlage$vkom";
		newRecord = StringUtils.removeExpansion(oldRecord);
		expected = "382 !041847180!$vkom";
		assertEquals(expected, newRecord);
		oldRecord = "\n382 !041847180!\n";
		newRecord = StringUtils.removeExpansion(oldRecord);
		expected = "382 !041847180!";
		assertEquals(expected, newRecord);
		oldRecord = "abc";
		newRecord = StringUtils.removeExpansion(oldRecord);
		expected = "abc";
		assertEquals(expected, newRecord);
		oldRecord = "";
		newRecord = StringUtils.removeExpansion(oldRecord);
		expected = "";
		assertEquals(expected, newRecord);
		try {
			oldRecord = null;
			newRecord = StringUtils.removeExpansion(oldRecord);
		} catch (IllegalArgumentException e) {
			// nix
		}

		oldRecord =
			"\n260 !041818199!Sonne$gMotiv"
				+ "\n260 !952737345!Aufgang$gAstronomie, Motiv"
				+ "\n550 !040403602!Motiv$4obin$X1";
		expected =
			"260 !041818199!" + "\n260 !952737345!"
				+ "\n550 !040403602!$4obin$X1";
		;
		newRecord = StringUtils.removeExpansion(oldRecord);
		assertEquals(expected, newRecord);

		oldRecord =
			"169 $aGND3$bP$x 0.000!1001618564!Ordine dei Medici del Cantone Ticino";

		expected =
			"169 $aGND3$bP$x 0.000!1001618564!"
		;
		newRecord = StringUtils.removeExpansion(oldRecord);
		assertEquals(expected, newRecord);

	}

	@Test
	public final void testfindLongestPrefix() {
		String parseString = "1 23456";
		String[][] data = { { "1", "2", "1" }, { "12", "13", "14" } };
		Pair<String, String[]> slp =
			StringUtils.findLongestPrefix(parseString, data, 0);
		String[] line = slp.second;

		String[] vektor = { "a", "ab", "" };
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
