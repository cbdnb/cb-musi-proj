package de.dnb.music.visitor.setsOfRules;

import de.dnb.music.genre.Genre;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public class RSWKSubfieldFactory extends GNDParticleFactory {

	public RSWKSubfieldFactory() {
		super();

		preferredNumerus = Genre.Numeri.SINGULAR;

		abbreviationMode = Instrument.AbbreviationMode.WRITTEN_OUT;

		btwNumber = ",";
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parse(null,
					"Stücke, Klavier$g1955-1956");
		WorkTitleVisitor vis = new WorkTitleVisitor(new RSWKSubfieldFactory());
		mt.accept(vis);
		System.out.println(vis.toString());

	}

}
