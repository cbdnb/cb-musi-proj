package de.dnb.music.genre;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.genre.Genre.Numeri;

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
		assertEquals("Präludien", g1.toString(Numeri.PLURAL));
		assertEquals("Adagio, Allegro", g2.toString(Numeri.PLURAL));
		assertEquals("Präludien und Fugen", gPrF.toString(Numeri.PLURAL));
		assertEquals("Präludium, Tokkata und Fugen", g3.toString(Numeri.PLURAL));
		assertEquals("Präludium, Fuge und Thema mit Variationen",
				gMult.toString(Numeri.PLURAL));
	}

	@Test
	public final void testAsRSWK() {
		assertEquals("Präludium", g1.toString(Numeri.SINGULAR));
		assertEquals("Adagio, Allegro", g2.toString(Numeri.SINGULAR));
		assertEquals("Präludium und Fuge", gPrF.toString(Numeri.SINGULAR));
		assertEquals("Präludium, Tokkata und Fugen",
				g3.toString(Numeri.SINGULAR));
		assertEquals("Präludium, Fuge und Thema mit Variationen",
				gMult.toString(Numeri.SINGULAR));
	}

	@Test
	public final void testGetGND3XX() {
		//		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetGND1XX() {
		assertEquals("Präludien", g1.toString(Numeri.PLURAL));
		assertEquals("Adagio, Allegro", g2.toString(Numeri.PLURAL));
		assertEquals("Präludien und Fugen", gPrF.toString(Numeri.PLURAL));
		assertEquals("Präludium, Tokkata und Fugen", g3.toString(Numeri.PLURAL));
		assertEquals("Präludium, Fuge und Thema mit Variationen",
				gMult.toString(Numeri.PLURAL));
	}

}
