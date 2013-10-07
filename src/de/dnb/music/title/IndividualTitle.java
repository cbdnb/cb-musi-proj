package de.dnb.music.title;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.genre.Genre;
import de.dnb.music.genre.GenreList;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public class IndividualTitle extends MusicTitle {

	public IndividualTitle(final IndividualTitle it) {
		super();
	}

	public IndividualTitle() {
		super();
	}

	public IndividualTitle(final String individualTitle) {
		super();
		this.individualTitle = individualTitle;
	}

	@Override
	public final void accept(final Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			visitChildren(visitor);
		}
		visitor.leave(this);
	}

	String individualTitle = "";

	public final String getIndividualTitle() {
		return individualTitle;
	}

	@Override
	public final boolean isAdditable(final TitleElement element) {
		if (containesNoAdditables())
			return true;
		// Also gibt es schon was
		if ((element instanceof GenreList || element instanceof Genre)
			&& !containsGenre())
			return false;
		if ((element instanceof InstrumentationList 
				|| element instanceof Instrument)
			&& !containsInstrumentation())
			return false;
		if (element instanceof AdditionalInformation
			&& !containsAdditionalInformation())
			return false;
		return true;
	}

	public static void main(final String[] args) {
	}

}
