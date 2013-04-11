package utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import applikationsbausteine.RangeCheckUtils;

public final class StringUtils {

	private StringUtils() {
	}

	public static String removeDollar8(String oldRecord) {
		final Pattern dollar8P =
			Pattern.compile("\\$8[^\\$]+", Pattern.MULTILINE);

		Matcher m = dollar8P.matcher(oldRecord);
		return m.replaceAll("");

	}

	private final static String idPatStr = "!\\d+X?!";

	public static String removeExpansion(String oldRecord) {
		if (oldRecord == null)
			throw new IllegalArgumentException();

		String newRecord;

		/*
		 * "!" ist ein Trick, da gar kein nachfolgendes Unterfeld 
		 * erlaubt ist. ($! gibt es nicht):
		 */
		newRecord = filter(oldRecord, "169", "!");
		newRecord = filter(newRecord, "260", "v");
		newRecord = filter(newRecord, "372", "wZv");

		newRecord = filter(newRecord, "380", "!");
		newRecord = filter(newRecord, "382", "npsv");

		newRecord = filter(newRecord, "5\\d\\d", "vXYZ45");

		newRecord = filter(newRecord, "682", "v");
		newRecord = filter(newRecord, "689", "v");

		return newRecord.toString().trim();

	}

	private static String filter(
			String recordStr,
			String tag,
			String allowedSubs) {
		// <tag> !..!<expansion>($[<allowed>])?.*[\n\r]
		String newRecord = "";
		/*
		 *  liefert die Zeile(n), in denen tag steht, die id kann auch
		 *  später kommen
		 */

		final Pattern linePat =
			Pattern.compile(tag + " " + "[^\\n\\r]*" + idPatStr
				+ "([^\\n\\r]+)");
		Matcher lineMatcher;
		lineMatcher = linePat.matcher(recordStr);
		int realTextStart = 0;
		while (lineMatcher.find()) {
			int realTextEnd = lineMatcher.start(1); // das ist ok, da zweites "!"
			newRecord += recordStr.substring(realTextStart, realTextEnd);
			String restOfLine = lineMatcher.group(1);

			final Pattern allowedDollar =
				Pattern.compile("\\$[" + allowedSubs + "]");
			Matcher mAllowed = allowedDollar.matcher(restOfLine);
			if (mAllowed.find()) {
				// Erlaubt ist alles ab der Fundstelle:
				realTextStart = realTextEnd + mAllowed.start();
			} else
				realTextStart = lineMatcher.end(1);
		}
		// letztes Teil geht bis zum Ende:
		newRecord += recordStr.substring(realTextStart);
		return newRecord;
	}

	/**
	 * Filtert aus einem alten Record einen neuen, indem alle Vorkommen in der
	 * ersten Klammer von filterStr entfernt werden.
	 * 
	 * @param oldRecord
	 * @param filterStr
	 * @return
	 */
	private static
			String
			filter(final String oldRecord, final String filterStr) {
		String newRecord = "";
		Matcher m;
		Pattern filterPat = Pattern.compile(filterStr);
		m = filterPat.matcher(oldRecord);
		int realTextStart = 0;
		while (m.find()) {
			int realTextEnd = m.start(1);
			newRecord += oldRecord.substring(realTextStart, realTextEnd);
			realTextStart = m.end(1);
		}
		newRecord += oldRecord.substring(realTextStart);
		return newRecord;
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
	 * Findet das längste Präfix in parseString, das im Vektor data enthalten 
	 * ist. Die Korrektheit der übergebenen Daten wird nur rudimentär überprüft.
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

	//----------------------------------------------------------------
	public static String gnd2Pica(String pica3) {
		String gnd = pica3;
		if (gnd == null || gnd.length() == 0)
			return gnd;
		if (gnd.charAt(0) != '\n')
			gnd = '\n' + gnd;
		gnd = gnd.replace("\n008 ", "\n004B $a");
		gnd = gnd.replace("\n043 ", "\n042B $a");
		gnd = gnd.replace("\n065 ", "\n042A $a");
		gnd = gnd.replace("\n130 ", "\n022A $a");
		gnd = gnd.replace("\n380 ", "\n032W $a");
		gnd = gnd.replace("\n382 ", "\n032X $a");
		gnd = gnd.replace("\n383 ", "\n032Y $a");
		gnd = gnd.replace("\n384 ", "\n032Z $a");
		gnd = gnd.replace("\n430 ", "\n022@ $a");
		gnd = gnd.replace("\n530 ", "\n022R $a");
		gnd = gnd.replace("\n548 ", "\n060R $a");
		gnd = gnd.replace("\n667 ", "\n050C $a");
		gnd = gnd.replace("\n670 ", "\n050E $a");
		gnd = gnd.replace("\n675 ", "\n050F $a");
		gnd = gnd.replace("\n678 ", "\n050G $a");
		gnd = gnd.replace("\n679 ", "\n050H $a");
		gnd = gnd.replace("\n680 ", "\n050D $a");

		// Erstes ! durch $9 ersetzen
		gnd = gnd.replace("$a!", "$9");
		// Zweites ! weg, wenn es am Ende steht (also keine Expansion erwünscht)
		gnd =
			Pattern.compile("!$", Pattern.MULTILINE).matcher(gnd)
					.replaceAll("");
		// Wenn ein $ (also ein Subfeld) folgt, so muss es verschwinden 
		gnd = gnd.replace("!$", "$");
		// Alle anderen ! stehen innerhalb, also durch $8 ersetzen
		gnd = gnd.replace("!", "$8");

		// jetzt alle $a$, die möglicherweise zuviel sind, korrigieren:
		gnd = gnd.replace("$a$", "$");

		return gnd.substring(1);
	}

	private static String rakKomment = "$vVerweisung nach RAK";

	/**
	 * RAK-Verweisung auf Wunsch des DMA.
	 * @param ansetzungOhneKommentar
	 * @return	$p wird durch $g ersetzt. Dadurch kriegt man eckige Klammern
	 * (<>). Ein Kommentar wird noch angehängt.
	 */
	public static String dma2GND430(String ansetzungOhneKommentar) {
		// Teile von Werken
		ansetzungOhneKommentar = ansetzungOhneKommentar.replace("$p", "$g");
		return "430 " + ansetzungOhneKommentar + rakKomment;
	}

	private static Pattern patYear = null;

	@Deprecated
	public static boolean containsYear(final String s) {
		if (patYear == null)
			patYear = Pattern.compile("\\d\\d\\d\\d");
		Matcher m = patYear.matcher(s);
		return m.find();
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

	/*
	 * Rückt Folgezeilen eines String um anz Tabs nach rechts.
	 */
	public static String einruecken(final String s, final int anz) {
		StringBuffer ein = new StringBuffer("");
		for (int i = 0; i < anz; i++) {
			ein.append("\t");
		}
		String a = "\n" + ein.toString();
		// Ersetze alle "\n" durch "\n\t..."

		//s = s.replaceAll("\n", a);
		return s.replaceAll("\n", a);
	}

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
	 * @param repeat  number of times to repeat delim
	 * @param padChar  character to repeat
	 * @return String with repeated character
	 */
	public static String padding(final int repeat, final char padChar) {
		if (repeat < 0) {
			throw new IndexOutOfBoundsException(
					"Cannot pad a negative amount: " + repeat);
		}
		final char[] buf = new char[repeat];
		Arrays.fill(buf, padChar);
		return new String(buf);
	}

	// $, gefolgt von a,m,n,f,o,p,r,s,v
	private static Pattern patDollar = Pattern.compile("\\$[amnfoprsv]");

	/**
	 * Erkennt alle Unterfelder von 130 ausser $g und $x.
	 * @param contentOfField nicht null.
	 * @return
	 */
	public static boolean containsSubfields(String contentOfField) {
		RangeCheckUtils.assertStringParamNotNullOrWhitespace("contentOfField",
				contentOfField);
		return contentOfField.contains("$")||contentOfField.contains("ƒ");
		
	}

	/**
	 * Zerlegt eingabe 
	 * 
	 * Zerlegt den String contentOfField Unterfelder (ausser $g und $x). Leere
	 * Unterfelder werden ignoriert!
	 * 
	 * @param contentOfField nicht null 
	 * @return Eine Liste mit Unterfeldern, jedes beginnend mit $<Indikator>. 
	 */
	public static
			List<String>
			breakUpIntoSubfields(final String contentOfField) {
		if (contentOfField == null)
			throw new IllegalArgumentException(
					"Null-String an breakUpIntoSubfields()übergeben");
		LinkedList<String> listStr = new LinkedList<String>();
		String withLeadingDollarA = "$a" + contentOfField;
		final Matcher m = patDollar.matcher(withLeadingDollarA);
		int pos1 = 0;
		String subfield;
		while (m.find()) {
			final int pos2 = m.start();
			subfield = withLeadingDollarA.substring(pos1, pos2);
			if (subfield.length() > 2)
				listStr.add(subfield);
			pos1 = pos2;
		}
		subfield = withLeadingDollarA.substring(pos1);
		listStr.add(subfield);

		return listStr;
	}

	/**
	 * Splittet einen Datensatz in eine Liste von Zeilen auf. Leere Zeilen
	 * werden entfernt.
	 * @param record
	 * @return
	 */
	public static List<String> record2Lines(String record) {

		String[] linesArr = record.split("\n");
		List<String> linesList = new LinkedList<String>();

		for (String line : linesArr) {
			line = line.trim();
			if (line.length() != 0)
				linesList.add(line);
		}

		return linesList;
	}

//@formatter:off
	public static String lines2Record(Collection<String> lines) {
		String s = "";
		for (Iterator<String> iterator = lines.iterator(); 
				iterator.hasNext();) {
			s += iterator.next();
			if (iterator.hasNext())
				s += "\n";
		}
		return s;
	}
	//@formatter:on

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

	public static String getIDN(String content) {
		if (content == null)
			throw new IllegalArgumentException(
					"Null-String an getIDN()übergeben");
		String s = content;
		if (s.startsWith("!")) {
			s = s.substring(1);
		} else if (s.startsWith("ƒ9") || s.startsWith("$9")
			|| s.startsWith("Ÿ9")) {
			s = s.substring(2);
		} else if (s.startsWith("&flor;9")) {
			s = s.substring(7);
		}
		final Pattern idnPat = Pattern.compile("^\\d{8,}X?");
		final Matcher m = idnPat.matcher(s);
		if (m.find()) {
			int end = m.end();
			String idn = s.substring(0, end);

			return idn;
		}

		return null;

	}

	public static String concatenate(List<String> sList) {
		if (sList == null)
			throw new IllegalArgumentException("Liste null");
		String s = "";
		for (String string : sList) {
			s += string;
		}
		return s;
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String s = args[0];
		System.out.println(removeExpansion(s));
	}

	/**
	 * Sortiert einen Datensatz nach den Feldern. Dabei bleibt die 
	 * Reihenfolge innerhalb der Felder erhalten.
	 * 
	 * @param titleStrOld nicht null.
	 * 
	 * @return Sortierten Datensatz.
	 */
	public static String sortFields(final String titleStrOld) {
		if (titleStrOld == null)
			throw new IllegalArgumentException();
		/*
		 * Die Tags verweisen auf eine Liste von Inhalten (für wiederhol-
		 * bare Felder):
		 */
		TreeMap<String, List<String>> data =
			new TreeMap<String, List<String>>();
		List<String> fieldList = record2Lines(titleStrOld);

		// umwandeln:
		for (String line : fieldList) {
			Pair<String, String> pair = getTagAndcontent(line);
			String tag = pair.first;
			String content = pair.second;
			List<String> tagLines = data.get(tag);
			if (tagLines == null) {
				tagLines = new LinkedList<String>();
				data.put(tag, tagLines);
			}
			tagLines.add(content);
		}
		// ausgeben:
		String out = "";
		for (Entry<String, List<String>> entry : data.entrySet()) {
			List<String> lines = entry.getValue();
			for (String line : lines) {
				out += entry.getKey() + " " + line + '\n';
			}
		}
		out = out.substring(0, out.length() - 1);
		return out;
	}

}
