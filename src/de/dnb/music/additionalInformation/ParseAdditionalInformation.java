package de.dnb.music.additionalInformation;

import utils.StringUtils;

public final class ParseAdditionalInformation {

	private ParseAdditionalInformation() {
	}

	/**
	 * 
	 * Gibt eine gültige Zusatzangabe oder null. parseString wird bis zum Ende
	 * gematcht, d.h. es gibt keinen unerkannten Rest
	 * 
	 * @param composer		null oder ein bekannter Komponist. Wird nur von
	 * 						matchThematicIndex() benutzt, das eine Ausnahme
	 * 						wirft, wenn der Komponist nicht zum WV passt.
	 * @param s				der zu parsende String
	 * @param leadingComma	setze true, wenn schon von vorneherein erkannt
	 * 						werden konnte, dass die mögliche Zustzangabe mit
	 * 						einem Komma beginnt. Das kann bei Sonstigen
	 * 						Zählungen, die nur aus einer Zahl bestehen
	 * 						wichtig werden. Dieser Parameter wird quasi 
	 * 						überschrieben, wenn in der Funktion noch ein Komma 
	 * 						erkannt	werden konnte.
	 * @return				gültige Zusatzangabe oder null.
	 */
	public static AdditionalInformation parse(
			final String composer,
			final String s,
			final boolean leadingComma) {
		if (s == null)
			throw new IllegalArgumentException("null-String als Zusatzangabe");

		boolean leadingSign = leadingComma;

		String parseString = StringUtils.stripLeadingBlanksAndCommas(s);
		if (parseString.length() == 0)
			return null;

		/*
		 * $g wird in den aufgerufenen Funktionen matchDate() und 
		 * matchSerialNumber() erkannt. Das ist deshalb so, da matchDate public
		 * ist. 
		 */
		if (StringUtils.getLeadingBlanksAndCommas().equals(", ")) {
			leadingSign = true;
		}

		/*
		 * Es wird angenommen, dass parseString voll 
		 * gematcht werden kann.
		 */
		AdditionalInformation zus = null;

		//		 op. ? 
		zus = matchOpus(parseString);
		if (zus != null) {
			return zus;
		}

		zus = matchThematicIndex(composer, parseString);
		if (zus != null) {
			return zus;
		}

		// Jahr ?
		zus = matchDate(parseString);
		if (zus != null) {
			return zus;
		}

		// sonstige Zählung? Muss nach Jahr stehen, da auch einzelne
		// Zahlen denkbar sind. matchDate() überprüft auch auf 
		// Plausibilität der Jahreszahl
		zus = matchSerialNumber(parseString, leadingSign);
		if (zus != null) {
			return zus;
		}

		// Tonart nach WV, da z.B. "D" auch WV (Schubert)
		zus = matchKey(parseString);
		if (zus != null) {
			return zus;
		}

		return null;

	}

	/*
	 * Kann erst sinnvoll abgefragt werden, wenn istZahl() aufgerufen worden
	 * ist:
	 */
	private static int szahl;

	static boolean istZahl(final String s) {
		if (s == null || s.length() == 0)
			return false;
		try {
			szahl = Integer.parseInt(s.trim());
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static OpusNumber matchOpus(final String s) {
		if (s == null)
			throw new IllegalArgumentException();
		if (s.trim().length() == 0)
			return null;

		// z.Z. op. und Op. (letzteres wg. Fassung)
		String prefix = StringUtils.findLongestPrefix(s, OpusNumber.OPERA);
		if (prefix != null) {
			OpusNumber op = new OpusNumber();
			op.op = prefix;
			// trim wg. mehrfacher Blanks (selten)
			op.zaehlung = s.substring(prefix.length()).trim();
			// ohne Inhalt absurd:
			if (op.zaehlung.length() == 0)
				return null;
			return op;
		}
		//		for (String opus : OpusNumber.OPERA) {
		//			if (s.startsWith(opus + ' ')) {
		//				op.op = opus;
		//				// trim wg. mehrfacher Blanks (selten)
		//				op.zaehlung = s.substring(opus.length()).trim();
		//				return op;
		//			}
		//		}

		return null;
	}

	public static ThematicIndexNumber matchThematicIndex(
			final String komponist,
			final String s) {
		if (s == null)
			throw new IllegalArgumentException();
		return ThematicIndexDB.matchThIdx(komponist, s);
	}

	private static Key matchKey(final String s) {
		if (s == null)
			throw new IllegalArgumentException();
		if (s.length() == 0)
			return null;
		Key t;
		try {
			t = new Key(s.trim());
		} catch (IllegalArgumentException e) {
			// der Konstruktor könnte eine Ausnahme schmeissen
			return null;
		}
		return t;
	}

	/**
	 * Matcht fortlaufende Zählungen. Die Zählung kann eingeleitet werden von
	 * 	- einer Phrase wie "Nr." ... 
	 * 	- $g 
	 * 	- ", "
	 * 
	 * @param s	Der zu parsende String.
	 * @param leadingSpaceAndComma 	true, wenn vor dem Aufruf ein einleitendes 
	 * 								Zeichen erkannt wurde. 
	 * @return	Gültiges DateOfComposition-Objekt oder null
	 */
	private static SerialNumber matchSerialNumber(
			final String s,
			final boolean leadingSpaceAndComma) {
		if (s == null)
			throw new IllegalArgumentException();
		if (s.length() == 0)
			return null;

		boolean leadingSign = leadingSpaceAndComma;

		String parseString;
		if (s.startsWith("$g")) {
			parseString = s.substring(2);
			leadingSign = true;
		} else {
			parseString = s;
		}
		String s1 = parseString.trim();
		for (String phrase : SerialNumber.PHRASES) {
			if (s1.startsWith(phrase + " ")) {
				String rest = s1.substring(phrase.length());
				if (istZahl(rest.trim())) {
					SerialNumber sz = new SerialNumber();
					// Blank am Ende, um Fälle abzufangen, in denen nur eine
					// Zahl steht (also keine Phrase)
					sz.phrase = phrase + " ";
					sz.sonstZahl = szahl;
					return sz;
				}
			}
		}

		// Einzelne Zahl mit Komma-Blank davor
		if (leadingSign && istZahl(s1.trim())) {
			SerialNumber sz = new SerialNumber();
			sz.sonstZahl = szahl;
			return sz;
		}
		return null;
	}

	private static final int MINDEST_JAHR = 1200;

	private static final int HOECHST_JAHR = 2100;

	/**
	 * Matcht Jahreszahlen. Die Jahreszahl kann 
	 * 	- von () 
	 * 	- von <> umgeben sein oder
	 * 	- mit $g eingeleitet werden.
	 * 
	 * @param parseString	Der zu parsende String.
	 * @return	Gültiges DateOfComposition-Objekt oder null
	 */
	public static DateOfComposition matchDate(final String parseString) {
		if (parseString == null)
			throw new IllegalArgumentException(
					"Null-String an matchDate() übergeben");
		if (parseString.length() == 0)
			return null;

		String s1 = parseString.trim();
		if (s1.startsWith("(") || s1.startsWith("<")) {
			if (s1.endsWith(")") || s1.endsWith(">")) {
				s1 = s1.substring(1, s1.length() - 1);
			} else {
				// seltsamer Fehler:
				return null;
			}
		} else if (s1.startsWith("$g"))
			s1 = s1.substring(2);

		if (istZahl(s1)) {
			DateOfComposition e = new DateOfComposition();
			if (szahl > MINDEST_JAHR && szahl < HOECHST_JAHR) {
				e.date = szahl;
				return e;
			} else if (szahl < MINDEST_JAHR) {
				/*
				 * Das alte Vorgehen an dieser Stelle (jetzt eine Zählung
				 * anzunehmen) war kontraproduktiv, da Werktitel wie
				 * 
				 * 		Psalm 8 			-> Psalm$n8
				 * und	Cathedral, 1 - 3	-> Cathedral, 1 - $n3
				 * 
				 * umgesetzt wurden, was als Unfug anzusehen ist. 
				 */
				// Aus obigem Grunde abgeschaltet:
				// doch eine Zählung
				//				z.sonstZahl=szahl;
				//				z.sonstZählung="";
				//				return z;
			}
		}
		return null;
	}

	public static void main(final String[] args) {
		AdditionalInformation add =
			ParseAdditionalInformation.matchDate("$g1234");
		System.out.println(add);
	}

}
