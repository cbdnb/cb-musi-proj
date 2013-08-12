package de.dnb.music.genre;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.genre.Genre.Numeri;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

/**
 * Enthält in 
 * 		LinkedList<Gattung> genres 
 * alle zum Werktitel gehörenden  Gattungen. Im Falle eines Titels wie 
 * "Toccata, Adagio und Fuge" wären das
 * [Toccata, Adagio, Fuge].
 * 
 * @author baumann
 *
 */
public class GenreList implements TitleElement {

	/**
	 * Die eigentlichen Daten. Zur genauen Struktur
	 * @see ParseGenre
	 */
	private final ArrayList<Genre> genres;

	public GenreList(final Genre g) {
		genres = new ArrayList<Genre>();
		genres.add(g);
	}

	public GenreList(final ArrayList<Genre> llg) {
		genres = llg;
	}

	public final void add(final Genre g) {
		if (g == null)
			return;
		genres.add(g);
	}

	public final void add(final GenreList otherGenres) {
		if (otherGenres == null)
			return;
		genres.addAll(otherGenres.genres);
	}

	public final LinkedList<String> nids() {
		if (genres == null)
			return null;
		LinkedList<String> lls = new LinkedList<String>();
		for (Genre g : genres) {
			lls.add(g.nid);
		}
		return lls;
	}

	public final LinkedList<String> idns() {
		if (genres == null)
			return null;
		LinkedList<String> lls = new LinkedList<String>();
		for (Genre g : genres) {
			lls.add(g.idn);
		}
		return lls;
	}

	/**
	 * Gibt eine Stringrepräsentation im geforderten Numerus.
	 * 
	 * @param numerus Singular oder PLural
	 * @return			String.	
	 */
	public final String toString(final Genre.Numeri numerus) {
		if (genres.size() == 0)
			throw new IllegalStateException("Gattungsliste leer");

		if (genres.size() == 1)
			return genres.get(0).toString(numerus);
		/*
		 *  Also sind es 2 oder mehr. 
		 */
		String match;
		if (!isParsed()) {
			// Selber bauen, einfach alles im Plural und mit 
			// Kommas und "und" verbinden:
			int size = genres.size();
			match = genres.get(0).asPlural();
			for (int i = 1; i < size - 1; i++) {
				match += ", " + genres.get(i).toString(numerus);
			}
			match += " und " + genres.get(size - 1).toString(numerus);
		}

		/*
		 *  Korrekterweise ist das Werk oft in einer Mischung 
		 *  aus Singular und Plural angesetzt: 
		 *  	"Lamento, Intermezzi und Marsch". Daher ist der Match die
		 *  beste Basis, da in ihn die Kenntnis des Bearbeiters eingeht.
		 *  
		 *  Fälschlicherweise doppelte Blanks werden entfernt und getrimmt,
		 *  da kein Individualsachtitel vorliegt.
		 */
		else {
			// also geparst:
			match = getMatch().replaceAll(" +", " ").trim();
		}
		// Sonderfall nach RAK:
		if (match.equals("Präludium und Fuge") && numerus == Numeri.PLURAL)
			match = "Präludien und Fugen";
		/* 
		 * Sonderfall nach RAK, kann aber in RSWK schrecklich schief laufen,
		 * wenn eine Sammlung vorliegt: 
		 * 800 |p|Mendelssohn Bartholdy, Felix
		 * 801 |t|Präludien und Fugen, Klavier op. 35
		 */
		if (match.equals("Präludien und Fugen") && numerus == Numeri.SINGULAR)
			match = "Präludium und Fuge";

		return match;
	}

	/**
	 * Gibt die Konkatenation der matchs der Einzelgattungen, was Sinn macht,
	 * wenn geparst wurde. Das ist natürlich
	 * Unsinn, wenn synthetisiert wurde. Daher ist immer vorher die Abfrage
	 * {@link #isParsed()} zu machen
	 * 
	 * @return	Gesamtmatch.
	 */
	public final String getMatch() {
		String s = "";
		for (Genre genre : genres) {
			s += genre.match;
		}

		return s;
	}

	/**
	 * Für das Parsen des Gesamttitels, das in der Regel mit den Instrumenten
	 * wietergeht.
	 * 
	 * @return rest.
	 */
	public final String getRest() {
		return genres.get(genres.size() - 1).rest;
	}

	/**
	 * Nichtmodifizierbare Liste der Gattungen.
	 * 
	 * @return	Gattungsliste.
	 */
	public final List<Genre> getGenres() {
		return Collections.unmodifiableList(genres);
	}

	@Override
	public final void accept(Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren)
			for (Genre genre : genres) {
				genre.accept(visitor);
			}
		visitor.leave(this);
	}

	@Override
	public final void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		for (Genre genre : genres) {
			genre.addToTitle(title);
		}
	}

	/**
	 * Ermittel aus den Einzelmatches, ob die Gattungsliste aus einer 
	 * textuellen Form geparst wurde. Gegenteil: synthetisiert.
	 * 
	 * @return	true, wenn geparst wurde <br>
	 * 			false, wenn aus Gattungsbegriffen synthetisiert.
	 */
	public final boolean isParsed() {
		List<Genre> theGenres = getGenres();
		Iterator<Genre> iterator = theGenres.iterator();
		iterator.next();
		for (; iterator.hasNext();) {
			String match = iterator.next().match;
			Matcher matcher = ParseGenre.SEPARATOR_PAT.matcher(match);
			if (!matcher.find())
				return false;
		}
		return true;
	}

	public static void main(final String[] args) {

		GenreList genreList = ParseGenre.parseGenreList("Introduktion");
		Genre genre = GenreDB.matchGenre("Fuge");
		genreList.add(genre);
		System.out.println(genreList.toString(Numeri.SINGULAR));

	}
}
