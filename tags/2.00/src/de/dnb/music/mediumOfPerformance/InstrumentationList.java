package de.dnb.music.mediumOfPerformance;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

/**
 * Enthält in 
 * 		LinkedList<Instrument> instruments
 *  alle zum Werktitel gehörenden Instrumente (die Instrument-Objekte wiederum
 *  enthalten noch die Anzahl der gleichartigen).
 *  Im Falle eines Titels wie "Quartette, Violine 1 2, Viola, Violoncello" wäre
 *  das:
 *  [Violine(2), Viola(1), Violoncello(1)].
 *  
 * @author baumann
 *
 */
public class InstrumentationList implements TitleElement {

	/**
	 * Die eigentlichen Daten. Jedes Instrument enthält den zu Zeitpunkt
	 * seines Parsens noch nicht erkannten Rest.
	 */
	private LinkedList<Instrument> instruments = new LinkedList<Instrument>();

	@Deprecated
	InstrumentationList() {
	}

	InstrumentationList(final LinkedList<Instrument> b) {
		instruments = b;
	}

	public InstrumentationList(final Instrument i) {
		instruments = new LinkedList<Instrument>();
		instruments.add(i);
	}

	public final void addAll(final InstrumentationList b) {
		if (b == null || b.instruments == null)
			return;
		instruments.addAll(b.instruments);
	}

	public final void add(final Instrument i) {
		if (i == null)
			return;
		instruments.add(i);
	}

	public final String getRest() {
		return instruments.getLast().rest;
	}

	public final LinkedList<String> idns() {
		if (instruments == null)
			return null;
		LinkedList<String> lls = new LinkedList<String>();
		for (Instrument i : instruments) {
			lls.add(i.idn);
		}
		return lls;
	}

	public final LinkedList<String> nids() {
		if (instruments == null)
			return null;
		LinkedList<String> lls = new LinkedList<String>();
		for (Instrument i : instruments) {
			lls.add(i.nid);
		}
		return lls;
	}

	public final List<Instrument> getInstruments() {
		return Collections.unmodifiableList(instruments);
	}

	@Override
	public void accept(Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren)
			for (Instrument instrument : instruments) {
				instrument.accept(visitor);
			}
		visitor.leave(this);

	}

}
