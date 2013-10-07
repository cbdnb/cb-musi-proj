package de.dnb.music.title;

import de.dnb.music.visitor.Visitor;

public class FormalTitle extends MusicTitle {

	public FormalTitle(final FormalTitle ft) {
		super();
	}

	public FormalTitle() {
	}

	@Override
	public final void accept(final Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			visitChildren(visitor);
		}
		visitor.leave(this);

	}

}
