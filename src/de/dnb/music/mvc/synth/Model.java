package de.dnb.music.mvc.synth;

import static utils.GNDConstants.TAG_DB;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Observable;
import java.util.Stack;
import java.util.TreeSet;
import java.util.regex.Matcher;

import javax.swing.JOptionPane;

import cloneable.CopyObjectUtils;

import utils.GNDTitleUtils;
import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.gnd.parser.Format;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.music.publicInterface.Constants;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.visitor.TitleElement;

public class Model extends Observable {

	private MusicTitle theTitle = null;

	private Stack<MusicTitle> history = new Stack<MusicTitle>();

	private boolean returnsPicaPlus;

	private boolean forceTotalCount = false;

	private boolean expansion = false;

	public final void reset() {
		history.push(CopyObjectUtils.copyObject(theTitle));
		theTitle = null;
		refresh();
	}

	public void undo() {
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
	public final boolean addElement(TitleElement element) {
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

	public final void addElement(MusicTitle title) {
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
		//		System.err.println("--------");
		//		for (MusicTitle title : history) {
		//			if (title == null)
		//				System.err.println("**" + title);
		//			else
		//				System.err.println("**" + TitleUtils.getStructured(title));
		//		}
	}

	public String getAleph() {
		if (theTitle == null)
			return "";
		return TitleUtils.getAleph(theTitle, forceTotalCount);

	}

}
