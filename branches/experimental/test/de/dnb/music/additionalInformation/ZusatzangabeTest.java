package de.dnb.music.additionalInformation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import utils.TestUtils;
import utils.TitleUtils;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.version.Version;

public class ZusatzangabeTest {

	@Before
	public void setUp() throws Exception {
		//
	}

	@Test
	public void testGetGND1XX() {
		// RAK-Version wird richtig erkannt
		MusicTitle mt =
			ParseMusicTitle.parseTitlePlusVersion(null, "Cantica, 1");
		assertEquals("130 Cantica$n1", TitleUtils.getGND1XXPlusTag(mt));
		// RSWK kann nicht erkannt werden
		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Cantica 1");
		assertEquals("130 Cantica 1", TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Adagio, 1");
		assertEquals("130 Adagio$n1", TitleUtils.getGND1XXPlusTag(mt));
		// RSWK kann auch erkannt werden, wg. Formalsachtitel:
		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Adagio 1");
		assertEquals("130 Adagio$n1", TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Sinfonien, Nr. 4");
		assertEquals("130 Sinfonien$nNr. 4", TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Groovy accordion suite, no. 1");
		assertEquals("130 Groovy accordion suite$nno. 1",
				TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseFullRAK(null, "Motetten, Buch 5 <Exsultate Deo>");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).startsWith(
				"130 Motetten$nBuch 5"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Centuria, tomus 1");
		assertEquals("130 Centuria$ntomus 1", TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Hölderlin-Lieder, Zyklus 3");
		assertEquals("130 Hölderlin-Lieder$nZyklus 3",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Das @Recht des Herrn. Suite Nr. 2");
		assertEquals("130 Das @Recht des Herrn$sSuite Nr. 2",
				TitleUtils.getGND1XXPlusTag(mt));

		// Klappt nicht:
		//		mt = ParseMusiktitel.parse(null, "Cathedral, 1 - 3. Fassung Ensemble");
		//		zus = mt.getZusatzangabe();
		//		assertEquals("383 1 - 3", zus.getGND3XX());

		// Klappt nicht:
		//		mt = ParseMusiktitel.parse(null, "... prisme/incidences ..., 2");
		//		zus = mt.getZusatzangabe();
		//		assertEquals("383 2", zus.getGND3XX());

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Te Deum, 1711");
		assertEquals("130 Te Deum$f1711", TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseFullRAK(null, "Stücke, Tb Kl, 1966. Fassung 2008");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$f1966"));

		mt =
			ParseMusicTitle
					.parseTitlePlusVersion(null, "Sonaten, Vl Bc (1716)");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$f1716"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Ave Maria, op. 24");
		assertEquals("130 Ave Maria$nop. 24", TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Hf, op. 16bis");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$nop. 16bis"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Quintette, Ob 1 2 Hr 1 2 Fg, ED 5 B flat 3");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$nED 5 B flat 3"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Tantum ergo, FWV 58");
		assertEquals("130 Tantum ergo$nFWV 58", TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Ouvertüren, Vl Orch, TWV 55 A 7");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$nTWV 55 A 7"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null, "Kantaten, TWV 1,1724");
		assertEquals("130 Kantaten$nTWV 1,1724",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Kl, P 12,3 - 7");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$nP 12,3 - 7"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Romanzen, Vl Kl, T II,31");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$nT II,31"));

		// Funktioniert nicht
		//		mt = ParseMusiktitel.parse(null, "Sinfonien, a-Moll 2");
		//		zus = mt.getZusatzangabe();
		//		assertEquals("384 $aa-Moll 2", zus.getGND3XX());

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzerte, Kb Orch, W 2 H Es");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$nW 2 H Es"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Lieder WoO 22");
		assertEquals("130 Lieder$nWoO 22", TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Vc Kl, o.op. 47");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$no.op. 47"));

	}

	@Test
	public void testGetGND3XX() {
		MusicTitle mt =
			ParseMusicTitle.parseTitlePlusVersion(null, "Cantica, 1");
		AdditionalInformation zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 1"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Sinfonien, Nr. 4");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 Nr. 4"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Groovy accordion suite, no. 1");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 no. 1"));

		mt = ParseMusicTitle.parseFullRAK(null, "Motetten, Buch 5 <Exsultate Deo>");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 Buch 5"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Centuria, tomus 1");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 tomus 1"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Hölderlin-Lieder, Zyklus 3");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 Zyklus 3"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Das @Recht des Herrn. Suite Nr. 2");
		Version fas = mt.getVersion();
		zus = fas.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 Nr. 2"));

		// Klappt nicht:
		//		mt = ParseMusiktitel.parse(null, "Cathedral, 1 - 3. Fassung Ensemble");
		//		zus = mt.getZusatzangabe();
		//		assertEquals("383 1 - 3", zus.getGND3XX());

		mt = ParseMusicTitle.parseFullRAK(null, "... prisme/incidences ..., 2");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 2"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Te Deum, 1711");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("548 $c1711$4dats"));

		mt = ParseMusicTitle.parseFullRAK(null, "Stücke, Tb Kl, 1966. Fassung 2008");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("548 $c1966$4dats"));

		mt =
			ParseMusicTitle
					.parseTitlePlusVersion(null, "Sonaten, Vl Bc (1716)");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("548 $c1716$4dats"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Ave Maria, op. 24");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $bop. 24"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Quintette, Ob 1 2 Hr 1 2 Fg, ED 5 B flat 3");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $cED 5 B flat 3"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Tantum ergo, FWV 58");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $cFWV 58"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Ouvertüren, Vl Orch, TWV 55 A 7");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $cTWV 55 A 7"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null, "Kantaten, TWV 1,1724");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $cTWV 1,1724"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Kl, P 12,3 - 7");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $cP 12,3-7"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Romanzen, Vl Kl, T II,31");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $cT II,31"));

		// Funktioniert nicht
		//		mt = ParseMusiktitel.parse(null, "Sinfonien, a-Moll 2");
		//		zus = mt.getAdditionalInformation();
		//		assertEquals("384 $aa-Moll 2", zus.getGND3XX());

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Lieder WoO 22");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $bWoO 22"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Vc Kl, o.op. 47");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $co.op. 47"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Hf, op. 16bis");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $bop. 16bis"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Konzerte, Kb Orch, W 2 H Es");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("383 $cW 2 H Es"));

	}

}
