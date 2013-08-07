package de.dnb.music.additionalInformation;

import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public abstract class AdditionalInformation implements TitleElement{
	
	@Override
	public void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		
	}
	
}
