package de.dnb.music.mvc.title;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Date;
import java.util.Observable;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.JOptionPane;

import applikationsbausteine.FileUtils;

import utils.GNDTitleUtils;
import utils.StringUtils;
import utils.TitleUtils;

import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public class TitleModel extends Observable {

	private MusicTitle musicTitle = null;

	private boolean returnsPicaPlus;

	private boolean forceTotalCount = false;

	private boolean expansion = false;

	private String title = "";

	private String composer = "";

	private Date creationDate;

	public final void setTitle(final String title) {
		this.title = title;
	}

	public final void setComposer(final String composer) {
		this.composer = composer;
	}

	public final void setReturnsPica(final boolean returnsPica) {
		this.returnsPicaPlus = returnsPica;
	}

	public final void setRegnognizeKeyName(final boolean regnognizeKeyName) {
		ParseMusicTitle.setRegnognizeKeyName(regnognizeKeyName);
	}

	public final void setRegnognizePopularMusic(
			final boolean regnognizePopularMusic) {
		ParseMusicTitle.setRegnognizePopularMusic(regnognizePopularMusic);
	}

	public final void setExpansion(final boolean expansion) {
		this.expansion = expansion;
	}

	public final void setForceTotalCount(final boolean forceTotalCount) {
		this.forceTotalCount = forceTotalCount;
	}

	public final String getGND() {
		Record record = GNDTitleUtils.getRecord(musicTitle, forceTotalCount);
		Format format = returnsPicaPlus ? Format.PICA_PLUS : Format.PICA3;
		return RecordUtils.toPica(record, format, expansion, "\n", '$');
	}

	public final String getRAK() {
		return TitleUtils.getRAK(musicTitle);
	}

	public final String getRSWK() {
		return TitleUtils.getRSWK(musicTitle);
	}

	public final String getStructured() {
		String structured = TitleUtils.getStructured(musicTitle);
		return structured;
	}

	public final String getSetOfRules() {
		String s = "";
		if (TitleUtils.isRAK(title))
			s += "RAK\n";
		if (TitleUtils.isRSWK(title))
			s += "RSWK\n";
		if (TitleUtils.isGND(title))
			s += "GND\n";
		return s;
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
		MusicTitle mt = null;
		try {
			mt = ParseMusicTitle.parseFullRAK(composer, title);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Fehlerhafte Eingabe", JOptionPane.OK_CANCEL_OPTION);
		}
		if (mt != null) {
			this.musicTitle = mt;
			setChanged();
			notifyObservers(null);
		}
	}

}
