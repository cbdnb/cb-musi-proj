package de.dnb.music.mvc.synth;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Observable;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.JOptionPane;

import utils.GNDTitleUtils;
import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.visitor.TitleElement;

public class Model extends Observable {

	private MusicTitle theTitle = null;

	private boolean returnsPicaPlus;

	private boolean forceTotalCount = false;

	private boolean expansion = false;

	private Date creationDate;

	public final void reset() {
		theTitle = null;
	}

	public final void addElement(TitleElement element) {
		RangeCheckUtils.assertReferenceParamNotNull("element", element);
		try {
			element.addToTitle(theTitle);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Bitte erst neuen Titel erzeugen",
					JOptionPane.OK_CANCEL_OPTION);
		}
	}

	public final void addElement(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		if (theTitle == null)
			theTitle = title;
		else
			title.addToTitle(theTitle);
	}

	public final void setReturnsPica(final boolean returnsPica) {
		this.returnsPicaPlus = returnsPica;
	}

	public final void setExpansion(final boolean expansion) {
		this.expansion = expansion;
	}

	public final void setForceTotalCount(final boolean forceTotalCount) {
		this.forceTotalCount = forceTotalCount;
	}

	public final String getGND() {
		if (theTitle == null)
			return "";
		Record record = GNDTitleUtils.getRecord(theTitle, forceTotalCount);
		Format format = returnsPicaPlus ? Format.PICA_PLUS : Format.PICA3;
		return RecordUtils.toPica(record, format, expansion, "\n", '$');
	}

	public final String getRAK() {
		if (theTitle == null)
			return "";
		return TitleUtils.getRAK(theTitle);
	}

	public final String getRSWK() {
		if (theTitle == null)
			return "";
		return TitleUtils.getRSWK(theTitle);
	}

	public final String getStructured() {
		if (theTitle == null)
			return "";
		String structured = TitleUtils.getStructured(theTitle);
		return structured;
	}

	/**
	 * @return the creationDate
	 */
	public final Date getCreationDate() {
		if (creationDate == null) {

			/*
			 * Die Manifest-Datei der eigenen jar-Datei ist immer aktuell. Daher
			 * Zugriff auf deren URL:
			 */
			URL url = this.getClass().getResource("/META-INF/MANIFEST.MF");
			String fileStr = url.getFile();
			/*
			 * Die URL ist etwas komplizierter aufgebaut. Sie hat - ein Präfix
			 * "file:/" - ein Postfix, das mit "!" beginnt, welches die Dateien
			 * in der .jar kennzeichnet.
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

	public final void refresh() {
		setChanged();
		notifyObservers(null);

	}

}
