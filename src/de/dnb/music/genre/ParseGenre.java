package de.dnb.music.genre;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Diese Klasse nimmt an, dass Form- und Gattungsbegriffe auch mehrfach,
 * durch "," und "und" verbunden auftreten können. RAK sieht zur Zeit solche
 * verbundenen Begriffe als Indiviuelle Benennung an. Es ist aber wegen der
 * Relationierung der GND sinnvoll, diese einzeln zu erkennen.
 * 
 * @author baumann
 *
 */
public final class ParseGenre {

	private ParseGenre() {
	}

//@formatter:off
	/**
	 *  mögliche führende Blanks gefolgt von "," , "und " oder "mit ".
	 */
	private static String patString = "^\\s*, *" + 
									  "|"	+ 
									  "^\\s*und +" +
									  "|"	+ 
									  "^\\s*mit +";
									
//							(\s = Whitespace, ^ = Anfang) 
	
	private static Pattern 
		pSeparator = Pattern.compile(patString);
	
//@formatter:on

	/**
	 * Liefert eine Liste von Gattungen, verpackt in einem GenreList-Objekt.
	 * 
	 * Jedes Genre enthält in g.match den matchenden String, ab Position 2 mit 
	 * führenden Kommas, Blanks und "und" (vergleiche dazu pSeparator). 
	 * Der Match (GenreList.getMatch()) enthält alle erkannten
	 * Zeichen, also kein abschließendes Komma.
	 * In g.rest befindet sich logischerweise der noch nicht erkannte Rest. 
	 * Dieser kann mit einem Komma beginnen. 
	 * 
	 * @param s		darf mit Blanks, nicht aber mit einem Komma beginnen.
	 * @return		ein gültiges GenreList-Objekt oder null
	 */
	public static GenreList parseGenreList(final String s) {

		if (s == null)
			throw new IllegalArgumentException(
					"Null-String an parseGenreList()Übergeben");

		if (s.length() == 0)
			return null;

		// Führende Blanks
		String parseString = s.trim();

		Genre g = GenreDB.matchGenre(parseString);
		if (g == null)
			return null;

		// -> es wurde was gefunden

		LinkedList<Genre> gg = new LinkedList<Genre>();
		gg.add(g);

		g = parseSeparatorPlusGenre(g.rest);

		while (g != null) {
			gg.add(g);
			g = parseSeparatorPlusGenre(g.rest);
		}

		return new GenreList(gg);

	}

	public static Genre parseSeparatorPlusGenre(final String parseString) {

		if (parseString == null)
			throw new IllegalArgumentException(
					"Null-String an parseSeparatorPlusGenre()Übergeben");

		if (parseString.length() == 0)
			return null;

		// ein Trennwort MUSS vorhanden sein.
		Matcher m = pSeparator.matcher(parseString);
		if (!m.find())
			return null;

		String trenner = m.group();

		String rest = parseString.substring(m.end());

		// trim() nicht nötig, da Pattern beliebig viele Leerzeichen matcht.
		if (rest.equals(""))
			return null;

		// -> es kommen noch Zeichen:
		Genre g = GenreDB.matchGenre(rest);
		if (g == null)
			return null;
		// den trenner dem Match zuschlagen:
		g.match = trenner + g.match;

		return g;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		GenreList llg = parseGenreList("    Präludium mit Fuge");

	}

}
