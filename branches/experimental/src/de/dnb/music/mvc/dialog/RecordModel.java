package de.dnb.music.mvc.dialog;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Observable;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.naming.OperationNotSupportedException;
import javax.swing.JOptionPane;

import applikationsbausteine.RangeCheckUtils;

import utils.GNDConstants;
import utils.GNDTitleUtils;
import utils.TitleUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.exceptions.WrappingHandler;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.RecordParser;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.parser.tag.GNDTagDB;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.music.additionalInformation.Composer;
import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.publicInterface.DefaultRecordTransformer;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public class RecordModel extends Observable {

	public RecordModel() {
		this.oldRecordStr = "";
		this.tagDB = GNDTagDB.getDB();
		newRecord = new Record(null, tagDB);
		this.parser = new RecordParser();
		this.parser.setHandler(new WrappingHandler());
		this.stackTrace = "";
		this.expanded = true;
		this.transformer = new DefaultRecordTransformer();
	}

	private String oldRecordStr;

	private GNDTagDB tagDB;

	private Record newRecord;;

	private RecordParser parser;

	private String stackTrace;

	private Date creationDate;

	private boolean expanded;

	private DefaultRecordTransformer transformer;

	public final String getStackTrace() {
		return stackTrace;
	}

	public final void setOldRecord(final String oldRecordS) {
		RangeCheckUtils.assertReferenceParamNotNull("oldRecordS", oldRecordS);
		this.oldRecordStr = oldRecordS;
	}

	public final String getNewRecordString() {
		return RecordUtils.toPica(newRecord, Format.PICA3, expanded, "\n", '$');
	}

	public final void refresh() {
		setChanged();
		notifyObservers(null);
	}

	/**
	 * @return the creationDate
	 */
	public final Date getCreationDate() {
		if (creationDate == null) {

			/*
			 * Die Manifest-Datei der eigenen jar-Datei ist immer aktuell. 
			 * Daher Zugriff auf deren URL:
			 */
			URL url = this.getClass().getResource("/META-INF/MANIFEST.MF");
			String fileStr = url.getFile();
			/*
			 * Die URL ist etwas komplizierter aufgebaut. Sie hat 
			 * 	-	ein Präfix "file:/"
			 * 	- 	ein Postfix, das mit "!" beginnt, welches die Dateien in
			 * 		der .jar kennzeichnet.
			 * 
			 */
			int pos1 = "file:/".length();
			int pos2 = fileStr.indexOf("!");
			fileStr = fileStr.substring(pos1, pos2);
			/*
			 * fileStr enthält nun nur noch den Pfad der eigenen jar-Datei
			 */
			JarFile jarFile = null;
			try {
				jarFile = new JarFile(fileStr);
				ZipEntry zEnt = jarFile.getEntry("META-INF/MANIFEST.MF");
				creationDate = new Date(zEnt.getTime());
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);

				stackTrace = sw.toString();
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"Fehler beim Datum", JOptionPane.OK_CANCEL_OPTION);
			} finally {
				if (jarFile != null)
					try {
						jarFile.close();
					} catch (IOException e) {
						// nix
					}
			}

		}
		return creationDate;
	}

	/**
	 * Nimmt den String (aus Textfeld für neuen Datensatz) und wandelt ihn
	 * in einen Record um.
	 * 
	 * @param recordStr	nicht null
	 */
	public final void setNewRecord(final String recordStr) {
		try {
			newRecord = parser.parse(recordStr);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehler im Datensatz", JOptionPane.OK_CANCEL_OPTION);
			refresh();
		}

	}

	public final void changeExpansion() {
		expanded = !expanded;
		refresh();
	}

	public final void analyze() {

		try {
			Record oldRecord = parser.parse(oldRecordStr);
			newRecord = transformer.transform(oldRecord);
			// dann soll ein neuer Datensatz generiert werden:
			if(RecordUtils.isEmpty(newRecord)) {
				transformer.addGeneralNote(newRecord);
				transformer.addGNDClassification(newRecord);
			}
		} catch (final Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			stackTrace =
				oldRecordStr + "\n\n----------------\n\n" + sw.toString();
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehler im Datensatz", JOptionPane.OK_CANCEL_OPTION);

		}
		refresh();

	}

	public final void addComposer(final Composer composer, final String code) {
		try {
			Line line =
				LineParser.parse("500 " + "!" + composer.idn + "!"
					+ composer.name + "$4" + code, tagDB);
			newRecord.add(line);
			line = LineParser.parse("670 " + composer.sourceAbb, tagDB);
			newRecord.add(line);
			line = LineParser.parse("043 " + composer.countrCode, tagDB);
			newRecord.add(line);
		} catch (IllFormattedLineException e) {
			// nix
		} catch (OperationNotSupportedException e) {
			// nix
		}
		refresh();
	}

	public final void addInstrument(final Instrument ins) {
		try {
			RecordUtils.addLines(newRecord,
					GNDTitleUtils.get3XXLines(ins, true));
		} catch (OperationNotSupportedException e) {
			// nix
		}
		refresh();
	}

	public final void addGenre(final Genre genre) {
		try {
			RecordUtils.addLines(newRecord,
					GNDTitleUtils.get3XXLines(genre, true));
		} catch (OperationNotSupportedException e) {
			// nix
		}
		refresh();
	}

	public final void addTitle(final String number, final String titleStr) {

		try {
			MusicTitle title = ParseMusicTitle.parse(null, titleStr);
			final boolean forceTotalCount = true;
			Line line = GNDTitleUtils.getLine(GNDConstants.TAG_130, title);
			newRecord.add(line);
			RecordUtils.addLines(newRecord,
					GNDTitleUtils.get3XXLines(title, forceTotalCount));
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			stackTrace =
				oldRecordStr + "\n\n----------------\n\n" + sw.toString();
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehler bei der Eingabe", JOptionPane.OK_CANCEL_OPTION);
			return;
		}
		refresh();
	}

}
