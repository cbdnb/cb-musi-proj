package de.dnb.music.visitor;

import static utils.GNDConstants.TAG_DB;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.gnd.utils.RecordUtils;
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
			final boolean forceTotalCount) {
		this.forceTotalCount = forceTotalCount;
	}

	public AuthorityDataVisitor() {
	}

	private LinkedHashSet<Line> genres = new LinkedHashSet<Line>();

	private LinkedHashSet<Line> instruments = new LinkedHashSet<Line>();

	private LinkedHashSet<Line> obins = new LinkedHashSet<Line>();

	private int totalCount = 0; // Gesamtzahl aller Instrumente

	private boolean forceTotalCount = false;

	private boolean outputTotalCount = true;

	/**
	 * Benötigt, wenn in Teilwerk oder Fassung eine neue Instrumentenliste
	 * entdeckt wird.
	 */
	private void resetInstrumentation() {
		totalCount = 0;

		outputTotalCount = true;
		instruments.clear();
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
	}

	public final Collection<Line> getLines() {
		LinkedList<Line> lines = new LinkedList<Line>();
		lines.addAll(genres);
		lines.addAll(instruments);
		lines.addAll(obins);
		return lines;
	}

	@Override
	public final String toString() {
		return RecordUtils.toPicaExpanded(getLines());
	}

	@Override
	public final boolean visit(final GenreList genreList) {

		genres.clear();
		return true;
	}

	@Override
	public final void visit(final Genre genre) {
		if (!"Werke".equals(genre.asPlural())) {
			if (genre.getIdn().length() > 0) {

				String genreStr =
					"380 !" + genre.getIdn() + "!" + genre.getSwd();
				try {
					Line line = LineParser.parse(genreStr, TAG_DB);
					genres.add(line);
				} catch (IllFormattedLineException e) {
					// nix.
				}
			}
		}

	}

	/**
	 * Hart codiert, möglicherweise nicht gut.
	 */
	@Override
	public final boolean visit(final Version version) {
		String obin = "550 !042875420!Fassung$4obin";
		try {
			Line line = LineParser.parse(obin, TAG_DB);
			obins.clear();
			obins.add(line);
		} catch (IllFormattedLineException e) {
			// nix.
		}
		return true; // tiefer gehen, um Instrumente ... zu extrahieren.
	}

	/**
	 * Hart codiert, möglicherweise nicht gut.
	 */
	@Override
	public final void visit(final Arrangement arrangement) {
		String obin = "550 !041209818!Bearbeitung$4obin";
		try {
			Line line = LineParser.parse(obin, TAG_DB);
			obins.clear();
			obins.add(line);
		} catch (IllFormattedLineException e) {
			// nix.
		}
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

		String instrStr =
			"382 !" + instrument.getIdn() + "!" + instrument.getSwd();

		int count = instrument.getCount();
		if (count > 1) {

			instrStr += "$n" + count;
		} else
			outputTotalCount = false;
		if (forceTotalCount || count > 1)
			totalCount += count;
		try {
			Line line = LineParser.parse(instrStr, TAG_DB);
			instruments.add(line);
		} catch (IllFormattedLineException e) {
			// nix.
		}

	}

	@Override
	public final void leave(final InstrumentationList instrumentationList) {
		if (totalCount > 0)
			if (outputTotalCount || forceTotalCount) {
				String instrStr = "382 $s" + totalCount;
				try {
					Line line = LineParser.parse(instrStr, TAG_DB);
					instruments.add(line);
				} catch (IllFormattedLineException e) {
					// nix.
				}
			}
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parseFullRAK(null, "Adagio und Fuge, Vl Va 1 2 3"
				+ "<Fuge KV 5a, Durchführung 1>. Fassung Kl");
		AuthorityDataVisitor vis = new AuthorityDataVisitor();
		vis.setForceTotalCount(true);
		vis.setExpansion(false);
		mt.accept(vis);		

		System.out.println(vis.toString());

	}

}
