package de.dnb.music.visitor;

import de.dnb.music.genre.Genre;
import de.dnb.music.genre.GenreList;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.title.Arrangement;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.version.Version;

public class AuthorityDataVisitor extends Visitor {

	public AuthorityDataVisitor(
			final boolean expansion,
			final boolean forceTotalCount) {
		this.expansion = expansion;
		this.forceTotalCount = forceTotalCount;
	}

	public AuthorityDataVisitor() {
	}

	private String genreIn380 = "";

	private String instrIn382 = "";

	private String relatedIn550 = ""; // Sachschlagwort in 550

	private int totalCount = 0; // Gesamtzahl aller Instrumente

	private boolean expansion = false;

	private boolean forceTotalCount = false;

	private boolean outputTotalCount = true;

	/**
	 * Benötigt, wenn in Teilwerk oder Fassung eine neue Instrumentenliste
	 * entdeckt wird.
	 */
	private void resetInstrumentation() {
		totalCount = 0;
		instrIn382 = "";
		outputTotalCount = true;
	}

	/**
	 * Erzwingt die Ausgabe der Gesamtbesetzungszahl. Das kann bei
	 * Chören oder Orchestern zu Unsinn führen.
	 * 
	 * @param forceTotalNumber
	 */
	public final void setForceTotalCount(final boolean forceTotalNumber) {
		this.forceTotalCount = forceTotalNumber;
	}

	/**
	 * Bestimmt, ob neben der IDN auch die Expansion ausgegeben wird.
	 * @param expansion
	 */
	public final void setExpansion(final boolean expansion) {
		this.expansion = expansion;
	}

	@Override
	public final String toString() {
		String s = "";
		s += genreIn380 + instrIn382 + relatedIn550;
		return s;
	}

	@Override
	public final boolean visit(final GenreList genreList) {
		genreIn380 = "";
		return true;
	}

	@Override
	public final void visit(final Genre genre) {
		if (genre.getIdn().length() > 0)
			genreIn380 +=
				"\n380 !" + genre.getIdn() + "!"
					+ (expansion ? genre.getSwd() : "");

	}

	/**
	 * Hart codiert, möglicherweise nicht gut.
	 */
	@Override
	public final boolean visit(final Version version) {
		relatedIn550 =
			"\n550 !042875420!" + (expansion ? "Fassung" : "") + "$4obin";
		return true; // tiefer gehen, um Instrumente ... zu extrahieren.
	}

	/**
	 * Hart codiert, möglicherweise nicht gut.
	 */
	@Override
	public final void visit(final Arrangement arrangement) {
		relatedIn550 =
			"\n550 !041209818!" + (expansion ? "Bearbeitung" : "") + "$4obin";
	}

	@Override
	public final boolean visit(final InstrumentationList instrumentationList) {
		resetInstrumentation();
		return true;
	}

	@Override
	public final void visit(final Instrument instrument) {
		String instrumString = instrument.getAbbreviated();

		/* Kl 4hdg. etc behandeln: nur eine neue 382, wenn nicht
		   irgendwas mit "Hand" im Spiel ist. 
		*/
		if (instrumString.contains("hdg") || instrumString.contains("linke")
			|| instrument.getIdn().length() == 0)
			return;

		instrIn382 +=
			"\n382 !" + instrument.getIdn() + "!"
				+ (expansion ? instrument.getSwd() : "");

		int count = instrument.getCount();
		if (count > 1) {
			instrIn382 += "$n" + count;
		} else
			outputTotalCount = false;
		if (forceTotalCount || count > 1)
			totalCount += count;

	}

	@Override
	public final void leave(final InstrumentationList instrumentationList) {
		if (totalCount > 0)
			if (outputTotalCount || forceTotalCount)
				instrIn382 += "\n382 $s" + totalCount;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parse(null, "Adagio und Fuge, Vl Va 1 2 3"
				+ "<Fuge KV 5a, Durchführung 1>. Fassung Kl");
		AuthorityDataVisitor vis = new AuthorityDataVisitor();
		vis.setExpansion(true);
		vis.setForceTotalCount(true);
		mt.accept(vis);

		System.out.println(vis);

	}

}
