package de.dnb.music.visitor.setsOfRules;

import de.dnb.music.genre.Genre;
import de.dnb.music.genre.Genre.Numeri;
import de.dnb.music.mediumOfPerformance.Instrument;

public class RSWKParticleFactory extends AbstractParticleFactory {


	public RSWKParticleFactory() {

		preTitle = "";

		preTitleFirstPart = " / ";

		preTitleFollowingPts = " / ";

		preGenreFormal = "";

		preGenreIndiv = ", ";

		preferredNumerus = Genre.Numeri.SINGULAR;
		
		preferredNumerusParts = Numeri.SINGULAR;
		
		preferredNumerusIndiv = Numeri.SINGULAR;

		preInstrList = ",";

		preInstrumnt = " ";

		abbreviationMode = Instrument.AbbreviationMode.WRITTEN_OUT;

		preNumberList = " ";

		btwNumber = ",";

		preKey = " ";

		preOpus = " ";

		preSerNum = " ";

		preThIdx = " ";
		
		preThIdxParts = " ";

		preOpusParts = " ";

		preSerNumParts = " ";

		preKeyParts = " ";

		preYear = " <";

		postYear = ">";
		
		preQualifier = " <";

		postQualifier = ">";

		preParts = "";

		postParts = "";

		preVersion = "; nicht definierte Fassung: ";

		preArrgmt = "; nicht definierte Bearbeitung: ";
		
		preComment = " *";

	}
	
	

}
