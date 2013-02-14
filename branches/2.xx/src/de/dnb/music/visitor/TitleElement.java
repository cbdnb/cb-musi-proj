package de.dnb.music.visitor;

public abstract interface TitleElement {

	public abstract void accept(Visitor visitor);
}
