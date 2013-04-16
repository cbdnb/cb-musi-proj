package de.dnb.music.publicInterface;

import java.util.List;

import javax.naming.OperationNotSupportedException;

import utils.TitleUtils;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Field;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.gnd.utils.Pair;
import de.dnb.music.publicInterface.Constants.SetOfRules;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.visitor.AuthorityDataVisitor;
import filtering.FilterUtils;

/**
 * Template-Klasse. Die Unterklassen überschreiben div. Methoden, um an das
 * zu bearbeitende Datenset anzupassen.
 *
 * @author baumann
 *
 */
public class RecordTransformer {

	protected Record oldRecord;
	protected Record newRecord;

	/**
	 * Template-Methode.
	 * 
	 * @param record	nicht null.
	 * @return			neuer Record.
	 */
	public Record transform(final Record record) {
		RangeCheckUtils.assertReferenceParamNotNull("oldRecord", record);
		this.oldRecord = record;
		newRecord = oldRecord.clone();
		return newRecord;
	}

	/**
	 * Liefert das Regelwerk, unter dem oldRecord angesetzt wurde.
	 * Dieses steht im Feld 913
	 * @return	RAK, RSWK, oder GND.
	 */
	@SuppressWarnings("boxing")
	protected final SetOfRules getRules() {
		Pair<Line, Integer> pair = GNDUtils.getFirstLine(oldRecord, "913");
		if (pair.second != 1)
			return SetOfRules.GND;
		Line line913 = pair.first;
		Subfield subS = GNDUtils.getSubfield(line913, 'S');
		if (subS == null)
			return SetOfRules.GND;
		String rules = subS.getContent();
		if (rules.equals("swd"))
			return SetOfRules.RSWK;
		else if (rules.equals("est"))
			return SetOfRules.RAK;
		else
			return SetOfRules.GND;

	}

	/**
	 * splitted Feldinhalt in Information + Kommentar.
	 * @param line	Zeile
	 * @return	Paar aus 
	 * a) Information und 
	 * b) Kommentar, so vorhanden, sonst "". 
	 */
	protected final Pair<List<Subfield>, String> splitComment(final Line line) {
		RangeCheckUtils.assertReferenceParamNotNull("line", line);
		List<Subfield> subfields = line.getSubfields();
		Subfield dollarv = GNDUtils.getSubfield(line, 'v');
		String comment = (dollarv == null) ? "" : dollarv.getContent();
		subfields.remove(dollarv);
		return new Pair<List<Subfield>, String>(subfields, comment);
	}

	protected final String getComposerID() {
		Field field500 = GNDUtils.getField(oldRecord, "500");
		if (field500 == null)
			return null;
		for (Line line : field500) {
			Subfield subfield4 = GNDUtils.getSubfield(line, '4');
			if (subfield4 != null) {
				String subCont = subfield4.getContent();
				if (subCont.equals("kom1")) {
					Subfield subfield9 = GNDUtils.getSubfield(line, '9');
					if (subfield9 == null)
						return null;
					else
						return GNDUtils.getSubfield(line, '9').getContent();
				}
			}
		}
		return null;
	}

	/**
	 * Fügt 3XX und 548 hinzu.
	 * @param title		nicht null.
	 */
	protected void enrich3XX(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		try {
			boolean forceTotalCount = true;
			GNDUtils.addLines(newRecord,
					TitleUtils.get3XXLines(title, forceTotalCount));
		} catch (OperationNotSupportedException e) {
			// kann nichts passieren, da alle wiederholbar.
		}
	}

	/**
	 * @param args
	 * @throws IllFormattedLineException 
	 * @throws OperationNotSupportedException 
	 */
	public static void main(String[] args)
			throws IllFormattedLineException,
			OperationNotSupportedException {
		Record record = new Record(null);
		Line line = LineParser.parse("500 erg, Alban$4kom1");
		record.add(line);
		line = LineParser.parse("500 !118509321!Zwerg, Alban$4koma");
		record.add(line);
		RecordTransformer transformer = new RecordTransformer();
		transformer.transform(record);

		System.err.println(transformer.getComposerID());

	}
}
