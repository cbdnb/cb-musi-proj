package de.dnb.music.visitor;

import java.util.Collection;
import java.util.HashSet;

import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Indicator;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.Tag;
import de.dnb.gnd.parser.TagDB;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.OpusNumber;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.additionalInformation.ThematicIndexNumber;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public class AdditionalDataIn3XXVisitor extends Visitor {

	private String dateOfCompositionStr = "";

	private Line dateOfComposition = null;

	private String thematicIndexStr = "";

	private Line thematicIndex = null;

	private String keyStr = "";

	private Line key = null;

	private String opusStr = "";

	private Line opus = null;

	private String serNumbStr = "";

	private Line serNumb = null;

	@Override
	public final String toString() {
		return dateOfCompositionStr + thematicIndexStr + keyStr + opusStr
			+ serNumbStr;
	};

	Collection<Line> getLines() {
		HashSet<Line> lines = new HashSet<Line>();
		lines.add(dateOfComposition);
		lines.add(key);
		lines.add(serNumb);
		lines.add(thematicIndex);
		lines.add(opus);
		lines.remove(null);
		return lines;
	}

	@Override
	public final void visit(final DateOfComposition dateOfComposition) {
		dateOfCompositionStr = "548 $c" + dateOfComposition + "$4dats";
		try {
			this.dateOfComposition = LineParser.parse(dateOfCompositionStr);
		} catch (IllFormattedLineException e) {
			// nix
		}
		dateOfCompositionStr = "\n" + dateOfCompositionStr;
	}

	@Override
	public final void visit(final Key key) {
		keyStr = "384 " + key;
		try {
			this.key = LineParser.parse(keyStr);
		} catch (IllFormattedLineException e) {
			// nix
		}
		keyStr = "\n" + keyStr;
	}

	@Override
	public final void visit(final OpusNumber opusNumber) {
		opusStr = "383 $b" + opusNumber;
		try {
			this.opus = LineParser.parse(opusStr);
		} catch (IllFormattedLineException e) {
			// nix
		}
		opusStr = "\n" + opusStr;
	}

	@Override
	public final void visit(final SerialNumber serialNumber) {
		serNumbStr = "\n383 " + serialNumber;
		try {
			this.serNumb = LineParser.parse(serNumbStr);
		} catch (IllFormattedLineException e) {
			// nix
		}
		serNumbStr = "\n " + serNumbStr;

	}

	@Override
	public final void visit(final ThematicIndexNumber thematicIndexNumber) {
		thematicIndexStr =
			"383 $c" + thematicIndexNumber.toStringWithoutBlanks();
		try {
			this.thematicIndex = LineParser.parse(thematicIndexStr);
		} catch (IllFormattedLineException e) {
			// nix
		}
		thematicIndexStr = "\n" + thematicIndexStr;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parseFullRAK(null,
					"Adagio und Fuge, Vl 1 2 Va 1 2, C-Dur"
						+ "<aa Nr. 2>. Op. 2");
		AdditionalDataIn3XXVisitor vis = new AdditionalDataIn3XXVisitor();
		mt.accept(vis);
		System.out.println(vis);

	}

}
