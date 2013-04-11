/**
 * 
 */
package de.dnb.music.visitor.setsOfRules;

import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;

/**
 * @author baumann
 * 
 * Liefert die Satzzeichen, die einen Werktitel zusammenhalten. Diese 
 * sind: (keine Eintragung = wie RAK) 
 * 
 * Bezeichnung				RAK			GND			RSWK
 *	preTitle				""  			
 *	preTitleFirstPart		"", 		"$p"		" / "
 *	preTitleFollowingPts	", "		"$p"		" / "
 *	preGenreFormal			"" 			
 *	preGenreIndiv			", " 
 *	preferredNumerus		pl			pl			sg
 *  preferredNumerusParts	sg			sg			sg
 *  preferredNumerusIndiv	sg			sg			sg
 *	preInstrList			","			""			", "
 *	preInstrumnt			" "			"$m"
 *	abbreviationMode		abb			abb			wrOut
 *	preNumberList			" "
 *	btwNumber				" "						","
 *	preThIdx,				", "		"$n"		" "
 *	preOpus,				", "		"$n"		" "
 *	preSerNum				", "		"$n"		" "
 *	preKey					", "		"$r" 		" "
 *	preThIdxParts			" "			"$n"		" "
 *	preOpusParts			" "			"$n"		" "
 *	preSerNumParts			" "			"$n"		" "
 *	preKeyParts				" "			"$r" 		" "
 *	preYear					", "		"$f"		" <" 
 *	postYear				""						">"
 *	preQualifier			?			"$g"		" <"
 *	postQualifier			?			""			">"
 *	preParts				" <"		""			""
 *	postParts				">"			""			""
 *	preVersion				". "		"$s"		?
 *	preArrgmt				" / "		"$o"		?
 *	preComment				?			"$v"		" *"
 *	
 *
 *
 *	btwGenre			nicht benötigt, da die Informationen aus GenreList
 *						entnommen werden.
 *
 *	Namenskonvention:		
 * 		pre = leitet ein 
 * 		btw = steht zwischen 
 * 		post = leitet aus
 * 	
 */
public abstract class AbstractParticleFactory {

	protected String preTitle;

	protected String preTitleFirstPart;

	protected String preTitleFollowingPts;

	protected String preGenreFormal;

	protected String preGenreIndiv;

	protected String preInstrList;

	protected String preInstrumnt;

	protected Genre.Numeri preferredNumerus;

	protected Genre.Numeri preferredNumerusParts;

	protected Genre.Numeri preferredNumerusIndiv;

	protected Instrument.AbbreviationMode abbreviationMode;

	protected String preNumberList;

	protected String btwNumber;

	protected String preKey;

	protected String preOpus;

	protected String preSerNum;

	protected String preThIdx;

	protected String preThIdxParts;
	
	protected String preOpusParts;
	
	protected String preSerNumParts;
	
	protected String preKeyParts;

	protected String preYear;

	protected String postYear;
	
	protected String preQualifier;

	protected String postQualifier;

	protected String preParts;

	protected String postParts;

	protected String preVersion;

	protected String preArrgmt;
	
	protected String preComment;

	public String getPreTitle() {
		return preTitle;
	}

	public String getPreTitleFirstPart() {
		return preTitleFirstPart;
	}

	public String getPreTitleFollowingPts() {
		return preTitleFollowingPts;
	}

	public String getPreGenreFormal() {
		return preGenreFormal;
	}

	public String getPreGenreIndiv() {
		return preGenreIndiv;
	}

	public String getPreInstrList() {
		return preInstrList;
	}

	public String getPreInstrumnt() {
		return preInstrumnt;
	}

	public String getPreNumberList() {
		return preNumberList;
	}

	public String getBtwNumber() {
		return btwNumber;
	}

	public String getPreKey() {
		return preKey;
	}

	public String getPreOpus() {
		return preOpus;
	}

	public String getPreSerNum() {
		return preSerNum;
	}

	public String getPreThIdx() {
		return preThIdx;
	}

	public String getPreThIdxParts() {
		return preThIdxParts;
	}

	public String getPreOpusParts() {
		return preOpusParts;
	}

	public String getPreSerNumParts() {
		return preSerNumParts;
	}

	public String getPreKeyParts() {
		return preKeyParts;
	}

	public String getPreYear() {
		return preYear;
	}

	public String getPostYear() {
		return postYear;
	}

	/**
	 * @return the preQualifier
	 */
	public String getPreQualifier() {
		return preQualifier;
	}

	/**
	 * @return the postQualifier
	 */
	public String getPostQualifier() {
		return postQualifier;
	}

	public String getPreParts() {
		return preParts;
	}

	public String getPostParts() {
		return postParts;
	}

	public String getPreVersion() {
		return preVersion;
	}

	public String getPreArrgmt() {
		return preArrgmt;
	}
	public String getPreComment() {
		return preComment;
	}

	public Genre.Numeri getPreferredNumerus() {
		return preferredNumerus;
	}

	public Genre.Numeri getPreferredNumerusParts() {
		return preferredNumerusParts;
	}

	public Genre.Numeri getPreferredNumerusIndiv() {
		return preferredNumerusIndiv;
	}

	public Instrument.AbbreviationMode getAbbreviationMode() {
		return abbreviationMode;
	}

}
