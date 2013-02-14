package de.dnb.music.publicInterface;

import utils.TitleUtils;
import de.dnb.music.title.Comment;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

/**
 * Eigentlich nicht mehr nötig, da der GANZE Datensatz auf einmal verändert 
 * werden sollte. Wenn aber nur die 130 transformiert werden soll, ist das 
 * immer noch der richtige Kandidat.
 * 
 * @author baumann
 *
 */
@Deprecated
public final class Transform130 {

	private Transform130() {
	}

	/**
	 * 
	 * @return	die vom DMA gewünschte 430, also $p durch $g ersetzt, sowie
	 * $v = KOM_PORTAL_430.
	 */
	private static String getDMA430(final MusicTitle mt) {
		String commStr = mt.getComment().toString();

		if (commStr.equals(TransformRecord.KOM_MASCHINELL_NACH_2003)
			|| commStr.equals(TransformRecord.KOM_VOR_2003))
			return TitleUtils.getDNBPortal4XX(mt) + "$v"
				+ TransformRecord.KOM_PORTAL_430;

		return "";
	}

	/**
	 * 
	 * Verwendet die relevanten Informationen des  
	 * Musiktitels mt, um die Ausgabe an die WIN-IBW zu erzeugen.
	 * @param mt	Musiktitel
	 * @return		neue 130, 430, 3XX, 5XX ...
	 */
	public static String getGNDinDNB(final MusicTitle mt) {

		// den richtigen Kommentar in der 130 erzeugen:
		String commentStr = "";
		if (mt.containsCommment())
			commentStr = mt.getComment().toString();
		if (commentStr.equals(TransformRecord.KOM_NACH_2003)) {
			mt.setComment(new Comment(TransformRecord.KOM_MASCHINELL_NACH_2003));
		} else {
			mt.setComment(new Comment(TransformRecord.KOM_MASCHINELL));
		}

		String s = TitleUtils.getGND1XXPlusTag(mt);

		s += "\n" + TitleUtils.getGND3XX(mt);

		if (mt.containsParts()) {
			s += "\n" + getDMA430(mt);
		}

		if (TransformRecord.RELATED_WORK_AS_STRING) {
			final boolean getRelatorCode = true;
			s += "\n" + TitleUtils.getGND530(mt, getRelatorCode);
		}

		return s + "\n"; // Umbruch für IBW-Skript
	}

	/**
	 * Zentrale Funktion. Liefert eine String-Repäsentation der Veränderungen,
	 * die eine 130-Zeile erfährt.
	 * 
	 * @param contentOf130 nicht null, nicht nur Leerzeichen.
	 * @return	Einen String, mit dem die alte 130-Zeile (inklusive "130 ")
	 * überschrieben werden muss.	
	 */
	public static String transform2DNB(final String contentOf130) {
		if (contentOf130 == null || contentOf130.trim().length() == 0)
			throw new IllegalArgumentException(
					"Null-String an transform2DNB()übergeben");

		final MusicTitle m = ParseMusicTitle.parseGND(null, contentOf130);
		String out = getGNDinDNB(m);
		out = out.replaceAll("\n\n+", "\n");
		return out;
	}

	/**
	 * args[0] wird analysiert. Selbiges enthält den Inhalt des
	 * Feldes 130, beginnt also nicht mit "130 "!
	 * 
	 * Treten Fehler auf, so wird mit einer Exception abgeschlossen, die
	 * WIN-IBW liest also nicht auf stdin.
	 * 
	 * @param args	Nicht null, nicht nur Leerzeichen. 	
	 */
	public static void main(final String[] args) {

		if (args.length == 0)
			return;

		final String titelAlt = args[0];
		// nur zum Skript-Debuggen
		//		if(titelAlt!=null)
		//		throw new IllegalArgumentException();

		final String out = transform2DNB(titelAlt);
		System.out.println(out);

	}

}
