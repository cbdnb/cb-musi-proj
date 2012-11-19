package de.dnb.music.visitor.setsOfRules;

import de.dnb.music.title.Comment;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.title.PartOfWork;

public class DNBPortal430Visitor extends WorkTitleVisitor {

	public DNBPortal430Visitor() {
		super(
				new GNDParticleFactory());

	}

	@Override
	public final boolean visit(final PartOfWork partOfWork) {
		lastComponent += "$g";
		factory = new RAKParticleFactory();
		// vor FIRST_PART setzen:
		state = States.TITLE;
		return true;
	}

	@Override
	public final void leave(final PartOfWork partOfWork) {
		factory = new GNDParticleFactory();
	}
	
	@Override
	public void visit(final Comment comment) {
		// tue nichts.
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parse(null,
					"Quartette op. 5 <Quartett op. 5, aa>. Fassung");
		DNBPortal430Visitor vis = new DNBPortal430Visitor();
		mt.accept(vis);

		System.out.println(vis);

	}

}
