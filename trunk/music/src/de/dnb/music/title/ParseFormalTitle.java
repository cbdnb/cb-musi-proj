package de.dnb.music.title;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import utils.TitleUtils;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.genre.GenreList;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;

public final class ParseFormalTitle {

	private ParseFormalTitle() {
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(final String[] args) throws IOException {
		final BufferedReader br =
			new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Datensatz bitte eingeben");
		System.out.println();
		MusicTitle f = parse(null, br.readLine());
		System.out.println(TitleUtils.getFullGND(f, true, true));
	}

	// Gibt den Formalsachtitel oder null zurück
	public static FormalTitle parse(
			final String komponist,
			final String parseString) {
		if (parseString == null)
			throw new IllegalArgumentException(
					"Null-String an parse()übergeben");
		FormalTitle f;
		GenreList gat = ParseGenre.parseGenreList(parseString);

		if (gat == null)
			return null;
		String rest = gat.getRest();
		if (rest == null || rest.trim().length() == 0) {
			f = new FormalTitle();
			f.genreList = gat;
			return f; // Das sollte nach RAK nicht sein, nimmt aber Fälle wie
			// "Werke" oder Einzelfälle wie "Swider: Ave Maria" mit.
			//return null; wäre korrekt nach RAK
		}
		InstrumentationList bes = ParseInstrumentation.parse(rest);

		if (bes != null)
			rest = bes.getRest();
		if (rest.trim().length() == 0) {
			f = new FormalTitle();
			f.genreList = gat;
			f.instrumentationList = bes;
			return f;
		}

		/*
		 * Man kann gefahrlos so tun, als wäre ein Komma erkannt, da
		 * schon eine ganze Menge an Informationen eines Formalsachtitels
		 * erkannt wurde. Fälle wie Psalm 8 sind ja Individualsachtitel
		 */
		boolean leadingComma = true;
		AdditionalInformation zus =
			ParseAdditionalInformation.parse(komponist, rest, leadingComma);

		if (zus == null) // Da war was, konnte aber nicht 
			// erkannt werden.
			return null;

		f = new FormalTitle();
		f.genreList = gat;
		f.instrumentationList = bes;
		f.additionalInformation = zus;
		return f;

	}
}
