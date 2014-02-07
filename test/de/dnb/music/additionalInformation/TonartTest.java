package de.dnb.music.additionalInformation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.title.AugmentableElement;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.utils.TestUtils;
import de.dnb.music.utils.TitleUtils;

public class TonartTest {

	@Before
	public void setUp() throws Exception {
		//
	}

	@Test
	public void testGetGND1XX() {
		MusicTitle mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Fl 1 2 Bc, B-Dur");
		assertTrue(TitleUtils.getGND1XXPlusTag(mt).contains("$rB-Dur"));

		mt =
			ParseMusicTitle
					.parseTitlePlusVersion(null, "Magnificat, c-Dorisch");
		assertEquals("130 Magnificat$rc-Dorisch",
				TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Messen, 4. Ton");
		assertEquals("130 Messen$r4. Ton", TitleUtils.getGND1XXPlusTag(mt));
	}

	@Test
	public void testGetGND3XX() {
		AugmentableElement mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Fl 1 2 Bc, B-Dur");
		AdditionalInformation zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("384 B-Dur"));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"190 $aMagnificat, c-Dorisch");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("384 c-Dorisch"));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Messen, 4. Ton");
		zus = mt.getAdditionalInformation();
		assertTrue(TitleUtils.getGND3XX(zus).contains("384 4. Ton"));

	}

}
