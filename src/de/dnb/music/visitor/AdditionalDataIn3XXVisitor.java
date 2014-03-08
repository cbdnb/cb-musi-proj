package de.dnb.music.visitor;

import static de.dnb.music.utils.GNDConstants.TAG_DB;

import java.util.Collection;
import java.util.HashSet;

import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.OpusNumber;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.additionalInformation.ThematicIndexNumber;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

/**
 * Enthält die Zeilen zu den Feldern 3XX und 548.
 * @author baumann
 *
 */
public class AdditionalDataIn3XXVisitor extends Visitor {

	private Line dateLine = null;

	private Line indexLine = null;

	private Line keyLine = null;

	private Line opusLine = null;

	private Line serNumbLine = null;

	@Override
	public final String toString() {
		return RecordUtils.toPicaExpanded(getLines());
	};

	/**
	 * Liefert die Zeilen zu den Feldern 3XX und 548.
	 * 
	 * @return nicht null. 
	 * 
	 */
	public final Collection<Line> getLines() {
		HashSet<Line> lines = new HashSet<Line>();
		lines.add(dateLine);
		lines.add(keyLine);
		lines.add(serNumbLine);
		lines.add(indexLine);
		lines.add(opusLine);
		lines.remove(null);
		return lines;
	}

	@Override
	public final void visit(final DateOfComposition dateOfComposition) {
		String dateOfCompositionStr = "548 $c" + dateOfComposition + "$4dats";
		try {
			this.dateLine = LineParser.parse(dateOfCompositionStr, TAG_DB, false);
		} catch (IllFormattedLineException e) {
			// nix
		}
	}

	@Override
	public final void visit(final Key key) {
		String keyStr = "384 " + key;
		try {
			this.keyLine = LineParser.parse(keyStr, TAG_DB, false);
		} catch (IllFormattedLineException e) {
			// nix
		}
	}

	@Override
	public final void visit(final OpusNumber opusNumber) {
		String opusStr = "383 $b" + opusNumber;
		try {
			this.opusLine = LineParser.parse(opusStr, TAG_DB, false);
		} catch (IllFormattedLineException e) {
			// nix
		}
	}

	@Override
	public final void visit(final SerialNumber serialNumber) {
		String serNumbStr = "383 " + serialNumber;
		try {
			Line line = LineParser.parse(serNumbStr, TAG_DB, false);
			this.serNumbLine = line;
		} catch (IllFormattedLineException e) {
			// nix
		}
	}

	@Override
	public final void visit(final ThematicIndexNumber thematicIndexNumber) {
		String thematicIndexStr =
			"383 $c" + thematicIndexNumber.toStringWithoutBlanks();
		try {
			this.indexLine = LineParser.parse(thematicIndexStr, TAG_DB, false);
		} catch (IllFormattedLineException e) {
			// nix
		}
	}

	/**
	 * @param args
	 * @throws IllFormattedLineException 
	 */
	public static void main(String[] args) throws IllFormattedLineException {
		MusicTitle mt =
			ParseMusicTitle.parseTitlePlusVersion(null, "Cantica, 1");
		AdditionalDataIn3XXVisitor vis = new AdditionalDataIn3XXVisitor();
		mt.accept(vis);
		System.out.println(vis.toString());

	}

}
