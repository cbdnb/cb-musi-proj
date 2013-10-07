package de.dnb.music.additionalInformation;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import de.dnb.music.visitor.Visitor;

public class SerialNumber extends AdditionalInformation {

	/**
	 * "Fortlaufende Zählung" nach GND.
	 * Wird in {@link ParseAdditionalInformation} gesetzt.
	 */
	String phrase = "";

	/**
	 * Wird in {@link ParseAdditionalInformation} gesetzt.
	 */
	int sonstZahl = 0;

	public static final String[] PHRASES = { "Nr.", "ordre", "Buch", "libro",
		"livre", "liber", "tomus", "Zyklus", "no.", "No.", "Teil", "Volume",
		"Theil", "Werk", "Vol.", "production", "Bd.", "conatum", "pars", "ps.",
		"t.", "fasc.", "Abt.", "Folge", "pt.", "parte", "H.", "Heft", "set",
		"Sammlung", "book", "livraison" };

	public static Set<String> getSerialNumberPhrases() {
		return new TreeSet<String>(Arrays.asList(PHRASES));
	}

	@Override
	public final void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public final String toString() {
		return phrase + sonstZahl;
	}

	public final int getSerialNumber() {
		return sonstZahl;
	}

	public final String getPhrase() {
		return phrase;
	}

}
