package de.dnb.music.additionalInformation;

import de.dnb.basics.applicationComponents.RangeCheckUtils;
import de.dnb.music.title.AugmentableElement;
import de.dnb.music.title.IndividualTitle;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.utils.TitleUtils;
import de.dnb.music.visitor.Visitor;

/**
 * Für den (Homonymzusatz). Taucht nur bei Individualsachtiteln
 * und auch dann nur bei RSWK-Daten auf.
 * 
 * @author baumann
 *
 */
public class Qualifier extends AdditionalInformation {

	/**
	 * Diese Methode funktioniert immer, muss daher mit
	 * Vorsicht gehandhabt werden.
	 * 
	 * @param aQualifier
	 */
	public Qualifier(final String aQualifier) {
		this.qualifier = aQualifier;
	}

	private final String qualifier;

	/**
	 * @return the qualifier
	 */
	public final String getQualifier() {
		return qualifier;
	}

	@Override
	public final void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public final String toString() {
		return qualifier;
	}

	@Override
	public final void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		AugmentableElement element = title.getActualAugmentable();
		if (element instanceof IndividualTitle)
			element.addAdditionalInformation(this);
		else
			throw new IllegalArgumentException(
					"Darf nur zu Individualsachtitel hinzugefügt werden");
	}

	public static void main(final String[] args) {
		MusicTitle mt = ParseMusicTitle.parseGND(null, "aa");
		AdditionalInformation aI =
			new Qualifier("ff");
		aI.addToTitle(mt);
		System.out.println(TitleUtils.getStructured(mt));
	}
}
