package de.dnb.music.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Observable;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.JOptionPane;

import utils.StringUtils;
import de.dnb.music.publicInterface.TransformRecord;
import de.dnb.music.publicInterface.TransformRecord.TransformMode;

public class RecordModel extends Observable {

	private String oldRecord;

	private String newRecord;

	private TransformMode transformMode = TransformMode.INTELLECT;

	private boolean isPicaMode = false;

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
			if (isPicaMode) {
				newRecord = StringUtils.gnd2Pica(newRecord);
			}
			setChanged();
			notifyObservers(null);
		}
	}

	public final void setPicaMode(final boolean picaMode) {
		isPicaMode = picaMode;
	}

	public final void setScriptMode(final boolean scriptMode) {
		if (scriptMode) {
			transformMode = TransformMode.MACHINE;
		} else {
			transformMode = TransformMode.INTELLECT;
		}

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
						"Fehler beim Datum",
						JOptionPane.OK_CANCEL_OPTION);
			}

		}
		return creationDate;
	}

}
