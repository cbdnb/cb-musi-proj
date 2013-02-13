package utils;

import de.dnb.music.publicInterface.TransformRecord;
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
		MusicTitle mt = ParseMusicTitle.parse(null, titleStr);
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
		MusicTitle mt = ParseMusicTitle.parse(null, titleStr);
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
		return "130 " + getGND130Or430(musicTitle);
	}

	/**
	 * Liefert die 3XX-Felder.
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
			new AuthorityDataVisitor(expansion, forceTotalCount);
		final AdditionalDataIn3XXVisitor advis =
			new AdditionalDataIn3XXVisitor();
		element.accept(auvis);
		element.accept(advis);
		return auvis.toString() + advis.toString();

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
				"\n430 " + getRAK(musicTitle) + "$v"
					+ TransformRecord.KOM_PORTAL_430;
		s += "\n" + getGND530(musicTitle, true);
		return s;
	}

	public static String getFullGND(final MusicTitle musicTitle) {
		final boolean expansion = false;
		final boolean forceTotalCount = false;
		return getFullGND(musicTitle, expansion, forceTotalCount);
	}

	/**
	 * Liefert die GND-Form des Titels (mit Unterfeldern, aber ohne 130).
	 * @param element TitleElement nicht null.
	 * @return GND-Titel als String.
	 */
	public static String getGND130Or430(final TitleElement element) {
		if (element == null)
			throw new IllegalArgumentException("Titel ist null");
		WorkTitleVisitor vis = new WorkTitleVisitor(new GNDParticleFactory());
		element.accept(vis);
		return vis.toString();
	}

	public static String getGND(final String titleStr) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		MusicTitle mt = ParseMusicTitle.parse(null, titleStr);
		return getGND130Or430(mt);
	}

	/**
	 * Ermittelt, ob titleStr nach GND-Regeln gebildet ist.
	 * @param titleStr Titel nicht null.
	 * @return	true, wenn titleStr nach GND-Regeln gebildet ist, false sonst.
	 */
	public static boolean isGND(String titleStr) {
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
		MusicTitle mt = ParseMusicTitle.parse(null, titleStr);
		return isIdenticalWithinRules(titleStr, mt, rulesFactory);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.err.println(isGND(" "));
	}

}
