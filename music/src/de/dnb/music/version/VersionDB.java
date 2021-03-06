package de.dnb.music.version;

import utils.StringUtils;

public class VersionDB {

	private static String[] phrasenStrings = { "Fassung", "Kurze Fassung",
		"Korrigierte Fassung", "Rev. Fassung", "Umgearb. Fassung",
		"überarb. Fassung", "Überarb. Fassung", "Kleine Fassung",
		"Erw. Fassung", "Grosse Fassung", "Vereinfachte Fassung",
		"Konzertante Fassung", "Dt. Fassung", "Gekürzte Fassung",
		"Reduzierte Fassung", "Engl. Fassung", "Frühe Fassung",
		"Ordentliche Fassung", "Franz. Fassung", "Ital. Fassung",
		"Konzertfassung", "Kurzfassung", "Alternativfassung", "Neufassung",
		"Erstfassung", "Ballettfassung", "Urfassung", "Frühfassung",
		"Vokalfassung", "Fragments", "Fragmente", "Entwürfe", "Entwurf" };

	/**
	 * 
	 * findet das längste Präfix aus phrasenSet in parseString.
	 * 
	 * @param parseString nicht null
	 * @return eine rudimentäre Fassung oder null.
	 */
	public static Version matchVersion(String parseString) {
		if (parseString == null)
			throw new IllegalArgumentException(
					"Null-String an matchVersion()übergeben");
		if (parseString.length() == 0)
			return null;

		String prefix =
			StringUtils.findLongestPrefix(parseString, phrasenStrings);

		if (prefix != null) {
			Version version = new Version();
			version.rakPhrase = prefix;
			version.rest = parseString.substring(prefix.length());
			return version;
		}

		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Version f = matchVersion("Rev. Fassung");
		System.out.println(f);

	}

}
