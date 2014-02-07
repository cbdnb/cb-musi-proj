package de.dnb.music.mvc.dialog;

import java.util.Observable;
import java.util.Stack;

import javax.swing.JOptionPane;

import de.dnb.basics.applicationComponents.RangeCheckUtils;
import de.dnb.basics.cloneable.CopyObjectUtils;
import de.dnb.gnd.parser.tag.GNDTagDB;
import de.dnb.gnd.parser.tag.TagDB;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.utils.TitleUtils;
import de.dnb.music.visitor.TitleElement;

public class DialogModel extends Observable {

	private MusicTitle theTitle = null;

	private Stack<MusicTitle> history = new Stack<MusicTitle>();

	private boolean returnsPicaPlus;

	private boolean expansion = false;

	private TagDB tagDB = GNDTagDB.getDB();

	public final void reset() {
		history.push(de.dnb.basics.cloneable.CopyObjectUtils.copyObject(theTitle));
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
		refresh();
	}

	public final String getGND() {
		if (theTitle == null)
			return "";
		return TitleUtils.getX30ContentAsString(theTitle);
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
	
	public final MusicTitle getTitle() {
		return theTitle;
	}

	public final void refresh() {
		setChanged();
		notifyObservers(null);
	}

}
