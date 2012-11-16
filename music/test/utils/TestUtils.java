package utils;

import utils.TitleUtils;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.setsOfRules.GNDParticleFactory;
import de.dnb.music.visitor.setsOfRules.WorkTitleVisitor;

public class TestUtils {
	public static String getLinkName(String s) {
		int index = s.indexOf('!', 7);
		return s.substring(index + 1);
	}

	/**
	 * Hat eine andere Funktionalitaet als 
	 * @see de.dnb.music.TitleUtils.getGND3XX(TitleElement) muss deshalb 
	 * bleiben.
	 * @param element
	 * @return
	 */
	public static String getGND3XX(TitleElement element) {
		final boolean expansion = true;
		final boolean forceTotalCount = false;
		return TitleUtils.getGND3XX(element, expansion, forceTotalCount);
	}

	

}
