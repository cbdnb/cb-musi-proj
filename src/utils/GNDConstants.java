package utils;

import de.dnb.gnd.parser.Indicator;
import de.dnb.gnd.parser.Tag;
import de.dnb.gnd.parser.TagDB;

public class GNDConstants {

	public static final Tag TAG_130 = TagDB.findTag("130");

	public static final Tag TAG_430 = TagDB.findTag("430");

	public static final Tag TAG_530 = TagDB.findTag("530");

	public static final Tag TAG_730 = TagDB.findTag("730");

	public static final Indicator DOLLAR_a = TAG_130.getIndicator('a', false);

	public static final Indicator DOLLAR_f = TAG_130.getIndicator('f', false);
	
	public static final Indicator DOLLAR_m = TAG_130.getIndicator('m', false);

	public static final Indicator DOLLAR_n = TAG_130.getIndicator('n', false);

	public static final Indicator DOLLAR_o = TAG_130.getIndicator('o', false);

	public static final Indicator DOLLAR_p = TAG_130.getIndicator('p', false);

	public static final Indicator DOLLAR_r = TAG_130.getIndicator('r', false);

	public static final Indicator DOLLAR_s = TAG_130.getIndicator('s', false);

}
