package de.dnb.music.mvc.synth;

import java.util.Observable;
import java.util.Stack;

import javax.naming.OperationNotSupportedException;
import javax.swing.JOptionPane;

import utils.MusicIDFinder;
import utils.GNDTitleUtils;
import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import cloneable.CopyObjectUtils;
import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.line.LineParser;
import de.dnb.gnd.parser.tag.GNDTagDB;
import de.dnb.gnd.parser.tag.TagDB;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.music.additionalInformation.Composer;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.visitor.TitleElement;

public class Model extends Observable {

	private MusicTitle theTitle = null;

	private Stack<MusicTitle> history = new Stack<MusicTitle>();

	private boolean returnsPicaPlus;

	private boolean forceTotalCount = false;

	private boolean expansion = false;

	private Composer theComposer = null;

	private TagDB tagDB = GNDTagDB.getDB();

	private Record theRecord;

	public final void reset() {
		history.push(CopyObjectUtils.copyObject(theTitle));
		theTitle = null;
		refresh();
	}

	public final void undo() {
		if (!history.empty())
			theTitle = history.pop();
		refresh();
	}

	/**
	 * Fügt Element dem Titel hinzu.
	 * 
	 * @param element	nicht null
	 * @return			Erfolg.
	 */
	public final boolean addElement(final TitleElement element) {
		RangeCheckUtils.assertReferenceParamNotNull("element", element);
		try {
			MusicTitle oldTitle = CopyObjectUtils.copyObject(theTitle);
			element.addToTitle(theTitle);
			history.push(oldTitle);
			refresh();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),
					"Erst neuen Titel erzeugen", JOptionPane.OK_CANCEL_OPTION);
			return false;
		}
	}

	public final void addElement(final MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		history.push(CopyObjectUtils.copyObject(theTitle));
		if (theTitle == null)
			theTitle = title;
		else
			title.addToTitle(theTitle);
		refresh();
	}

	public final void setReturnsPica(final boolean returnsPica) {
		this.returnsPicaPlus = returnsPica;
		refresh();
	}

	public final void setExpansion(final boolean expansion) {
		this.expansion = expansion;
		refresh();
	}

	public final void setForceTotalCount(final boolean forceTotalCount) {
		this.forceTotalCount = forceTotalCount;
		refresh();
	}

	public final String getGND() {
		if (theTitle == null)
			return "";
		buildRecord();
		Format format = returnsPicaPlus ? Format.PICA_PLUS : Format.PICA3;
		return RecordUtils.toPica(theRecord, format, expansion, "\n", '$');
	}

	/**
	 * @return
	 */
	private void buildRecord() {
		if (theTitle == null){
			theRecord = null;
			return;
		}
		theRecord = GNDTitleUtils.getRecord(theTitle, forceTotalCount);
		if (theComposer != null) {
			try {
				Line line =
					LineParser.parse("500 " + "!" + theComposer.idn + "!"
						+ theComposer.name + "$4kom1", tagDB);
				theRecord.add(line);
				line = LineParser.parse("670 " + theComposer.sourceAbb, tagDB);
				theRecord.add(line);
				line = LineParser.parse("043 " + theComposer.countrCode, tagDB);
				theRecord.add(line);
			} catch (IllFormattedLineException e) {
				// nix
			} catch (OperationNotSupportedException e) {
				// nix
			}
		}
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

	public final void refresh() {
		buildRecord();
		setChanged();
		notifyObservers(null);
	}
	
	private MusicIDFinder finder = new MusicIDFinder();

	public final String getAleph() {
		if (theTitle == null)
			return "";
		return GNDUtils.toAleph(theRecord,finder);
//		return TitleUtils.getAleph(theTitle, forceTotalCount, theComposer);

	}

	public final void addComposer(final Composer composer) {
		this.theComposer = composer;
		refresh();
	}

}
