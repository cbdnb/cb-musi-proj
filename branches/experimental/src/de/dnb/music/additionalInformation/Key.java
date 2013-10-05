package de.dnb.music.additionalInformation;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import utils.TitleUtils;

import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
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
		
		MusicTitle title = ParseMusicTitle.parseGND(null, "aa$rC-Dur$pbb");
		System.out.println(TitleUtils.getStructured(title));
		Key key = new Key("D-Dur");
		System.out.println(TitleUtils.getStructured(key));
		key.addToTitle(title);
		System.out.println(TitleUtils.getStructured(title));
		
		title = ParseMusicTitle.parseGND(null, "aa$rC-Dur$pbb$rC-Dur");
		System.out.println(TitleUtils.getStructured(title));
	}

	private static String[] tonBuchstaben = { "C", "Cis", "Des", "D", "Dis",
		"E", "Es", "F", "Fis", "Ges", "G", "Gis", "As", "A", "B", "H",

		"Ceses", "Ces", "Deses", "Eses", "Fes", "Feses", "Asas", "Heses",

		"Cisis", "Disis", "Eis", "Fisis", "Gisis", "Ais", "Aisis", "His"

	};

	public static Collection<String> getKeyNames() {
		return Arrays.asList(tonBuchstaben);
	}

	public static Collection<String> getModeNames() {
		List<String> list = new LinkedList<String>();
		list.add("Dur");
		list.add("Moll");
		list.addAll(Arrays.asList(tonGeschlechterAltMoll));
		list.addAll(Arrays.asList(tonGeschlechterAltDur));

		return list;
	}

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
