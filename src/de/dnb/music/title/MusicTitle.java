package de.dnb.music.title;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.genre.GenreList;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.version.Version;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public abstract class MusicTitle implements TitleElement {

	// Bestandteile eines Titels nach RAK-M
	protected GenreList genreList = null;

	protected InstrumentationList instrumentationList = null;

	/**
	 * Auch: unterscheidende Angabe nach RAK-M §505, 6.
	 * Diese entspricht der zusätzlichen Angabe. Es kann aber auch ein
	 * Gattungsbegriff (Motette) folgen.
	 */
	protected AdditionalInformation additionalInformation = null;

	// Anzeige nach RAK in Winkelklammern (Ordnungshilfe)
	protected PartOfWork partOfWork = null;

	// Folgt in RAK nach: ". "
	protected Version version = null;

	//	In der Form: " / Arr."
	protected Arrangement arrangement = null;

	protected Comment comment = null;

	//----------------------------------------------

	public final GenreList getGenreList() {
		return genreList;
	}

	public final boolean containsGenre() {
		return genreList != null;
	}

	public final InstrumentationList getInstrumentationList() {
		return instrumentationList;
	}

	final void setInstrumentationList(final InstrumentationList iList) {
		instrumentationList = iList;
	}

	public final boolean containsInstrumentation() {
		return instrumentationList != null;
	}

	public final AdditionalInformation getAdditionalInformation() {
		return additionalInformation;
	}

	final void setAdditionalInformation(final AdditionalInformation ai) {
		additionalInformation = ai;
	}

	public final boolean containsAdditionalInformation() {
		return additionalInformation != null;
	}

	public final PartOfWork getPartOfWork() {
		return partOfWork;
	}

	final void setPartOfWork(final PartOfWork teil) {
		this.partOfWork = teil;
	}

	public final boolean containsParts() {
		return partOfWork != null;
	}

	public final Version getVersion() {
		return version;
	}

	final void setVersion(final Version ver) {
		this.version = ver;
	}

	public final boolean containsVersion() {
		return version != null;
	}

	public final Arrangement getArrangement() {
		return arrangement;
	}

	final void setArrangement(final Arrangement arrangement) {
		this.arrangement = arrangement;
	}

	public final boolean containsArrangement() {
		return arrangement != null;
	}

	public final Comment getComment() {
		return comment;
	}

	/**
	 * Setzt den Kommentar.
	 * 
	 * @param comment	Kommentar.
	 * 
	 * Die Methode muss öffentlich sein, da sie im Laufe des 
	 * Transformationsprozesses aufgerufen wird.
	 */
	public final void setComment(final Comment comment) {
		this.comment = comment;
	}

	public boolean containsCommment() {
		return comment != null;
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
		if (containsCommment())
			comment.accept(visitor);
	}
	
	public abstract MusicTitle clone();

}
