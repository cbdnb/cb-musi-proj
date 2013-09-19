package utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.utils.Pair;

public final class StringUtils {

	private StringUtils() {
	}

	/**
	 * Findet das längste Präfix in parseString, das in den durch rows 
	 * angegebenen Spalten einer Matrix enthalten ist und liefert die 
	 * zugehörige Zeile. Die Korrektheit der übergebenen
	 * Daten wird nur rudimentär überprüft.
	 * 
	 * @param parseString nicht null
	 * @param data	String-Matrix nicht null
	 * @param rows	Spaltenindizes >=0. 
	 * @return	den Match und die Zeile des Matches oder null
	 */
	public static Pair<String, String[]> findLongestPrefix(
			final String parseString,
			final String[][] data,
			final int... rows) {
		if (parseString == null)
			throw new IllegalArgumentException("kein String zum Parsen da");
		if (data == null)
			throw new IllegalArgumentException("Übergebene Matrix ist null");
		if (rows == null)
			throw new IllegalArgumentException("keine Spaltenidizes übergeben");

		String[] maxZeile = null;
		String match = null;
		int maxLaenge = 0;

		// schlichte lineare Suche
		for (String[] zeile : data) {
			for (int row : rows) {
				String tableEntry = zeile[row];
				if (matchesPrecisely(tableEntry, parseString)) {
					int entryLength = tableEntry.length();
					if (entryLength > maxLaenge) {
						maxLaenge = entryLength;
						match = tableEntry;
						maxZeile = zeile;
					}
				}
			}
		}

		if (maxLaenge != 0) {
			return new Pair<String, String[]>(match, maxZeile);
		}

		return null;

	}

	/**
	 * Findet das längste Präfix in parseString, das im Vektor prefixes 
	 * enthalten ist. Die Korrektheit der übergebenen Daten wird nur 
	 * rudimentär überprüft.
	 * 
	 * @param parseString nicht null
	 * @param prefixes	String-Vektor nicht null
	 * @return	den Match oder null
	 */
	public static String findLongestPrefix(
			final String parseString,
			final String[] prefixes) {
		if (parseString == null)
			throw new IllegalArgumentException("kein String zum Parsen da");
		if (prefixes == null)
			throw new IllegalArgumentException("Übergebener Vektor ist null");

		String match = null;
		int maxLenghth = 0;

		// schlichte lineare Suche
		for (String prefix : prefixes) {
			if (matchesPrecisely(prefix, parseString)) {
				int prefixLength = prefix.length();
				if (prefixLength > maxLenghth) {
					maxLenghth = prefixLength;
					match = prefix;
				}
			}
		}

		if (maxLenghth != 0) {
			return match;
		}

		return null;
	}

	private static String vorOrdnungsgruppe = null;

	private static String nachOrdnungsgruppe = null;

	/**
	 * Dürfen erst aufgerufen werden, wenn 
	 * containsOrdnungsgruppe aufgerufen wurde.
	 * 
	 * @return
	 */
	public static String getOrdnungsgruppe() {
		return nachOrdnungsgruppe;
	}

	public static String getVorOrdnungsgruppe() {
		return vorOrdnungsgruppe;
	}

	/**
	 * Findet die "Ordnungsgruppe" (Ausw., Arr.).
	 * @param s
	 * @return
	 */
	public static boolean containsOrdnungsgruppe(final String s) {
		final int ordnOffset = 3; // Länge von " / "
		if (s == null)
			return false;
		if (s.length() == 0)
			return false;
		int index = s.indexOf(" / Ausw.");
		if (index < 0)
			index = s.indexOf(" / Arr.");
		if (index < 0)
			return false;
		// was gefunden, also:
		vorOrdnungsgruppe = s.substring(0, index).trim();
		nachOrdnungsgruppe = s.substring(index + ordnOffset).trim();

		return true;

	}

	private static Pattern patKomma = null;

	/**
	 * Splittet nach ", ".
	 * 
	 * Im DMA stehen in der Ornungshilfe (= Teil) oft Kommata, die
	 * Teile von Teilen eines Werks anzeigen sollen (undokumentiert).
	 * Beispiel: Concerti grossi, HWV 319 - 330 <Konzert HWV 324, 3. Satz>
	 * 
	 * @param s		String to split
	 * @return		ArrayList mit gesplitteten Werten oder null
	 */
	public static LinkedList<String> splitPartsOfWork(final String s) {
		if (s == null)
			return null;
		if (s.length() == 0)
			return null;
		if (patKomma == null)
			patKomma = Pattern.compile(", ");
		String[] sa = patKomma.split(s);
		return new LinkedList<String>(Arrays.asList(sa));
	}

	// --------------------------------------------------------------------

	private static String ansetzung, ordnungshilfe, fassung;

	// dürfen erst aufgerufen werden, wenn containsOrdnungshilfe() aufgerufen
	// wurde
	public static String getAnsetzung() {
		return ansetzung;
	}

	public static String getOrdnungshilfe() {
		return ordnungshilfe;
	}

	public static String getFassung() {
		return fassung;
	}

	/*
	 * 		Typischer Fall:
	 * 		"Sinfonien, WAB 103 <2. Satz>. Fassung 1876"
	 * 
	 * 		"2. Satz" ist die sog. Ordnungshilfe
	 */
	public static boolean containsOrdnungshilfe(final String s) {
		int index1, index2;
		if (s == null)
			throw new IllegalArgumentException(
					"Null-String an containsOrdnungshilfe()übergeben");
		index1 = s.lastIndexOf('<');
		if (index1 == -1) {
			ansetzung = ordnungshilfe = fassung = null;
			return false;
		}

		index2 = s.lastIndexOf('>');
		if (index2 <= index1) {
			ansetzung = ordnungshilfe = fassung = null;
			return false;
		}

		ansetzung = s.substring(0, index1);
		ordnungshilfe = s.substring(index1 + 1, index2);
		fassung = s.substring(index2 + 1).trim();
		if (fassung.length() != 0 && fassung.charAt(0) == '.')
			fassung = fassung.substring(1).trim();
		return true;
	}

	/**
	 * überprüft, ob prefix ein "echtes" Präfix von parseString ist. 
	 * Der leere Strin "" wird als nicht matchend angesehen.
	 * 
	 * "Echt" bedeutet, dass nach prefix höchstens noch Leerzeichen, $ oder
	 * Komma kommen dürfen.
	 * @param prefix
	 * @param parseString
	 * @return
	 */
	public static boolean matchesPrecisely(
			final String prefix,
			final String parseString) {
		if (prefix == null || parseString == null)
			throw new IllegalArgumentException("Nullstrings");

		if (prefix.length() == 0)
			return false;

		if (parseString.startsWith(prefix))
			if (parseString.length() == prefix.length()
				|| parseString.charAt(prefix.length()) == ' '
				|| parseString.charAt(prefix.length()) == ','
				|| parseString.charAt(prefix.length()) == '$')
				return true;
		return false;
	}

	//----------------------------------------------------------------

	private static Pattern patKommasBlanks = Pattern.compile("[ ,]+");

	private static String leadingBlanksAndCommas = null;

	/**
	 * Gibt führende Leerzeichen zurück.
	 * 
	 * Darf nur aufgerufen werden, wenn zuvor stripBlanksAndCommas() 
	 * aufgerufen wurde.
	 * 
	 * @return	Mix aus Leerzeichen und Kommas.
	 */
	public static String getLeadingBlanksAndCommas() {
		return leadingBlanksAndCommas;
	}

	public static String stripLeadingBlanksAndCommas(final String s) {
		if (s == null)
			throw new IllegalArgumentException(
					"Null-String an stripLeadingBlanksAndCommas()übergeben");
		Matcher m = patKommasBlanks.matcher(s);
		if (m.lookingAt()) {
			int endIndex = m.end();
			leadingBlanksAndCommas = s.substring(0, endIndex);
			return s.substring(endIndex);
		}
		leadingBlanksAndCommas = "";
		return s;
	}

	

	/**
	 * Liefert ein Paar von Strings. Der erste String ist der Tag im Pica- oder
	 * Pica+-Format, der zweite der Inhalt der Zeile.
	 * 
	 * @param line	Zeile eines Datensatzes.
	 * @return	2 Strings oder null, wenn keine Aufspaltung möglich.
	 */
	public static Pair<String, String> getTagAndcontent(final String line) {
		if (line == null)
			throw new IllegalArgumentException(
					"Null-String an getTagAndcontent()übergeben");
		// 3 bis 7 Zeichen am Anfang gefolgt von Blank:
		final Pattern tagPat = Pattern.compile("^\\S{3,7} ");
		final Matcher m = tagPat.matcher(line);
		if (m.find()) {
			int end = m.end();
			String tag = line.substring(0, end - 1);
			String content = line.substring(end);
			return new Pair<String, String>(tag, content);
		}

		return null;

	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		

	}

}
