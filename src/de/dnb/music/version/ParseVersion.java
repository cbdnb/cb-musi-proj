package de.dnb.music.version;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.genre.GenreList;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;

public final class ParseVersion {

	private ParseVersion() {
	}

	public static void main(final String[] args) {
		Version version = ParseVersion.parse(null, "a-Moll");
		System.out.println(version.getFallgruppeParagraphM511());

	}

	/**
	 * Gibt eine gültige Fassung oder null zurück. Die Fallgruppe wird in die 
	 * entsprechenden Felder eingetragen.
	 * 
	 * @param composer		beliebig
	 * @param parseString	nicht null
	 * @return				Fassung oder null, wenn nicht den Regeln 
	 * 						entsprechend. Dann kann aber immer noch
	 * 						der Konstruktor aufgerufen werden.
	 */
	public static
			Version
			parse(final String composer, final String parseString) {
		RangeCheckUtils.assertReferenceParamNotNull("parseString", parseString);

		Version version = null;
		// Versuch nach §511  c)

		version = VersionDB.matchVersion(parseString);
		if (version != null) {
			/* 
			 * jetzt kann eine Jahreszahl, eine Besetzung oder etwas
			 * total abstruses (Fall 3) folgen. auf jeden Fall wird davon
			 * ausgegangen, dass eine gültige Fassung vorliegt.
			 */
			version.match = parseString.trim();
			String rest = version.rest.trim();

			InstrumentationList besetzungsliste =
				ParseInstrumentation.parse(rest);
			if (besetzungsliste != null)
				if (besetzungsliste.getRest().trim().length() == 0) {
					version.setInstrumentationList(besetzungsliste);
					return version;
				} else { // Rest vorhanden
					AdditionalInformation zusatzangabe =
						ParseAdditionalInformation.parse(composer,
								besetzungsliste.getRest().trim(), false);
					if (zusatzangabe != null) {
						version.setInstrumentationList(besetzungsliste);
						version.setAdditionalInformation(zusatzangabe);
						return version;
					}
				}
			AdditionalInformation zusatzangabe =
				ParseAdditionalInformation.matchDate(rest);
			if (zusatzangabe != null) {
				version.setAdditionalInformation(zusatzangabe);
				return version;
			}
			// Also easy and dirty: Fall 3:
			return version;
		}

		// Versuch nach §511  b)		
		version = new Version(parseString.trim());

		GenreList gL = ParseGenre.parseGenreList(parseString);
		if (gL != null) {
			version.setGenreList(gL);
			String rest = gL.getRest();
			if (rest == null || rest.trim().length() == 0) {
				// Fall b 1 zurückgeben
				return version;
			}
			//@formatter:off
			InstrumentationList bes = ParseInstrumentation.parse(rest);
			if (bes != null
				&& bes.getChildren().size() != 0
				&& (bes.getRest() == null 
				|| bes.getRest().trim().length() == 0)) {
				// Fall b 2 zurückgeben
				version.setInstrumentationList(bes);
				return version;
			}
			//@formatter:on
			AdditionalInformation zus =
				ParseAdditionalInformation.parse("", rest, false);
			if (zus != null) {
				if (zus instanceof Key) {
					// Fall b3 zurückgeben
					version.setAdditionalInformation(zus);
					return version;
				}
				if (zus instanceof DateOfComposition) {
					// Fall b 4 zurückgeben
					version.setAdditionalInformation(zus);
					return version;
				}
				if (zus instanceof SerialNumber) {
					// Fall b 5 zurückgeben
					version.setAdditionalInformation(zus);
					return version;
				}
			}
		}

		// Versuch nach §511  a)
		/*
		 *  Opus wie in 
		 *  de.dnb.music.additionalInformation.ParseAdditionalInformation.
		 *  	parse(String, String)
		 *  zuerst, um Fehlermeldung auszuschließen.
		 */

		AdditionalInformation addInf =
			ParseAdditionalInformation.matchOpus(parseString.trim());
		if (addInf != null) {
			version.setAdditionalInformation(addInf);
			version.match = parseString.trim();
			return version;
		}
		addInf =
			ParseAdditionalInformation.matchThematicIndex(composer,
					parseString.trim());
		if (addInf != null) {
			version.setAdditionalInformation(addInf);
			version.match = parseString.trim();
			return version;
		}

		// Versuch nach e)
		InstrumentationList instrList = ParseInstrumentation.parse(parseString);
		if (instrList != null && instrList.getRest().trim().length() == 0) {
			version.setInstrumentationList(instrList);
			return version;
		}
		addInf = ParseAdditionalInformation.matchDate(parseString);
		if (addInf != null) {
			version.setAdditionalInformation(addInf);
			return version;
		}

		return null;

	}
}
