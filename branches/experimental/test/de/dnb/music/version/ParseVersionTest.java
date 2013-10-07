package de.dnb.music.version;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import utils.TitleUtils;

import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;


public class ParseVersionTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testParseFassung() {
		MusicTitle mt =
			ParseMusicTitle.parseTitlePlusVersion(null, "Casbah of Tetouan. Rev. Fassung");
		assertEquals("130 Casbah of Tetouan$sRev. Fassung", TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Ave Maria. Fassung Frauenchor");
		assertEquals("130 Ave Maria$sFassung Frauenchor", TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Saint Cecilia's blues. Fassung Pos 1 2 3 4 5");
		assertEquals("130 Saint Cecilia's blues$sFassung Pos 1 2 3 4 5",
				TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Amériques. Fassung Kl 1 2 8hdg.");
		assertEquals("130 Amériques$sFassung Kl 1 2 8hdg.", TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Andere Räume. Fassung Schz 1 2 3 4 Tonband");
		assertEquals("130 Andere Räume$sFassung Schz 1 2 3 4 Tonband",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Liebe, wunderschönes Leben. Fassung Vc 1 2 3 4, 2009");
		assertEquals(
				"130 Liebe, wunderschönes Leben$sFassung Vc 1 2 3 4, 2009",
				TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Solos, Blfl, 1993. Fassung 2009");
		assertEquals("130 Solos$mBlfl$f1993$sFassung 2009", TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Widerschein. Fassung 2");
		assertEquals("130 Widerschein$sFassung 2", TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Quartette, Vl 1 2 Va Vc, Nr. 1. Fassung 1990");
		assertEquals("130 Quartette$mVl 1 2$mVa$mVc$nNr. 1$sFassung 1990",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parse(null,
					"Stücke, Vl Org, op. 150 <Abendlied>. Fassung Vc Org");
		assertEquals(
				"130 Stücke$mVl$mOrg$nop. 150$pAbendlied$sFassung Vc Org",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Stücke$mVl$mOrg$nop. 150$pAbendlied$4werk",
				TitleUtils.getGND530(mt, true));

		mt =
			ParseMusicTitle.parse(null,
					"Klang <5. Stunde>. Fassung Fl");
		assertEquals("130 Klang$p5. Stunde$sFassung Fl", TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Klang$p5. Stunde$4werk", TitleUtils.getGND530(mt, true));
					  

	}

}
