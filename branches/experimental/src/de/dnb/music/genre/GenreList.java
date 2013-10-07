package de.dnb.music.genre;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

import utils.TitleUtils;
import de.dnb.music.genre.Genre.Numeri;
import de.dnb.music.title.ListOfElements;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

/**
 * Enthält in 
 * 		LinkedList<Gattung> children 
 * alle zum Werktitel gehörenden  Gattungen. Im Falle eines Titels wie 
 * "Toccata, Adagio und Fuge" wären das
 * [Toccata, Adagio, Fuge].
 * 
 * @author baumann
 *
 */
public class GenreList extends ListOfElements<Genre> implements TitleElement {

	public GenreList(final Genre g) {
		children.add(g);
	}

	public GenreList(final ArrayList<Genre> llg) {
		children = llg;
	}

	public final LinkedList<String> nids() {
		if (children == null)
			return null;
		LinkedList<String> lls = new LinkedList<String>();
		for (Genre g : children) {
			lls.add(g.nid);
		}
		return lls;
	}

	public final LinkedList<String> idns() {
		if (children == null)
			return null;
		LinkedList<String> lls = new LinkedList<String>();
		for (Genre g : children) {
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
		if (children.size() == 0)
			throw new IllegalStateException("Gattungsliste leer");

		if (children.size() == 1)
			return children.get(0).toString(numerus);
		/*
		 *  Also sind es 2 oder mehr. 
		 */
		String match;
		if (!isParsed()) {
			// Selber bauen, einfach alles im Plural und mit 
			// Kommas und "und" verbinden:
			int size = children.size();
			match = children.get(0).asPlural();
			for (int i = 1; i < size - 1; i++) {
				match += ", " + children.get(i).toString(numerus);
			}
			match += " und " + children.get(size - 1).toString(numerus);
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
		for (Genre genre : children) {
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
		return children.get(children.size() - 1).rest;
	}

	@Override
	public final void accept(final Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren)
			visitChildren(visitor);
		visitor.leave(this);
	}

	/**
	 * Ermittel aus den Einzelmatches, ob die Gattungsliste aus einer 
	 * textuellen Form geparst wurde. Gegenteil: synthetisiert.
	 * 
	 * @return	true, wenn geparst wurde <br>
	 * 			false, wenn aus Gattungsbegriffen synthetisiert.
	 */
	public final boolean isParsed() {
		List<Genre> theGenres = getChildren();
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

		MusicTitle mt =
			ParseMusicTitle
					.parseTitlePlusVersion(null, "Magnificat, c-Dorisch");
		System.out.println(TitleUtils.getStructured(mt));
	}
}
