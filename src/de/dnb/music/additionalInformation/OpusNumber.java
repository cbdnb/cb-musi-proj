package de.dnb.music.additionalInformation;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import de.dnb.music.visitor.Visitor;

public class OpusNumber extends AdditionalInformation {

	/**
	 * Wird in {@link ParseAdditionalInformation} gesetzt.
	 */
	String op = null;

	/**
	 * Wird in {@link ParseAdditionalInformation} gesetzt.
	 */
	String zaehlung = null;

	public static final String[] OPERA = { "op.", "Op.", "WoO" };
	
	public static Set<String> getOperaPhrases(){
		return new TreeSet<String>(Arrays.asList(OPERA));
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String toString() {
		return op.trim() + " " + zaehlung;
	};

	public final String getOp() {
		return op;
	}

	public final String getOpusNumber() {
		return zaehlung;
	}

}
