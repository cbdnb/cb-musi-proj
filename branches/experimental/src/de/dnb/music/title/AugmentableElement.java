package de.dnb.music.title;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.genre.Genre;
import de.dnb.music.genre.GenreList;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.visitor.TitleElement;
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

	public final void setGenreList(final GenreList other) {
		genreList = other;
	}

	public final void addGenre(final Genre genre) {
		RangeCheckUtils.assertReferenceParamNotNull("genre", genre);
		if (!isAdditable(genre))
			throw new UnsupportedOperationException(
					"Enthält schon etwas anderes als "
						+ genre.getClass().getSimpleName());
		if (containsGenre()) {
			genreList.add(genre);
		} else {
			setGenreList(new GenreList(genre));
		}
	}

	public final void addGenreList(final GenreList genreList) {
		RangeCheckUtils.assertReferenceParamNotNull("genreList", genreList);
		for (Genre genre : genreList.getChildren()) {
			addGenre(genre);
		}
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

	public final void addInstrument(final Instrument instrument) {
		RangeCheckUtils.assertReferenceParamNotNull("instrument", instrument);
		if (!isAdditable(instrument))
			throw new UnsupportedOperationException(
					"Enthält schon etwas anderes als "
						+ instrument.getClass().getSimpleName());
		if (containsInstrumentation()) {
			instrumentationList.add(instrument);
		} else {
			setInstrumentationList((new InstrumentationList(instrument)));
		}
	}

	public final void addInstrumentationListList(
			final InstrumentationList instrumentationList) {
		RangeCheckUtils.assertReferenceParamNotNull("instrumentationList",
				instrumentationList);
		for (Instrument instrument : instrumentationList.getChildren()) {
			addInstrument(instrument);
		}
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

	public final void addAdditionalInformation(
			final AdditionalInformation additionalInformation) {
		RangeCheckUtils.assertReferenceParamNotNull("additionalInformation",
				additionalInformation);
		if (!isAdditable(additionalInformation))
			throw new UnsupportedOperationException(
					"Enthält schon etwas anderes als "
						+ additionalInformation.getClass().getSimpleName());
		if (containsAdditionalInformation())
			throw new UnsupportedOperationException(
					"Titel enthält schon eine Zusatzinformation ");
		setAdditionalInformation(additionalInformation);

	}

	/**
	 * Darf das Element hinzugefügt werden?
	 * 
	 * @param element	nicht null.
	 * @return			true, wenn hinzugefügt werden darf
	 */
	public boolean isAdditable(TitleElement element) {
		RangeCheckUtils.assertReferenceParamNotNull("element", element);
		// default:
		return true;
	}

	/**
	 * Enthält keine Informationen?
	 * 
	 * @return true, wenn additionalInformation == null, 
	 * 			genreList == null und
				instrumentationList == null
	 */
	public final boolean containesNoAdditables() {
		return (additionalInformation == null) && (genreList == null)
			&& (instrumentationList == null);
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