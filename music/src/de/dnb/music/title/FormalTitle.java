package de.dnb.music.title;

import de.dnb.music.visitor.Visitor;

public class FormalTitle extends MusicTitle implements Cloneable {

	public FormalTitle(FormalTitle ft) {
		super();
		// TODO Auto-generated constructor stub
	}

	public FormalTitle() {
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		visitChildren(visitor);
		visitor.leave(this);

	}

	@Override
	public MusicTitle clone() {
		return new FormalTitle(this);
	}

}
