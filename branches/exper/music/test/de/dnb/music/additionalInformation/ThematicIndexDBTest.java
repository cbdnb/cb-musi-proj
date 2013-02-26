package de.dnb.music.additionalInformation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ThematicIndexDBTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDatabaseConsistency() {
		for (String[] line : ThematicIndexDB.DATA) {

			assertTrue(line[ThematicIndexDB.IDN].length() >= 9);
			assertTrue(line[ThematicIndexDB.IDN].trim().equals(
					line[ThematicIndexDB.IDN]));
			assertFalse(line[ThematicIndexDB.IDN].contains("-"));
			assertTrue(Character.isDigit(line[ThematicIndexDB.IDN].charAt(0)));

			assertTrue(line[ThematicIndexDB.COUNTRY_CODE].length() != 0);
			assertTrue(line[ThematicIndexDB.COUNTRY_CODE].trim().equals(
					line[ThematicIndexDB.COUNTRY_CODE]));
			assertTrue(line[ThematicIndexDB.COUNTRY_CODE].charAt(0) == 'X');
			assertTrue(line[ThematicIndexDB.COUNTRY_CODE].charAt(2) == '-');

			assertTrue(line[ThematicIndexDB.NAME].length() != 0);
			assertTrue(line[ThematicIndexDB.NAME].trim().equals(
					line[ThematicIndexDB.NAME]));

			assertTrue(line[ThematicIndexDB.THEMATIC_INDEX].length() != 0);
			assertTrue(line[ThematicIndexDB.THEMATIC_INDEX].trim().equals(
					line[ThematicIndexDB.THEMATIC_INDEX]));

			//			assertTrue(line[ThematicIndexDB.SOURCE].length() != 0);

			assertTrue(
					line[ThematicIndexDB.SOURCE],
					line[ThematicIndexDB.SOURCE].trim().equals(
							line[ThematicIndexDB.SOURCE]));

			assertTrue(line[ThematicIndexDB.SOURCE_ABB].length() != 0);

		}
	}

}
