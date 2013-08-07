package de.dnb.music.genre;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import utils.TitleUtils;

import applikationsbausteine.ListUtils;
import applikationsbausteine.RangeCheckUtils;

import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
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
	private final LinkedList<Genre> genres; // = new LinkedList<Gattung>();

	public GenreList(final Genre g) {
		genres = new LinkedList<Genre>();
		genres.add(g);
	}

	public GenreList(final LinkedList<Genre> llg) {
		genres = llg;
	}

	public final void add(final Genre g) {
		if (g == null)
			return;
		genres.add(g);
	}

	public final void add(final GenreList gatt) {
		if (gatt == null)
			return;
		genres.addAll(gatt.genres);
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

	public final String toString(Genre.Numeri numerus) {
		switch (numerus) {
		case PLURAL:
			return pluralPreferred();

		case SINGULAR:
			return singularPreferred();

		default:
			throw new IllegalArgumentException("Numerus not supported");
		}
	}

	public final String pluralPreferred() {
		if (genres.size() == 0)
			throw new IllegalStateException("Gattungsliste leer");

		if (genres.size() == 1)
			return genres.get(0).asPlural();

		/*
		 *  Also sind es 2 oder mehr. Daher ist das Werk oft in einer Mischung 
		 *  aus Singular und Plural angesetzt: 
		 *  	"Lamento, Intermezzi und Marsch"
		 *  Fälschlicherweise doppelte Blanks werden entfernt und getrimmt,
		 *  da kein Individualsachtitel vorliegt.
		 */
		String match = getMatch().replaceAll(" +", " ").trim();
		// Sonderfall nach RAK:
		if (match.equals("Präludium und Fuge"))
			match = "Präludien und Fugen";

		return match;
	}

	public final String singularPreferred() {
		if (genres.size() == 0)
			throw new IllegalStateException("Gattungsliste leer");

		/*
		 * Singular kann zwar bei Sammlungen falsch sein. Das Risiko müssen
		 * wir eingehen ...
		 */
		if (genres.size() == 1)
			return genres.get(0).asSingular();

		/*
		 *  Also sind es 2 oder mehr. Daher ist das Werk oft in einer Mischung 
		 *  aus Singular und Plural angesetzt: 
		 *  	"Lamento, Intermezzi und Marsch"
		 *  Fälschlicherweise doppelte Blanks werden entfernt und getrimmt,
		 *  da kein Individualsachtitel vorliegt.
		 */
		String match = getMatch().replaceAll(" +", " ").trim();
		/* 
		 * Sonderfall nach RAK, kann aber in RSWK schrecklich schief laufen,
		 * wenn eine Sammlung vorliegt: 
		 * 800 |p|Mendelssohn Bartholdy, Felix
		 * 801 |t|Präludien und Fugen, Klavier op. 35
		 */
		if (match.equals("Präludien und Fugen"))
			match = "Präludium und Fuge";

		return match;
	}

	public final String getMatch() {
		String s = "";
		for (Genre gattung : genres) {
			s += gattung.match;
		}
		return s;
	}

	public final String getRest() {
		return genres.get(genres.size() - 1).rest;
	}

	public final List<Genre> getGenres() {
		return Collections.unmodifiableList(genres);
	}

	@Override
	public void accept(Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren)
			for (Genre genre : genres) {
				genre.accept(visitor);
			}
		visitor.leave(this);
	}

	@Override
	public void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		for (Genre genre : genres) {
			genre.addToTitle(title);
		}
	}

	public Genre getLast() {
		if (genres.isEmpty())
			throw new IllegalStateException("Liste der Gattungen ist leer");
		return ListUtils.getLast(genres);
	}
	
	public static void main(final String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parseFullRAK(null, "aa");
		GenreList genreList = ParseGenre.parseGenreList("Adagio und Fuge");
		genreList.addToTitle(mt);
		System.out.println(TitleUtils.getStructured(mt));
	}

}
