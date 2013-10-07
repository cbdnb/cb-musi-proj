package de.dnb.music.title;

import de.dnb.music.visitor.Visitor;

public class FormalTitle extends MusicTitle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5970209730845494678L;

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
