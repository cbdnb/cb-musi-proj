package de.dnb.music.additionalInformation;

import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.genre.Genre;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.title.PartOfWork;
import de.dnb.music.version.Version;
import de.dnb.music.visitor.TitleElement;

public abstract class AdditionalInformation implements TitleElement {

	@Override
	public final void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		/*
		 * Kann entweder der Fassung oder dem letzten Teil oder
		 * dem Titel selbst hinzugefügt werden.
		 */

		if (title.containsVersion()) {
			Version version = title.getVersion();
			version.setAdditionalInformation(this);
		} else if (title.containsParts()) {
			PartOfWork partOfWork = title.getPartOfWork();
			MusicTitle lastTitle = partOfWork.getLastPart();
			addToTitle(lastTitle);

		} else {
			title.setAdditionalInformation(this);
		}
	}

	public static void main(final String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parseFullRAK(null, "aa <Adagio, bb>. cc");
		AdditionalInformation aI =
			ParseAdditionalInformation.parse(null, "op. 23", true);
		aI.addToTitle(mt);
		System.out.println(TitleUtils.getStructured(mt));
	}
}
