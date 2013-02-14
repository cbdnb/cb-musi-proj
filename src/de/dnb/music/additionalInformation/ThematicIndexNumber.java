package de.dnb.music.additionalInformation;

import de.dnb.music.visitor.Visitor;

public class ThematicIndexNumber extends AdditionalInformation {

	/**
	 * Abkürzung des Werkverzeichnisses (z.B. BWV). Wird in 
	 * {@link DBWerkverzeichnisse} gesetzt.
	 */
	String abbreviation = null;

	/**
	 * Die Nummerierung des Werks im Werkverzeichnis (also die "1 - 4" in 
	 * "BWV 1 - 4"). Wird in {@link DBWerkverzeichnisse} gesetzt.
	 */
	String number = null;

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return abbreviation + " " + number;
	}

	public final String getNumber() {
		return number;
	}

	public final String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Ersetzt etwa "1 - 4" durch "1-4". Das kommt daher (Beschluss Fr.
	 * Ellermann), dass in der 130 die Blanks stehen sollen, aber nicht in
	 * der 383.
	 * @return String != null, ""
	 */
	public final String getNumberWithoutBlanks() {
		if (number == null)
			throw new IllegalArgumentException("WV-Nummer nicht initialisiert");
		String withoutBlank = number.replace(" - ", "-");
		return withoutBlank;
	}

	public final String toStringWithoutBlanks() {
		return abbreviation + " " + getNumberWithoutBlanks();
	}
}
