package de.dnb.music.genre;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.genre.Genre;
import de.dnb.music.genre.GenreDB;
import de.dnb.music.mediumOfPerformance.InstrumentDB;

public class GenreDBTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPrefix() {

		Genre g;

		g = GenreDB.matchGenre("Allegrop");
		assertNull(g);
		g = GenreDB.matchGenre("Allegro");
		assertEquals(g.singular, "Allegro");
		g = GenreDB.matchGenre("Allegro m");
		assertEquals(g.singular, "Allegro");
		g = GenreDB.matchGenre("Allegro n");
		assertEquals(g.singular, "Allegro");
		g = GenreDB.matchGenre("Allegro v");
		assertEquals(g.singular, "Allegro");
		g = GenreDB.matchGenre("Allegro vib");
		assertEquals(g.singular, "Allegro");
		g = GenreDB.matchGenre("Allegro w");
		assertEquals(g.singular, "Allegro");

		g = GenreDB.matchGenre("Allegro moderato");
		assertEquals(g.singular, "Allegro moderato");

		g = GenreDB.matchGenre("Allegro vivace");
		assertEquals(g.singular, "Allegro vivace");

		g = GenreDB.matchGenre("Allegro vivo");
		assertEquals(g.singular, "Allegro vivo");

		//----------------------------------------

		g = GenreDB.matchGenre("Sinfonische Lieder");
		assertEquals(g.singular, "Sinfonisches Lied");
		g = GenreDB.matchGenre("Sinfonische Lieder o");
		assertEquals(g.singular, "Sinfonisches Lied");
		g = GenreDB.matchGenre("Sinfonische Lieder p");
		assertEquals(g.singular, "Sinfonisches Lied");

		g = GenreDB.matchGenre("Sinfonische Lieder ohne Worte");
		assertEquals(g.singular, "Sinfonisches Lied ohne Worte");

		//---------------------------------
		g = GenreDB.matchGenre("Lied");
		assertEquals(g.singular, "Lied");
		g = GenreDB.matchGenre("Lied o");
		assertEquals(g.singular, "Lied");
		g = GenreDB.matchGenre("Lied p");
		assertEquals(g.singular, "Lied");

		g = GenreDB.matchGenre("Lied ohne Worte");
		assertEquals(g.singular, "Lied ohne Worte");

		//---------------------------------------------

		g = GenreDB.matchGenre("Konzertantes Lied");
		assertEquals(g.singular, "Konzertantes Lied");
		g = GenreDB.matchGenre("Konzertantes Lied o");
		assertEquals(g.singular, "Konzertantes Lied");
		g = GenreDB.matchGenre("Konzertantes Lied p");
		assertEquals(g.singular, "Konzertantes Lied");

		g = GenreDB.matchGenre("Konzertantes Lied ohne Worte");
		assertEquals(g.singular, "Konzertantes Lied ohne Worte");

		//--------------------------------------------------

		g = GenreDB.matchGenre("Sinfonisches Lied");
		assertEquals(g.singular, "Sinfonisches Lied");
		g = GenreDB.matchGenre("Sinfonisches Lied o");
		assertEquals(g.singular, "Sinfonisches Lied");
		g = GenreDB.matchGenre("Sinfonisches Lied p");
		assertEquals(g.singular, "Sinfonisches Lied");

		g = GenreDB.matchGenre("Sinfonisches Lied ohne Worte");
		assertEquals(g.singular, "Sinfonisches Lied ohne Worte");

		//-----------------------------------

		g = GenreDB.matchGenre("Konzertante Lieder");
		assertEquals(g.singular, "Konzertantes Lied");
		g = GenreDB.matchGenre("Konzertante Lieder o");
		assertEquals(g.singular, "Konzertantes Lied");
		g = GenreDB.matchGenre("Konzertante Lieder p");
		assertEquals(g.singular, "Konzertantes Lied");

		g = GenreDB.matchGenre("Konzertante Lieder ohne Worte");
		assertEquals(g.singular, "Konzertantes Lied ohne Worte");

		//------------------------------------

		g = GenreDB.matchGenre("Lieder o");
		assertEquals(g.singular, "Lied");
		g = GenreDB.matchGenre("Lieder p");
		assertEquals(g.singular, "Lied");

		g = GenreDB.matchGenre("Lieder ohne Worte");
		assertEquals(g.singular, "Lied ohne Worte");
	}

	@Test
	public void testMatchGattung() {
		Genre g =
			GenreDB.matchGenre("Lieder ohne Worte, Violine, "
				+ "Viola, Orchester, KV 364");
		assertEquals(g.idn, "943374480");
		assertEquals(g.match, "Lieder ohne Worte");
		assertEquals(g.rest, ", Violine, Viola, Orchester, KV 364");

		g =
			GenreDB.matchGenre("Lied ohne Worte, Violine, "
				+ "Viola, Orchester, KV 364");
		assertEquals(g.idn, "943374480");
		assertEquals(g.match, "Lied ohne Worte");
		assertEquals(g.rest, ", Violine, Viola, Orchester, KV 364");

		g =
			GenreDB.matchGenre(" Lieder ohne Worte, Violine, "
				+ "Viola, Orchester, KV 364");
		assertNull(g);

		g = GenreDB.matchGenre("");
		assertNull(g);

		try {
			g = GenreDB.matchGenre(null);
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}

	}

	@Test
	public void testDatabaseConsistency() {
		for (String[] line : GenreDB.DATA) {

			assertTrue(line[GenreDB.SWD], line[GenreDB.IDN].length() != 0);
			assertTrue(line[GenreDB.IDN].trim().equals(line[GenreDB.IDN]));

			assertTrue(line[GenreDB.NID].length() != 0);
			assertTrue(line[GenreDB.NID].trim().equals(line[GenreDB.NID]));

			assertTrue(line[GenreDB.SWD].length() != 0);

			assertTrue(line[GenreDB.SING].length() != 0);
			assertTrue(line[GenreDB.SING].trim().equals(line[GenreDB.SING]));

			assertTrue(line[GenreDB.PLUR].length() != 0);
			assertTrue(line[GenreDB.PLUR].trim().equals(line[GenreDB.PLUR]));

			assertFalse(line[GenreDB.IDN].contains("-"));
			assertTrue(line[GenreDB.NID].contains("-"));
		}
	}
}
