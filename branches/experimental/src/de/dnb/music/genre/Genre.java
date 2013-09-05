package de.dnb.music.genre;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.title.AugmentableElement;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.PartOfWork;
import de.dnb.music.version.Version;
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
	public boolean equals(final Object obj) {
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

	public final String toString(final Numeri numerus) {
		switch (numerus) {
		case PLURAL:
			return plural;
		case SINGULAR:
			return singular;
		default:
			throw new IllegalArgumentException("numerus unbekannt oder null");
		}
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

	@Override
	public void addToTitle(MusicTitle title) {
		/*
		 * Kann entweder der Fassung oder dem letzten Teil oder
		 * dem Titel selbst hinzugefügt werden.
		 */
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		if (!title.isAdditable(this))
			throw new UnsupportedOperationException(
					"Titel enthält schon etwas anderes als "
						+ this.getClass().getSimpleName());
		GenreList instrumList = null;
		AugmentableElement element = title.getActualAugmentable();
		if (element.containsGenre()) {
			instrumList = element.getGenreList();
			instrumList.add(this);
		} else {
			element.setGenre(new GenreList(this));
		}
	}

	public static void main(final String[] args) {
		Genre g = GenreDB.matchGenre("Fuge");
		System.out.println(g.toString(Numeri.SINGULAR));
	}

}
