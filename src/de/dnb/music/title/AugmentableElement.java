package de.dnb.music.title;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.genre.GenreList;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.visitor.Visitor;

/**
 *
 * Element, dem Gattung, Instrumente und Zusatzangaben hinzugefügt
 * werden können, also {@link FormalTitle}, {@link IndividualTitle}
 * und {@link Version}. Enthält alle Elemente und Methoden, die die
 * Baumstruktur verwalten.
 * 
 * @author baumann
 *
 */
public class AugmentableElement {

	public AugmentableElement() {
		super();
	}

	protected GenreList genreList = null;
	protected InstrumentationList instrumentationList = null;
	/**
	 * Auch: unterscheidende Angabe nach RAK-M §505, 6.
	 * Diese entspricht der zusätzlichen Angabe. Es kann aber auch ein
	 * Gattungsbegriff (Motette) folgen.
	 */
	protected AdditionalInformation additionalInformation = null;

	public final GenreList getGenreList() {
		return genreList;
	}

	public final boolean containsGenre() {
		return genreList != null;
	}

	public final void setGenre(final GenreList other) {
		genreList = other;
	}

	public final InstrumentationList getInstrumentationList() {
		return instrumentationList;
	}

	public final void setInstrumentationList(final InstrumentationList iList) {
		instrumentationList = iList;
	}

	public final boolean containsInstrumentation() {
		return instrumentationList != null;
	}

	public final AdditionalInformation getAdditionalInformation() {
		return additionalInformation;
	}

	public final void setAdditionalInformation(final AdditionalInformation ai) {
		additionalInformation = ai;
	}

	public final boolean containsAdditionalInformation() {
		return additionalInformation != null;
	}

	/**
	 * @param visitor
	 */
	protected void visitChildren(final Visitor visitor) {
		if (containsGenre()) {
			genreList.accept(visitor);
		}
		if (containsInstrumentation()) {
			instrumentationList.accept(visitor);
		}
		if (containsAdditionalInformation()) {
			additionalInformation.accept(visitor);
		}
	}

}