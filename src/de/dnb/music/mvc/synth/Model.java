package de.dnb.music.mvc.synth;

import java.util.Observable;
import java.util.Stack;

import javax.naming.OperationNotSupportedException;
import javax.swing.JOptionPane;

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

	private Composer tehComposer = null;

	private TagDB tagDB = GNDTagDB.getDB();

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
		Record record = GNDTitleUtils.getRecord(theTitle, forceTotalCount);
		if (tehComposer != null) {
			try {
				Line line =
					LineParser.parse("500 " + "!" + tehComposer.idn + "!"
						+ tehComposer.name + "$4kom1", tagDB);
				record.add(line);
				line = LineParser.parse("670 " + tehComposer.sourceAbb, tagDB);
				record.add(line);
				line = LineParser.parse("043 " + tehComposer.countrCode, tagDB);
				record.add(line);
			} catch (IllFormattedLineException e) {
				// nix
			} catch (OperationNotSupportedException e) {
				// nix
			}
		}
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

	public final void refresh() {
		setChanged();
		notifyObservers(null);
	}

	public final String getAleph() {
		if (theTitle == null)
			return "";
		return TitleUtils.getAleph(theTitle, forceTotalCount, tehComposer);

	}

	public final void addComposer(final Composer composer) {
		this.tehComposer = composer;
		refresh();
	}

}
