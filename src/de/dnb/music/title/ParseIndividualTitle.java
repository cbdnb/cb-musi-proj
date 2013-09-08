package de.dnb.music.title;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.TitleUtils;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.genre.GenreList;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;

public final class ParseIndividualTitle {

	private ParseIndividualTitle() {
	}

	public static void main(String[] args) {
		MusicTitle mt = parse(null, "<>=6");
		System.out.println(TitleUtils.getStructured(mt));
	}

	/* Idee: da vor der unterscheidenden Angabe ", " steht, finde die Kommata 
	 * und schaue nach, ob danach eine zus. Angabe oder ein Gattungsbegriff 
	 * folgt. Für RSWK sieht die Sache anders aus. Hier muss man Wort für Wort 
	 * den String absuchen und hoffen, der Rest sei eine zus. Angabe
	 * 
	 */

//@formatter:off
	// Whitespace oder Kommas
	private static Pattern trennPat = 
			Pattern.compile("\\s*,\\s*" 
							+ 
							"|" 
							+
							"\\s+" 
							)							
							;
	//                    =  \s*|\s*,\s+|\$g		\s = Whitespace
//@formatter:on
	/**
	 * Gibt einen gültigen Individualtitel oder null zurück. Erkennt die 
	 * "unterscheidende Angabe". Diese muss im Falle einer Besetzung, eines 
	 * Form- und Gattungsbegriffs und einer Nummer von Komma-Blank oder $g
	 * eingeleitet werden.
	 * 
	 * @param composer	Komponist
	 * @param s			ein String != null
	 * @return			gültigen Individualtitel oder null, wenn s =""
	 */
	public static IndividualTitle parse(final String composer, final String s) {
		if (s == null)
			throw new IllegalArgumentException(
					"Null-String an parse()übergeben");
		String parseString = s.trim();
		if (parseString.length() == 0)
			return null;
		IndividualTitle indTitle = new IndividualTitle();

		Matcher m = trennPat.matcher(parseString);
		String vor = "";
		String nach = "";
		String match = "";
		// Probiere alles aus, um noch eine gültige Zusatzangabe etc. zu finden
		while (m.find()) {
			int startMatch = m.start();
			int endMatch = m.end();
			vor = parseString.substring(0, startMatch);
			nach = parseString.substring(endMatch);
			match = parseString.substring(startMatch, endMatch);
			// wird benötigt, um völlig falsche Ergebnisse auszuschliessen:
			boolean matchIsValidSeparator =
				match.equals(", ");// || match.equals("$g");

			/*
			 *  Vorgehen nach Regelwerk, wobei das Regelwerk nicht im Text, 
			 *  sondern nur als Beispiel auch die sonstigen Zählungen hat. 
			 *  Daher:  §M505, 6, 1.-4.:
			 */
			// 
			AdditionalInformation zus =
				ParseAdditionalInformation.parse(composer, nach,
						matchIsValidSeparator);
			if (zus != null) {

				indTitle.additionalInformation = zus;
				indTitle.individualTitle = vor;
				return indTitle;
			}

			/*
			 * Besetzung und Gattung haben nach RAK und RSWK ein
			 * führendes Komma zu haben. Wird das nicht beachtet, so
			 * gibt es schreckliche Dinge wie:
			 * 
			 * 	$aKunst der, Fugen
			 * 	$aAllein Gott in der Höh sei$mEhr
			 * 
			 */

			if (matchIsValidSeparator) {

				// 5. Besetzung?
				InstrumentationList lli = ParseInstrumentation.parse(nach);
				if (lli != null && lli.getRest().trim().length() == 0) {
					indTitle.instrumentationList = lli;
					indTitle.individualTitle = vor;
					return indTitle;
				}

				// 6. Form, Gattung
				GenreList gat = ParseGenre.parseGenreList(nach);

				if (gat != null && gat.getRest().trim().length() == 0) {
					indTitle.genreList = gat;
					indTitle.individualTitle = vor;
					return indTitle;
				}
			}

		}

		// keine weiteren Teile gefunden, also
		indTitle.individualTitle = parseString;
		return indTitle;
	}

}
