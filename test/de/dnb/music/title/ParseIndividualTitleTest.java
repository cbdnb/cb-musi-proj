package de.dnb.music.title;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.title.IndividualTitle;
import de.dnb.music.title.ParseIndividualTitle;
import de.dnb.music.utils.TitleUtils;

public class ParseIndividualTitleTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testParse() {
		IndividualTitle in;
		in = ParseIndividualTitle.parse(null, "Kunst der Fuge");
		assertTrue(TitleUtils.getFullGND(in).startsWith("130 Kunst der Fuge"));

		in =
			ParseIndividualTitle.parse(null,
					"Allein Gott in der Höh sei Ehr");
		assertTrue(TitleUtils.getFullGND(in).startsWith(
				"130 Allein Gott in der Höh sei Ehr"));
	}

}
