package de.dnb.music.title;

import de.dnb.music.visitor.Visitor;

public class FormalTitle extends MusicTitle {

	public FormalTitle(FormalTitle ft) {
		super();
		// TODO Auto-generated constructor stub
	}

	public FormalTitle() {
	}

	@Override
	public void accept(Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			visitChildren(visitor);
		}
		visitor.leave(this);

	}

}
