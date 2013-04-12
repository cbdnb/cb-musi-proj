package de.dnb.music.publicInterface;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.Pair;
import utils.StringUtils;
import utils.TitleUtils;
import de.dnb.music.additionalInformation.ThematicIndexDB;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.version.Version;
import static de.dnb.music.publicInterface.Constants.*;
/**
 * Hauptklasse zur Bearbeitung alter Datensätze. Enthält alle wichtigen 
 * Stringkonstanten und die öffentliche Methode transform().
 * 
 * Die Felder, die nicht bearbeitet werden sollen, stehen in
 * isUnmodifiable()
 * 
 * @author baumann
 *
 */
public final class TransformRecord {	

	// ---- Zum Transformieren als globale Variable benutzt
	private static String sTag;

	private static String sCommentStr;

	private static TransformMode sTransformMode;

	private static SetOfRules sRules;

	private static List<String> sOldLines;

	private static MusicRecord sNewRecord;

	private static String sTitleStr;

	private static String sOldRecordStr;

	private static MusicTitle sMusicTitle;

	private static String sContent;

	private static String rejectionCause;

	/**
	 * @return the rejectionCause
	 */
	public static String getRejectionCause() {
		return rejectionCause;
	}

	private TransformRecord() {
	}

	//@formatter:off
	private static List<String> sUnmodifiables = Arrays.asList(
			"006", "003U", 
			"035", "007K", 
			"039", "007N",
			// 913 kann überschrieben werden?
			"913", "047C");
	//@formatter:on

	/**
	 * Setzt die Felder, die möglichst nicht modifiziert werden sollen, da
	 * sie maschinell vom CBS erzeugt werden. Sollte nicht aus der WinIBW
	 * heraus gearbeitet werden, muss auf null gesetzt werden, da dann
	 * alle Felder verarbeitet werden müssen.
	 * 
	 * @param unmodifiables	Liste von Tags (Pica3 und Pica+), auch null.
	 */
	public static void setUnmodifiables(final List<String> unmodifiables) {
		TransformRecord.sUnmodifiables = unmodifiables;
	}

	/**
	 * Überprüft, ob der alte Datensatz überhaupt bearbeitet werden darf.
	 * Wenn nicht, wird eine Ausnahme ausgelöst.
	 */
	public static void checkPermission() {
		//@formatter:off
		final Pattern pat130CommMasch =
			Pattern.compile("^" 
					+ "(130 |022A \\$a)" 
					+ ".*\\$v" 
					+ "(" 
					+ KOM_MASCHINELL
					+ "|" 
					+ KOM_MASCHINELL_NACH_2003 
					+ ")$",
					Pattern.MULTILINE);
		Matcher m = pat130CommMasch.matcher(sOldRecordStr);
		if (m.find()) {
			throw new IllegalStateException(
					"Datensatz ist bereits bearbeitet (aus Kommentar in 130");
		}
		final Pattern pat667 = 
				Pattern.compile("^" 
						+ "(667 |050C \\$a)" 
						+ "(" 
						+ GEW_INTELL
						+ "|" 
						+ GEW_MASCH	
						+ "|" 
						+ SATZ_AUFG	
						+ "|" 
						+ VERL_MASCH 
						+ "|" 
						+ MASCH_AUFG // sollte so sein, damit nicht 2-mal 
									 // maschinell aufgearbeitet wird!
						+ "|" 
						+ VORZ_BEN_AUFGEARB 
						+ ")",
						Pattern.MULTILINE);
		m = pat667.matcher(sOldRecordStr);
		if (m.find())
			throw new IllegalStateException(
					"Datensatz ist bereits bearbeitet (aus 667)");
		//@formatter:on
	}

	/**
	 * Reichert den Datensatz mit Ländercode, GND-Systematik, Quelle und 
	 * Redaktioneller Bemerkung an.
	 */
	private static void enrichRecord() {

		final Pattern patCountryCode =
			Pattern.compile("^(043 |042B \\$a)", Pattern.MULTILINE);
		Matcher matcherCC = patCountryCode.matcher(sOldRecordStr);
		boolean ccFound = matcherCC.find();

		final Pattern patComposer =
			Pattern.compile("^" + "(500|028R) .*\\$4kom1$", Pattern.MULTILINE);
		Matcher matcherComp = patComposer.matcher(sOldRecordStr);
		boolean compFound = matcherComp.find();

		// Irgendein Komponist gefunden:
		if (compFound) {
			String line =
				sOldRecordStr.substring(matcherComp.start(), matcherComp.end());
			Pair<String, String> pair = StringUtils.getTagAndcontent(line);
			String content = pair.second;
			String idn = StringUtils.getIDN(content);
			// Irgendeine IDN gefunden (muss aber nicht bekannt sein!):
			if (idn != null) {
				String cc = ThematicIndexDB.getCountryCode(idn);
				// bisher kein Ländercode:
				if (!ccFound && cc != null)
					sNewRecord.add("043 " + cc);
				// WV aus NSW (nicht bei Script):	
				if (sTransformMode == TransformMode.INTELLECT) {
					String sourceAbb = ThematicIndexDB.getSourceAbb(idn);
					if (sourceAbb != null)
						sNewRecord.add("670 " + sourceAbb);
				}
			}

		}

		// GND-Systematik
		if (getSetOfRules() != SetOfRules.RAK) {
			final Pattern patGNDClass =
				Pattern.compile("^(065 |042A \\$a)", Pattern.MULTILINE);
			matcherCC = patGNDClass.matcher(sOldRecordStr);
			if (!matcherCC.find()) {
				sNewRecord.add("065 14.4p");
			}
		}
		// Redaktionelle Bemerkung
		switch (sTransformMode) {
		case INTELLECT:
			sNewRecord.add("667 " + SATZ_AUFG);
			break;
		case MACHINE:
			//			sNewRecord.add("667 " + MASCH_AUFG);
			break;

		default:
			break;
		}
	}

	/**
	 * Bringt den gesamten Datensatz auf neues Format. 
	 * Für die Transformation werden nur die Felder 130 und 430 (oder ihre
	 * Pica+-Äquivalente) analysiert. Der Datensatz wird mit allen verfügbaren
	 * Informationen (Feldern) angereichert, die 008 (Entitätencode) wird
	 * neu analysiert.
	 * 
	 * Anhand der 913 wird ermittelt, ob ein alter RSWK-Satz vorliegt.
	 * Wenn dieser ein Formalsachtitel ist (dann sind die Abweichungen nicht
	 * mehr tolerierbar),wird eine 430-Verweisung mit der alten Ansetzung 
	 * und dem Code $4nswd erzeugt. (Sinnvoll?)
	 * 
	 * Felder, die nicht geändert werden, werden unverändert übernommen.
	 * 
	 * @param oldRecord	Alter Datensatz
	 * @param transformMode INTELLECT oder MACHINE:
	 * 	- Im MACHINE-Modus wird eine Kennung hinterlegt, dass die 
	 * 		Änderung automatisch erfolgte.
	 * 	- im INTELLECT-Modus werden die Expansionen angezeigt und die Gesamtzahl
	 * 		der Instrumente ausgegeben.
	 * @return	Neuen Datensatz. Wenn der Datensatz nicht bearbeitet werden
	 * 			soll, wird eine Ausnahme geworfen.
	 */
	public static String transform(
			final String oldRecord,
			final TransformMode transformMode) {

		if (oldRecord == null || transformMode == null)
			throw new IllegalArgumentException(
					"Alter Datensatz oder Transformations-Modus sind null");

		sOldRecordStr = oldRecord;
		// führt eventuell zum Abbruch:
		checkPermission();

		sTransformMode = transformMode;
		sRules = getSetOfRules();

		sOldLines = StringUtils.record2Lines(sOldRecordStr);
		sNewRecord = new MusicRecord();

		rejectionCause = "";

		if (getSetOfRules() != SetOfRules.RAK)
			enrichRecord();

		for (String oldLine : sOldLines) {
			Pair<String, String> pair = StringUtils.getTagAndcontent(oldLine);

			if (pair == null) {
				// Ist wohl irgendwie hineingekommen
				continue;
			}

			sTag = pair.first;
			sContent = pair.second;

			//			System.err.println(sTag);
			//			System.err.println(sContent);

			if (isUnmodifiable(sTag))
				continue;

			// Entitätencode wird vom Programm modifiziert
			if (sTag.equals("008") || sTag.equals("004B"))
				continue;

			// Berücksichtigte Tags auf MARC normieren:
			if (sTag.equals("130") || sTag.equals("022A")) {
				sTag = "130";
			}

			if (sTag.equals("430") || sTag.equals("022@")) {
				sTag = "430";
			}

			if (sTag.equals("130") || sTag.equals("430")) {
				transformTitle();
			} else {
				sNewRecord.add(oldLine);
			}

		}

		return sNewRecord.toString();

	}

	private static void transformTitle() {
		// Kommentar am Titel entfernen und sichern.
		Pair<String, String> p = splitComment(sContent);
		sTitleStr = p.first;
		sCommentStr = p.second;
		sMusicTitle = ParseMusicTitle.parseGND(null, sTitleStr);
		enrich3XX();
		String newComment = null;
		String newLine = sTag + " ";

		if (sTag.equals("130")) {

			// Entitätencode erzeugen
			if (sMusicTitle.containsArrangement()
				|| sMusicTitle.containsVersion())
				sNewRecord.add("008 wif");
			else
				sNewRecord.add("008 wim");

			// Relationiertes Werk
			if (sTransformMode == TransformMode.INTELLECT
				|| RELATED_WORK_AS_STRING) {
				boolean getRelatorCode = true;
				String relatedWorkStr =
					TitleUtils.getGND530(sMusicTitle, getRelatorCode);
				if (sTransformMode == TransformMode.INTELLECT) {
					// Alle $i (außer $4) durch Leerzeichen ersetzen
					relatedWorkStr = relatedWorkStr.replaceAll("\\$\\D", " ");
					relatedWorkStr =
						relatedWorkStr.replaceAll("530 ", "530 sp ");
				}
				sNewRecord.add(relatedWorkStr);
			}

			// ist der alte Titel richtig erraten?
			String titleFrom913 = getTitleFrom913();
			boolean coincidence = true;

			if (sRules == SetOfRules.RSWK) {
				newLine += TitleUtils.getGND130Or430(sMusicTitle);

				if (sTransformMode == TransformMode.MACHINE)
					newComment = KOM_MASCHINELL;
				else
					newComment = null;

			} else if (sRules == SetOfRules.RAK) {

				coincidence =
					TitleUtils.getRAK(titleFrom913).equals(titleFrom913);
				if (KOM_NACH_2003.equals(sCommentStr)
					&& sTransformMode == TransformMode.INTELLECT) {
					//	RAK, 130, nach 2003, INTELLECT: ---------------
					newComment = null;
					/*
					 * Sicherheitshalber lieber die alte $a aufheben:
					 */
					if (coincidence)
						newLine += TitleUtils.getGND130Or430(sMusicTitle);
					else {
						newLine += sTitleStr;
						rejectionCause +=
							"\n130 nicht verwendet wegen "
								+ "Differenz zu 913, daher alte 130:\n"
								+ "130 neu:"
								+ TitleUtils.getGND130Or430(sMusicTitle)
								+ "\n130 alt:" + sTitleStr + "\n";
					}
					sMusicTitle.setComment(null);

					// Absprache mit DMA, VC vom 2.11.2012:
					if (sMusicTitle.containsParts()) {
						String newLine430 = "430 ";
						/*
						 * Sicherheitshalber lieber die alte RAK aus 913
						 * verwenden:
						 */
						if (coincidence)
							newLine430 += TitleUtils.getRAK(sMusicTitle);
						else {
							newLine430 += titleFrom913;
							rejectionCause +=
								"\nerzeugte Portal-430 nicht verwendet wegen "
									+ "Differenz zu 913:\n" + "430:"
									+ TitleUtils.getRAK(sMusicTitle) + "\n913:"
									+ titleFrom913 + "\n";
						}
						newLine430 += "$v" + KOM_PORTAL_430;
						sNewRecord.add(newLine430);
					}

				} else if (KOM_NACH_2003.equals(sCommentStr)
					&& sTransformMode == TransformMode.MACHINE) {
					//	RAK, 130, nach 2003, maschinell: ------------------
					newComment = KOM_MASCHINELL_NACH_2003;
					/*
					 * Sicherheitshalber lieber die alte $a aufheben:
					 */
					if (coincidence)
						newLine += TitleUtils.getGND130Or430(sMusicTitle);
					else {
						newLine += sTitleStr;
						rejectionCause +=
							"\n130 nicht verwendet wegen "
								+ "Differenz zu 913, daher alte 130:\n"
								+ "130 neu:"
								+ TitleUtils.getGND130Or430(sMusicTitle)
								+ "\n130 alt:" + sTitleStr + "\n";
					}
					sMusicTitle.setComment(null);

					// Absprache mit DMA, VC vom 2.11.2012:
					if (sMusicTitle.containsParts()) {
						String newLine430 = "430 ";
						/*
						 * Sicherheitshalber lieber die alte RAK aus 913
						 * verwenden:
						 */
						if (coincidence)
							newLine430 += TitleUtils.getRAK(sMusicTitle);
						else {
							newLine430 += titleFrom913;
							rejectionCause +=
								"\nerzeugte Portal-430 nicht verwendet wegen "
									+ "Differenz zu 913:\n" + "430:"
									+ TitleUtils.getRAK(sMusicTitle) + "\n913:"
									+ titleFrom913 + "\n";
						}
						newLine430 += "$v" + KOM_PORTAL_430;
						newLine430 = newLine430.replaceAll("\\{", "");
						sNewRecord.add(newLine430);
					}

				} else if (KOM_VOR_2003.equals(sCommentStr)
					&& sTransformMode == TransformMode.INTELLECT) {
					//	RAK, 130, vor 2003, INTELLECT: ---------------------
					newComment = null;
					newLine += TitleUtils.getGND130Or430(sMusicTitle);
				} else if (KOM_VOR_2003.equals(sCommentStr)
					&& sTransformMode == TransformMode.MACHINE) {
					//	RAK, 130, vor 2003, maschinell: ------------------
					newComment = KOM_MASCHINELL_VOR_2003;
					newLine += transformOldRAK(sContent);
				} else {
					// seltsam, sollte es nicht geben, unverändert: ---------
					newLine += sTitleStr;
					newComment = sCommentStr;
				}

			} else { // GND
				newLine += TitleUtils.getGND130Or430(sMusicTitle);
				;
				newComment = sCommentStr;
			}
		} else {
			// (sTag.equals("430"))
			if (sRules == SetOfRules.RSWK) {
				// 		RSWK, 430: ---------------------------------
				newLine += TitleUtils.getRSWKInSubfields(sMusicTitle);
				newComment = sCommentStr;
			} else if (sRules == SetOfRules.RAK) {
				if (KOM_VOR_2003_430.equals(sCommentStr)) {
					// 	RAK, 430, vor 2003: ---------------------------------
					newLine += transformOldRAK(sContent);
					newComment = sCommentStr;
				} else {
					// 	RAK, 430, alle anderen: ------------------------------
					newLine += TitleUtils.getGND130Or430(sMusicTitle);
					newComment = sCommentStr;
				}
			} else { // GND
				// GND, 430:  ---------------------------------
				if (KOM_VOR_2003_430.equals(sCommentStr)) {
					// irgenwoher ein altes RAK:
					newLine += transformOldRAK(sContent);
					newComment = sCommentStr;
				} else {
					newLine += TitleUtils.getGND130Or430(sMusicTitle);
					newComment = sCommentStr;
				}
			}

		}
		if (newComment != null)
			newLine += "$v" + newComment;
		newLine = newLine.replace("{", "");
		sNewRecord.add(newLine);

	}

	private static SetOfRules getSetOfRules() {
		final Pattern patRSWK =
			Pattern.compile("^(913|047C) \\$Sswd", Pattern.MULTILINE);
		final Matcher matcherRSWK = patRSWK.matcher(sOldRecordStr);
		if (matcherRSWK.find())
			return SetOfRules.RSWK;

		final Pattern patRAK =
			Pattern.compile("^(913|047C) \\$Sest", Pattern.MULTILINE);
		final Matcher matcherRAK = patRAK.matcher(sOldRecordStr);
		if (matcherRAK.find())
			return SetOfRules.RAK;

		return SetOfRules.GND;
	}

	/**
	 * Feld ist nicht modifizierbar.
	 * 
	 * @param sTag nicht null.
	 * @return	true, wenn Feld nicht modifizerbar:
	 */
	private static boolean isUnmodifiable(final String tag) {
		if (tag == null)
			throw new IllegalArgumentException("sTag ist null");
		if (sUnmodifiables == null)
			return false;
		//@formatter:on
		if (sUnmodifiables.contains(tag))
			return true;
		return false;
	}

	/**
	 * Erzeugt die 3XX-Felder. Im Script-Mode werden Expansion der idn nicht
	 * und Gesamtzahl der Instrumente nur unter bestimmten Umständen erzeugt.
	 */
	private static void enrich3XX() {
		boolean expansion = true;
		boolean forceTotalCount = true;
		if (sTransformMode == TransformMode.MACHINE) {
			expansion = false;
			forceTotalCount = false;
		}
		String s =
			TitleUtils.getGND3XX(sMusicTitle, expansion, forceTotalCount);
		sNewRecord.addAll(s);
	}

	/**
	 * splitted Feldinhalt in Information + Kommentar.
	 * @param content	Feldinhalt
	 * @return	Paar aus Information und Kommentar. Der Kommentar beginnt 
	 * nicht mit $v. 
	 */
	public static Pair<String, String> splitComment(final String content) {
		if (content == null)
			throw new IllegalArgumentException("kein String zum Splitten");
		int pos = content.indexOf("$v");
		if (pos != -1) {
			return new Pair<String, String>(content.substring(0, pos),
					content.substring(pos + 2));
		} else
			return new Pair<String, String>(content, null);

	}

	/**
	 * Die 913 enthält in der Regel eine Boilerplate
	 * 	"913 $Sswd$ipt$a". 
	 * Dann folgt der eventuelle Komponist, gefolgt von
	 * 	", ", 
	 * danach der Titel, danach die alte nid 
	 * 	"$04425694-2".
	 * 
	 * 913 $Sswd$it$aMagnus liber <Musik>$04759556-5
	 * 
	 * @return null oder ein Werktitel.
	 */
	public static String getTitleFrom913() {
		final Pattern pat913 =
			Pattern.compile("^(913|047C) " + "\\$S(swd|est)\\$i.*\\$a(.+)\\$0",
					Pattern.MULTILINE);
		/*
		 * Capturing Group für den Titel in der 913. Das ist dritte öffnende
		 * Klammer in pat913 (die 0. Gruppe ist das Pattern selbst).
		 */
		final int titleGroup = 3;
		Matcher matcher = pat913.matcher(sOldRecordStr);
		if (matcher.find()) {
			String content = matcher.group(titleGroup);
			String[] parts = content.split(": ");
			if (parts.length == 2) {
				return parts[1];
			} else
				return content;
		}

		return null;

	}

	/**
	 * Berücksichtigt das alte Regelwerk. Werktitel, die nach diesem aufgebaut
	 * sind, sollen nicht in die üblichen Unterfelder zerlegt werden. Die
	 * einzigen erlaubten Unterfelder sind $p, $s und $v. 
	 * 
	 * @param content	Inhalt der Zeile des nach altem Regelwerk angesetzten
	 * 					Titels inklusive $v.	
	 * @return			Alten Titel mit Unterfeldern $p und $s ohne Kommentar.
	 */
	public static String transformOldRAK(final String content) {
		if (content == null || content.trim().length() == 0)
			throw new IllegalArgumentException(
					"Übergebener Werktitel null oder leer");
		if (!content.contains("$v"))
			throw new IllegalArgumentException("Übergebener Werktitel ohne $v");
		/*
		 * jetzt aufpassen: hier sollen nur die Unterfelder
		 * $p und $s erzeugt werden.
		 */
		List<String> strL = StringUtils.breakUpIntoSubfields(content);
		// "$a" entfernen:
		String first = strL.get(0);
		first = first.substring(2);
		strL.set(0, first);
		// Kommentar entfernen
		int strLSize = strL.size();
		strL.remove(strLSize - 1);

		/*
		 *  Wenn diese Liste mehr als 2 Einträge hat, dann lag
		 *  in den alten DMA-Daten ein Werkteil (und eventuell noch
		 *  eine Fassung) vor und die Unterfelder sind schon 
		 *  korrekt gesetzt:
		 */
		if (strL.size() > 1) {
			return StringUtils.concatenate(strL);
		} else {
			// 1 Eintrag, also nur Titel 
			Pair<String, Version> sVpair =
				ParseMusicTitle.splitTitlePlusVersion(null, strL.get(0));
			if (sVpair != null) { // Fassung gefunden
				String vor = sVpair.first;
				String nach = sVpair.second.getMatch();
				return vor + "$s" + nach;

			} else { // keine Fassung gefunden:
				return strL.get(0);

			}
		}
	}

	/**
	 * Nur zum Experimentieren.
	 * 
	 * @param args	nicht benötigt.
	 */
	public static void main(final String[] args) {
		String oldRecord =
			"130 Sacrae cantiones ... , liber 4$pIn principio erat verbum"
				+ "\n913 $Sest$ipt$aLasso, Orlando /di: "
				+ "Sacrae cantiones ... , liber 4 <In principio erat verbum>"
				+ "$0300941358";
		System.err.println(transform(oldRecord, TransformMode.MACHINE));

	}
}
