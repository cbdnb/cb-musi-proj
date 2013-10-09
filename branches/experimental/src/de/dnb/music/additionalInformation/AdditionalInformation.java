package de.dnb.music.additionalInformation;

import java.io.Serializable;

import utils.TitleUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.title.AugmentableElement;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.visitor.TitleElement;

public abstract class AdditionalInformation implements TitleElement, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1494467999400306326L;

	@Override
	public void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		AugmentableElement element = title.getActualAugmentable();
		element.addAdditionalInformation(this);
	}

	public static void main(final String[] args) {
		MusicTitle mt = ParseMusicTitle.parseGND(null, "aa, Va");
		AdditionalInformation aI =
			ParseAdditionalInformation.parse(null, "op. 23", true);
		aI.addToTitle(mt);
		System.out.println(TitleUtils.getStructured(mt));
	}
}
