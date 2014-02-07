package de.dnb.music.mediumOfPerformance;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.dnb.music.utils.StringUtils;

public final class ParseInstrumentation {

	private ParseInstrumentation() {
	}

	/**
	 *  Zahlenreihe am Anfang: 1,2,3,4 oder 1 2 3 oder ,1,2,3 .
	 */
	private static Pattern pNumberSequence = Pattern
			.compile("^[\\s,]*((\\d+[\\s,]*)+)");

	//            	  ^[\s,]*((\d+[\s,]*)+)
	// 				  ^=Anfang, \s = Whitespace, \d = Digit

	/**
	 *  einzelne Zahlen \d+.
	 */
	private static Pattern pSingelNumber = Pattern.compile("\\d+");

	/**
	 *  Akzeptiert führende Blanks und Kommas. Im letzten der erkannten 
	 *  Instrumente findet sich der nicht erkannte Rest. 
	 *
	 * @param s				der zu parsende String.
	 * @return				eine gültige Besetzung oder null
	 */
	public static InstrumentationList parse(final String s) {
		if (s == null)
			throw new IllegalArgumentException(
					"Null-String an parse()übergeben");

		String parseString = s;

		Instrument i = parseSingleInstrument(parseString);
		if (i == null)
			return null;

		LinkedList<Instrument> besetzung = new LinkedList<Instrument>();

		while (i != null) {
			besetzung.add(i);
			i = parseSingleInstrument(i.rest);
		}

		return new InstrumentationList(besetzung);

	}

	/**
	 *  Akzeptiert führende Blanks und Kommas. 
	 *
	 * @param s				der zu parsende String.
	 * @return				eine gültiges Instrument oder null
	 */
	public static Instrument parseSingleInstrument(final String s) {
		if (s == null)
			throw new IllegalArgumentException(
					"Null-String an parseSingleInstrument()übergeben");

		// führende Blanks und Kommas
		String parseString = StringUtils.stripLeadingBlanksAndCommas(s);
		if (parseString.length() == 0)
			return null;

		Instrument i = InstrumentDB.matchInstrument(parseString);
		if (i == null)
			//Nichts erkannt
			return null;

		// Zahlen suchen
		Matcher mNumberSequence = pNumberSequence.matcher(i.rest);

		if (!mNumberSequence.find())
			return i;

		// Also Zahlenreihe am Anfang von i.rest entdeckt:
		String numberSequence = mNumberSequence.group();

		// Zahlenreihe analysieren
		int lenghtOfNumberSequence = 0; // Position in String
		Matcher mNumber = pSingelNumber.matcher(numberSequence);
		int numberExpected = 1;
		int numberFound = 0;
		while (mNumber.find()) {
			String sNumberFound = mNumber.group();
			numberFound = Integer.parseInt(sNumberFound);
			if (numberFound == numberExpected) { // aufsteigende Folge
				numberExpected++;
				lenghtOfNumberSequence = mNumber.end();
			} else
				break;
		}
		// korrigieren:
		numberFound = numberExpected - 1;

		/*	
		 * Jetzt noch den Fall behandeln, dass Vl 1 (ohne folgende 2). 
		 * Dann gehört die 1 schon zu einer nachfolgenden Zählung(?).
		 * Oder, dass gar keine 1 am Anfang gefunden wurde:
		 */
		if (numberFound <= 1)
			return i;

		// jetzt aufräumen:
		i.rest = i.rest.substring(lenghtOfNumberSequence);
		i.count = numberFound;

		return i;
	}

	public static void main(final String[] args) {
		Instrument i = parseSingleInstrument("Vla");
		System.out.println(i);
	}

}
