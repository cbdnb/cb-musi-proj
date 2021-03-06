package de.dnb.music.title;

import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public class Arrangement implements TitleElement {

	private String arrangementPhrase;

	public Arrangement(String arrangementPhrase) {

		if (arrangementPhrase == null)
			throw new IllegalArgumentException("keine 2. Ordnungsgruppe");
		this.arrangementPhrase = arrangementPhrase;
	}

	@Override
	public String toString() {
		return arrangementPhrase;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

}
