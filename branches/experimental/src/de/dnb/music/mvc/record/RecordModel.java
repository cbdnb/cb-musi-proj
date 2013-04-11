package de.dnb.music.mvc.record;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Observable;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.JOptionPane;

import utils.StringUtils;
import utils.TitleUtils;
import de.dnb.music.additionalInformation.Composer;
import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.publicInterface.MusicRecord;
import de.dnb.music.publicInterface.TransformRecord;
import de.dnb.music.publicInterface.TransformRecord.TransformMode;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public class RecordModel extends Observable {

	private String oldRecord;

	private String newRecord = "";

	private TransformMode transformMode = TransformMode.INTELLECT;

	private String stackTrace = "";

	private Date creationDate;

	public final String getStackTrace() {
		return stackTrace;
	}

	public final void setOldRecord(final String oldRecord) {
		this.oldRecord = oldRecord;
	}

	public final String getNewRecord() {
		return newRecord;
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

			try {
				JarFile jarFile = new JarFile(fileStr);
				ZipEntry zEnt = jarFile.getEntry("META-INF/MANIFEST.MF");
				creationDate = new Date(zEnt.getTime());
			} catch (Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);

				stackTrace = sw.toString();
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"Fehler beim Datum", JOptionPane.OK_CANCEL_OPTION);
			}

		}
		return creationDate;
	}

	public final void setNewRecord(final String newRecord2) {
		newRecord = newRecord2;
	}

	public final void removeExpansion() {
		newRecord = StringUtils.removeExpansion(newRecord);
		newRecord = new MusicRecord(newRecord).toString(); // doppelte entfernen
		refresh();
	}

	public final void analyze() {
		if (oldRecord != null) {

			try {
				newRecord = TransformRecord.transform(oldRecord, transformMode);
			} catch (final Exception e) {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);

				stackTrace =
					oldRecord + "\n\n----------------\n\n" + sw.toString();
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"Fehler im Datensatz", JOptionPane.OK_CANCEL_OPTION);

			}
			refresh();
		}

	}

	public final void addComposer(final Composer composer, final String code) {
		MusicRecord rec = new MusicRecord(newRecord);
		rec.add("500", "!" + composer.idn + "!" + composer.name + "$4" + code);
		rec.add("670", composer.sourceAbb);
		rec.add("043", composer.countrCode);

		newRecord = rec.toString();
		refresh();
	}

	public final void addInstrument(final Instrument ins) {
		MusicRecord rec = new MusicRecord(newRecord);
		rec.addAll(TitleUtils.getGND3XX(ins, true, true));
		newRecord = rec.toString();
		refresh();
	}

	public final void addGenre(final Genre genre) {
		MusicRecord rec = new MusicRecord(newRecord);
		rec.addAll(TitleUtils.getGND3XX(genre, true, true));
		newRecord = rec.toString();
		refresh();
	}

	public final void addTitle(final String number, final String titleStr) {
		MusicRecord rec = new MusicRecord(newRecord);
		MusicTitle title = ParseMusicTitle.parseFullRAK(null, titleStr);
		final boolean expansion = true;
		final boolean forceTotalCount = true;
		String s;
		try {
			s = number + " " + TitleUtils.getGND130Or430(title) + "\n"
				+ TitleUtils.getGND3XX(title, expansion, forceTotalCount);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			stackTrace =
				oldRecord + "\n\n----------------\n\n" + sw.toString();
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehler bei der Eingabe", JOptionPane.OK_CANCEL_OPTION);
			return;
		}
		rec.addAll(s);
		newRecord = rec.toString();
		refresh();
	}

}
