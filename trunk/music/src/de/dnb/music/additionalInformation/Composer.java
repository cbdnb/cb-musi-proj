package de.dnb.music.additionalInformation;

public class Composer implements Comparable<Composer> {
	public Composer(
			final String name,
			final String thematicIndexAbb,
			final String sourceAbb,
			final String idn,
			final String countrCode) {
		this.name = name;
		this.thematicIndexAbb = thematicIndexAbb;
		this.sourceAbb = sourceAbb;
		this.idn = idn;
		this.countrCode = countrCode;
	}

	public final String name;

	public final String thematicIndexAbb;

	public final String sourceAbb;

	public final String idn;

	@Override
	public final String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idn == null) ? 0 : idn.hashCode());
		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Composer)) {
			return false;
		}
		Composer other = (Composer) obj;
		if (idn == null) {
			if (other.idn != null) {
				return false;
			}
		} else if (!idn.equals(other.idn)) {
			return false;
		}
		return true;
	}

	public final String countrCode;

	@Override
	public final int compareTo(final Composer o) {
		return this.name.compareTo(o.name);
	}
	
	@Override
	protected final void finalize() throws Throwable {
		System.err.println("Decomposing: Composer " + name);
	}

}
