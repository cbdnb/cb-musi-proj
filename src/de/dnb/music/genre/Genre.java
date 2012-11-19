package de.dnb.music.genre;

import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public class Genre implements TitleElement, Comparable<Genre> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idn == null) ? 0 : idn.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Genre)) {
			return false;
		}
		Genre other = (Genre) obj;
		if (idn == null) {
			if (other.idn != null) {
				return false;
			}
		} else if (!idn.equals(other.idn)) {
			return false;
		}
		return true;
	}

	public static enum Numeri {
		SINGULAR, PLURAL;
	}

	//	Felder
	String plural = "";

	String singular = "";

	String nid = "";

	String idn = "";

	/**
	 * match absolut notwendig, da bei zusammengesetzten Titeln 
	 * (Toccate, Variationen und Fuge) nur der Originalstring die
	 * korrekte Ansetzung ergibt. Dann sind in der Regel auch RAK und RSWK
	 * übereinstimmend.
	 */
	String match = "";

	String rest = "";

	String swd = "";

	public String getSwd() {
		return swd;
	}

	@Override
	public final String toString() {
		return swd;
	}

	public final String asSingular() {
		return singular;
	}

	public final String asPlural() {
		return plural;
	}

	public String getIdn() {
		return idn;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	@Override
	public int compareTo(final Genre o) {
		return this.swd.compareTo(o.swd);
	}

}
