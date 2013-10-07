package de.dnb.music.mediumOfPerformance;

import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.title.AugmentableElement;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public class Instrument implements TitleElement, Comparable<Instrument>,
		Cloneable {

	/**
	 * ABBREVIATED, WRITTEN_OUT.
	 * 
	 * @author baumann
	 *
	 */
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

	public final String getWrittenOut() {
		return writtenOut;
	}

	public final String toString(final AbbreviationMode mode) {
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

	public final String getSwd() {
		return swd;
	}

	public final int getCount() {
		return count;
	}

	public final void setCount(int c) {
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
	public final void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public final String getIdn() {
		return idn;
	}

	@Override
	public final int compareTo(final Instrument o) {
		return this.swd.compareTo(o.swd);
	}

	@Override
	public final void addToTitle(MusicTitle title) {
		/*
		 * Kann entweder der Fassung oder dem letzten Teil oder
		 * dem Titel selbst hinzugefügt werden.
		 */
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		if (!title.isAdditable(this))
			throw new UnsupportedOperationException(
					"Instrument darf nicht mehr hinzugefügt werden");
		AugmentableElement element = title.getActualAugmentable();
		element.addInstrument(this);
	}

	@Override
	public Instrument clone() throws CloneNotSupportedException {
		return (Instrument) super.clone();
	}

	public static void main(final String[] args) {
		MusicTitle mt = ParseMusicTitle.parse(null, "aa <bb>. Fassung Vl");
		Instrument instrument =
			ParseInstrumentation.parseSingleInstrument("Va");
		instrument.addToTitle(mt);
		System.out.println(TitleUtils.getStructured(mt));
	}

}
