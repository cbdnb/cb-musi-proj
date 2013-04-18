package de.dnb.music.publicInterface;

import java.util.Arrays;
import java.util.List;

public final class Constants {

	private Constants() {
		//
	}

	/**
	 * Aufzählung der verwendeten Modi.
	 * @author baumann
	 *
	 */
	public static enum TransformMode {
		INTELLECT, MACHINE
	}

	/**
	 * Datensätze können aus alten Regelwerken stammen oder neu sein.
	 * 
	 * @author baumann
	 *
	 */
	public static enum SetOfRules {
		RAK, RSWK, GND
	}

	//------- Mögliche Kommentarzeilen in 130

	public static final String KOM_VOR_2003 =
		"R:Umsetzung GND aus RAK-M vor 2003";

	public static final String KOM_NACH_2003 = "R:Umsetzung GND aus RAK-M 2003";

	public static final String KOM_RSWK = null;

	public static final String KOM_MASCHINELL_NACH_2003 =
		"R:Maschinelle Umsetzung GND aus RAK-M 2003";

	public static final String KOM_MASCHINELL_VOR_2003 =
		"R:Maschinelle Umsetzung GND aus RAK-M vor 2003";

	public static final String KOM_MASCHINELL = "R:Maschinelle Umsetzung GND";

	// ------------------ 430 ------------------------

	public static final String KOM_PORTAL_430 = "R:Ansetzung nach RAK-Musik";

	public static final String KOM_VOR_2003_430 = "R:EST vor RAK-M 2003";

	public static final String KOM_VW_RAK = "R:Titelverweisung nach RAK";

	public static final String KOM_VW_FREI = "R:Freie Titelverweisung";

	public static final String KOM_ZIT = "R:Zitierter Sachtitel";

	// -------------------------------------------

	// sollen relationierte Werke als String in der 530 stehen? Wohl eher nicht!
	public static final boolean RELATED_WORK_AS_STRING = false;

	//-------- Redaktionelle Bemerkungen

	// = "Bevorzugter Name Prüfung erfolgt":
	public static final String VORZ_BEN_AUFGEARB = "BNPe";

	// = "Vollständige Prüfung erfolgt":
	public static final String SATZ_AUFG = "VPe";

	// Vorschlag, "Musiksatz maschinelle Verarbeitung":
	public static final String MASCH_AUFG = "MmV";

	public static final String GEW_INTELL = "MMi";

	public static final String GEW_MASCH = "MMm";

	public static final String VERL_MASCH = "MMv";

	/**
	 *  redaktionelle Bemerkungen, die anzeigen, dass (partiell) aufgearbeitet.
	 */
	public static final List<String> GENERAL_NOTES = Arrays.asList(GEW_INTELL,
			GEW_MASCH, SATZ_AUFG, VERL_MASCH, MASCH_AUFG, VORZ_BEN_AUFGEARB);

	/**
	 * Zeigen in der 130 an, dass maschinell verändert.
	 */
	public static final List<String> COMMENT_MACHINE = Arrays.asList(
			KOM_MASCHINELL, KOM_MASCHINELL_NACH_2003, KOM_MASCHINELL_VOR_2003);

	public static final List<String> COMMENT_OLD_RAK = Arrays.asList(
			KOM_VOR_2003, KOM_VOR_2003_430);
}
