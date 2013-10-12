package utils;

import java.io.IOException;

import javax.naming.OperationNotSupportedException;

import de.dnb.gnd.exceptions.IllFormattedLineException;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.gnd.utils.GNDUtils.GNDNumberFinder;
import de.dnb.music.genre.GenreDB;
import de.dnb.music.mediumOfPerformance.InstrumentDB;

public class Field3XXFinder extends GNDNumberFinder {
	@Override
	public String find(String idn) {
		String gndID = null;
		gndID = InstrumentDB.getGNDIdn(idn);
		if (gndID != null)
			return gndID;
		gndID = GenreDB.getGNDIdn(idn);
		if (gndID != null)
			return gndID;
		return super.find(idn);
	}
	
	public static void main(String[] args)
			throws IllFormattedLineException,
			OperationNotSupportedException,
			IOException {

			Record record = GNDUtils.readFromConsole();
			System.out.println(GNDUtils.toAleph(record, new Field3XXFinder()));
		}
}
