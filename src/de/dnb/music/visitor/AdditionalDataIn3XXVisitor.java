package de.dnb.music.visitor;

import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.OpusNumber;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.additionalInformation.ThematicIndexNumber;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public class AdditionalDataIn3XXVisitor extends Visitor {

	private String dateOfCompositionStr = "";

	private String thematicIndexStr = "";

	private String keyStr = "";

	private String opusStr = "";

	private String serNumbStr = "";

	@Override
	public final String toString() {
		return dateOfCompositionStr + thematicIndexStr + keyStr + opusStr
			+ serNumbStr;
	};

	@Override
	public final void visit(final DateOfComposition dateOfComposition) {
		dateOfCompositionStr = "\n548 $c" + dateOfComposition + "$4dats";
	}

	@Override
	public final void visit(final Key key) {
		keyStr = "\n384 " + key;
	}

	@Override
	public final void visit(final OpusNumber opusNumber) {
		opusStr = "\n383 $b" + opusNumber;
	}

	@Override
	public final void visit(final SerialNumber serialNumber) {
		serNumbStr = "\n383 " + serialNumber;
	}

	@Override
	public final void visit(final ThematicIndexNumber thematicIndexNumber) {
		thematicIndexStr =
			"\n383 $c" + thematicIndexNumber.toStringWithoutBlanks();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parse(null, "Adagio und Fuge, Vl 1 2 Va 1 2, C-Dur"
				+ "<aa Nr. 2>. Op. 2");
		AdditionalDataIn3XXVisitor vis = new AdditionalDataIn3XXVisitor();
		mt.accept(vis);
		System.out.println(vis);

	}

}
