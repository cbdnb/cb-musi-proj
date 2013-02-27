package de.dnb.music.visitor;

import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.OpusNumber;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.additionalInformation.ThematicIndexNumber;
import de.dnb.music.genre.Genre;
import de.dnb.music.genre.GenreList;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.title.Arrangement;
import de.dnb.music.title.Comment;
import de.dnb.music.title.FormalTitle;
import de.dnb.music.title.IndividualTitle;
import de.dnb.music.title.PartOfWork;
import de.dnb.music.version.Version;

/**
 * Für jede konkrete Unterklasse ConcreteTitleElementX von TitleElement muss 
 * hier visit() folgendermaßen überladen werden:
 * 
 * 	public void visit(ConcreteTitleElementX concreteTitleElementX) {
 * 		// tuWas()
 * 	}
 *  
 *  In mindestens einer der Unterklassen von Visitor muss zudem 
 *  visit(ConcreteTitleElementX) überschrieben werden, damit etwas sinnvolles
 *  gemacht wird.
 *  
 * Das konkrete Titel-Element concreteTitleElementX ruft in der Regel 
 * visit(this) aus der Funktion accept(Visitor) heraus auf. 
 * 
 * 
 */

public abstract class Visitor {

	public void visit(IndividualTitle individualTitle) {
		// default		
	}

	public void leave(IndividualTitle individualTitle) {
		// default
	}
	
	public void visit(FormalTitle formalTitle) {
		// default		
	}

	public void leave(FormalTitle formalTitle) {
		// default
	}

	public boolean visit(Version version) {
		return true;		
	}

	public void leave(Version version) {
		// default		
	}

	public void visit(DateOfComposition dateOfComposition) {
		// default
	}

	public void visit(Key key) {
		// default
	}

	public void visit(OpusNumber opusNumber) {
		// default
	}

	public void visit(SerialNumber serialNumber) {
		// default
	}

	public void visit(ThematicIndexNumber thematicIndexNumber) {
		// default
	}

	public boolean visit(GenreList genreList) {
		return true;		
	}

	public void leave(GenreList genreList) {
		// default
	}

	public void visit(Genre genre) {
		// default
	}

	public void visit(Instrument instrument) {
		// default
	}

	public boolean visit(InstrumentationList instrumentationList) {
		return true;
	}

	public void leave(InstrumentationList instrumentationList) {
		// default
	}

	public void visit(Arrangement arrangement) {
		// default
	}

	public boolean visit(PartOfWork partOfWork) {
		return true;
	}

	public void leave(PartOfWork partOfWork) {
		// default
	}

	public void visit(Comment comment) {
		// TODO Auto-generated method stub
		
	}

}
