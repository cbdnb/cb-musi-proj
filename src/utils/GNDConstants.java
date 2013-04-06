package utils;

import de.dnb.gnd.parser.Indicator;
import de.dnb.gnd.parser.Tag;
import de.dnb.gnd.parser.TagDB;

public class GNDConstants {
	
public static final Tag TAG_130 = TagDB.findTag("130");
	
	public static final Tag TAG_430 = TagDB.findTag("430");
	
	public static final Tag TAG_530 = TagDB.findTag("530");
	
	public static final Tag TAG_730 = TagDB.findTag("730");

	public static final Indicator DOLLAR_A = TAG_130.getIndicator('a', false);

	public static final Indicator DOLLAR_F = TAG_130.getIndicator('f', false);

	public static final Indicator DOLLAR_N = TAG_130.getIndicator('n', false);

	public static final Indicator DOLLAR_O = TAG_130.getIndicator('o', false);

	public static final Indicator DOLLAR_P = TAG_130.getIndicator('p', false);

	public static final Indicator DOLLAR_R = TAG_130.getIndicator('r', false);

	public static final Indicator DOLLAR_S = TAG_130.getIndicator('s', false);

	public static final Indicator DOLLAR_G = TAG_130.getIndicator('g', false);


}
