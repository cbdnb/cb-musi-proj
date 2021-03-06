package de.dnb.music.version;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.additionalInformation.ParseAdditionalInformation;
import de.dnb.music.genre.GenreList;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public class Version implements TitleElement {

	/**
	 * Was nach der rak-Phrase kommt. Wird in DBFassung gesetzt und dann
	 * von
	 */
	String rest = "";

	char fallgruppeParagraphM511 = 0;

	int untergruppe = 0;

	// Mögliche Fälle:

	/*	 Fall § M 511 a)
	 Hier folgt eine WV-Nummer oder eine op.-Zahl. Beispiel:
	 800 |p|Beethoven, Ludwig {van
	 801 |t|Fuge, Violine 1,2 Viola Violoncello op. 133. Op. 134	
	 
	 Untergruppen:
	 1: WV
	 2: op.
	 * 
	 */
	// ->
	AdditionalInformation additionalInformation;

	/* 
	 * Fall § M 511 b)
	 * ein Form- oder Gattungsbegriff gemäß Anlage M 10 im Singular, (!!!)
	 * gegebenenfalls erweitert um  eine  zusätzliche  Angabe  (Besetzung,  
	 * Tonart,  Entstehungsjahr);  vor  dem  Form-  oder Gattungsbegriff  steht
	 * Punkt,  Spatium  (.  ),  vor  der  zusätzlichen  Angabe  lediglich  ein 
	 * Spatium; 
	 * Unterruppe: 
	 * 1: keine weitere Angabe, nur Form- und Gattungsbegriff
	 * 2: Besetzung
	 * 3: Tonart
	 * 4: Entstehungsjahr
	 * 5: sonstige Zählung (nicht im Regelwerk, kommt aber beim DMA vor)
	 * 
	 * */

	// ->
	GenreList genreList; // im Singular

	InstrumentationList instrumentationList;

	// Nicht nötig, da oben schon definiert:
	// public Zusatzangabe additionalInformation;

	//	 Fall § M 511 c)
	/*  eine  andere  erläuternde  Angabe  (z.B.  Entstehungsjahr  oder  
	 * Besetzung),  eingeleitet  durch das  Wort  "Fassung" (Anm: auch andere 
	 * Worte sind möglich);  vor  dem  Wort "Fassung"  steht  Punkt,  Spatium  
	 * (.  ),  vor  der erläuternden Angabe lediglich ein Spatium; 
	 * 
	 * Untergruppe:
	 * 1: Jahr
	 * 2: Besetzung
	 * 3: Irgend eine andere Angabe oder auch keine: "Frühfassung" 
	 * 	  "Fassung für Bläser" (Nicht im Regelwerk explizit, aber oft 
	 * 	  in der Datenbank). 66-mal kommt so etwas wie "Fassung 2" vor,
	 * 	  was auch hier abgehandelt wird.
	 * 4: die Kombination Besetzung + Jahr (9-mal in Datenbank)
	 * 
	 * */

	// -> für "Fassung" oder "Erw. Fassung" ...
	String rakPhrase = null;
	// für Untergruppe 3: (getrimmt), aber schon definiert:
	// 

	// Nicht nötig, da oben schon definiert:
	//  Gattungsliste genreList
	//  Zusatzangabe additionalInformation; 

	/* Der Fall § M 511 d) (alles andere) kann m.E. nicht geparst werden, da 
	 * er mit anderen möglichen Fällen, z.B. "op. 34" kollidiert.
	 *  
	 */

	/*
	 * Dummerweise kommen einige Fälle, die das heutige Regelwerk nicht 
	 * abdeckt, recht häufig vor: ähnlich wie bei c) steht nach dem ". " eine
	 * Jahreszahl oder eine Besetzung, allerdings ohne die einleitende 
	 * Phrase. Daher
	 * 
	 * 	Fall e) 
	 * 
	 * Untergruppe
	 *  1: Jahr
	 * 	2: Besetzung
	 */

	/*
	 * Manchmal ist die Fassungsphrase aus $s schon bekannt.
	 *  Auf ein genaues Parsen wird dann verzichtet.
	 *  
	 *  Fall $)
	 *  Untergruppe 1 
	 */

	String match = null;

	public final String getMatch() {
		return match;
	}

	/**
	 * 
	 */
	Version() {
	}

	/**
	 * Wird für den Fall aufgerufen, dass die Fassung schon aus anderer Quelle
	 * bekannt ist.
	 * 
	 * @param versionStr unstrukturierter String, der die Fassung enthält.
	 */
	public Version(final String versionStr) {
		if (versionStr == null)
			this.match = "";
		else
			this.match = versionStr;
		fallgruppeParagraphM511 = '$';
		untergruppe = 1;
	}

	public final char getFallgruppeParagraphM511() {
		return fallgruppeParagraphM511;
	}

	public final int getUntergruppe() {
		return untergruppe;
	}


	public final InstrumentationList getInstrumentationList() {
		return instrumentationList;
	}

	public final GenreList getGenreList() {
		return genreList;
	}

	/**
	 * Zur Zeit nicht benötigt.
	 * 
	 * @return
	 */
	public final String getRakPhrase() {
		return rakPhrase;
	}

	public final String getRest() {
		return rest;
	}

	public final AdditionalInformation getAdditionalInformation() {
		return additionalInformation;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Version fas = new Version();
		fas.fallgruppeParagraphM511 = 'c';
		fas.untergruppe = 2;

		//		Zusatzangabe zus1 = ParseZusatzangabe.parse("", "op. 3");
		AdditionalInformation zus =
			ParseAdditionalInformation.parse("", "1996", false);
		//		Zusatzangabe zus2 = ParseZusatzangabe.parse("", "a-Moll");
		InstrumentationList bes = ParseInstrumentation.parse(("Vl 1 2"));
		GenreList gat = ParseGenre.parseGenreList("Quartette");

		fas.genreList = gat;
		fas.instrumentationList = bes;
		fas.rakPhrase = "Alternativfassung";
		fas.additionalInformation = zus;

	}

	@Override
	public final void accept(final Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren) {
			if (containsGenre())
				genreList.accept(visitor);
			if (containsInstrumentation())
				instrumentationList.accept(visitor);
			if (containsAdditionalInformation())
				additionalInformation.accept(visitor);
		}
		visitor.leave(this);
	}

	private boolean containsGenre() {
		return genreList != null;
	}

	private boolean containsInstrumentation() {
		return instrumentationList != null;
	}

	private boolean containsAdditionalInformation() {
		return additionalInformation != null;
	}

}
