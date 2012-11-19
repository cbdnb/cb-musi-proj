package de.dnb.music.additionalInformation;

import de.dnb.music.visitor.Visitor;

public class DateOfComposition extends AdditionalInformation {

	/**
	 * Wird in {@link ParseAdditionalInformation} gesetzt.
	 */
	int date = 0;

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return "" + date;
	}

	public final int getDate() {
		return date;
	}

}
