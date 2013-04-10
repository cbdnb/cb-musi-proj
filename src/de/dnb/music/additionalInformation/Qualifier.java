package de.dnb.music.additionalInformation;

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
	public final void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return qualifier;
	}

}
