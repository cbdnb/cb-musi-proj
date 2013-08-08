package de.dnb.music.visitor;

import de.dnb.music.title.MusicTitle;

public abstract interface TitleElement {

	public abstract void accept(Visitor visitor);

}
