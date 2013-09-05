package de.dnb.music.mediumOfPerformance;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.dnb.music.title.ListOfElements;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

/**
 * Enthält in 
 * 		LinkedList<Instrument> children
 *  alle zum Werktitel gehörenden Instrumente (die Instrument-Objekte wiederum
 *  enthalten noch die Anzahl der gleichartigen).
 *  Im Falle eines Titels wie "Quartette, Violine 1 2, Viola, Violoncello" wäre
 *  das:
 *  [Violine(2), Viola(1), Violoncello(1)].
 *  
 * @author baumann
 *
 */
public class InstrumentationList extends ListOfElements<Instrument> implements
		TitleElement {

	InstrumentationList(final List<Instrument> iList) {
		children = new ArrayList<Instrument>(iList);
	}

	public InstrumentationList(final Instrument i) {
		children.add(i);
	}

	public final String getRest() {
		return getLast().rest;
	}

	public final LinkedList<String> idns() {
		if (children == null)
			return null;
		LinkedList<String> lls = new LinkedList<String>();
		for (Instrument i : children) {
			lls.add(i.idn);
		}
		return lls;
	}

	public final LinkedList<String> nids() {
		if (children == null)
			return null;
		LinkedList<String> lls = new LinkedList<String>();
		for (Instrument i : children) {
			lls.add(i.nid);
		}
		return lls;
	}

	@Override
	public void accept(Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren)
			visitChildren(visitor);
		visitor.leave(this);
	}

}
