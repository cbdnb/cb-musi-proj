package de.dnb.music.visitor.setsOfRules;

import de.dnb.music.genre.Genre;
import de.dnb.music.genre.Genre.Numeri;
import de.dnb.music.mediumOfPerformance.Instrument;

public class RAKParticleFactory extends AbstractParticleFactory {

	public RAKParticleFactory() {

		preTitle = "";

		preTitleFirstPart = "";

		preTitleFollowingPts = ", ";

		preGenreFormal = "";

		preGenreIndiv = ", ";

		preferredNumerus = Genre.Numeri.PLURAL;
		
		preferredNumerusParts = Numeri.SINGULAR;
		
		preferredNumerusIndiv = Numeri.SINGULAR;

		preInstrList = ",";

		preInstrumnt = " ";

		abbreviationMode = Instrument.AbbreviationMode.ABBREVIATED;

		preNumberList = " ";

		btwNumber = " ";

		preKey = ", ";

		preOpus = ", ";

		preSerNum = ", ";

		preThIdx = ", ";

		preYear = ", ";

		postYear = "";
		
		preThIdxParts = " ";

		preOpusParts = " ";

		preSerNumParts = " ";

		preKeyParts = " ";

		preParts = " <";

		postParts = ">";

		preVersion = ". ";

		preArrgmt = " / ";
		
		preComment = "; nicht definierter Kommentar: ";

	}
}
