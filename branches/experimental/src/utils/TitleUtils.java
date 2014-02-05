package utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;

import de.dnb.basics.applicationComponents.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.music.additionalInformation.Composer;
import de.dnb.music.genre.GenreList;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.publicInterface.Constants;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.visitor.AdditionalDataIn3XXVisitor;
import de.dnb.music.visitor.AuthorityDataAlephVisitor;
import de.dnb.music.visitor.StructuredVisitor;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.setsOfRules.AbstractParticleFactory;
import de.dnb.music.visitor.setsOfRules.GNDParticleFactory;
import de.dnb.music.visitor.setsOfRules.RAKParticleFactory;
import de.dnb.music.visitor.setsOfRules.RSWKParticleFactory;
import de.dnb.music.visitor.setsOfRules.RSWKSubfieldFactory;
import de.dnb.music.visitor.setsOfRules.WorkTitleVisitor;

/**
 * Utilities, die aus Titelelementen Strings oder bools machen.
 * 
 * @author baumann
 *
 */
public final class TitleUtils {

	private TitleUtils() {
	}

	/**
	 * Liefert die RSWK-Darstellung eines Titel-Elementes.
	 * @param element	nicht null
	 * @return			Titel als RSWK, soweit möglich.
	 */
	public static String getRSWK(final TitleElement element) {
		RangeCheckUtils.assertReferenceParamNotNull("", element);
		final WorkTitleVisitor vis =
			new WorkTitleVisitor(new RSWKParticleFactory());
		element.accept(vis);
		return vis.toString();
	}

	public static String getRSWK(final String titleStr) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		MusicTitle mt = ParseMusicTitle.parse(null, titleStr);
		return getRSWK(mt);
	}

	/**
	 * 
	 * Liefert die Darstellung eines Titel(elementes) gemäß dem
	 * übergebenen Regelwerk, das in factory implementiert ist.
	 * 
	 * @param element	nicht null
	 * @param factory	nicht null
	 * @return			nicht null
	 */
	public static String getTitelAccordingRules(
			final TitleElement element,
			final AbstractParticleFactory factory) {
		RangeCheckUtils.assertReferenceParamNotNull("element", element);
		final WorkTitleVisitor vis = new WorkTitleVisitor(factory);
		element.accept(vis);
		return vis.toString();
	}

	/**
	 * Setzt die vermutete RSWK-Ansetzung in GND-Unterfelder. Es
	 * werden also z.B. <br><br>
	 * - Instrumente als $m kodiert, diese aber ausgeschrieben<br>
	 * - Gattungen bevorzugt im Singular ausgegeben usw.
	 * <br><br>
	 * Parst titleStr und liefert eine String-Repräsentation der
	 * RSWK-Fassung des Titels. Da der Aufbau eines Strings einfacher
	 * ist als der Aufbau der Unterfelder, ist das zur Zeit die grundlegende
	 * Methode - das sollte aber geändert werden.
	 * 
	 * @param musicTitle nicht null.
	 * @return	RSWK-Form mit GND-Unterfeldern als String.
	 */
	public static String getRSWKStringInSubfields(final MusicTitle musicTitle) {
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
		RangeCheckUtils.assertReferenceParamNotNull("element", element);
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
		RangeCheckUtils.assertReferenceParamNotNull("element", element);
		final StructuredVisitor sVis = new StructuredVisitor();
		element.accept(sVis);
		return sVis.toString();
	}

	public static String getGND1XXPlusTag(final MusicTitle musicTitle) {
		RangeCheckUtils.assertReferenceParamNotNull("musicTitle", musicTitle);
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
		return RecordUtils.toPica(
				GNDTitleUtils.get3XXLines(element, forceTotalCount),
				Format.PICA3, expansion, de.dnb.gnd.Constants.LINE_SEPARATOR,
				'$');
	}

	/**
	 * Liefert die 3XX-Felder als String.
	 * 
	 * @param element	TitleElement
	 * @param forceTotalCount	Gesamtzahl bei Instrumenten erzwingen
	 * @return 3XX-Felder der GND inclusive 548 (Zeitangabe)
	 */
	public static String getGND3XXAleph(
			final TitleElement element,
			final boolean forceTotalCount) {
		AuthorityDataAlephVisitor ausvis =
			new AuthorityDataAlephVisitor(forceTotalCount);
		element.accept(ausvis);
		final AdditionalDataIn3XXVisitor advis =
			new AdditionalDataIn3XXVisitor();
		element.accept(advis);
		return ausvis.toString() + "\n" + advis.toString();
	}

	public static String getGND3XX(final TitleElement element) {
		final boolean expansion = false;
		final boolean forceTotalCount = false;
		return getGND3XX(element, expansion, forceTotalCount);
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

	/**
	 * Liefert die GND-Zeilen, die aus einem Werktitel gewonnen werden
	 * können, als String.
	 * 
	 * @param musicTitle		nicht null
	 * @param expansion			Links expandiert
	 * @param forceTotalCount	Gesamtzahl bei Ausführenden wird immer
	 * 							ausgegeben
	 * @return					String der GND-Informationen.
	 */
	public static String getFullGND(
			final MusicTitle musicTitle,
			final boolean expansion,
			final boolean forceTotalCount) {
		RangeCheckUtils.assertReferenceParamNotNull("musicTitle", musicTitle);
		Record record = GNDTitleUtils.getRecord(musicTitle, forceTotalCount);
		return RecordUtils.toPica(record, Format.PICA3, expansion, "\n", '$');
	}

	/**
	 * Liefert die GND-Zeilen, die aus einem Werktitel gewonnen werden
	 * können, als String. <br>
	 * Links sind expandiert. <br>
	 * Gesamtzahl bei Ausführenden wird immer ausgegeben.
	 * 
	 * @param musicTitle		nicht null 		
	 * 	
	 * @return					String der GND-Informationen.
	 */
	public static String getFullGND(final MusicTitle musicTitle) {
		RangeCheckUtils.assertReferenceParamNotNull("musicTitle", musicTitle);
		final boolean expansion = false;
		final boolean forceTotalCount = false;
		return getFullGND(musicTitle, expansion, forceTotalCount);
	}

	/**
	 * Liefert eine String-Repräsentation der GND-Form des Titels 
	 * ohne Tag und ohne führendes $a.
	 * 
	 * Da der Aufbau eines Strings einfacher ist als der Aufbau der 
	 * Unterfelder, ist das zur Zeit die grundlegende
	 * Methode - das sollte aber geändert werden.
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

	/**
	 * 
	 * Parst titleStr und liefert eine String-Repräsentation der
	 * GND-Fassung des Titels.
	 * 
	 * @param titleStr	nicht null
	 * @return			GND als String
	 */
	public static String getX30ContentAsString(final String titleStr) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		MusicTitle mt = ParseMusicTitle.parse(null, titleStr);
		return getX30ContentAsString(mt);
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

	/**
	 * Überprüft, ob der übergebene titelStr nach den Regeln von
	 * rulesFactory aus element abgeleitet werden kann.
	 * 
	 * @param titleStr		nicht null, nicht nur Blanks, kann mit $a beginnen.
	 * 						Vor und nach $a können Blanks stehen, vor und nach 
	 * 						allen anderen Unterfelder nicht.
	 * @param rulesFactory	Menge von Regeln (z.Z. RAK, RSWK, GND)
	 * @return				titleStr == element.
	 */
	public static boolean isConformToRules(
			final String titleStr,
			final AbstractParticleFactory rulesFactory) {
		if (titleStr == null)
			throw new IllegalArgumentException("übergebener Titel ist null");
		MusicTitle mt = ParseMusicTitle.parse(null, titleStr);
		return isIdenticalWithinRules(titleStr, mt, rulesFactory);
	}

	/**
	 * Liefert die Aleph-Form eines Titels.
	 * 
	 * @param theMusicTitle		nicht null
	 * @param forceTotalCount	Zählung der Instrumente
	 * @param composer 			auch null
	 * @return					Aleph.
	 */
	public static String getAleph(
			final MusicTitle theMusicTitle,
			final boolean forceTotalCount,
			final Composer composer) {
		final String owner = "(DE-588)";
		RangeCheckUtils.assertReferenceParamNotNull("theTitle", theMusicTitle);
		String s;
		String composerPhrase = "";
		if (theMusicTitle.containsVersion()) {
			s = "093 $a wif";
		} else {
			s = "093 $a wim";
		}

		if (composer != null) {
			composerPhrase = "$p" + composer.name;
			s += "\n043 $a" + composer.countrCode;
			s += "\n500 " + composerPhrase + "$4kom1$9" + owner + composer.idn;
			s += "\n670 $a" + composer.sourceAbb;

		}

		String titleStr = TitleUtils.getX30ContentAsString(theMusicTitle);
		// Alephspezifische Unterfelder
		titleStr =
			titleStr.replaceAll(Matcher.quoteReplacement("$p"),
					Matcher.quoteReplacement("$u"));
		titleStr =
			titleStr.replaceAll(Matcher.quoteReplacement("$g"),
					Matcher.quoteReplacement("$h"));
		s += "\n130 " + composerPhrase + "$t" + titleStr;

		s += "\n" + TitleUtils.getGND3XXAleph(theMusicTitle, forceTotalCount);
		if (theMusicTitle.containsParts())
			s +=
				"\n430 " + composerPhrase + "$t"
					+ TitleUtils.getRAK(theMusicTitle) + "$v"
					+ Constants.KOM_PORTAL_430;

		// Sortieren und komprimieren - nötig?
		String[] lines = s.split("\n");
		TreeSet<String> lineSet = new TreeSet<String>(Arrays.asList(lines));
		String result = "";
		//@formatter:off
		for (Iterator<String> iterator = lineSet.iterator(); 
				iterator.hasNext();) {
			//@formatter:on
			String line = iterator.next();
			if (!line.isEmpty()) {
				result += line;
				if (iterator.hasNext())
					result += "\n";
			}
		}
		return result;
	}

	/**
	 * @param args
	 * @throws IllFormattedLineException 
	 */
	public static void main(String[] args) throws IllFormattedLineException {
		GenreList list = ParseGenre.parseGenreList("Präludium und Fuge");
		MusicTitle mt =
			ParseMusicTitle.parseSimpleTitle(null, "Präludium und Fuge");
		System.out.println(getRAK(list));
		System.out.println(getRAK(mt));
	}
}
