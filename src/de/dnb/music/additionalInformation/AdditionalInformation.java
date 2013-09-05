package de.dnb.music.additionalInformation;

import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.genre.Genre;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.title.AugmentableElement;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.title.PartOfWork;
import de.dnb.music.version.Version;
import de.dnb.music.visitor.TitleElement;

public abstract class AdditionalInformation implements TitleElement {

	@Override
	public final void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		if (!title.isAdditable(this))
			throw new UnsupportedOperationException(
					"Titel enthält schon etwas anderes als "
						+ this.getClass().getSimpleName());
		if (title.getActualAugmentable().containsAdditionalInformation())
			throw new UnsupportedOperationException(
					"Titel enthält schon eine Zusatzinformation ");
		AugmentableElement element = title.getActualAugmentable();
		element.setAdditionalInformation(this);
	}

	public static void main(final String[] args) {
		MusicTitle mt = ParseMusicTitle.parseGND(null, "aa, Va");
		AdditionalInformation aI =
			ParseAdditionalInformation.parse(null, "op. 23", true);
		aI.addToTitle(mt);
		System.out.println(TitleUtils.getStructured(mt));
	}
}
