package utils;

import java.util.Collection;
import java.util.List;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.Tag;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineFactory;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.music.publicInterface.Constants;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.visitor.AdditionalDataIn3XXVisitor;
import de.dnb.music.visitor.AuthorityDataVisitor;
import de.dnb.music.visitor.StructuredVisitor;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.setsOfRules.AbstractParticleFactory;
import de.dnb.music.visitor.setsOfRules.DNBPortal430Visitor;
import de.dnb.music.visitor.setsOfRules.GNDParticleFactory;
import de.dnb.music.visitor.setsOfRules.RAKParticleFactory;
import de.dnb.music.visitor.setsOfRules.RSWKParticleFactory;
import de.dnb.music.visitor.setsOfRules.RSWKSubfieldFactory;
import de.dnb.music.visitor.setsOfRules.WorkTitleVisitor;

public final class TitleUtils {

	private TitleUtils() {
	}

	public static String getRSWK(final MusicTitle musicTitle) {
		final WorkTitleVisitor vis =
			new WorkTitleVisitor(new RSWKParticleFactory());
		musicTitle.accept(vis);
		return vis.toString();
	}

	public static String getRSWK(final String titleStr) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		MusicTitle mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		return getRSWK(mt);
	}

	/**
	 * Setzt die vermutete RSWK-Ansetzung in GND-Unterfelder. 
	 * 
	 * @param musicTitle nicht null.
	 * @return	RSWK-Form mit GND-Unterfeldern.
	 */
	public static String getRSWKInSubfields(final MusicTitle musicTitle) {
		WorkTitleVisitor vis = new WorkTitleVisitor(new RSWKSubfieldFactory());
		musicTitle.accept(vis);
		return vis.toString();
	}

	/**
	 * Ermittelt, ob titleStr nach RSWK-Regeln gebildet ist.
	 * @param titleStr Titel nicht null.
	 * @return	true, wenn titleStr nach RSWK-Regeln gebildet ist, false sonst.
	 */
	public static boolean isRSWK(final String titleStr) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		return isConformToRules(titleStr, new RSWKParticleFactory());
	}

	public static String getRAK(final TitleElement element) {
		final WorkTitleVisitor vis =
			new WorkTitleVisitor(new RAKParticleFactory());
		element.accept(vis);
		return vis.toString();
	}

	public static String getRAK(final String titleStr) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		MusicTitle mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		return getRAK(mt);
	}

	/**
	 * Ermittelt, ob titleStr nach RAK-Regeln gebildet ist.
	 * @param titleStr Titel nicht null.
	 * @return	true, wenn titleStr nach RAK-Regeln gebildet ist, false sonst.
	 */
	public static boolean isRAK(final String titleStr) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		return isConformToRules(titleStr, new RAKParticleFactory());
	}

	public static String getStructured(final TitleElement element) {
		final StructuredVisitor sVis = new StructuredVisitor();
		element.accept(sVis);
		return sVis.toString();
	}

	public static String getGND1XXPlusTag(final MusicTitle musicTitle) {
		return "130 " + getX30ContentAsString(musicTitle);
	}

	/**
	 * Liefert die 3XX-Felder als String.
	 * 
	 * @param element	TitleElement
	 * @param expansion		Normdaten expandiert
	 * @param forceTotalCount	Gesamtzahl bei Instrumenten erzwingen
	 * @return 3XX-Felder der GND inclusive 548 (Zeitangabe)
	 */
	public static String getGND3XX(
			final TitleElement element,
			final boolean expansion,
			final boolean forceTotalCount) {
		final AuthorityDataVisitor auvis =
			new AuthorityDataVisitor(forceTotalCount);
		final AdditionalDataIn3XXVisitor advis =
			new AdditionalDataIn3XXVisitor();
		element.accept(auvis);
		element.accept(advis);
		String s = GNDUtils.toPica(auvis.getLines(), Format.PICA3, expansion);
		return s + advis.toString();

	}

	/**
	 * Liefert die 3XX-Felder.
	 * 
	 * @param element	TitleElement
	 * @param forceTotalCount	Gesamtzahl bei Instrumenten erzwingen
	 * @return 3XX-Felder der GND inclusive 548 (Zeitangabe)
	 */
	public static Collection<Line> get3XXLines(
			final TitleElement element,
			final boolean forceTotalCount) {
		RangeCheckUtils.assertReferenceParamNotNull("element", element);
		final AuthorityDataVisitor auvis =
			new AuthorityDataVisitor(forceTotalCount);
		final AdditionalDataIn3XXVisitor advis =
			new AdditionalDataIn3XXVisitor();
		element.accept(auvis);
		element.accept(advis);
		Collection<Line> lines = auvis.getLines();
		lines.addAll(advis.getLines());
		return lines;

	}

	public static String getGND3XX(final TitleElement element) {
		final boolean expansion = false;
		final boolean forceTotalCount = false;
		return getGND3XX(element, expansion, forceTotalCount);
	}

	@Deprecated
	public static String getDNBPortal4XX(final TitleElement element) {
		final DNBPortal430Visitor vis = new DNBPortal430Visitor();
		element.accept(vis);
		return "430 " + vis.toString();
	}

	public static String getGND530(
			final MusicTitle musicTitle,
			final boolean getRelatorCode) {
		final GNDParticleFactory factory = new GNDParticleFactory();
		final WorkTitleVisitor vis = new WorkTitleVisitor(factory);
		musicTitle.accept(vis);
		final String related = vis.relatedWork();
		if (related.length() != 0) {
			String s = "530 " + related;
			if (getRelatorCode)
				return s + factory.getRelatorCode(vis.getState());
			else
				return s;
		} else
			return "";
	}

	public static String getFullGND(
			final MusicTitle musicTitle,
			final boolean expansion,
			final boolean forceTotalCount) {
		String s =
			getGND1XXPlusTag(musicTitle) + "\n"
				+ getGND3XX(musicTitle, expansion, forceTotalCount);
		if (musicTitle.containsParts())
			s +=
				"\n430 " + getRAK(musicTitle) + "$v" + Constants.KOM_PORTAL_430;
		s += "\n" + getGND530(musicTitle, true);
		return s;
	}

	public static String getFullGND(final MusicTitle musicTitle) {
		final boolean expansion = false;
		final boolean forceTotalCount = false;
		return getFullGND(musicTitle, expansion, forceTotalCount);
	}

	/**
	 * Liefert die GND-Form des Titels ohne Tag und ohne führendes $a.
	 * 
	 * @param element TitleElement nicht null.
	 * @return GND-Titel als String.
	 */
	public static String getX30ContentAsString(final TitleElement element) {
		if (element == null)
			throw new IllegalArgumentException("Titel ist null");
		WorkTitleVisitor vis = new WorkTitleVisitor(new GNDParticleFactory());
		element.accept(vis);
		return vis.toString();
	}

	public static String getX30ContentAsString(final String titleStr) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		MusicTitle mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		return getX30ContentAsString(mt);
	}

	public static Line getLine(final Tag tag, final String titleStr)
			throws IllFormattedLineException {
		RangeCheckUtils.assertReferenceParamNotNull("tag", tag);
		RangeCheckUtils.assertStringParamNotNullOrWhitespace("titleStr",
				titleStr);
		String gnd = getX30ContentAsString(titleStr);
		Line line = LineParser.parse(tag, Format.PICA3, gnd);
		return line;
	}

	/**
	 * Liefert eine Zeile zu Tag und Titel.
	 * 
	 * @param tag		nicht null.
	 * @param title		nicht null.
	 * @return			nicht null.
	 */
	public static Line getLine(final Tag tag, final MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("tag", tag);
		RangeCheckUtils.assertReferenceParamNotNull("element", title);
		String gnd = getX30ContentAsString(title);
		Line line = null;
		try {
			line = LineParser.parse(tag, Format.PICA3, gnd);
		} catch (IllFormattedLineException e) {
			// OK
		}
		return line;
	}

	/**
	 * Gibt basierend auf dem Tag 130 eine Liste der Unterfelder.
	 * 
	 * @param title	nicht null.
	 * @return		nicht null, nicht leer.
	 */
	public static List<Subfield> getSubfields(final MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		String gnd = getX30ContentAsString(title);
		final LineFactory factory = LineParser.getFactory("130");
		try {
			factory.load(gnd);
		} catch (IllFormattedLineException e) {
			// nix
		}
		return factory.getSubfieldList();
	}

	/**
	 * Ermittelt, ob titleStr nach GND-Regeln gebildet ist.
	 * @param titleStr Titel nicht null.
	 * @return	true, wenn titleStr nach GND-Regeln gebildet ist, false sonst.
	 */
	public static boolean isGND(final String titleStr) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		return isConformToRules(titleStr, new GNDParticleFactory());
	}

	/**
	 * Überprüft, ob der übergebene titelStr nach den Regeln von
	 * rulesFactory aus element abgeleitet werden kann.
	 * 
	 * @param titleStr		nicht null, nicht nur Blanks, kann mit $a beginnen.
	 * 						Vor und nach $a können Blanks stehen, vor und nach 
	 * 						allen anderen Unterfelder nicht.
	 * @param element		Titel(element), das i.d.R. durch Parsen von
	 * 						titleStr gewonnen wird.
	 * @param rulesFactory	Menge von Regeln (z.Z. RAK, RSWK, GND)
	 * @return				titleStr == element.
	 */
	public static boolean isIdenticalWithinRules(
			String titleStr,
			final TitleElement element,
			final AbstractParticleFactory rulesFactory) {

		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		titleStr = titleStr.trim();
		if (titleStr.length() == 0)
			throw new IllegalArgumentException(
					"übergebener Titel hat nur Blanks");
		final String dolA = "$a";
		if (titleStr.startsWith(dolA))
			titleStr = (titleStr.substring(dolA.length())).trim();

		WorkTitleVisitor vis = new WorkTitleVisitor(rulesFactory);
		element.accept(vis);
		String newTitle = vis.toString();

		return titleStr.equals(newTitle);

	}

	public static boolean isConformToRules(
			final String titleStr,
			final AbstractParticleFactory rulesFactory) {

		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		MusicTitle mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		return isIdenticalWithinRules(titleStr, mt, rulesFactory);

	}

	/**
	 * @param args
	 * @throws IllFormattedLineException 
	 */
	public static void main(String[] args) throws IllFormattedLineException {
		MusicTitle title =
			ParseMusicTitle.parseFullRAK(null,
					"Adagio und Fuge Vl 1 2 Va KV 5 "
						+ "<Fuge KV 5a, Durchführung 1>. Fassung Kl / Arr.");
		System.out.println(getSubfields(title));
	}
}
