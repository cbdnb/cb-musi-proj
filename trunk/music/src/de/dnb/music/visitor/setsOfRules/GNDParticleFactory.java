package de.dnb.music.visitor.setsOfRules;

import de.dnb.music.genre.Genre;
import de.dnb.music.genre.Genre.Numeri;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.visitor.setsOfRules.WorkTitleVisitor.States;

public class GNDParticleFactory extends AbstractParticleFactory {

	public GNDParticleFactory() {

		preTitle = "";

		preTitleFirstPart = "$p";

		preTitleFollowingPts = "$p";

		preGenreFormal = "";

		preGenreIndiv = ", ";

		preferredNumerus = Genre.Numeri.PLURAL;

		preferredNumerusParts = Numeri.SINGULAR;

		preferredNumerusIndiv = Numeri.SINGULAR;

		preInstrList = "";

		preInstrumnt = "$m";

		abbreviationMode = Instrument.AbbreviationMode.ABBREVIATED;

		preNumberList = " ";

		btwNumber = " ";

		preKey = "$r";

		preOpus = "$n";

		preSerNum = "$n";

		preThIdx = "$n";

		preThIdxParts = "$n";

		preOpusParts = "$n";

		preSerNumParts = "$n";

		preKeyParts = "$r";

		preYear = "$f";

		postYear = "";

		preParts = "";

		postParts = "";

		preVersion = "$s";

		preArrgmt = "$o";
		
		preComment = "$v";

	}

	public String getRelatorCode(WorkTitleVisitor.States state) {
		switch (state) {
		case FIRST_PART:
		case FOLLOWING_PARTS:
			return "$4obpa";

		case VERSION:
		case ARRANGEMENT:
			return "$4werk";

		default:
			return "";
		}

	}
}
