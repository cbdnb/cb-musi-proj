package de.dnb.music.title;

import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.version.Version;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public abstract class MusicTitle extends AugmentableElement implements TitleElement {

	// Anzeige nach RAK in Winkelklammern (Ordnungshilfe)
	protected PartOfWork partOfWork = null;

	// Folgt in RAK nach: ". "
	protected Version version = null;

	//	In der Form: " / Arr."
	protected Arrangement arrangement = null;

	//----------------------------------------------

	public final PartOfWork getPartOfWork() {
		return partOfWork;
	}

	public final void setPartOfWork(final PartOfWork part) {
		this.partOfWork = part;
	}

	public final boolean containsParts() {
		return partOfWork != null;
	}

	public final Version getVersion() {
		return version;
	}

	public final void setVersion(final Version ver) {
		this.version = ver;
	}

	public final boolean containsVersion() {
		return version != null;
	}

	public final Arrangement getArrangement() {
		return arrangement;
	}

	public final void setArrangement(final Arrangement arrangement) {
		this.arrangement = arrangement;
	}

	public final boolean containsArrangement() {
		return arrangement != null;
	}

	public final void visitChildren(final Visitor visitor) {
		if (containsGenre())
			genreList.accept(visitor);
		if (containsInstrumentation())
			instrumentationList.accept(visitor);
		if (containsAdditionalInformation())
			additionalInformation.accept(visitor);
		if (containsParts())
			partOfWork.accept(visitor);
		if (containsVersion())
			version.accept(visitor);
		if (containsArrangement())
			arrangement.accept(visitor);

	}

	
	@Override
	/**
	 * this kann eigentlich nur ein Werkteil von title sein.
	 */
	public void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		PartOfWork newPart = new PartOfWork(this);
		newPart.addToTitle(title);
	}
	
	public static void main(final String[] args) {
		MusicTitle mt1 = ParseMusicTitle.parseSimpleTitle(null, "aa");
		MusicTitle mt2 = ParseMusicTitle.parseSimpleTitle(null, "bb");		
		mt2.addToTitle(mt1);
		System.out.println(TitleUtils.getRAK(mt1));
	}

	public abstract MusicTitle clone();

}
