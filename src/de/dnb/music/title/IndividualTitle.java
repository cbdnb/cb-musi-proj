package de.dnb.music.title;

import de.dnb.music.visitor.Visitor;

public class IndividualTitle extends MusicTitle {

	public IndividualTitle(IndividualTitle it) {
		super();
	}

	public IndividualTitle() {
		super();
	}

	@Override
	public void accept(Visitor visitor) {
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

	public static void main(final String[] args) {
	}

}
