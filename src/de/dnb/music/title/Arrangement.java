package de.dnb.music.title;

import java.io.Serializable;

import de.dnb.basics.applicationComponents.RangeCheckUtils;
import de.dnb.music.utils.TitleUtils;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public class Arrangement implements TitleElement, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7382022301660193100L;
	private String arrangementPhrase;

	public Arrangement(final String arrangementPhrase) {

		if (arrangementPhrase == null)
			throw new IllegalArgumentException("keine 2. Ordnungsgruppe");
		this.arrangementPhrase = arrangementPhrase;
	}

	@Override
	public final String toString() {
		return arrangementPhrase;
	}

	@Override
	public final void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public final void addToTitle(final MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		title.setArrangement(this);
	}

	public static void main(final String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parse(null, "aa <Adagio, bb>. cc");
		Arrangement arrangement = new Arrangement("Ausw.");
		arrangement.addToTitle(mt);
		System.out.println(TitleUtils.getStructured(mt));
	}

}
