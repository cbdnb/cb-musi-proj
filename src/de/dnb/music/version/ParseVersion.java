package de.dnb.music.version;

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
		String s = "Vl Viola";
		Version f = new Version("bla");
		//				parse("", s);
		System.out.println(f.fallgruppeParagraphM511);
		System.out.println(f.untergruppe);
		System.out.println(f.getMatch());

	}

	/*
	 * Gibt eine gültige Fassung oder null zurück. Die Fallgruppe wird in die 
	 * entsprechenden Felder eingetragen.
	 */
	public static
			Version
			parse(final String komponist, final String parseString) {

		if (parseString == null)
			throw new IllegalArgumentException(
					"Null-String an parse()übergeben");

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
					version.instrumentationList = besetzungsliste;
					version.fallgruppeParagraphM511 = 'c';
					version.untergruppe = 2;

					return version;
				} else { // Rest vorhanden
					AdditionalInformation zusatzangabe =
						ParseAdditionalInformation.parse(komponist,
								besetzungsliste.getRest().trim(), false);
					if (zusatzangabe != null) {
						version.instrumentationList = besetzungsliste;
						version.additionalInformation = zusatzangabe;
						version.fallgruppeParagraphM511 = 'c';
						version.untergruppe = 4;
						return version;
					}
				}
			AdditionalInformation zusatzangabe =
				ParseAdditionalInformation.matchDate(rest);
			if (zusatzangabe != null) {
				version.additionalInformation = zusatzangabe;
				version.fallgruppeParagraphM511 = 'c';
				version.untergruppe = 1;
				return version;
			}
			// Also easy and dirty: Fall 3:
			version.fallgruppeParagraphM511 = 'c';
			version.untergruppe = 3;

			return version;

		}

		// Versuch nach §511  b)
		version = new Version(parseString.trim());
		GenreList gat = ParseGenre.parseGenreList(parseString);
		if (gat != null) {

			version.genreList = gat;
			String rest = gat.getRest();

			if (rest == null || rest.trim().length() == 0) {
				// Fall b 1 zurückgeben
				version.fallgruppeParagraphM511 = 'b';
				version.untergruppe = 1;
				return version;
			}
			InstrumentationList bes = ParseInstrumentation.parse(rest);
			if (bes != null
				&& bes.getInstruments().size() != 0
				&& (bes.getRest() == null || bes.getRest().trim().length() == 0)) {
				// Fall b 2 zurückgeben
				version.fallgruppeParagraphM511 = 'b';
				version.untergruppe = 2;
				version.instrumentationList = bes;
				return version;
			}
			AdditionalInformation zus =
				ParseAdditionalInformation.parse("", rest, false);
			if (zus != null) {
				if (zus instanceof Key) {
					// Fall b3 zurückgeben
					version.fallgruppeParagraphM511 = 'b';
					version.untergruppe = 3;
					version.additionalInformation = zus;
					return version;
				}
				if (zus instanceof DateOfComposition) {
					// Fall b 4 zurückgeben
					version.fallgruppeParagraphM511 = 'b';
					version.untergruppe = 4;
					version.additionalInformation = zus;
					return version;
				}
				if (zus instanceof SerialNumber) {
					// Fall b 5 zurückgeben
					version.fallgruppeParagraphM511 = 'b';
					version.untergruppe = 5;
					version.additionalInformation = zus;
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

		AdditionalInformation zusatzangabe =
			ParseAdditionalInformation.matchOpus(parseString.trim());
		if (zusatzangabe != null) {
			version.fallgruppeParagraphM511 = 'a';
			version.untergruppe = 2;
			version.additionalInformation = zusatzangabe;
			version.match = parseString.trim();
			return version;
		}
		zusatzangabe =
			ParseAdditionalInformation.matchThematicIndex(komponist,
					parseString.trim());
		if (zusatzangabe != null) {
			version.fallgruppeParagraphM511 = 'a';
			version.untergruppe = 1;
			version.additionalInformation = zusatzangabe;
			version.match = parseString.trim();
			return version;
		}

		// Versuch nach e)

		InstrumentationList besetzungsliste =
			ParseInstrumentation.parse(parseString);
		if (besetzungsliste != null
			&& besetzungsliste.getRest().trim().length() == 0) {
			version.instrumentationList = besetzungsliste;
			version.fallgruppeParagraphM511 = 'e';
			version.untergruppe = 2;
			return version;
		}
		zusatzangabe = ParseAdditionalInformation.matchDate(parseString);
		if (zusatzangabe != null) {
			version.additionalInformation = zusatzangabe;
			version.fallgruppeParagraphM511 = 'e';
			version.untergruppe = 1;
			return version;
		}

		return null;

	}
}
