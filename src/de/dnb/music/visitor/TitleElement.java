package de.dnb.music.visitor;

import de.dnb.music.title.MusicTitle;

public abstract interface TitleElement {

	void accept(Visitor visitor);
	
	/**
	 * Fügt das betreffende Titel-Element an der richtigen Stelle des
	 * Titels ein. Dabei soll angenommen werden, dass die Synthese des
	 * Titels Schritt für Schritt von vorne nach hinten erfolgt.
	 * 
	 * @param title	Titel, dem das Element hinzugefügt wird.
	 */
	void addToTitle(MusicTitle title);
}
