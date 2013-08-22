package de.dnb.music.publicInterface;

import static de.dnb.music.publicInterface.Constants.KOM_PORTAL_430;
import static de.dnb.music.publicInterface.Constants.SATZ_AUFG;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import utils.GNDConstants;
import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Indicator;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.parser.tag.GNDTag;
import de.dnb.gnd.parser.tag.GNDTagDB;
import de.dnb.gnd.parser.tag.Tag;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.gnd.utils.Pair;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.gnd.utils.WorkUtils;
import de.dnb.music.additionalInformation.ThematicIndexDB;
import de.dnb.music.publicInterface.Constants.SetOfRules;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.version.Version;
import filtering.IPredicate;

/**
 * Template-Klasse. Die Unterklassen überschreiben div. Methoden, um an das
 * zu bearbeitende Datenset anzupassen. Daher entfällt auch der Modus 
 * (intellektuell ...).
 *
 * @author baumann
 *
 */
public class DefaultRecordTransformer {

	/**
	 * Der gesicherte alte Datensatz.
	 */
	protected Record oldRecord;

	/**
	 * Der schrittweise aufzubauende neue Datensatz.
	 */
	protected Record newRecord;

	protected static final GNDTagDB TAG_DB = GNDTagDB.getDB();

	/**
	 * Zur Zeit bearbeiteter Werktitel.
	 */
	protected MusicTitle actualMusicTitle;

	/**
	 * Zur Zeit bearbeiteter Kommentar.
	 */
	protected String actualCommentStr;

	/**
	 * Zur Zeit aktueller {@link Tag}.
	 */
	protected Tag actualTag;

	/**
	 * Unterfelder, die beim Parsen des Musiktitels nicht berücksichtigt
	 * wurden.
	 */
	protected List<Subfield> unusedSubs = new LinkedList<Subfield>();

	/**
	 * Unterfelder, die zum aktuellen Titel ({@link #actualMusicTitle})
	 * gehören.
	 */
	List<Subfield> titleSubs = new LinkedList<Subfield>();

	/**
	 * 
	 * Zentrale Methode.
	 * <br><br>
	 * Template-Methode.<br>
	 * Es können die Methoden
	 * <br> {@link #addComposerData()}
	 * <br> {@link #addGeneralNote()}
	 * <br> {@link #addGNDClassification()} 
	 * <br> {@link #transform430(Line)} 
	 * <br>überschrieben werden.
	 * @param record	nicht null.
	 * @return			neuer Record.
	 */
	public final Record transform(final Record record) {
		RangeCheckUtils.assertReferenceParamNotNull("oldRecord", record);
		this.oldRecord = record;
		newRecord = oldRecord.clone();
		if (isPermitted(oldRecord)) {
			addComposerData();
			addGeneralNote();
			addGNDClassification();
			removeHeadings();
			transform130();
			transform430();
		}
		return newRecord;
	}

	/**
	 * Baut alle 430-Zeilen in den neuen Datensatz ein.
	 */
	protected final void transform430() {
		Collection<Line> lines430 = RecordUtils.getLines(oldRecord, "430");
		for (Line line : lines430) {
			transform430(line);
		}
	}

	/**
	 * transformiertline und baut es in neuen Datensatz ein.
	 * 
	 * Eventuell zu überschreiben. 
	 * 
	 * @param line nicht null.
	 */
	protected void transform430(final Line line) {
		RangeCheckUtils.assertReferenceParamNotNull("line", line);

		// soll nicht verändert werden!
		if (isOldRAK(line)) {
			buildAndInsertOldRAK(line);
			return;
		}

		actualTag = line.getTag();
		actualCommentStr = null;
		Pair<MusicTitle, List<Subfield>> pair =
			ParseMusicTitle.parseGND(null, line);
		actualMusicTitle = pair.first;
		unusedSubs = pair.second;

		if (getRules() == SetOfRules.RSWK)
			titleSubs = TitleUtils.getRSWKSubfields(actualMusicTitle);
		else
			titleSubs = TitleUtils.getSubfields(actualMusicTitle);

		buildAndAddLine(true);

	}

	/**
	 * Template-Methode.
	 * 
	 * Es müssen die Methoden
	 * <br> {@link #add3XX(MusicTitle)}
	 * <br> {@link #makeNew130Comment()}
	 * <br> {@link #makePortal430()} 
	 * <br>überschrieben werden.
	 * <br><br>
	 * Nimmt die 130 aus {@link #oldRecord} verändert sie und fügt sie in
	 * {@link #newRecord} ein. 
	 * <br><br>
	 * Der Entitätencode wird korrigiert.<br>
	 * Es wird eine Portal-430 erzeugt. 
	 * 
	 */
	private void transform130() {
		// globale Variablen vorbereiten
		actualTag = GNDConstants.TAG_130;
		Line titleLine = WorkUtils.getTitleLine(oldRecord);
		actualCommentStr = GNDUtils.getFirstComment(titleLine);
		Pair<MusicTitle, List<Subfield>> musicTitleP =
			ParseMusicTitle.parseGND(null, titleLine);
		actualMusicTitle = musicTitleP.first;
		unusedSubs = new LinkedList<Subfield>(musicTitleP.second);

		// 3XX hinzufügen:
		add3XX(actualMusicTitle);

		// Entitätencode erzeugen:
		Line entit;
		try {
			if (actualMusicTitle.containsArrangement()
				|| actualMusicTitle.containsVersion())
				entit = LineParser.parse("008 wif", TAG_DB);
			else
				entit = LineParser.parse("008 wim", TAG_DB);
			newRecord.add(entit);
		} catch (IllFormattedLineException e) {
			// nix
		} catch (OperationNotSupportedException e) {
			// nix
		}

		titleSubs =
			new LinkedList<Subfield>(TitleUtils.getSubfields(actualMusicTitle));
		makeNew130Comment();
		buildAndAddLine(false);
		if (actualMusicTitle.containsParts())
			makePortal430();
	}

	/**
	 * Erzeugt aus der 130 eine alte RAK-Form, da die Portal-Software das
	 * angeblich nicht kann. Diese Form wird aber nur dann erzeugt, wenn
	 * ein Werkteil vorliegt (da dann abweichende Regeln für 
	 * Deskriptionszeichen gelten.).
	 * 
	 * Eventuell zu überschreiben.
	 */
	protected void makePortal430() {
		// Vorderteil basteln:
		actualTag = GNDConstants.TAG_430;
		String rak = TitleUtils.getRAK(actualMusicTitle);
		Indicator a = actualTag.getIndicator('a');
		Subfield sa = null;
		try {
			sa = new Subfield(a, rak);
		} catch (IllFormattedLineException e) {
			// nix
		}
		titleSubs.clear();
		titleSubs.add(sa);
		// Hinterteil basteln:
		unusedSubs.clear();
		actualCommentStr = KOM_PORTAL_430;
		buildAndAddLine(false);
	}

	/**
	 * Default: kein Kommentar.
	 * Für maschinell zu überschreiben.
	 */
	protected void makeNew130Comment() {
		// default für intellektuell:
		actualCommentStr = null;
	}

	/**
	 * Nimmt {@link #actualTag}, {@link #titleSubs} und {@link #unusedSubs},
	 * baut eine Zeile zusammen und fügt sie in {@link #newRecord} ein.
	 * <br><br>
	 * Wenn keepOldComments == falseAus {@link #unusedSubs} werden die alten 
	 * Kommentare entfernt. <br><br>
	 * Wenn {@link #actualCommentStr} != null, wird ein neuer Kommentar
	 * eingefügt.
	 * 
	 * @param keepOldComments 	wenn true, werden die alten Kommentare
	 * 							übernommen.
	 */
	@SuppressWarnings("boxing")
	private void buildAndAddLine(final boolean keepOldComments) {
		// alte Kommentare aus unusedSubs entfernen
		if (!keepOldComments)
			unusedSubs =
				new LinkedList<Subfield>(
						RecordUtils.removeSubfieldsFromCollection(unusedSubs,
								'v'));

		// neuen Kommentar einfügen
		if (actualCommentStr != null) {
			try {
				Indicator v = actualTag.getIndicator('v');
				Subfield sv = new Subfield(v, actualCommentStr);
				RecordUtils.insertAtBestPosition(sv, titleSubs, actualTag);
			} catch (IllFormattedLineException e1) {
				//nix
			}
		}

		// beide Listen zusammenfügen und Zeile basteln:
		RecordUtils.insertAtBestPosition(unusedSubs, titleSubs, actualTag);
		Line newLine;
		try {
			newLine = LineParser.parse(actualTag, titleSubs);
			newRecord.add(newLine);
		} catch (IllFormattedLineException e) {
			// nix
		} catch (OperationNotSupportedException e) {
			// nix
		}
	}

	/**
	 * Gibt den alten Titel, wenn nur einer vorhanden ist. Sonst wird
	 * GND angenommen und null zurückgegeben.
	 * 
	 * @return Titel oder null.
	 * 
	 */
	protected final String getTitleFrom913() {
		SetOfRules rules = getRules();
		if (rules != SetOfRules.GND)
			return WorkUtils.getOriginalTitles(oldRecord).get(0);
		else
			return null;
	}

	/**
	 * Entfernt aus {@link #newRecord} die oo8, 1XX- und 4XX-Felder.
	 * Diese müssen dann aus {@link #oldRecord} wieder aufgebaut werden.
	 */
	protected final void removeHeadings() {
		Collection<GNDTag> tags1xx = TAG_DB.getTag1XX();
		RecordUtils.removeTags(newRecord, tags1xx);
		Collection<GNDTag> tags4xx = TAG_DB.getTag4XX();
		RecordUtils.removeTags(newRecord, tags4xx);
		RecordUtils.removeTags(newRecord, TAG_DB, "008");
	}

	/**
	 * Ermittelt, ob der Datensatz überhaupt verändert werden darf.
	 * Dazu werden die Kommentare und die redaktionellen Bemerkungen
	 * analysiert.
	 * 
	 * @param record 	nicht null
	 * @return			true, wenn Veränderung erlaubt.
	 */
	protected final boolean isPermitted(Record record) {
		RangeCheckUtils.assertReferenceParamNotNull("record", record);
		Line heading;
		try {
			heading = GNDUtils.getHeading(record);
		} catch (IllegalStateException e) {
			return false;
		}
		String comment = GNDUtils.getFirstComment(heading);
		if (Constants.COMMENTS_MACHINE.contains(comment))
			throw new IllegalStateException(
					"Datensatz wurde maschinell verändert "
						+ "(aus Kommentar zu 130)");
		IPredicate<Line> predicate667 = new IPredicate<Line>() {
			@Override
			public boolean accept(final Line line) {
				Subfield subfield = RecordUtils.getFirstSubfield(line, 'a');
				if (subfield == null)
					return false;
				String content = subfield.getContent();
				return Constants.GENERAL_NOTES.contains(content);
			}
		};
		Collection<Line> lines667 =
			RecordUtils.getLinesByTagsGivenAsStrings(record, predicate667,
					"667");
		return lines667.isEmpty();
	}

	/**
	 * Liefert das Regelwerk, unter dem record angesetzt wurde.
	 * Dieses steht im Feld 913
	 * 
	 * @param record	nicht null.
	 * @return	RAK, RSWK, oder GND.
	 */
	protected final SetOfRules getRules() {
		RangeCheckUtils.assertReferenceParamNotNull("record", oldRecord);
		List<String> authorities = GNDUtils.getOriginalAuthorityFile(oldRecord);
		// neu angesetzt oder "Gemerged":
		if (authorities.size() != 1)
			return SetOfRules.GND;
		String authority = authorities.get(0);
		if (authority.equals("swd"))
			return SetOfRules.RSWK;
		else if (authority.equals("est"))
			return SetOfRules.RAK;
		// ultima Ratio:
		else
			return SetOfRules.GND;
	}

	/**
	 * Zeigt der (erste) Kommentar, dass die Zeile nach RAK vor 2003
	 * gebildet wurde?
	 *  
	 * @param line	nicht null.
	 * 
	 * @return true, wenn altes Regelwerk vorliegt.
	 */
	protected final boolean isOldRAK(final Line line) {
		RangeCheckUtils.assertReferenceParamNotNull("line", line);
		String comment = GNDUtils.getFirstComment(line); // auch null möglich
		return Constants.COMMENT_OLD_RAK.contains(comment);
	}

	/**
	 * Berücksichtigt das alte Regelwerk. Werktitel, die nach diesem aufgebaut
	 * sind, sollen nicht in die üblichen Unterfelder zerlegt werden. Die
	 * einzigen erlaubten Unterfelder sind $p, $s und $v. 
	 * 
	 * @param line		nach altem Regelwerk (durch Kommentar kenntlich)
	 * 					angesetzte Zeile.	
	 */
	protected final void buildAndInsertOldRAK(final Line line) {
		RangeCheckUtils.assertReferenceParamNotNull("line", line);
		if (!isOldRAK(line))
			throw new IllegalArgumentException("kein RAK vor 2003");
		actualTag = line.getTag();
		actualCommentStr = null;
		Pair<List<Subfield>, List<Subfield>> pair =
			GNDUtils.splitCommentsFrom(line);
		titleSubs = pair.first;
		unusedSubs = pair.second;

		/*
		 *  Wenn oldSubfields mehr als 2 Einträge hat, dann lag
		 *  in den alten DMA-Daten ein Werkteil (und eventuell noch
		 *  eine Fassung) vor und die Unterfelder sind schon 
		 *  korrekt gesetzt. Ansonsten: bearbeiten!
		 */
		//
		if (titleSubs.size() == 1) {
			Pair<String, Version> titelVersPair =
				ParseMusicTitle.splitTitlePlusVersion(null, titleSubs.get(0)
						.getContent());
			if (titelVersPair != null) { // Fassung gefunden
				try {
					Subfield vor =
						new Subfield(GNDConstants.DOLLAR_a, titelVersPair.first);
					Subfield nach =
						new Subfield(GNDConstants.DOLLAR_s,
								titelVersPair.second.getMatch());
					titleSubs.clear();
					titleSubs.add(vor);
					titleSubs.add(nach);
				} catch (IllFormattedLineException e) {
					// nix
				}
			}
		}
		buildAndAddLine(true);
	}

	/**
	 * Fügt die aus title gewonnenen 3XX und 548 zu newRecord hinzu.
	 * 
	 * Da die Gesamtzahl erzeugt wird, eventuell zu überschreiben.
	 * 
	 * @param title		nicht null.
	 */
	protected void add3XX(final MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		try {
			boolean forceTotalCount = true;
			RecordUtils.addLines(newRecord,
					TitleUtils.get3XXLines(title, forceTotalCount));
		} catch (OperationNotSupportedException e) {
			// kann nichts passieren, da alle wiederholbar.
		}
	}

	/**
	 * Fügt aus altem Datensatz gewonnene Komponistendaten dem
	 * neuen Datensatz hinzu.
	 * 
	 * Eventuell zu überschreiben.
	 */
	protected void addComposerData() {
		String idn = WorkUtils.getAuthorID(oldRecord);
		if (idn == null)
			return;
		String cc = ThematicIndexDB.getCountryCode(idn);
		// bisher kein Ländercode:
		if (cc != null) {
			Line line;
			try {
				line = LineParser.parse("043 " + cc, TAG_DB);
				newRecord.add(line);
				String sourceAbb = ThematicIndexDB.getSourceAbb(idn);
				line = LineParser.parse("670 " + sourceAbb, TAG_DB);
				newRecord.add(line);
			} catch (IllFormattedLineException e) {
				//nix
			} catch (OperationNotSupportedException e) {
				//nix
			}

		}
	}

	/**
	 * Fügt newRecord 14.4p hinzu.
	 * 
	 * Eventuell zu überschreiben.
	 */
	protected void addGNDClassification() {
		Line line;
		try {
			line = LineParser.parse("065 14.4p", TAG_DB);
			newRecord.add(line);
		} catch (IllFormattedLineException e) {
			//nix
		} catch (OperationNotSupportedException e) {
			//nix
		}
	}

	/**
	 * Fügt newRecord die redaktionelle Bemerkung "VPe" hinzu.
	 * 
	 * Eventuell zu überschreiben.
	 */
	protected void addGeneralNote() {
		Line line;
		try {
			line = LineParser.parse("667 " + SATZ_AUFG, TAG_DB);
			newRecord.add(line);
		} catch (IllFormattedLineException e) {
			//nix
		} catch (OperationNotSupportedException e) {
			//nix
		}
	}

	/**
	 * @param args
	 * @throws IllFormattedLineException 
	 * @throws OperationNotSupportedException 
	 */
	public static void main2(String[] args)
			throws IllFormattedLineException,
			OperationNotSupportedException {

		Line line = LineParser.parse("430 Adagio, 3", TAG_DB);
		DefaultRecordTransformer transformer = new DefaultRecordTransformer();
	}

	/**
	 * @param args
	 * @throws IllFormattedLineException 
	 * @throws OperationNotSupportedException 
	 * @throws IOException 
	 */
	public static void main(String[] args)
			throws IllFormattedLineException,
			OperationNotSupportedException,
			IOException {
		DefaultRecordTransformer transformer = new DefaultRecordTransformer();
		Record record = RecordUtils.readFromConsole(TAG_DB);
		System.out.println(transformer.transform(record));
	}
}
