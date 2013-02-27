package de.dnb.music.genre;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GenreListTest {

	private GenreList g1, g2, gPrF, g3, gMult;

	@Before
	public void setUp() throws Exception {
		g1 = ParseGenre.parseGenreList(" Präludium  ");
		g2 = ParseGenre.parseGenreList(" Adagio, Allegro  ");
		gPrF = ParseGenre.parseGenreList(" Präludien  und  Fugen  ");
		g3 = ParseGenre.parseGenreList(" Präludium,  Tokkata  und  Fugen  ");
		gMult =
			ParseGenre
					.parseGenreList("Präludium, Fuge und Thema mit Variationen, "
						+ "Org (1992)");
	}

	@Test
	public final void testAsRAK() {
		assertEquals("Präludien", g1.pluralPreferred());
		assertEquals("Adagio, Allegro", g2.pluralPreferred());
		assertEquals("Präludien und Fugen", gPrF.pluralPreferred());
		assertEquals("Präludium, Tokkata und Fugen", g3.pluralPreferred());
		assertEquals("Präludium, Fuge und Thema mit Variationen",
				gMult.pluralPreferred());
	}

	@Test
	public final void testAsRSWK() {
		assertEquals("Präludium", g1.singularPreferred());
		assertEquals("Adagio, Allegro", g2.singularPreferred());
		assertEquals("Präludium und Fuge", gPrF.singularPreferred());
		assertEquals("Präludium, Tokkata und Fugen", g3.singularPreferred());
		assertEquals("Präludium, Fuge und Thema mit Variationen",
				gMult.singularPreferred());
	}

	@Test
	public final void testGetGND3XX() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetGND1XX() {
		assertEquals("Präludien", g1.pluralPreferred());
		assertEquals("Adagio, Allegro", g2.pluralPreferred());
		assertEquals("Präludien und Fugen", gPrF.pluralPreferred());
		assertEquals("Präludium, Tokkata und Fugen", g3.pluralPreferred());
		assertEquals("Präludium, Fuge und Thema mit Variationen",
				gMult.pluralPreferred());
	}

}
