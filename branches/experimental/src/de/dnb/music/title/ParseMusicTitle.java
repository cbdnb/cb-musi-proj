package de.dnb.music.title;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Pair;
import utils.StringUtils;
import utils.TitleUtils;

import de.dnb.gnd.parser.Indicator;
import de.dnb.gnd.parser.Tag;
import de.dnb.gnd.parser.TagDB;
import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.mediumOfPerformance.InstrumentDB;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;
import de.dnb.music.version.ParseVersion;
import de.dnb.music.version.Version;

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
			return ParseMusicTitle.parseWithoutVersion(composer, parseString);
		else {
			final MusicTitle mt =
				ParseMusicTitle.parseWithoutVersion(composer, pair.first);
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
	 * Parst nach Werkteil (in <>), Fassung und Ordnungsgruppe ("Arr.")
	 * 
	 * Diese vollständige Version ist nur für Fremddaten nötig, da in DNB
	 * Werkteile immer in Unterfeldern vorliegen und Ordnungsgruppen
	 * nicht in Normdaten verwendet werden.
	 * 
	 * @param composer		Zur Überprüfung des korrekten WVs
	 * @param parseString	Zu untersuchend, nicht null
	 * @return				Gültiger Musiktitel.
	 */
	public static MusicTitle parse(
			final String composer,
			final String parseString) {
		if (parseString == null)
			throw new IllegalArgumentException(
					"Null-String an parse()übergeben");

		String ansetzung = parseString.trim();
		if (ansetzung.length() == 0)
			return null;

		// Erkennen, ob schon eine GND-Form (ausser $g) vorliegt:
		if (StringUtils.containsSubfields(ansetzung))
			return parseGND(composer, ansetzung);

		MusicTitle mt = null;
		String ordnungsgruppe = null; // / Arr.
		String werkteilString = null;
		String fasString = null;

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
			&& StringUtils.getOrdnungshilfe().trim().length() > 0) {
			/*
			 *  Also enthält der Parsetring die sogenannte Ordnungshilfe <...>.
			 *  Diese enthält in der Regel einen Werkteil. Um zu vermeiden, 
			 *  dass eine Jahreszahl (häufigster Fall) drinsteht, wird das 
			 *  zunächst untersucht:
			 */

			werkteilString = StringUtils.getOrdnungshilfe();
			final AdditionalInformation z =
				ParseAdditionalInformation.matchDate(werkteilString);
			//err.println(z.strukturiert());
			if (z == null) { // -> keine Jahreszahl

				ansetzung = StringUtils.getAnsetzung();
				fasString = StringUtils.getFassung();

				// Die Fassung bekommen wir ja gratis:
				mt = ParseMusicTitle.parseWithoutVersion(composer, ansetzung);
				// Sollte nicht vorkommen
				if (mt == null)
					return null;
				//	jetzt noch Werkteile und Fassung einfügen:
				if (werkteilString != null
					&& werkteilString.trim().length() != 0) {
					final PartOfWork wt = new PartOfWork(werkteilString);
					mt.setPartOfWork(wt);
				}

				if (fasString != null && fasString.trim().length() != 0) {
					Version fas = ParseVersion.parse(composer, fasString);
					if (fas == null) {
						fas = new Version(fasString);
					}
					mt.setVersion(fas);
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
	public static MusicTitle parseWithoutVersion(
			final String composer,
			final String titleString) {
		if (titleString == null)
			throw new IllegalArgumentException(
					"Nullstring in parseWithoutVersion");
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
	 * Baut aus GND-Unterfeldliste eine Baumstruktur auf.
	 * 
	 * Dabei können mehrere Möglichkeiten auftreten:
	 * 	- Altdaten mit $p und $s. Diese stammen vom DMA.
	 * 	- Altdaten mit mehreren $p. Diese stammen aus der SWD.
	 * 	- Altdaten mit einem $p. Diese können aus der SWD oder dem DMA stammen.
	 * 	All diese Fälle werden durch die besondere Behandlung von $p 
	 * 	aufgefangen. (s. case-Anweisung)
	 *  - Altdaten mit $g. Diese können nur aus der SWD stammen. 
	 *  	Die Altdaten enthalten keine Unterfelder $f,$m,$n ..
	 *  	Daher kann das $g nur Bestandteil eines $a- oder $p-Feldes sein.
	 *  	In das Aufteilen durch breakUpIntoSubfields() wird das $g nicht
	 *  	einbezogen. Daher wird $g (in case 'a': oder evtl. case 'p')
	 *  	entweder als Jahreszahl erkannt und in $f umgewandelt oder	
	 *  	aber so, wie es ist, belassen. In diesem zweiten Fall wird 
	 *  	aber der Titel ($a oder $p) als Individualtitel angesehen. 
	 *  - Maschinell modifizierte Altdaten. Diese enthalten nur ein $g, wenn
	 *  	ein Individualtitel vorliegt.
	 *  - Neudaten enthalten nur dann ein $g, wenn ein Individualsachtitel
	 *  	vorliegt. Das ist vor allem bei anonymen Werken der Fall. 
	 *  	In Formalsachtiteln der Neudaten hat ein $g nichts zu suchen, 
	 *  	insbesondere sind Bildungen wie $aAdagio$mVl$g1234 unsinnig.
	 *  	Die Behandlung der case-Anweisung bei $m würde hier das $g-Feld 
	 *  	vernachlässigen, da ParseInstrumentation das $g-Feld ignoriert.
	 *  	Das ist nach dem zuvor Gesagten hinnehmbar.
	 * 
	 * @param composer Komponist
	 * @param subfields	Liste von Unterfeldern, die mit $<Indikator> beginnen.
	 * @return	gültigen Musiktitel oder null
	 * 
	 */
	@SuppressWarnings("null")
	// wegen partOfWork.addPartOfWork() in case 'p':
			public static
			MusicTitle
			parseGND(final String composer, final List<String> subfields) {
		if (subfields == null || subfields.size() == 0)
			throw new IllegalArgumentException(
					"parseGND(): subfields ist null oder hat Länge 0");
		MusicTitle mTitle = null;
		/*
		 *  Zeigt auf das Werk, das gerade mit Unterfeldern aufgefüllt wird.
		 *  Das kann das übergeordnete Werk, aber auch der Werkteil sein. 
		 *  Im case-Fall $p wird dieser Zeiger weitergerückt. 
		 */
		MusicTitle actualPartTitle = null;
		PartOfWork partOfWork = null;

		for (String subfield : subfields) {
			final char indicator = subfield.charAt(1);
			final String contentOfSubfield = subfield.substring(2);

			switch (indicator) {

			case 'a':
				/*
				 * Das ist (garantiert) immer das erste Unterfeld und damit
				 * der erste behandelte Fall in der Schleife. Sollten
				 * Altdaten vorliegen, kann höchstens eine Fassung in diesem
				 * Feld vorkommen. Daher parseTitlePlusVersion():
				 */
				mTitle = ParseMusicTitle.parse(composer, contentOfSubfield);
				actualPartTitle = mTitle;
				break;
			case 'm':
				final InstrumentationList iList =
					ParseInstrumentation.parse(contentOfSubfield);
				if (!actualPartTitle.containsInstrumentation()) {
					actualPartTitle.setInstrumentationList(iList);
				} else {
					actualPartTitle.getInstrumentationList().addAll(iList);
				}
				break;
			case 'f': // Jahr
			case 'n': // op. oder Zählung
			case 'r': // Tonart
				/*
				 * Auch hier können wir wieder so tun, als wäre ein
				 * Komma erkannt, da ja nicht (gegenbenenfalls) ein
				 * $n mit einer Einzelzahl aufgebaut worden wäre.
				 */
				final boolean comma = true;
				final AdditionalInformation ai =
					ParseAdditionalInformation.parse(composer,
							contentOfSubfield, comma);
				actualPartTitle.setAdditionalInformation(ai);
				break;

			//------------
			case 'p':
				if (!mTitle.containsParts()) {
					/*
					 *  neue Werkteilstrukur anlegen, dabei davon ausgehen,
					 *  dass auch vollkommen Unstrukturiertes (Altdaten?) in
					 *  $p steht, also auch Teile von Teilen. Daher wird der
					 *  Konstruktor PartOfWork(String) aufgerufen:
					 */
					partOfWork = new PartOfWork(contentOfSubfield);
					mTitle.setPartOfWork(partOfWork);
					final List<MusicTitle> tList = partOfWork.getPartsOfWork();
					final int partsSize = tList.size();
					actualPartTitle = tList.get(partsSize - 1);
				} else {
					/*
					 * Dann gibt es zwei Möglichkeiten:
					 * 	1. Es liegen Altdaten aus SWD-Zeiten vor, die
					 * 		auch schon damals Teile von Teilen enthalten
					 * 		konnten.
					 * 	2. Es liegen neue oder aufgearbeitete Daten vor.
					 * 
					 * In beiden Fällen enthalten das 2. und alle folgenden
					 * $p-Felder einen und nur einen Musiktitel.
					 */
					actualPartTitle =
						ParseMusicTitle.parseWithoutVersion(composer,
								contentOfSubfield);
					partOfWork.addPartOfWork(actualPartTitle);
				}

				break;
			//-----------------
			case 's':
				Version version =
					ParseVersion.parse(composer, contentOfSubfield);
				// Notbremse für unbekannten Inhalt von $s:
				if (version == null) {
					version = new Version(contentOfSubfield);
				}
				mTitle.setVersion(version);
				break;
			case 'o':
				mTitle.setArrangement(new Arrangement(contentOfSubfield));
				break;
			case 'v':
				mTitle.setComment(new Comment(contentOfSubfield));
				break;

			default:
				break;
			}
		}

		return mTitle;

	}

	/**
	 * Parst GND-Fassung. Ist diese Funktion überhaupt noch nötig? Insgesamt
	 * zu viele gegenseitig rekursive Aufrufe!
	 * 
	 * @param parseString 	nicht null, nicht leer, nicht mit $ beginnend.
	 * @param composer	Komponist 
	 * @return	gültigen Musiktitel oder null
	 */
	public static MusicTitle parseGND(
			final String composer,
			final String parseString) {
		if (parseString == null || parseString.length() == 0)
			throw new IllegalArgumentException(
					"parseGND(): parseString ist null oder hat Länge 0");

		// String in Tokens zerlegen
		final List<String> subfields =
			StringUtils.breakUpIntoSubfields(parseString);

		return parseGND(composer, subfields);

	}

	/**
	 * @param args nichts.
	 * @throws IOException 
	 */
	public static void main(final String[] args) throws IOException {
		final BufferedReader br =
			new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Titel bitte eingeben");
		System.out.println();
		final MusicTitle mt = ParseMusicTitle.parse("", br.readLine());
		System.out.println((TitleUtils.getStructured(mt)));
	}

}
