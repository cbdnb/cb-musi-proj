package de.dnb.music.title;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.GNDConstants;

import utils.StringUtils;
import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Indicator;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.Tag;
import de.dnb.gnd.parser.TagDB;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineFactory;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.utils.Pair;
import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.additionalInformation.Qualifier;
import de.dnb.music.mediumOfPerformance.InstrumentDB;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;
import de.dnb.music.version.ParseVersion;
import de.dnb.music.version.Version;
import filtering.FilterUtils;
import filtering.IPredicate;

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
	 * Erkennt den vollständigen Titel nach RAK, aber auch nach RSWK und GND.
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
	public static MusicTitle parseFullRAK(
			final String composer,
			final String parseString) {
		if (parseString == null)
			throw new IllegalArgumentException(
					"Null-String an parseFullRAK()übergeben");

		String ansetzung = parseString.trim();
		if (ansetzung.length() == 0)
			return null;

		// Erkennen, ob schon eine GND-Form vorliegt:
		if (StringUtils.containsSubfields(ansetzung))
			return parseGND(composer, ansetzung);

		MusicTitle mt = null;
		String ordnungsgruppe = null; // / Arr.
		String partsString = null;
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

			partsString = StringUtils.getOrdnungshilfe();
			final AdditionalInformation z =
				ParseAdditionalInformation.matchDate(partsString);
			if (z == null) { // -> keine Jahreszahl

				ansetzung = StringUtils.getAnsetzung();
				fasString = StringUtils.getFassung();

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
	public static MusicTitle parseSimpleTitle(
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
	 * Parst eine Zeile.
	 * 
	 * @param composer	egal
	 * @param line		nicht null, korrekter Tag
	 * @return			Paar aus Musiktitel und nicht verwendeten
	 * 					Unterfeldern.
	 */
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
	 * @return	gültigen Musiktitel oder null, 
	 * 
	 */
	public static Pair<MusicTitle, List<Subfield>> parseGND(
			final String composer,
			final List<Subfield> subfields) {
		RangeCheckUtils.assertCollectionParamNotNullOrEmpty("value", subfields);
		List<Subfield> unused = new LinkedList<Subfield>();

		final IPredicate<Subfield> dollarGPred = new IPredicate<Subfield>() {
			@Override
			public boolean accept(final Subfield element) {
				return element.getIndicator() == TagDB.dollarg;
			}
		};

		final boolean containsG =
			(FilterUtils.find(subfields, dollarGPred) != null);

		MusicTitle mTitle = null;
		/*
		 *  Zeigt auf das Werk, das gerade mit Unterfeldern aufgefüllt wird.
		 *  Das kann das übergeordnete Werk, aber auch der Werkteil sein. 
		 *  Im case-Fall $p wird dieser Zeiger weitergerückt. 
		 */
		MusicTitle actualPartTitle = null;
		PartOfWork partOfWork = null;

		for (Subfield subfield : subfields) {
			final Indicator indicator = subfield.getIndicator();
			final String contentOfSubfield = subfield.getContent();

			if (indicator == GNDConstants.DOLLAR_a) {
				/*
				 * Das ist (garantiert) immer das erste Unterfeld und damit
				 * der erste behandelte Fall in der Schleife. Sollten
				 * Altdaten vorliegen, kann höchstens eine Fassung in diesem
				 * Feld vorkommen. Daher parseTitlePlusVersion():
				 */
				mTitle =
					ParseMusicTitle.parseFullRAK(composer, contentOfSubfield);
				actualPartTitle = mTitle;
			} else if (indicator == GNDConstants.DOLLAR_m) {

				final InstrumentationList iList =
					ParseInstrumentation.parse(contentOfSubfield);
				if (!actualPartTitle.containsInstrumentation()) {
					actualPartTitle.setInstrumentationList(iList);
				} else {
					actualPartTitle.getInstrumentationList().addAll(iList);
				}
			} else if (indicator == GNDConstants.DOLLAR_f
				|| indicator == GNDConstants.DOLLAR_n
				|| indicator == GNDConstants.DOLLAR_r
				|| indicator == TagDB.dollarg) {
				/*
				 * Auch hier können wir wieder so tun, als wäre ein
				 * Komma erkannt, da ja nicht (gegenbenenfalls) ein
				 * $n mit einer Einzelzahl aufgebaut worden wäre.
				 */
				final boolean comma = true;
				final AdditionalInformation ai =
					ParseAdditionalInformation.parse(composer,
							contentOfSubfield, comma);
				// Ist das $g ein maskiertes $f, $n oder $r?
				if (ai != null)
					actualPartTitle.setAdditionalInformation(ai);
				else
					actualPartTitle.setAdditionalInformation(new Qualifier(
							contentOfSubfield));
			} else if (indicator == GNDConstants.DOLLAR_p) {

				if (!mTitle.containsParts()) {
					/*
					 *  neue Werkteilstrukur anlegen, dabei davon ausgehen,
					 *  dass auch vollkommen Unstrukturiertes (Altdaten?) in
					 *  $p steht, also auch Teile von Teilen. Daher wird der
					 *  Konstruktor PartOfWork(String) aufgerufen:
					 */
					if (containsG) {
						/*
						 * RSWK-Daten, enthalten mehrere $p, wenn notwendig:
						 */
						actualPartTitle =
							parseSimpleTitle(composer, contentOfSubfield);
						partOfWork = new PartOfWork(actualPartTitle);
					} else {
						partOfWork = new PartOfWork(contentOfSubfield);
						final List<MusicTitle> tList =
							partOfWork.getPartsOfWork();
						final int partsSize = tList.size();
						actualPartTitle = tList.get(partsSize - 1);
					}
					mTitle.setPartOfWork(partOfWork);

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
						ParseMusicTitle.parseSimpleTitle(composer,
								contentOfSubfield);
					partOfWork.addPartOfWork(actualPartTitle);
				}

			} else if (indicator == GNDConstants.DOLLAR_s) {
				Version version =
					ParseVersion.parse(composer, contentOfSubfield);
				// Notbremse für unbekannten Inhalt von $s:
				if (version == null) {
					version = new Version(contentOfSubfield);
				}
				mTitle.setVersion(version);
			} else if (indicator == GNDConstants.DOLLAR_o) {
				mTitle.setArrangement(new Arrangement(contentOfSubfield));
			} else {
				unused.add(subfield);
			}

		}
		return new Pair<MusicTitle, List<Subfield>>(mTitle, unused);
	}

	// reichhaltigste:
	private static LineFactory factory730 = LineParser.getFactory("730");

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
			line = LineParser.parse(parseString);
		} catch (IllFormattedLineException e) {
			// noch nicht alles verloren
		}
		if (line == null) {
			try {
				factory730.load(parseString);
				line = factory730.createLine();
			} catch (IllFormattedLineException e) {
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
		final BufferedReader br =
			new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Titel bitte eingeben");
		System.out.println();
		String string = br.readLine();
		Line line = LineParser.parse(string);
		Pair<MusicTitle, List<Subfield>> pair = parseGND(null, line);
		System.out.println((TitleUtils.getStructured(pair.first)));
		System.out.println(pair.second);
	}

}
