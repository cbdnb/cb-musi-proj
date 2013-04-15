package de.dnb.music.publicInterface;

import javax.naming.OperationNotSupportedException;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.gnd.utils.Pair;
import de.dnb.music.publicInterface.Constants.SetOfRules;

/**
 * Template-Klasse. Die Unterklassen überschreiben div. Methoden, um an das
 * zu bearbeitende Datenset anzupassen.
 *
 * @author baumann
 *
 */
public class RecordTransformer {

	protected Record oldRecord;

	/**
	 * Template-Methode.
	 * 
	 * @param oldRecord	nicht null.
	 * @return			neuer Record.
	 */
	public Record transform(final Record oldRecord) {
		RangeCheckUtils.assertReferenceParamNotNull("oldRecord", oldRecord);
		this.oldRecord = oldRecord;
		Record newRecord = new Record(oldRecord.getId());
		return newRecord;
	}

	/**
	 * Liefert das Regelwerk, unter dem oldRecord angesetzt wurde.
	 * Dieses steht im Feld 913
	 * @param record TODO
	 * 
	 * @return	RAK, RSWK, oder GND.
	 */
	protected SetOfRules getRules(Record record) {
		Pair<Line, Integer> pair = GNDUtils.getFirstLine(record, "913");
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
	 * @param args
	 * @throws IllFormattedLineException 
	 * @throws OperationNotSupportedException 
	 */
	public static void main(String[] args)
			throws IllFormattedLineException,
			OperationNotSupportedException {
		Line line =
			LineParser.parse("913 $Sswd$ipt$aBerg, Alban: Wozzeck$04112712-2");

		Record record = new Record(null);
//		record.add(line);
		line =
			LineParser.parse("913 $Sest$ipt$aBerg, Alban: Wozzeck$04112712-2");
		record.add(line);
		RecordTransformer transformer = new RecordTransformer();

		System.err.println(transformer.getRules(record));

	}

}
