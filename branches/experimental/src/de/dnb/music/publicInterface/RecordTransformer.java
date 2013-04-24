package de.dnb.music.publicInterface;

import static de.dnb.music.publicInterface.Constants.SATZ_AUFG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import utils.GNDConstants;
import utils.StringUtils;
import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Field;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.RecordParser;
import de.dnb.gnd.parser.Subfield;
import de.dnb.gnd.parser.Tag;
import de.dnb.gnd.parser.TagDB;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineFactory;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.gnd.utils.GNDUtils2;
import de.dnb.gnd.utils.Pair;
import de.dnb.gnd.utils.WorkUtils;
import de.dnb.music.additionalInformation.ThematicIndexDB;
import de.dnb.music.publicInterface.Constants.SetOfRules;
import de.dnb.music.publicInterface.Constants.TransformMode;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.version.Version;
import filtering.IPredicate;

/**
 * Template-Klasse. Die Unterklassen überschreiben div. Methoden, um an das
 * zu bearbeitende Datenset anzupassen.
 *
 * @author baumann
 *
 */
public class RecordTransformer {

	protected Record oldRecord;
	protected Record newRecord;

	/**
	 * Template-Methode.
	 * 
	 * @param record	nicht null.
	 * @return			neuer Record.
	 */
	public Record transform(final Record record) {
		RangeCheckUtils.assertReferenceParamNotNull("oldRecord", record);
		this.oldRecord = record;
		newRecord = oldRecord.clone();
		if (isPermitted(oldRecord)) {
			addComposerData();
			addGeneralNote();
			addGNDClassification();
			removeTitles();
			transform130();
			Collection<Line> lines430 = GNDUtils.getLines(oldRecord, "430");
			for (Line line : lines430) {
				transform430(line);
			}
		}
		return newRecord;
	}

	protected void transform430(Line line) {
		// TODO Auto-generated method stub
		
	}

	protected void transform130() {
		// TODO Auto-generated method stub
		
	}

	protected void removeTitles() {
		Collection<Tag> tags1xx = TagDB.getTag1XX();
		GNDUtils.removeTags(newRecord, tags1xx);
		Collection<Tag> tags4xx = TagDB.getTag4XX();
		GNDUtils.removeTags(newRecord, tags4xx);
	}

	protected boolean isPermitted(Record record) {
		Line heading;
		try {
			heading = GNDUtils2.getHeading(record);
		} catch (IllegalStateException e) {
			return false;
		}
		String comment = GNDUtils2.getFirstComment(heading);
		if (Constants.COMMENTS_MACHINE.contains(comment))
			return false;
		IPredicate<Line> predicate667 = new IPredicate<Line>() {
			@Override
			public boolean accept(Line line) {
				Subfield subfield = GNDUtils.getFirstSubfield(line, 'a');
				if (subfield == null)
					return false;
				String content = subfield.getContent();
				return Constants.GENERAL_NOTES.contains(content);
			}
		};
		Collection<Line> lines667 =
			GNDUtils.getLinesTagsGivenAsString(record, predicate667, "667");
		return lines667.isEmpty();
	}

	/**
	 * Liefert das Regelwerk, unter dem record angesetzt wurde.
	 * Dieses steht im Feld 913
	 * 
	 * @param record	nicht null.
	 * @return	RAK, RSWK, oder GND.
	 */
	protected final SetOfRules getRules(final Record record) {
		RangeCheckUtils.assertReferenceParamNotNull("record", record);
		ArrayList<String> authorities =
			GNDUtils2.getOriginalAuthorityFile(record);
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
	 * Berücksichtigt das alte Regelwerk. Werktitel, die nach diesem aufgebaut
	 * sind, sollen nicht in die üblichen Unterfelder zerlegt werden. Die
	 * einzigen erlaubten Unterfelder sind $p, $s und $v. 
	 * 
	 * @param line		nach altem Regelwerk (durch Kommentar kenntlich)
	 * 					angesetzte Zeile.	
	 * @return			Alten Titel mit Unterfeldern $p und $s mit biherigem
	 *  				Kommentar.
	 */
	protected final Line transformOldRAK(final Line line) {
		RangeCheckUtils.assertReferenceParamNotNull("line", line);
		LineFactory factory = LineParser.getFactory(line.getTag());

		/*
		 * jetzt aufpassen: hier sollen nur die Unterfelder
		 * $p und $s erzeugt werden.
		 */
		Pair<List<Subfield>, List<Subfield>> pair =
			GNDUtils2.splitComment(line);
		List<Subfield> oldSubfields = pair.first;
		List<Subfield> newSubfields;
		List<Subfield> comments = pair.second;

		/*
		 *  Wenn oldSubfields mehr als 2 Einträge hat, dann lag
		 *  in den alten DMA-Daten ein Werkteil (und eventuell noch
		 *  eine Fassung) vor und die Unterfelder sind schon 
		 *  korrekt gesetzt:
		 */
		int size = oldSubfields.size();
		if (size == 1) {
			newSubfields = new LinkedList<Subfield>();
			Pair<String, Version> titelVersPair =
				ParseMusicTitle.splitTitlePlusVersion(null, oldSubfields.get(0)
						.getContent());
			if (titelVersPair != null) { // Fassung gefunden
				try {
					Subfield vor =
						new Subfield(GNDConstants.DOLLAR_a, titelVersPair.first);
					Subfield nach =
						new Subfield(GNDConstants.DOLLAR_s,
								titelVersPair.second.getMatch());
					newSubfields.add(vor);
					newSubfields.add(nach);
				} catch (IllFormattedLineException e) {
					// nix
				}
			} else { // keine Fassung gefunden:
				newSubfields = oldSubfields;
			}
		} else {
			newSubfields = oldSubfields;
		}
		newSubfields.addAll(comments);
		try {
			factory.load(newSubfields);
		} catch (IllFormattedLineException e) {
			//nix
		}
		return factory.createLine();
	}

	/**
	 * Fügt 3XX und 548 hinzu.
	 * @param title		nicht null.
	 */
	protected void add3XX(final MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		try {
			boolean forceTotalCount = true;
			GNDUtils.addLines(newRecord,
					TitleUtils.get3XXLines(title, forceTotalCount));
		} catch (OperationNotSupportedException e) {
			// kann nichts passieren, da alle wiederholbar.
		}
	}

	/**
	 * Fügt aus altem Datensatz gewonnene Komponistendaten dem
	 * neuen Datensatz hinzu.
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
				line = LineParser.parse("043 " + cc);
				newRecord.add(line);
				String sourceAbb = ThematicIndexDB.getSourceAbb(idn);
				line = LineParser.parse("670 " + sourceAbb);
				newRecord.add(line);
			} catch (IllFormattedLineException e) {
				//nix
			} catch (OperationNotSupportedException e) {
				//nix
			}

		}
	}

	/**
	 * Fügt 14.4p hinzu.
	 */
	protected void addGNDClassification() {
		Line line;
		try {
			line = LineParser.parse("065 14.4p");
			newRecord.add(line);
		} catch (IllFormattedLineException e) {
			//nix
		} catch (OperationNotSupportedException e) {
			//nix
		}
	}

	/**
	 * Redaktionelle Bemerkung.
	 */
	protected void addGeneralNote() {
		Line line;
		try {
			line = LineParser.parse("667 " + SATZ_AUFG);
			newRecord.add(line);
		} catch (IllFormattedLineException e) {
			//nix
		} catch (OperationNotSupportedException e) {
			//nix
		}
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
	 * @param record	nicht null.
	 * @return Liste der Werktitel, eventuell leer.
	 */
	protected final ArrayList<String> getOriginalTitles(final Record record) {
		RangeCheckUtils.assertReferenceParamNotNull("record", record);
		ArrayList<String> headings = GNDUtils2.getOriginalHeadings(record);
		ArrayList<String> titles = new ArrayList<String>();
		for (String heading : headings) {
			String[] parts = heading.split(": ");
			if (parts.length == 2) {
				titles.add(parts[1].replace("{", ""));
			} else
				titles.add(heading.replace("{", ""));
		}
		return titles;
	}

	/**
	 * @param args
	 * @throws IllFormattedLineException 
	 * @throws OperationNotSupportedException 
	 */
	public static void main2(String[] args)
			throws IllFormattedLineException,
			OperationNotSupportedException {

		Line line = LineParser.parse("430 Adagio, 3");

		RecordTransformer transformer = new RecordTransformer();

		System.out.println(transformer.transformOldRAK(line));

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
		RecordTransformer transformer = new RecordTransformer();
		BufferedReader reader =
			new BufferedReader(new InputStreamReader(System.in));
		RecordParser parser = new RecordParser(reader);
		Record record = parser.getNextRecord();
		System.out.println(transformer.transform(record));
	}
}
