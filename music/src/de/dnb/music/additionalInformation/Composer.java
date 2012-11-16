package de.dnb.music.additionalInformation;

public class Composer implements Comparable<Composer>{
	public Composer(
			String name,
			String thematicIndexAbb,
			String sourceAbb,
			String idn,
			String countrCode) {
		this.name = name;
		this.thematicIndexAbb = thematicIndexAbb;
		this.sourceAbb = sourceAbb;
		this.idn = idn;
		this.countrCode = countrCode;
	}

	final public String name;

	final public String thematicIndexAbb;

	final public String sourceAbb;

	final public String idn;

	@Override
	public String toString() {
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
	public boolean equals(Object obj) {
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

	final public String countrCode;

	@Override
	public int compareTo(final Composer o) {
		return this.name.compareTo(o.name);
	}
	
	@Override
	protected void finalize() throws Throwable {
		System.err.println("Decomposing: Composer " + name);
	}

}
