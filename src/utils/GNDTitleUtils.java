package utils;

import static utils.GNDConstants.TAG_DB;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineFactory;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.parser.tag.Tag;
import de.dnb.gnd.utils.RecordUtils;
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
		MusicTitle title = ParseMusicTitle.parseFullRAK(null, musicTitle);
		return getLines(title, forceTotalCount);
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
		MusicTitle title = ParseMusicTitle.parseFullRAK(null, musicTitle);
		return getRecord(title, forceTotalCount);
	}
	
	public static void main(String[] args) {
		System.out.println(getRecord("Adagio Vl <Fuge>", true));
	}

}
