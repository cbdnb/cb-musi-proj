package de.dnb.music.genre;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.genre.Genre;
import de.dnb.music.genre.GenreList;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.genre.Genre.Numeri;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.utils.TestUtils;


public class ParseGenreTest {

	@Before
	public void setUp() throws Exception {
//		MusicTitle.setGND3XXExplicit(true);
	}
	
	@Test
	public void testtest() {
//		fail();
	}

	@Test
	public void testParseSeparatorPlusGenre() {
		Genre g;
		try {
			g = ParseGenre.parseSeparatorPlusGenre(null);
			fail();
		} catch (IllegalArgumentException e) {
			//OK
		}
		g = ParseGenre.parseSeparatorPlusGenre("");
		assertNull(g);

		g = ParseGenre.parseSeparatorPlusGenre("und ");
		assertNull(g);

		g = ParseGenre.parseSeparatorPlusGenre(" ,  ");
		assertNull(g);

		g = ParseGenre.parseSeparatorPlusGenre("Adagio");
		assertNull(g);

		g = ParseGenre.parseSeparatorPlusGenre("und Adagio");
		assertEquals(g.match, "und Adagio");
		assertEquals(g.rest, "");
		
		g = ParseGenre.parseSeparatorPlusGenre("und Adagio x");
		assertEquals(g.match, "und Adagio");
		assertEquals(g.rest, " x");
	}

	@Test
	public void testParseGenreList() {
		GenreList llg;
		try {
			llg = ParseGenre.parseGenreList(null);
			fail();
		} catch (IllegalArgumentException e) {
			//OK
		}
		llg = ParseGenre.parseGenreList("");
		assertNull(llg);

		llg = ParseGenre.parseGenreList("a Adagio");
		assertNull(llg);

		llg = ParseGenre.parseGenreList("Adagio x");
		assertNotNull(llg);

		// Beginn Blank
		llg = ParseGenre.parseGenreList(" Adagio x");
		assertNotNull(llg);

		// Beginn Komma
		llg = ParseGenre.parseGenreList(", Adagio");
		assertNull(llg);
		
		llg = ParseGenre.parseGenreList("Adagio und Fugen x");
		assertEquals(llg.getMatch(), "Adagio und Fugen");
		assertEquals(llg.toString(Numeri.PLURAL), "Adagio und Fugen");
		assertEquals(llg.getRest(), " x");
	}

	@Test
	public void test380() {
		MusicTitle mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Das @Recht des Herrn. Suite Nr. 2");
		GenreList gl = mt.getVersion().getGenreList();
		String gnd380 = TestUtils.getGND3XX(gl);
		assertEquals("Suite", TestUtils.getLinkName(gnd380));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Veni creator spiritus, Motette. Fassung 2000");
		gl = mt.getGenreList();
		gnd380 = TestUtils.getGND3XX(gl);
		assertEquals("Motette", TestUtils.getLinkName(gnd380));

		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Präludien und Fugen, Orch, 1918");
		gl = mt.getGenreList();
		List<Genre> lg = gl.getChildren();
		gnd380 = TestUtils.getGND3XX(lg.get(0));
		assertEquals("Präludium", TestUtils.getLinkName(gnd380));
		gnd380 = TestUtils.getGND3XX(lg.get(1));
		assertEquals("Fuge", TestUtils.getLinkName(gnd380));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Sinfonien, Nr. 4");
		gl = mt.getGenreList();
		gnd380 = TestUtils.getGND3XX(gl);
		assertEquals("Sinfonie", TestUtils.getLinkName(gnd380));

		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Tantum ergo, FWV 58");
		gl = mt.getGenreList();
		gnd380 = TestUtils.getGND3XX(gl);
		assertEquals("Tantum ergo sacramentum",
				TestUtils.getLinkName(gnd380));

	}

}
