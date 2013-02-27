package de.dnb.music.mediumOfPerformance;

import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public class Instrument implements TitleElement, Comparable<Instrument> {

	public static enum AbbreviationMode {
		ABBREVIATED, WRITTEN_OUT;
	}

	//Felder sind paketöffentlich, da ParseBesetzung darauf zurückgreift.
	/**
	 * Abkürzung nach RAK-M Anh.
	 */
	String abbreviated;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idn == null) ? 0 : idn.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Instrument)) {
			return false;
		}
		Instrument other = (Instrument) obj;
		if (idn == null) {
			if (other.idn != null) {
				return false;
			}
		} else if (!idn.equals(other.idn)) {
			return false;
		}
		return true;
	}

	/**
	 * Ausgeschrieben nach RAK-M Anh.
	 */
	String writtenOut;

	public String getAbbreviated() {
		return abbreviated;
	}

	public String getWrittenOut() {
		return writtenOut;
	}

	public String toString(AbbreviationMode mode) {
		switch (mode) {
		case ABBREVIATED:
			return getAbbreviated();

		case WRITTEN_OUT:
			return writtenOut;

		default:
			throw new IllegalArgumentException(
					"Abbreviation mode not supported");
		}
	}

	/**
	 * die SWD-Form kann durch Homonymzusätze von der ausgeschriebenen
	 * Form abweichen.
	 */
	String swd;

	String idn;

	public String getSwd() {
		return swd;
	}

	public int getCount() {
		return count;
	}
	
	public void setCount(int c) {
		count = c;
	}

	String nid;

	/**
	 * Zahl gleichartiger Instrumente. 
	 */
	int count = 1;

	//	String match = "";

	String rest = "";

	@Override
	public final String toString() {
		return swd;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getIdn() {
		return idn;
	}

	@Override
	public int compareTo(Instrument o) {
		return this.swd.compareTo(o.swd);
	}

	//	
}
