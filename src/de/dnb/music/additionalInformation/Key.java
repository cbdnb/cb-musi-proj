package de.dnb.music.additionalInformation;

import de.dnb.music.visitor.Visitor;

public class Key extends AdditionalInformation {

	@Override
	public final void accept(final Visitor visitor) {
		visitor.visit(this);
	}

	private String tonBuchstabe = null; // String wg. "cis"

	private String tonGeschlecht = null;

	private String ton = null; // in der Regel == "1" ...

	private String tonRest = null; // in der Regel == ". Ton"

	private static boolean tonBuchstabenErkennen = false;

	@Override
	public final String toString() {

		// Fallunterscheidungen:-----------

		// einfachster Fall zuerst
		if (ton != null) {
			if (tonRest.equals(". Ton"))
				return ton + tonRest;
		}

		// Nur Tonbuchstabe, kommt das vor?
		if (tonBuchstabe != null && tonGeschlecht == null)
			return tonBuchstabe;

		// nur Tongeschlecht
		if (tonBuchstabe == null && tonGeschlecht != null) {
			if (istTongeschlechtAlt(tonGeschlecht))
				return tonGeschlecht;
		}

		//beides

		return tonBuchstabe + "-" + tonGeschlecht;

	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		tonBuchstabenErkennen = true;
		Key t = new Key("a");
		System.out.println(t);
		System.out.println(t.tonBuchstabe);
		System.out.println(t.tonGeschlecht);
	}

	private static String[] tonBuchstaben = { "C", "D", "E", "F", "G", "A",
		"H",

		"Ceses", "Ces", "Deses", "Des", "Eses", "Es", "Fes", "Feses", "Ges",
		"Asas", "As", "Heses", "B",

		"Cis", "Cisis", "Dis", "Disis", "Eis", "Fis", "Fisis", "Gis", "Gisis",
		"Ais", "Aisis", "His"

	};

	//	private static String tonGeschlechterNeu[] = { "Dur", "Moll" };

	private static String[] tonGeschlechterAltDur = { "Phrygisch",
		"Hypophrygisch", "Lydisch", "Hypolydisch", "Mixolydisch",
		"Hypomixolydisch", "Ionisch", "Hypoionisch" };

	private static String[] tonGeschlechterAltMoll = { "Dorisch",
		"Hypodorisch", "äolisch", "Hypoäolisch" };

	public static boolean istTongeschlechtAlt(final String geschlecht) {
		if (geschlecht == null)
			return false;
		for (String g : tonGeschlechterAltMoll) {
			if (geschlecht.equals(g))
				return true;
		}
		for (String g : tonGeschlechterAltDur) {
			if (geschlecht.equals(g))
				return true;
		}
		return false;
	}

	public static boolean istDurig(final String geschlecht) {
		if (geschlecht == null)
			return false;
		if (geschlecht.equals("Dur"))
			return true;
		for (String g : tonGeschlechterAltDur) {
			if (geschlecht.equals(g))
				return true;
		}

		return false;
	}

	public static boolean istMollig(final String geschlecht) {
		if (geschlecht == null)
			return false;
		if (geschlecht.equals("Moll"))
			return true;
		for (String g : tonGeschlechterAltMoll) {
			if (geschlecht.equals(g))
				return true;
		}
		return false;
	}

	public static boolean istTonbuchstabe(final String b) {
		if (b == null)
			return false;
		for (String buchstabe : tonBuchstaben) {
			if (buchstabe.equalsIgnoreCase(b))
				return true;
		}
		return false;
	}

	private static IllegalArgumentException keyException =
		new IllegalArgumentException("Tonart falsch");

	/*
	 *  Der Konstruktor nimmt an, dass kein Rest mehr folgt, die Tonart also
	 *  das letzte Glied in der Kette ist. 
	 */
	public Key(final String s) {

		if (s == null || s.length() == 0)
			throw keyException;
		if (tonBuchstabenErkennen)
			if (istTonbuchstabe(s)) {
				tonBuchstabe = s;
				return;
			}
		if (istTongeschlechtAlt(s)) {
			tonGeschlecht = s;
			return;
		}

		//@formatter:off
		// "12. Ton" erkennen
		int stelle = 0;
		if (Character.isDigit(s.charAt(stelle))) {
			for (; stelle < s.length() && Character.isDigit(s.charAt(stelle)); 
					stelle++);
			ton = s.substring(0, stelle);
			tonRest = s.substring(stelle, s.length());
			if (!tonRest.equals(". Ton"))
				throw keyException;
			return;
		}
		//@formatter:on
		// Also ist es eine Art "C-Dur"
		stelle = s.indexOf('-');
		if (stelle == -1 || stelle == 0 || stelle == s.length() - 1)
			throw keyException;
		// beide Strings existieren und sind >0
		tonBuchstabe = s.substring(0, stelle);
		tonGeschlecht = s.substring(stelle + 1);
		if (istDurig(tonGeschlecht))
			if (Character.isUpperCase(tonBuchstabe.charAt(0))) {
				if (istTonbuchstabe(tonBuchstabe)) {
					return;
				}
			}

		if (istMollig(tonGeschlecht))
			if (Character.isLowerCase(tonBuchstabe.charAt(0))) {
				if (istTonbuchstabe(tonBuchstabe)) {
					return;
				}
			}
		throw keyException;
	} // Konstruktor

	public static void setRegnognizeKeyName(final boolean erkennen) {
		Key.tonBuchstabenErkennen = erkennen;
	}

	public final String getTon() {
		return ton;
	}

	public final String getTonBuchstabe() {
		return tonBuchstabe;
	}

	public final String getTonGeschlecht() {
		return tonGeschlecht;
	}

}
