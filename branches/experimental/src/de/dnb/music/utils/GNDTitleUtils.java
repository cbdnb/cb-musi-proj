package de.dnb.music.utils;

import static de.dnb.music.utils.GNDConstants.TAG_DB;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import de.dnb.basics.applicationComponents.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Field;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Indicator;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineFactory;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.parser.tag.GNDTag;
import de.dnb.gnd.parser.tag.Tag;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.gnd.utils.SubfieldUtils;
import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentDB;
import de.dnb.music.publicInterface.Constants;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.visitor.AdditionalDataIn3XXVisitor;
import de.dnb.music.visitor.AuthorityDataVisitor;
import de.dnb.music.visitor.TitleElement;

/**
 * Klasse, die alle Hilfsmethoden bereitstellt, die auf Record, Zeilen
 * und Unterfeldern basieren.
 * 
 * @author baumann
 *
 */
public final class GNDTitleUtils {

	private GNDTitleUtils() {
	}

	/**
	 * Liefert eine Zeile zu Tag und Titel.
	 * 
	 * @param tag		nicht null.
	 * @param title		nicht null.
	 * @return			nicht null.
	 */
	public static Line getLine(final Tag tag, final MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("tag", tag);
		RangeCheckUtils.assertReferenceParamNotNull("element", title);
		String gnd = TitleUtils.getX30ContentAsString(title);
		Line line = null;
		try {
			line = LineParser.parse(tag, Format.PICA3, gnd);
		} catch (IllFormattedLineException e) {
			// OK
		}
		return line;
	}

	/**
	 * Liefert eine Zeile zu Tag und Titel (als String).
	 * 
	 * @param tag		nicht null.
	 * @param titleStr	nicht null.
	 * @return			nicht null.
	 * 
	 * @throws IllFormattedLineException
	 * 					wenn
	 */
	public static Line getLine(final Tag tag, final String titleStr)
			throws IllFormattedLineException {
		RangeCheckUtils.assertReferenceParamNotNull("tag", tag);
		RangeCheckUtils.assertStringParamNotNullOrWhitespace("titleStr",
				titleStr);
		String gnd = TitleUtils.getX30ContentAsString(titleStr);
		Line line = LineParser.parse(tag, Format.PICA3, gnd);
		return line;
	}

	/**
	 * Gibt basierend auf dem Tag 130 eine Liste der Unterfelder.
	 * 
	 * @param title	nicht null.
	 * @return		nicht null, nicht leer.
	 */
	public static List<Subfield> getSubfields(final MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		String gnd = TitleUtils.getX30ContentAsString(title);
		final LineFactory factory = LineParser.getFactory("130", TAG_DB);
		try {
			factory.load(gnd);
		} catch (IllFormattedLineException e) {
			// nix
		}
		return factory.getSubfieldList();
	}

	/**
	 * Gibt basierend auf dem Tag 130 eine Liste der Unterfelder der
	 * vermuteten RSWK-Ansetzung.
	 * 
	 * @param title	nicht null.
	 * @return		nicht null, nicht leer.
	 */
	public static List<Subfield> getRSWKSubfields(final MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		String rswk = TitleUtils.getRSWKStringInSubfields(title);
		final LineFactory factory = LineParser.getFactory("130", TAG_DB);
		try {
			factory.load(rswk);
		} catch (IllFormattedLineException e) {
			// nix
		}
		return factory.getSubfieldList();
	}

	/**
	 * Liefert die 3XX-Felder.
	 * 
	 * @param element			TitleElement
	 * @param forceTotalCount	Gesamtzahl bei Instrumenten erzwingen
	 * @return 3XX-Felder der GND inclusive 548 (Zeitangabe)
	 */
	public static Collection<Line> get3XXLines(
			final TitleElement element,
			final boolean forceTotalCount) {
		RangeCheckUtils.assertReferenceParamNotNull("element", element);
		final AuthorityDataVisitor auvis =
			new AuthorityDataVisitor(forceTotalCount);
		final AdditionalDataIn3XXVisitor advis =
			new AdditionalDataIn3XXVisitor();
		element.accept(auvis);
		element.accept(advis);
		Collection<Line> lines = auvis.getLines();
		lines.addAll(advis.getLines());
		return lines;
	}

	/**
	 * Liefert die GND-Zeilen, die aus einem Werktitel gewonnen werden
	 * können. Der Werktitel kommt in die 130.
	 * 
	 * @param musicTitle		nicht null
	 * @param forceTotalCount	Gesamtzahl bei Ausführenden wird immer
	 * 							ausgegeben
	 * @return					GND-Informationen als Zeilen.
	 */
	public static Collection<Line> getLines(
			final MusicTitle musicTitle,
			final boolean forceTotalCount) {
		RangeCheckUtils.assertReferenceParamNotNull("musicTitle", musicTitle);
		Collection<Line> lines = new LinkedList<Line>();
		Line line = getLine(GNDConstants.TAG_130, musicTitle);
		lines.add(line);
		lines.addAll(get3XXLines(musicTitle, forceTotalCount));
		if (musicTitle.containsParts()) {
			try {
				line =
					LineParser.parse("430 " + TitleUtils.getRAK(musicTitle)
						+ "$v" + Constants.KOM_PORTAL_430, TAG_DB);
				lines.add(line);
			} catch (IllFormattedLineException e) {
				// nix
			}
		}
		try {
			if (musicTitle.containsVersion()) {
				line = LineParser.parse("008 wif", TAG_DB);
			} else {
				line = LineParser.parse("008 wim", TAG_DB);
			}
		} catch (IllFormattedLineException e) {
			// nix
		}
		lines.add(line);
		return lines;
	}

	/**
	 * Liefert die GND-Zeilen, die aus einem Werktitel gewonnen werden
	 * können. Die Gesamtzahl wird nicht ausgegeben.
	 * 
	 * @param tag				130 oder 140
	 * @param musicTitle		nicht null
	 * 							
	 * @return					GND-Informationen als Zeilen.
	 */
	public static
			Collection<Line>
			getLines(final Tag tag, final MusicTitle musicTitle) {
		RangeCheckUtils.assertReferenceParamNotNull("musicTitle", musicTitle);
		if (tag != GNDConstants.TAG_130 && tag != GNDConstants.TAG_430)
			throw new IllegalArgumentException("Tag ist keiner für Werktitel");
		Collection<Line> lines = new LinkedList<Line>();
		Line line = getLine(tag, musicTitle);
		lines.add(line);
		lines.addAll(get3XXLines(musicTitle, false));
		if (musicTitle.containsParts() && tag != GNDConstants.TAG_430) {
			try {
				line =
					LineParser.parse("430 " + TitleUtils.getRAK(musicTitle)
						+ "$v" + Constants.KOM_PORTAL_430, TAG_DB);
				lines.add(line);
			} catch (IllFormattedLineException e) {
				// nix
			}
		}
		try {
			if (musicTitle.containsVersion()) {
				line = LineParser.parse("008 wif", TAG_DB);
			} else {
				line = LineParser.parse("008 wim", TAG_DB);
			}
		} catch (IllFormattedLineException e) {
			// nix
		}
		lines.add(line);
		return lines;
	}

	/**
	 * Liefert die GND-Zeilen, die aus einem Werktitel gewonnen werden
	 * können. Der Werktitel kommt in die 130.
	 * 
	 * @param musicTitle		nicht null
	 * @param forceTotalCount	Gesamtzahl bei Ausführenden wird immer
	 * 							ausgegeben
	 * @return					GND-Informationen als Zeilen.
	 */
	public static Collection<Line> getLines(
			final String musicTitle,
			final boolean forceTotalCount) {
		RangeCheckUtils.assertStringParamNotNullOrWhitespace("musicTitle",
				musicTitle);
		MusicTitle title = ParseMusicTitle.parse(null, musicTitle);
		return getLines(title, forceTotalCount);
	}

	public static final GNDTag TAG_382 = (GNDTag) TAG_DB.findTag("382");

	private static LineFactory factory382 = TAG_382
			.getLineFactory();

	private static Indicator dollarS = TAG_382.getIndicator('s');

	/**
	 * Liefert eine Zeile 382 $s<Anzahl>.
	 * 
	 * @param lines	nicht null, alle mit Tag 382
	 * @return		Zeile mit Gesamtzahl oder null
	 */
	public static Line getTotalInstrumentCount(Iterable<Line> lines) {
		RangeCheckUtils.assertReferenceParamNotNull("lines", lines);
		int count = 0;
		for (Line line : lines) {
			if (line.getTag() != TAG_382)
				throw new IllegalArgumentException("Kein 382-Feld");
			// Zeile(n) mit $n überspringen:
			if (SubfieldUtils.containsIndicator(line, '9')) {
				// Alle Sonderfälle ausschließen:
				Subfield dollar8 = SubfieldUtils.getFirstSubfield(line, '8');
				if (dollar8 != null) {
					String instr = dollar8.getContent();
					// Orchester:
					if (instr.contains("orch") || instr.contains("Orch"))
						return null;
					// Chor:
					if (instr.contains("chor") || instr.contains("Chor"))
						return null;
					// ensemble:
					if (instr.contains("ensemble")
						|| instr.contains("Ensemble"))
						return null;
					// Tonband ...
					if (instr.contains("Tonb"))
						return null;
					// mehrstimmig:
					if (instr.contains("stimmig")
						&& !instr.equals("Einstimmige Musik")) {
						return null;
					}
				}
				Subfield dollarN = SubfieldUtils.getFirstSubfield(line, 'n');
				if (dollarN == null)
					count++;
				else {
					int diff = Integer.parseInt(dollarN.getContent());
					count += diff;
				}
			}
		}

		try {
			Subfield subDollarS =
				new Subfield(dollarS, Integer.toString(count));
			factory382.load(subDollarS);
			return factory382.createLine();
		} catch (IllFormattedLineException e) {
			// nix
		}

		return null;
	}

	/**
	 * Liefert eine Zeile 382 $s<Anzahl>.
	 * 
	 * @param lines	nicht null, alle mit Tag 382
	 * @return		Zeile mit Gesamtzahl oder null
	 */
	public static Line getTotalInstrumentCount(Record record) {
		RangeCheckUtils.assertReferenceParamNotNull("record", record);
		Field field382 = record.getField(TAG_382);
		if (field382 != null) {
			return getTotalInstrumentCount(field382);
		}
		return null;
	}

	/**
	 * Fügt einem Musikdatensatz die Besetzungsstärke hinzu. Die alte
	 * Besetzungsstärke wird überschrieben.
	 * 
	 * @param record			nicht null
	 * @param line382dollarS	Enthält die Gesamtbesetzungsstärke. 
	 * 							Kann auch null sein, dann wird nichts geändert.
	 */
	public static void setTotalInstrumentCount(
			Record record,
			Line line382dollarS) {
		RangeCheckUtils.assertReferenceParamNotNull("record", record);
		if (record.tagDB != TAG_DB)
			throw new IllegalArgumentException("Kein GND-Datensatz");
		if (line382dollarS == null)
			return;
		if (line382dollarS.getTag() != TAG_382)
			throw new IllegalArgumentException("Kein 382-Feld");
		// Zeile(n) mit $n überspringen:
		if (!SubfieldUtils.containsIndicator(line382dollarS, 's'))
			throw new IllegalArgumentException(
					"Enthält keine Gesamtbesetzungsstärke");
		// alte Zeile entfernen:
		Field field382 = record.getField(TAG_382);
		if (field382 != null) {
			for (Line line : field382) {
				if (SubfieldUtils.containsIndicator(line, 's'))
					record.remove(line);
			}
		}
		try {
			record.add(line382dollarS);
		} catch (OperationNotSupportedException e) {
			// nix
		}
	}

	/**
	 * Fügt einem Musikdatensatz die aktuelle Besetzungsstärke hinzu. Die alte
	 * Besetzungsstärke wird überschrieben.
	 * 
	 * @param record			nicht null
	 */
	public static void setTotalInstrumentCount(Record record) {
		RangeCheckUtils.assertReferenceParamNotNull("record", record);
		setTotalInstrumentCount(record, getTotalInstrumentCount(record));
	}

	/**
	 * Liefert die GND-Zeilen, die aus einem Werktitel gewonnen werden
	 * können. Der Werktitel kommt in die 130.
	 * 
	 * @param musicTitle		nicht null
	 * @param forceTotalCount	Gesamtzahl bei Ausführenden wird immer
	 * 							ausgegeben
	 * @return					GND-Informationen als Zeilen.
	 */
	public static Record getRecord(
			final MusicTitle musicTitle,
			final boolean forceTotalCount) {
		RangeCheckUtils.assertReferenceParamNotNull("musicTitle", musicTitle);
		Record record = new Record(null, TAG_DB);
		try {
			RecordUtils.addLines(record, getLines(musicTitle, forceTotalCount));
		} catch (OperationNotSupportedException e) {
			// ok.
		}
		return record;
	}

	/**
	 * Liefert die GND-Zeilen, die aus einem Werktitel gewonnen werden
	 * können. Der Werktitel kommt in die 130.
	 * 
	 * @param musicTitle		nicht null
	 * @param forceTotalCount	Gesamtzahl bei Ausführenden wird immer
	 * 							ausgegeben
	 * @return					GND-Informationen als Zeilen.
	 */
	public static Record getRecord(
			final String musicTitle,
			final boolean forceTotalCount) {
		RangeCheckUtils.assertStringParamNotNullOrWhitespace("musicTitle",
				musicTitle);
		MusicTitle title = ParseMusicTitle.parse(null, musicTitle);
		return getRecord(title, forceTotalCount);
	}

	/**
	 * Wandelt ein Instrument in eine GND-Zeile um.
	 * 
	 * @param instrument	nicht null
	 * @return				null, wenn ein Fehler auftritt.
	 */
	public static Line makeLine(final Instrument instrument) {
		RangeCheckUtils.assertReferenceParamNotNull("instrument", instrument);
		String instrStr =
			"382 !" + instrument.getIdn() + "!" + instrument.getSwd();
		int count = instrument.getCount();
		if (count > 1) {
			instrStr += "$n" + count;
		}
		try {
			return LineParser.parse(instrStr, TAG_DB);
		} catch (IllFormattedLineException e) {
			return null;
		}
	}

	/**
	 * Wandelt eine Form in eine GND-Zeile um.
	 * 
	 * @param genre	nicht null
	 * @return		null, wenn ein Fehler auftritt.
	 */
	public static Line makeLine(final Genre genre) {
		RangeCheckUtils.assertReferenceParamNotNull("genre", genre);
		if (genre.getIdn().length() > 0) {

			String genreStr = "380 !" + genre.getIdn() + "!" + genre.getSwd();
			try {
				return LineParser.parse(genreStr, TAG_DB);
			} catch (IllFormattedLineException e) {
				// nix
			}
		}
		return null;
	}

	public static void main(String[] args)
			throws OperationNotSupportedException {
		Record record = getRecord("Adagio Vl", true);
		System.out.println(record);
		System.out.println();
		Instrument instrument = InstrumentDB.matchInstrument("Va");
		instrument.setCount(3);
		Line line = makeLine(instrument);
		record.add(line);
		setTotalInstrumentCount(record);
		System.out.println(record);
	}

}
