package de.dnb.music.utils;

import de.dnb.gnd.parser.Indicator;
import de.dnb.gnd.parser.tag.GNDTagDB;
import de.dnb.gnd.parser.tag.Tag;
import de.dnb.gnd.parser.tag.TagDB;

public class GNDConstants {

	public static final TagDB TAG_DB = GNDTagDB.getDB();

	public static final Tag TAG_130 = TAG_DB.findTag("130");

	public static final Tag TAG_430 = TAG_DB.findTag("430");

	public static final Tag TAG_530 = TAG_DB.findTag("530");

	public static final Tag TAG_730 = TAG_DB.findTag("730");

	public static final Indicator DOLLAR_a = TAG_130.getIndicator('a');

	public static final Indicator DOLLAR_f = TAG_130.getIndicator('f');

	public static final Indicator DOLLAR_m = TAG_130.getIndicator('m');

	public static final Indicator DOLLAR_n = TAG_130.getIndicator('n');

	public static final Indicator DOLLAR_o = TAG_130.getIndicator('o');

	public static final Indicator DOLLAR_p = TAG_130.getIndicator('p');

	public static final Indicator DOLLAR_r = TAG_130.getIndicator('r');

	public static final Indicator DOLLAR_s = TAG_130.getIndicator('s');

}
