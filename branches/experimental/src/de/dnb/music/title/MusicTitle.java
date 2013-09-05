package de.dnb.music.title;

import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.version.Version;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public abstract class MusicTitle extends AugmentableElement implements
		TitleElement {

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
		// Gattung, Instrumente und Zusatz-Info:
		super.visitChildren(visitor);
		if (containsParts())
			partOfWork.accept(visitor);
		if (containsVersion())
			version.accept(visitor);
		if (containsArrangement())
			arrangement.accept(visitor);
	}

	/**
	 * Liefert das Element (diesen Titel selbst, das unterste Werkteil
	 * oder die Fassung), an das zur Zeit Gattungen, Instrumente und
	 * Zusatzinformationen angehängt werden dürfen.
	 * 
	 * @return	nicht null
	 */
	public AugmentableElement getActualAugmentable() {
		AugmentableElement element;
		if (containsVersion()) {
			element = getVersion();
		} else if (containsParts()) {
			PartOfWork partOfWork = getPartOfWork();
			element = partOfWork.getLast();
		} else {
			element = this;
		}
		return element;
	}

	@Override
	/**
	 * this kann eigentlich nur ein Werkteil von title sein.
	 */
	public void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		if (title.containsParts()) {
			PartOfWork titlesParts = title.getPartOfWork();
			titlesParts.add(this);
		} else {
			title.setPartOfWork(new PartOfWork(this));
		}
	}

	public static void main(final String[] args) {
		MusicTitle mt1 = ParseMusicTitle.parseSimpleTitle(null, "aa");
		MusicTitle mt2 = ParseMusicTitle.parseSimpleTitle(null, "bb");
		mt2.addToTitle(mt1);
		System.out.println(TitleUtils.getRAK(mt1));
	}

}
