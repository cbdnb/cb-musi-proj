package de.dnb.music.title;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.genre.GenreList;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;
import de.dnb.music.utils.TitleUtils;

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
		GenreList genres = ParseGenre.parseGenreList(parseString);

		if (genres == null)
			return null;
		String rest = genres.getRest();
		if (rest == null || rest.trim().length() == 0) {
			f = new FormalTitle();
			f.genreList = genres;
			return f; // Das sollte nach RAK nicht sein, nimmt aber Fälle wie
			// "Werke" oder Einzelfälle wie "Swider: Ave Maria" mit.
			//return null; wäre korrekt nach RAK
		}
		InstrumentationList instruments = ParseInstrumentation.parse(rest);

		if (instruments != null)
			rest = instruments.getRest();
		if (rest.trim().length() == 0) {
			f = new FormalTitle();
			f.genreList = genres;
			f.instrumentationList = instruments;
			return f;
		}

		/*
		 * Man kann gefahrlos so tun, als wäre ein Komma erkannt, da
		 * schon eine ganze Menge an Informationen eines Formalsachtitels
		 * erkannt wurde. Fälle wie Psalm 8 sind ja Individualsachtitel
		 */
		boolean leadingComma = true;
		AdditionalInformation addInf =
			ParseAdditionalInformation.parse(komponist, rest, leadingComma);

		if (addInf == null) // Da war was, konnte aber nicht 
			// erkannt werden.
			return null;

		f = new FormalTitle();
		f.genreList = genres;
		f.instrumentationList = instruments;
		f.additionalInformation = addInf;
		return f;

	}
}
