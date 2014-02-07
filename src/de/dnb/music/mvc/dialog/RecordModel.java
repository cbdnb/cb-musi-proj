package de.dnb.music.mvc.dialog;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Observable;
import java.util.Stack;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.naming.OperationNotSupportedException;
import javax.swing.JOptionPane;

import de.dnb.basics.applicationComponents.RangeCheckUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.exceptions.WrappingHandler;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.RecordParser;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.parser.tag.GNDTagDB;
import de.dnb.gnd.parser.tag.TagDB;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.music.additionalInformation.Composer;
import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.publicInterface.DefaultRecordTransformer;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.utils.GNDConstants;
import de.dnb.music.utils.GNDTitleUtils;
import de.dnb.music.utils.MusicIDFinder;
import de.dnb.music.utils.TitleUtils;

public class RecordModel extends Observable {

	public RecordModel() {
		this.oldRecordStr = "";
		this.tagDB = GNDTagDB.getDB();
		theRecord = new Record(null, tagDB);
		this.parser = new RecordParser();
		this.parser.setHandler(new WrappingHandler());
		this.stackTrace = "";
		this.expanded = true;
		this.transformer = new DefaultRecordTransformer();
		returnsPicaPlus = false;
		explicitWanted = false;
	}

	private Stack<Record> history = new Stack<Record>();

	private String oldRecordStr;

	private TagDB tagDB;

	private Record theRecord;;

	private RecordParser parser;

	private String stackTrace;

	private Date creationDate;

	private boolean expanded;

	private DefaultRecordTransformer transformer;

	private boolean returnsPicaPlus;
	
	private boolean explicitWanted;

	public final void undo() {
		if (!history.empty())
			theRecord = history.pop();
		refresh();
	}

	public final String getStackTrace() {
		return stackTrace;
	}

	public final void setOldRecord(final String oldRecordS) {
		RangeCheckUtils.assertReferenceParamNotNull("oldRecordS", oldRecordS);
		this.oldRecordStr = oldRecordS;
		transformOldRecordString();
	}
	
	public final void reset() {
		Record record = theRecord.clone();
		theRecord = new Record(null, tagDB);
		Line line;
		try {
			line = LineParser.parse("065 14.4p", tagDB);
			theRecord.add(line);
		} catch (IllFormattedLineException e) {
			//nix
		} catch (OperationNotSupportedException e) {
			//nix
		}
		history.push(record);
		refresh();
	}

	public final String getPica() {
		if (theRecord == null)
			return "";
		Format format = returnsPicaPlus ? Format.PICA_PLUS : Format.PICA3;
		return RecordUtils.toPica(theRecord, format, expanded, "\n", '$');
	}

	public final void refresh() {
		GNDTitleUtils.setTotalInstrumentCount(theRecord);
		setChanged();
		notifyObservers(null);
	}

	public final void add(Line line) {
		try {
			Record record = theRecord.clone();
			theRecord.add(line);
			history.push(record);
		} catch (OperationNotSupportedException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehler im Datensatz", JOptionPane.OK_CANCEL_OPTION);
		}
		refresh();
	}

	/**
	 * Nimmt den String (aus Textfeld für neuen Datensatz) und wandelt ihn
	 * in einen Record um.
	 * 
	 * @param recordStr	nicht null
	 */
	public final void analyzeNewRecordString(final String recordStr) {
		try {
			Record record = theRecord.clone();
			theRecord = parser.parse(recordStr);
			if (!theRecord.equals(record))
				history.push(record);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehler im Datensatz", JOptionPane.OK_CANCEL_OPTION);
		}
		refresh();
	}

	public final void setExpansion(final boolean expansion) {
		this.expanded = expansion;
		refresh();
	}

	public final void setReturnsPica(final boolean returnsPica) {
		this.returnsPicaPlus = returnsPica;
		refresh();
	}

	private final void transformOldRecordString() {
		try {
			Record clone = theRecord.clone();
			Record oldRecord = parser.parse(oldRecordStr);
			theRecord = transformer.transform(oldRecord);
			// dann soll ein neuer Datensatz generiert werden:
			if (RecordUtils.isEmpty(theRecord)) {
				transformer.addGeneralNote(theRecord);
				transformer.addGNDClassification(theRecord);
			}
			history.push(clone);
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
			Record clone = theRecord.clone();
			Line line =
				LineParser.parse("500 " + "!" + composer.idn + "!"
					+ composer.name + "$4" + code, tagDB);
			theRecord.add(line);
			line = LineParser.parse("670 " + composer.sourceAbb, tagDB);
			theRecord.add(line);
			line = LineParser.parse("043 " + composer.countrCode, tagDB);
			theRecord.add(line);
			history.push(clone);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehler im Datensatz", JOptionPane.OK_CANCEL_OPTION);
		}
		refresh();
	}

	private MusicIDFinder finder = new MusicIDFinder();



	public final String getAleph() {
		if (theRecord == null)
			return "";
		try {
			return GNDUtils.toAleph(theRecord, finder, explicitWanted);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehler im Datensatz", JOptionPane.OK_CANCEL_OPTION);
		}
		return "";
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

	public void setReturnsExplicit(boolean explicitWanted) {
		this.explicitWanted = explicitWanted;
		refresh();
		
	}

}
