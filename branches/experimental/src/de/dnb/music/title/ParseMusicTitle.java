package de.dnb.music.title;

import static de.dnb.music.utils.GNDConstants.TAG_DB;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dnb.basics.applicationComponents.Pair;
import de.dnb.basics.applicationComponents.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Indicator;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineFactory;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.parser.tag.GNDTagDB;
import de.dnb.gnd.parser.tag.Tag;
import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.additionalInformation.Qualifier;
import de.dnb.music.mediumOfPerformance.InstrumentDB;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;
import de.dnb.music.utils.GNDConstants;
import de.dnb.music.utils.StringUtils;
import de.dnb.music.version.ParseVersion;
import de.dnb.music.version.Version;
import de.dnb.music.visitor.TitleElement;

/**
 * Zentrale Parserklasse. Verschiedene statische Dienstfunktionen werden
 * angeboten.
 * @author baumann
 *
 */
public final class ParseMusicTitle {

	private ParseMusicTitle() {
	}

	/**
	 * Liefert einen Titel mit eventueller Fassung (erkennbar an ". ") oder
	 * null
	 * 
	 * @param composer Komponist
	 * @param titleString	zugehöriger Titel
	 * @return MusicTitel oder null.
	 */
	public static MusicTitle parseTitlePlusVersion(
			final String composer,
			final String titleString) {
		if (titleString == null)
			throw new IllegalArgumentException(
					"Null-String an parseTitlePlusVersion()übergeben");

		final String parseString = titleString.trim();
		if (parseString.length() == 0)
			return null;

		Pair<String, Version> pair =
			splitTitlePlusVersion(composer, parseString);
		if (pair == null)
			return ParseMusicTitle.parseSimpleTitle(composer, parseString);
		else {
			final MusicTitle mt =
				ParseMusicTitle.parseSimpleTitle(composer, pair.first);
			mt.version = pair.second;
			return mt;
		}
	}

	/**
	 * Liefert ein Paar aus Präfix und Fassung (erkennbar an ". ") 
	 * oder null. Das ist unabhängig von RAK oder RSWK.
	 * 
	 * @param composer	Komponist oder null.
	 * @param titleString	Werktitel als String nicht null.
	 * @return ein Paar aus Präfix und Fassung  
	 * Wird keine Fassung erkannt: null.
	 */
	public static Pair<String, Version> splitTitlePlusVersion(
			final String composer,
			final String titleString) {
		if (titleString == null)
			throw new IllegalArgumentException(
					"Null-String an splitTitlePlusVersion()übergeben");
		final String parseString = titleString.trim();
		if (parseString.length() == 0)
			return null;

		/*
		 *  Idee: parseString nach allen ". " splitten und die beiden Teile an
		 *  ParseVersion.parse() übergeben.
		 */
		final Pattern versionSepPat = Pattern.compile("[^\\.]\\. ");
		// kein Punkt, Punkt, Blank
		final Matcher m = versionSepPat.matcher(parseString);
		String vor = "";
		String nach = "";
		while (m.find()) {
			final int pos = m.end();
			vor = parseString.substring(0, pos - 2);
			nach = parseString.substring(pos);
			/*
			 * Fälle wie 4hdg., Sopran-Instr., Frauenst. ausschließen
			 */
			if (vor.endsWith("hdg") || vor.endsWith("nstr")
				|| vor.endsWith("Frauenst") || vor.endsWith("Männerst")
				|| vor.endsWith("Sprechst") || vor.endsWith("Singst")
				|| vor.endsWith(" op") || vor.endsWith("Op")
				|| vor.endsWith("Anh")) {
				continue;
			}
			final Version version = ParseVersion.parse(composer, nach);
			if (version != null) {
				return new Pair<String, Version>(vor, version);
			}
		}
		return null;

	}

	/**
	 * Flexibelste Methode.
	 * 
	 * Erkennt den vollständigen Titel nach RAK, aber auch nach RSWK und GND.
	 * Parst nach Werkteil (in <>), Fassung und Ordnungsgruppe ("Arr.")
	 * 
	 * Diese vollständige Version ist nur für Fremddaten nötig, da in DNB
	 * Werkteile immer in Unterfeldern vorliegen und Ordnungsgruppen
	 * nicht in Normdaten verwendet werden.
	 * 
	 * @param composer		Zur Überprüfung des korrekten WVs
	 * @param parseString	Zu untersuchend, nicht null
	 * @return				Gültiger Musiktitel oder null.
	 */
	public static MusicTitle parse(
			final String composer,
			final String parseString) {
		RangeCheckUtils.assertReferenceParamNotNull("parseString", parseString);

		String ansetzung = parseString.trim();
		if (ansetzung.length() == 0)
			return null;

		// Erkennen, ob schon eine GND-Form vorliegt:
		try {
			factory730.load(ansetzung);
			List<Subfield> subfields = factory730.getSubfieldList();
			/*
			 * - wurden mehr als ein Unterfeld erkannt?
			 * - wurde ein Unterfeld erkann, das aber mit $a anfängt?
			 */
			if (subfields.size() != 1
				|| !subfields.get(0).getContent().equals(ansetzung)) {
				Pair<MusicTitle, List<Subfield>> pair =
					parseGND(composer, subfields);
				return pair.first;
			}
		} catch (Exception e) {
			// nix
		}
		MusicTitle mt = null;
		String ordnungsgruppe = null; // / Arr.
		String partsString = null;
		String verString = null; // Fassung

		if (StringUtils.containsOrdnungsgruppe(ansetzung)) {
			// Ordnungsgruppe / Arr. etc. extrahieren
			ansetzung = StringUtils.getVorOrdnungsgruppe();
			ordnungsgruppe = StringUtils.getOrdnungsgruppe();
		}

		/*
		 * Die Abfrage auf die Länge der Ordnungshilfe ist nötig, da einige
		 * verrückte Titel wie "<>=6" vorkommen!
		 */
		if (StringUtils.containsOrdnungshilfe(ansetzung)
			&& !StringUtils.getOrdnungshilfe().trim().isEmpty()) {
			/*
			 *  Also enthält der Parsetring die sogenannte Ordnungshilfe <...>.
			 *  Diese enthält in der Regel einen Werkteil. Um zu vermeiden, 
			 *  dass eine Jahreszahl (häufigster Fall) drinsteht, wird das 
			 *  zunächst untersucht:
			 */

			partsString = StringUtils.getOrdnungshilfe();
			final AdditionalInformation z =
				ParseAdditionalInformation.matchDate(partsString);
			if (z == null) { // -> keine Jahreszahl

				ansetzung = StringUtils.getAnsetzung();
				verString = StringUtils.getFassung();

				// Die Fassung bekommen wir ja gratis:
				mt = ParseMusicTitle.parseSimpleTitle(composer, ansetzung);
				// Sollte nicht vorkommen
				if (mt == null)
					return null;
				//	jetzt noch Werkteile und Fassung einfügen:
				if (partsString != null && partsString.trim().length() != 0) {
					final PartOfWork wt = new PartOfWork(partsString);
					mt.setPartOfWork(wt);
				}

				if (verString != null && verString.trim().length() != 0) {
					Version ver = ParseVersion.parse(composer, verString);
					if (ver == null) {
						ver = new Version(verString);
					}
					mt.setVersion(ver);
				}
			} else { // -> Jahreszahl (seltsam), also muss Fassung möglicher-
				// weise noch extrahiert werden.
				mt = ParseMusicTitle.parseTitlePlusVersion(composer, ansetzung);
				if (mt == null)
					return null;
			}

		} else { // -> !Stringfunktionen.containsOrdnungshilfe(ansetzung))
			//also müssen wir die Information über ev. Fassung selbst gewinnen:
			mt = ParseMusicTitle.parseTitlePlusVersion(composer, ansetzung);
			if (mt == null)
				return null;
		}

		if (ordnungsgruppe != null) {
			mt.setArrangement(new Arrangement(ordnungsgruppe));
		}
		return mt;
	}

	/**
	 * Erkennt reine Musiktitel ohne Werkteil und ohne Fassung.
	 * 
	 * @param composer	Komponist
	 * @param titleString	Werktitel als String nicht null.
	 * @return IMMER einen gültigen Werktitel nicht null.
	 */
	public static MusicTitle parseSimpleTitle(
			final String composer,
			final String titleString) {
		RangeCheckUtils.assertReferenceParamNotNull("titleString", titleString);
		MusicTitle m = null;
		m = ParseFormalTitle.parse(composer, titleString);
		if (m != null)
			return m;
		else
			return ParseIndividualTitle.parse(composer, titleString);
	}

	/**
	 * Weist den Parser an, auch Instrumente der U-Musik (kleingeschrieben)
	 * zu erkennen. In der Regel unnötig, daher wird als Default Nichts 
	 * erkannt.
	 * 
	 * @param recognize true -> erkenne U-Musik-Instrument.
	 */
	public static void setRegnognizePopularMusic(final boolean recognize) {
		InstrumentDB.setRegnognizePopularMusic(recognize);
	}

	/**
	 * Weist den Parser an, auch Tonbuchstaben als Tonart
	 * zu erkennen. In der Regel unnötig, daher wird als Default Nichts 
	 * erkannt.
	 * 
	 * @param recognize true -> erkenne Tonbuchstaben.
	 */
	public static void setRegnognizeKeyName(final boolean recognize) {
		Key.setRegnognizeKeyName(recognize);
	}

	/**
	 * Parst eine Zeile.
	 * 
	 * @param composer	egal
	 * @param line		nicht null, korrekter Tag
	 * @return			Paar aus Musiktitel und nicht verwendeten
	 * 					Unterfeldern. Wenn alle
	 * 					Unterfelder ungenutzt sind, kann der Musiktitel 
	 * 					null sein. Ist das erste nutzbare Unterfeld kein $a 
	 * 					(z.B. [$mVl, $f 1900]"), so wird eine 
	 * 					{@link IllegalArgumentException} geworfen.			
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Pair<MusicTitle, List<Subfield>> parseGND(
			final String composer,
			final Line line) {
		RangeCheckUtils.assertReferenceParamNotNull("line", line);
		Tag tag = line.getTag();
		boolean tagOK = (tag == GNDConstants.TAG_130);
		tagOK |= tag == GNDConstants.TAG_430;
		tagOK |= tag == GNDConstants.TAG_530;
		tagOK |= tag == GNDConstants.TAG_730;
		if (!tagOK)
			throw new IllegalArgumentException("tag von line gehört nicht"
				+ "zu einem Titel");
		List<Subfield> subfields = line.getSubfields();
		return parseGND(composer, subfields);
	}

	/**
	 * Baut aus GND-Unterfeldliste eine Baumstruktur auf. Die
	 * nicht erkannten Felder werden als zweiter Bestandteil
	 * zurückgegeben.
	 * 
	 * @param composer Komponist
	 * @param subfields	Liste von Unterfeldern
	 * @return	Paar aus Musiktitel und den ungenutzten Unterfeldern. Wenn alle
	 * 			Unterfelder ungenutzt sind, kann der Musiktitel null sein.
	 * 			Ist das erste nutzbare Unterfeld kein $a 
	 * 			(z.B. [$mVl, $f 1900]"), so wird eine 
	 * 			{@link IllegalArgumentException} geworfen.
	 *  
	 */
	public static Pair<MusicTitle, List<Subfield>> parseGND(
			final String composer,
			final List<Subfield> subfields) {
		RangeCheckUtils.assertCollectionParamNotNullOrEmpty("subfields",
				subfields);
		List<Subfield> unused = new LinkedList<Subfield>();

		MusicTitle mTitle = null;

		for (Subfield subfield : subfields) {
			TitleElement element = parseSubfield(composer, subfield);
			// wird nicht erkannt, also zu unused hinzufügen:
			if (element == null) {
				unused.add(subfield);
				continue;
			}
			// also nicht null
			if (mTitle == null) {
				try {
					mTitle = (MusicTitle) element;
				} catch (ClassCastException e) {
					throw new IllegalArgumentException(
							"Liste fängt nicht mit $a an");
				}
				continue;
			}
			element.addToTitle(mTitle);
		}
		return new Pair<MusicTitle, List<Subfield>>(mTitle, unused);
	}

	/**
	 * Liefert zum Unterfeld das passende Titel-Element.
	 * 
	 * Dabei können mehrere Möglichkeiten auftreten:
	 * 	- Altdaten mit $p und $s. Diese stammen vom DMA.
	 * 	- Altdaten mit mehreren $p. Diese stammen aus der SWD.
	 * 	- Altdaten mit einem $p. Diese können aus der SWD oder dem DMA stammen.
	 * 	All diese Fälle werden durch die besondere Behandlung von $p 
	 * 	aufgefangen. (s. if-Anweisung)
	 *  - Altdaten mit $g. Diese können nur aus der SWD stammen.
	 *    	Daher wird $g entweder als Jahreszahl erkannt und in $f 
	 *    	umgewandelt oder aber so, wie es ist, belassen. 
	 *    	In diesem zweiten Fall muss	aber der Titel ($a oder $p) ein 
	 *    	Individualtitel sein!
	 *  - Maschinell modifizierte Altdaten. Diese enthalten nur ein $g, wenn
	 *  	ein Individualtitel vorliegt.
	 *  - Neudaten enthalten nur dann ein $g, wenn ein Individualsachtitel
	 *  	vorliegt. Das ist vor allem bei anonymen Werken der Fall. 
	 *  	In Formalsachtiteln der Neudaten hat ein $g nichts zu suchen, 
	 *  	insbesondere sind Bildungen wie $aAdagio$mVl$g1234 unsinnig.
	 * 
	 * @param composer	auch null möglich
	 * @param subfield	nicht null
	 * @return			Unterfeld oder null, wenn zu keinem Titelelement
	 * 					gehörig (z.B. $v)
	 */
	public static TitleElement parseSubfield(
			final String composer,
			final Subfield subfield) {
		RangeCheckUtils.assertReferenceParamNotNull("subfield", subfield);
		final Indicator indicator = subfield.getIndicator();
		final String contentOfSubfield = subfield.getContent();
		if (indicator == GNDConstants.DOLLAR_a) {
			return ParseMusicTitle.parse(composer, contentOfSubfield);
		} else if (indicator == GNDConstants.DOLLAR_m) {
			return ParseInstrumentation.parse(contentOfSubfield);
		} else if (indicator == GNDConstants.DOLLAR_f
			|| indicator == GNDConstants.DOLLAR_n
			|| indicator == GNDConstants.DOLLAR_r
			|| indicator == GNDTagDB.DOLLAR_G) {
			/*
			 * Auch hier können wir wieder so tun, als wäre ein
			 * Komma erkannt, da ja nicht (gegenbenenfalls) ein
			 * $n mit einer Einzelzahl aufgebaut worden wäre.
			 */
			final boolean comma = true;
			//				final AdditionalInformation ai =
			TitleElement element =
				ParseAdditionalInformation.parse(composer, contentOfSubfield,
						comma);
			// Ist das $g ein maskiertes $f, $n oder $r?
			if (element == null)
				element = new Qualifier(contentOfSubfield);
			return element;
		} else if (indicator == GNDConstants.DOLLAR_p) {

			/*
			 *  neue Werkteilstrukur anlegen, dabei davon ausgehen,
			 *  dass auch vollkommen Unstrukturiertes (Altdaten?) in
			 *  $p steht, also auch Teile von Teilen. Daher wird der
			 *  Konstruktor PartOfWork(String) aufgerufen:
			 */
			return new PartOfWork(contentOfSubfield);

		} else if (indicator == GNDConstants.DOLLAR_s) {
			TitleElement element =
				ParseVersion.parse(composer, contentOfSubfield);
			// Notbremse für unbekannten Inhalt von $s:
			if (element == null) {
				element = new Version(contentOfSubfield);
			}
			return element;
		} else if (indicator == GNDConstants.DOLLAR_o) {
			return new Arrangement(contentOfSubfield);
		} else {
			return null;
		}
	}

	// reichhaltigste:
	private static LineFactory factory730 = TAG_DB.findTag("730")
			.getLineFactory();

	/**
	 * Parst GND. Die Zeile oder ihr Inhalt wird als String übergeben.
	 * 
	 * @param parseString 	nicht null, nicht leer, beginnend mit einem
	 * 						korrekten Tag (130, 430, 530, 730) oder auch
	 * 						nur der Inhalt der Zeile. 
	 * @param composer	Komponist oder null 
	 * @return	gültigen Musiktitel oder null.
	 */
	public static MusicTitle parseGND(
			final String composer,
			final String parseString) {
		RangeCheckUtils.assertStringParamNotNullOrWhitespace("parseString",
				parseString);
		Line line = null;
		try {
			line = LineParser.parse(parseString, TAG_DB, false);
		} catch (IllFormattedLineException e) {
			// noch nicht alles verloren: es liegt nur der Inhalt vor.
		}
		if (line == null) {
			try {
				factory730.load(parseString);
				line = factory730.createLine();
			} catch (IllFormattedLineException e) {
				System.err.println("hier");
				return null;
			}
		}
		final MusicTitle mt = ParseMusicTitle.parseGND(null, line).first;
		return mt;
	}

	/**
	 * @param args nichts.
	 * @throws IOException 
	 * @throws IllFormattedLineException 
	 */
	public static void main(final String[] args)
			throws IOException,
			IllFormattedLineException {

		MusicTitle title = parse(null, "Adagio Kl A-Dur");
		System.out.println(title);

	}

}
