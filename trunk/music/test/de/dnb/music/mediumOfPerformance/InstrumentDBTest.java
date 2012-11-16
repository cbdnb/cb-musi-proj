package de.dnb.music.mediumOfPerformance;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.genre.GenreDB;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentDB;

public class InstrumentDBTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testMatchInstrument() {

		InstrumentDB.setRegnognizePopularMusic(false);
		Instrument ins = InstrumentDB.matchInstrument("linke Hd");
		assertEquals(ins.writtenOut, "linke Hand");

		InstrumentDB.setRegnognizePopularMusic(false);
		ins = InstrumentDB.matchInstrument("sax");
		assertNull(ins);

		InstrumentDB.setRegnognizePopularMusic(true);
		ins = InstrumentDB.matchInstrument("sax");
		assertEquals(ins.writtenOut, "Saxophon");

		ins = InstrumentDB.matchInstrument("saxi");
		assertNull(ins);

		ins = InstrumentDB.matchInstrument("");
		assertNull(ins);

		ins = InstrumentDB.matchInstrument("EHr");
		assertEquals(ins.writtenOut, "Englischhorn");

		ins = InstrumentDB.matchInstrument("4hdg");
		assertEquals(ins.writtenOut, "vierhändig");

		ins = InstrumentDB.matchInstrument("Va op. 1");
		assertEquals(ins.abbreviated, "Va");

		try {
			ins = InstrumentDB.matchInstrument(null);
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}

	}

	@Test
	public void testRest() {
		InstrumentDB.setRegnognizePopularMusic(false);
		Instrument ins = InstrumentDB.matchInstrument("linke Hd,");
		assertEquals(ins.writtenOut, "linke Hand");

		assertEquals(ins.rest, ",");

		InstrumentDB.setRegnognizePopularMusic(false);
		ins = InstrumentDB.matchInstrument("linke Hand ");
		assertEquals(ins.writtenOut, "linke Hand");
		assertEquals(ins.rest, " ");

	}

	@Test
	public void testVorzugsbenennung() {
		Instrument i;

		i = InstrumentDB.matchInstrument("Ehr");
		assertEquals("Ehr", i.abbreviated);
		i = InstrumentDB.matchInstrument("EHr");
		assertEquals("Ehr", i.abbreviated);

		i = InstrumentDB.matchInstrument("linke Hand");
		assertEquals("linke Hand", i.abbreviated);
		i = InstrumentDB.matchInstrument("linke Hd");
		assertEquals("linke Hand", i.abbreviated);

		i = InstrumentDB.matchInstrument("4hdg.");
		assertEquals("4hdg.", i.abbreviated);
		i = InstrumentDB.matchInstrument("4hdg");
		assertEquals("4hdg.", i.abbreviated);

		i = InstrumentDB.matchInstrument("3hdg.");
		assertEquals("3hdg.", i.abbreviated);
		i = InstrumentDB.matchInstrument("3hdg");
		assertEquals("3hdg.", i.abbreviated);

		i = InstrumentDB.matchInstrument("6hdg.");
		assertEquals("6hdg.", i.abbreviated);
		i = InstrumentDB.matchInstrument("6hdg");
		assertEquals("6hdg.", i.abbreviated);

		i = InstrumentDB.matchInstrument("8hdg.");
		assertEquals("8hdg.", i.abbreviated);
		i = InstrumentDB.matchInstrument("8hdg");
		assertEquals("8hdg.", i.abbreviated);

		i = InstrumentDB.matchInstrument("10hdg.");
		assertEquals("10hdg.", i.abbreviated);
		i = InstrumentDB.matchInstrument("10hdg");
		assertEquals("10hdg.", i.abbreviated);

		i = InstrumentDB.matchInstrument("Bass-Instr.");
		assertEquals("Bass-Instr.", i.abbreviated);
		i = InstrumentDB.matchInstrument("Baß-Instr.");
		assertEquals("Bass-Instr.", i.abbreviated);

		i = InstrumentDB.matchInstrument("E-Kl");
		assertEquals("E-Kl", i.abbreviated);
		i = InstrumentDB.matchInstrument("E-Kl");
		assertEquals("E-Kl", i.abbreviated);
		i = InstrumentDB.matchInstrument("E-Cel");
		assertEquals("E-Kl", i.abbreviated);
		/*
		 * Nicht nötig, da U-Musik:
		 * 
		i = DBInstrument.matchInstrument("el-cel");	
		System.out.println(i.abgekuerzt);
		assertEquals("el-cel", i.abgekuerzt);
		i = DBInstrument.matchInstrument("el-p");
		assertEquals("el-cel", i.abgekuerzt);
		
		i = DBInstrument.matchInstrument("fr-h");
		i = DBInstrument.matchInstrument("h");
		
		i = DBInstrument.matchInstrument("el-dr");
		i = DBInstrument.matchInstrument("el-perc");
		*/

		i = InstrumentDB.matchInstrument("Mezzosopr");
		assertEquals("Mezzosopr", i.abbreviated);
		i = InstrumentDB.matchInstrument("Msopr");
		assertEquals("Mezzosopr", i.abbreviated);

		i = InstrumentDB.matchInstrument("Sprechst.");
		assertEquals("Sprechst.", i.abbreviated);
		i = InstrumentDB.matchInstrument("Stimme");
		assertEquals("Sprechst.", i.abbreviated);

		i = InstrumentDB.matchInstrument("Singst.");
		assertEquals("Singst.", i.abbreviated);
		i = InstrumentDB.matchInstrument("Singst");
		assertEquals("Singst.", i.abbreviated);

		i = InstrumentDB.matchInstrument("Hmk");
		assertEquals("Hmk", i.abbreviated);
		i = InstrumentDB.matchInstrument("Mundhmk");
		assertEquals("Hmk", i.abbreviated);

	}

	@Test
	public void testPrefix() {
		/*
		 * Einige Instrumente sind Präfix des anderen. Daher muss die 
		 * Konsistenz der Datenbank, insbesondere unter Redesign getestet 
		 * werden.
		 */
		Instrument i;

		i = InstrumentDB.matchInstrument("Va");
		assertEquals("Va", i.abbreviated);
		i = InstrumentDB.matchInstrument("Va d'");
		assertEquals("Va", i.abbreviated);
		i = InstrumentDB.matchInstrument("Va e");
		assertEquals("Va", i.abbreviated);
		i = InstrumentDB.matchInstrument("Va d'amore");
		assertEquals("Va d'amore", i.abbreviated);

		i = InstrumentDB.matchInstrument("Fl");
		assertEquals("Fl", i.abbreviated);
		i = InstrumentDB.matchInstrument("Fl d");
		assertEquals("Fl", i.abbreviated);
		i = InstrumentDB.matchInstrument("Fl e");
		assertEquals("Fl", i.abbreviated);
		i = InstrumentDB.matchInstrument("Fl d'amore");

		i = InstrumentDB.matchInstrument("Viola");
		assertEquals("Va", i.abbreviated);
		assertEquals("Viola", i.writtenOut);
		i = InstrumentDB.matchInstrument("Viola d'");
		assertEquals("Va", i.abbreviated);
		i = InstrumentDB.matchInstrument("Viola da");
		assertEquals("Va", i.abbreviated);
		i = InstrumentDB.matchInstrument("Viola e");
		assertEquals("Va", i.abbreviated);
		i = InstrumentDB.matchInstrument("Viola da gamba");
		assertEquals("Viola da gamba", i.writtenOut);
		i = InstrumentDB.matchInstrument("Viola d'amore");
		assertEquals("Viola d'amore", i.writtenOut);

		i = InstrumentDB.matchInstrument("Ob");
		assertEquals("Ob", i.abbreviated);
		i = InstrumentDB.matchInstrument("Ob d");
		assertEquals("Ob", i.abbreviated);
		i = InstrumentDB.matchInstrument("Ob e");
		assertEquals("Ob", i.abbreviated);
		i = InstrumentDB.matchInstrument("Ob d'amore");
		assertEquals("Ob d'amore", i.abbreviated);

		i = InstrumentDB.matchInstrument("Oboe");
		assertEquals("Oboe", i.writtenOut);
		i = InstrumentDB.matchInstrument("Oboe d");
		assertEquals("Oboe", i.writtenOut);
		i = InstrumentDB.matchInstrument("Oboe e");
		assertEquals("Oboe", i.writtenOut);
		i = InstrumentDB.matchInstrument("Oboe d'amore");
		assertEquals("Oboe d'amore", i.writtenOut);

	}

	@Test
	public void testDatabaseConsistency() {
		for (String[] line : InstrumentDB.DATA) {
			
			assertTrue(line[InstrumentDB.IDN].length() != 0);
			assertTrue(line[InstrumentDB.IDN].trim().equals(
					line[InstrumentDB.IDN]));
			assertFalse(line[InstrumentDB.IDN].contains("-"));
			
			assertTrue(line[InstrumentDB.NID].length() != 0);
			assertTrue(line[InstrumentDB.NID].contains("-"));
			assertTrue(line[InstrumentDB.NID].trim().equals(
					line[InstrumentDB.NID]));
			
			assertTrue(line[InstrumentDB.SWD].length() != 0);
			
			assertTrue(line[InstrumentDB.ABBR].trim().equals(
					line[InstrumentDB.ABBR]));
			assertTrue(line[InstrumentDB.ABBR].length() != 0);
			
			assertTrue(line[InstrumentDB.WR_OUT].trim().equals(
					line[InstrumentDB.WR_OUT]));
			assertTrue(line[InstrumentDB.WR_OUT].length() != 0);

		}
	}

}
