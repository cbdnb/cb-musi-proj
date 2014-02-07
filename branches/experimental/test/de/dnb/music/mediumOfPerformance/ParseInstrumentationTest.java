package de.dnb.music.mediumOfPerformance;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;
import de.dnb.music.title.AugmentableElement;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.utils.TestUtils;
import de.dnb.music.utils.TitleUtils;

public class ParseInstrumentationTest {

	@Test
	public void testParse() {
		InstrumentationList bl = ParseInstrumentation.parse("");
		assertNull(bl);

		try {
			bl = ParseInstrumentation.parse(null);
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}
		bl = ParseInstrumentation.parse("Vl");
		assertEquals(bl.getRest(), "");

		bl = ParseInstrumentation.parse("Vl Va");
		assertEquals(bl.getRest(), "");

		bl = ParseInstrumentation.parse("Vl Va x");
		assertEquals(bl.getRest(), " x");

		bl = ParseInstrumentation.parse("Vl Va ");
		assertEquals(bl.getRest(), " ");

	}

	@Test
	public void testParseSingleInstrument() {
		Instrument i;
		try {
			i = ParseInstrumentation.parseSingleInstrument(null);
			fail();
		} catch (IllegalArgumentException e) {
			//	OK
		}
		i = ParseInstrumentation.parseSingleInstrument("");
		assertNull(i);

		i = ParseInstrumentation.parseSingleInstrument("VV");
		assertNull(i);

		i = ParseInstrumentation.parseSingleInstrument("Vl");
		
		assertEquals(TitleUtils.getRAK(i), " Vl");
		assertEquals(i.rest, "");

		i = ParseInstrumentation.parseSingleInstrument("Vl 1");
		assertEquals(TitleUtils.getRAK(i), " Vl");
		assertEquals(i.rest, " 1");

		i = ParseInstrumentation.parseSingleInstrument("Vl 2");
		assertEquals(TitleUtils.getRAK(i), " Vl");
		assertEquals(i.rest, " 2");

		i = ParseInstrumentation.parseSingleInstrument("Vl 1 2 3");
		assertEquals(TitleUtils.getRAK(i), " Vl 1 2 3");
		assertEquals(i.rest, "");

		i = ParseInstrumentation.parseSingleInstrument("Vl 1 2 3 ");
		assertEquals(TitleUtils.getRAK(i), " Vl 1 2 3");
		assertEquals(i.rest, " ");

		i = ParseInstrumentation.parseSingleInstrument("Vl 1 2 3 aa");
		assertEquals(TitleUtils.getRAK(i), " Vl 1 2 3");
		assertEquals(i.rest, " aa");

		i = ParseInstrumentation.parseSingleInstrument("Vl 1 2 4");
		assertEquals(TitleUtils.getRAK(i), " Vl 1 2");
		assertEquals(i.rest, " 4");

	}

	@Test
	public void test380() {
		AugmentableElement mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Vl Bc, L 3,77");
		InstrumentationList bl = mt.getInstrumentationList();
		List<Instrument> il = bl.getChildren();
		Instrument i = il.get(0);
		assertEquals("Violine",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(1);
		assertEquals("Generalbass",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));

		ParseMusicTitle.parseTitlePlusVersion(null,
				"Sonate, Violine Basso continuo L 3,77");
		bl = mt.getInstrumentationList();
		il = bl.getChildren();
		i = il.get(0);
		assertEquals("Violine",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(1);
		assertEquals("Generalbass",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzerte, Vc Orch, op. 5");
		bl = mt.getInstrumentationList();
		il = bl.getChildren();
		i = il.get(0);
		assertEquals("Violoncello",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(1);
		assertEquals("Orchester",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzert, Violoncello Orchester op. 5");
		bl = mt.getInstrumentationList();
		il = bl.getChildren();
		i = il.get(0);
		assertEquals("Violoncello",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(1);
		assertEquals("Orchester",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sextette, Vl 1 2 3 Va Vc 1 2, op. 38");
		bl = mt.getInstrumentationList();
		il = bl.getChildren();
		i = il.get(0);
		assertEquals("Violine$n3",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(1);
		assertEquals("Viola", TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(2);
		assertEquals("Violoncello$n2",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		String gnd380 = TestUtils.getGND3XX(bl);
		assertTrue(gnd380.contains("Violine$n3"));
		assertTrue(gnd380.contains("Violoncello$n2"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sextett, Violine 1,2,3 Viola Violoncello 1,2, op. 38");
		bl = mt.getInstrumentationList();
		il = bl.getChildren();
		i = il.get(0);
		assertEquals("Violine$n3",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(1);
		assertEquals("Viola", TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(2);
		assertEquals("Violoncello$n2",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		gnd380 = TestUtils.getGND3XX(bl);
		assertTrue(gnd380.contains("Violine$n3"));
		assertTrue(gnd380.contains("Violoncello$n2"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Notturni, Singst. 1 2 3 4 Kl, op. 22");
		bl = mt.getInstrumentationList();
		il = bl.getChildren();
		i = il.get(0);
		assertEquals("Gesangsstimme$n4",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(1);
		assertEquals("Klavier",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		gnd380 = TestUtils.getGND3XX(bl);
		assertTrue(gnd380.contains("Gesangsstimme$n4"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Fantasien, Kl 4hdg., op. 31");
		bl = mt.getInstrumentationList();
		assertEquals("Klavier",
				TestUtils.getLinkName(TestUtils.getGND3XX(bl)));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Notturno, Singstimme 1,2,3,4 Klavier op. 22");
		bl = mt.getInstrumentationList();
		il = bl.getChildren();
		i = il.get(0);
		assertEquals("Gesangsstimme$n4",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		i = il.get(1);
		assertEquals("Klavier",
				TestUtils.getLinkName(TestUtils.getGND3XX(i)));
		gnd380 = TestUtils.getGND3XX(bl);
		assertTrue(gnd380.contains("Gesangsstimme$n4"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Fantasien, Kl 4hdg., op. 31");
		bl = mt.getInstrumentationList();
		assertEquals("Klavier",
				TestUtils.getLinkName(TestUtils.getGND3XX(bl)));
		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonatensätze, Kl 1 2 8hdg., e-Moll");
		bl = mt.getInstrumentationList();
		gnd380 = TestUtils.getGND3XX(bl);
		assertTrue(gnd380.contains("Klavier$n2"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Fantasie, Klavier vierhändig op. 31");
		bl = mt.getInstrumentationList();
		assertEquals("Klavier",
				TestUtils.getLinkName(TestUtils.getGND3XX(bl)));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonatensätze, Kl 1 2 8hdg., e-Moll");
		bl = mt.getInstrumentationList();
		gnd380 = TestUtils.getGND3XX(bl);
		assertTrue(gnd380.contains("Klavier$n2"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonatensatz, Klavier 1,2 achthändig e-Moll");
		bl = mt.getInstrumentationList();
		gnd380 = TestUtils.getGND3XX(bl);
		assertTrue(gnd380.contains("Klavier$n2"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzerte, Kl linke Hand Orch, Es-Dur");
		bl = mt.getInstrumentationList();
		gnd380 = TestUtils.getGND3XX(bl);
		assertTrue(gnd380.contains("Klavier"));
		assertTrue(gnd380.contains("Orchester"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzert, Klavier linke Hand Orchester Es-Dur");
		bl = mt.getInstrumentationList();
		gnd380 = TestUtils.getGND3XX(bl);
		assertTrue(gnd380.contains("Klavier"));
		assertTrue(gnd380.contains("Orchester"));

	}

	@Test
	public void test130() {
//		MusicTitle.setGND3XXExplicit(true);
		AugmentableElement mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Vl Bc, L 3,77");
		InstrumentationList bl = mt.getInstrumentationList();
		assertEquals("$mVl$mBc", TitleUtils.getX30ContentAsString(bl));

//		MusicTitle.setGND3XXExplicit(true);
		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonate, Violine Basso continuo L 3,77");
		bl = mt.getInstrumentationList();
		assertEquals("$mVl$mBc", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzerte, Vc Orch, op. 5");
		bl = mt.getInstrumentationList();
		assertEquals("$mVc$mOrch", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzert, Violoncello Orchester op. 5");
		bl = mt.getInstrumentationList();
		assertEquals("$mVc$mOrch", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sextett, Violine 1,2,3 Viola Violoncello 1,2, op. 38");
		bl = mt.getInstrumentationList();
		assertEquals("$mVl 1 2 3$mVa$mVc 1 2", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sextette, Vl 1 2 3 Va Vc 1 2, op. 38");
		bl = mt.getInstrumentationList();
		assertEquals("$mVl 1 2 3$mVa$mVc 1 2", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Notturni, Singst. 1 2 3 4 Kl, op. 22");
		bl = mt.getInstrumentationList();
		assertEquals("$mSingst. 1 2 3 4$mKl", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Fantasien, Kl 4hdg., op. 31");
		bl = mt.getInstrumentationList();
		assertEquals("$mKl 4hdg.", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Notturno, Singstimme 1,2,3,4 Klavier op. 22");
		bl = mt.getInstrumentationList();
		assertEquals("$mSingst. 1 2 3 4$mKl", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Fantasien, Kl 4hdg., op. 31");
		bl = mt.getInstrumentationList();
		assertEquals("$mKl 4hdg.", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Fantasie, Klavier vierhändig op. 31");
		bl = mt.getInstrumentationList();
		assertEquals("$mKl 4hdg.", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonatensätze, Kl 1 2 8hdg., e-Moll");
		bl = mt.getInstrumentationList();
		assertEquals("$mKl 1 2 8hdg.", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonatensatz, Klavier 1,2 achthändig e-Moll");
		bl = mt.getInstrumentationList();
		assertEquals("$mKl 1 2 8hdg.", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzerte, Kl linke Hand Orch, Es-Dur");
		bl = mt.getInstrumentationList();
		assertEquals("$mKl linke Hand$mOrch", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzerte, Kl linke Hd Orch, Es-Dur");
		bl = mt.getInstrumentationList();
		assertEquals("$mKl linke Hand$mOrch", TitleUtils.getX30ContentAsString(bl));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzert, Klavier linke Hand Orchester Es-Dur");
		bl = mt.getInstrumentationList();
		assertEquals("$mKl linke Hand$mOrch", TitleUtils.getX30ContentAsString(bl));

	}

}
