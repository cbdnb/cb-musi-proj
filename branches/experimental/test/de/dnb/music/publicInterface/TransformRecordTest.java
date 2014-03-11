package de.dnb.music.publicInterface;

import static org.junit.Assert.*;
import static de.dnb.music.publicInterface.Constants.*;

import org.junit.Before;
import org.junit.Test;

import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.RecordParser;
import de.dnb.gnd.parser.tag.GNDTagDB;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.music.utils.StringUtils;

public class TransformRecordTest {

	/**
	 * Hilfsmethode, um einen String schnell in einen String zu transformieren.
	 * 
	 * @param old
	 * @return
	 */
	public String transformIntellect(String old) {
		DefaultRecordTransformer transformer = new DefaultRecordTransformer();
		RecordParser parser = new RecordParser();
		parser.setDefaultTagDB(GNDTagDB.getDB());
		Record record = parser.parse(old);
		Record newR = transformer.transform(record);
		return RecordUtils.toPica(newR);
	}

	/**
	 * Hilfsmethode, um einen String schnell in einen String zu transformieren.
	 * 
	 * @param old
	 * @return
	 */
	public String transformMachine(String old) {
		DefaultRecordTransformer transformer = new DefaultMachineTransformer();
		RecordParser parser = new RecordParser();
		parser.setDefaultTagDB(GNDTagDB.getDB());
		Record record = parser.parse(old);
		Record newR = transformer.transform(record);
		return RecordUtils.toPica(newR);
	}

	private String titleStrOld;
	private String recordStrNew;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testTransform130KomAltNeu() {
		// RAK 130 intellektuell alt
		titleStrOld =
			"130 Quartette Vl 1 2. Kl 4hdg$vR:Umsetzung GND aus RAK-M vor 2003"
				+ "\n913 $Sest$ipt$a Quartette Vl 1 2. Kl 4hd$0";
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("130 Quartette$mVl 1 2$sKl 4hdg.\r\n"));

		// RAK 130 intellektuell neu
		titleStrOld =
			"130 Quartette, Vl 1 2 <aa>" + "$vR:Umsetzung GND aus RAK-M 2003"
				+ "\n913 $Sest$ipt$aQuartette, Vl 1 2 <aa>$0";
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("130 Quartette$mVl 1 2$paa"));
		assertTrue(recordStrNew.contains("430 Quartette, Vl 1 2 <aa>" + "$v"
			+ KOM_PORTAL_430));

		assertFalse(recordStrNew
				.contains("430 Quartette, Vl 1 2. Fassung Kl 4hdg" + "$v"
					+ KOM_PORTAL_430));

		// RAK 130 maschinell alt
		titleStrOld =
			"130 Quartette Vl 1 2. Kl 4hdg$vR:Umsetzung GND aus RAK-M vor 2003"
				+ "\n913 $Sest$ipt$aQuartette Vl 1 2. Kl 4hdg$0";
		recordStrNew = transformMachine(titleStrOld);
		assertTrue(recordStrNew.contains("130 Quartette Vl 1 2$sKl 4hdg$v"
			+ KOM_MASCHINELL_VOR_2003));

		// 130 maschinell RSWK 
		titleStrOld =
			"130 Quartett, Violine 1,2"
				+ "\n913 $Sswd$ipt$aQuartett, Violine 1,2$0";
		recordStrNew = transformMachine(titleStrOld);
		assertTrue(recordStrNew.contains("130 Quartette$mVl 1 2$v"
			+ KOM_MASCHINELL));

		// 130 maschinell RAK neu
		titleStrOld =
			"130 Quartette, Vl 1 2 <aa>" + "$vR:Umsetzung GND aus RAK-M 2003"
				+ "\n913 $Sest$ipt$aQuartette, Vl 1 2 <aa>$0";
		recordStrNew = transformMachine(titleStrOld);
		assertTrue(recordStrNew.contains("130 Quartette$mVl 1 2$paa$v"
			+ KOM_MASCHINELL_NACH_2003));
		assertTrue(recordStrNew.contains("430 Quartette, Vl 1 2 <aa>" + "$v"
			+ KOM_PORTAL_430));

	}

	/**
	 * Werkverzeichnisse.
	 */
	@Test
	public final void testTHI() {
		// + INTELLECT + Komponist
		titleStrOld =
			"130 aa\n" + "500 !11862119X!Telemann, Georg Philipp$4kom1";
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("670 TWV"));

		// + INTELLECT - Komponist
		titleStrOld =
			"130 aa\n" + "500 !11862119!Telemann, Georg Philipp$4kom1";
		recordStrNew = transformIntellect(titleStrOld);
		assertFalse(recordStrNew.contains("670 "));

		// - INTELLECT + Komponist
		titleStrOld =
			"130 aa\n" + "500 !11862119X!Telemann, Georg Philipp$4kom1";
		recordStrNew = transformMachine(titleStrOld);
		assertFalse(recordStrNew.contains("670 TWV"));

		// - INTELLECT - Komponist
		titleStrOld =
			"130 aa\n" + "500 !11862119!Telemann, Georg Philipp$4kom1";
		recordStrNew = transformIntellect(titleStrOld);
		assertFalse(recordStrNew.contains("670 "));
	}

	/**
	 * Ländercode.
	 */
	@Test
	public final void testCC() {
		// + INTELLECT + Komponist
		titleStrOld =
			"130 aa\n" + "500 !11862119X!Telemann, Georg Philipp$4kom1";
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("043 XA-DE"));

		// + INTELLECT - Komponist
		titleStrOld =
			"130 aa\n" + "500 !11862119!Telemann, Georg Philipp$4kom1";
		recordStrNew = transformIntellect(titleStrOld);
		assertFalse(recordStrNew.contains("043 XA-DE"));

		// - INTELLECT + Komponist
		titleStrOld =
			"130 aa\n" + "500 !11862119X!Telemann, Georg Philipp$4kom1";
		recordStrNew = transformMachine(titleStrOld);
		assertFalse(recordStrNew.contains("043 XA-DE"));

		// - INTELLECT - Komponist
		titleStrOld =
			"130 aa\n" + "500 !11862119!Telemann, Georg Philipp$4kom1";
		recordStrNew = transformMachine(titleStrOld);
		assertFalse(recordStrNew.contains("043 XA-DE"));
	}

	@Test
	public final void testTransform430() {
		// INTELLEKTUELL ----------------
		// 430 alt 
		titleStrOld =
			"130 aa\n" + "430 Quartette Vl 1 2. Kl 4hdg$vR:EST vor RAK-M 2003"
				+ "\n913 $Sest$ipt$aQuartette Vl 1 2. Kl 4hd$0";
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("430 Quartette Vl 1 2$sKl 4hdg"
			+ "$vR:EST vor RAK-M 2003"));

		// 430 neu 
		titleStrOld =
			"130 aa\n" + "430 Quartette Vl 1 2. Fassung Kl 4hdg"
				+ "\n913 $Sest$ipt$a130 Quartette$mVl 1 2$sKl 4hd$0";
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew
				.contains("430 Quartette$mVl 1 2$sFassung Kl 4hdg"));

		// 430 RAK neu (irgendein Kommentar)
		titleStrOld =
			"130 aa\n" + "430 Quartette Vl 1 2. Kl 4hdg$v11"
				+ "\n913 $Sest$ipt$a";
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("430 Quartette$mVl 1 2$sKl 4hdg.$v11"));

		// 430 RSWK (irgendein Kommentar)
		titleStrOld =
			"130 aa\n" + "430 Quartett, Violine 1,2$v11"
				+ "\n913 $Sswd$ipt$a130 Quartette$mVl 1 2$sKl 4hd$0";
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("430 Quartett$mVioline 1,2$v11"));

		// MASCHINELL -----------------------
		// 430 alt 
		titleStrOld =
			"130 aa\n" + "430 Quartette Vl 1 2. Kl 4hdg$vR:EST vor RAK-M 2003"
				+ "\n913 $Sest$ipt$a130 Quartette$mVl 1 2$sKl 4hd$0";
		recordStrNew = transformMachine(titleStrOld);
		assertTrue(recordStrNew.contains("430 Quartette Vl 1 2$sKl 4hdg$v"
			+ KOM_VOR_2003_430));

		// 430 RAK, kein Komm 
		titleStrOld =
			"130 aa\n" + "430 Quartette Vl 1 2. Kl 4hdg" + "\n913 $Sest$ipt$a";
		recordStrNew = transformMachine(titleStrOld);
		assertTrue(recordStrNew.contains("430 Quartette$mVl 1 2$sKl 4hdg"));

		// 430 RAK, bel. Komm.
		titleStrOld =
			"130 aa\n" + "430 Quartette Vl 1 2. Kl 4hdg$v11"
				+ "\n913 $Sest$ipt$a";
		recordStrNew = transformMachine(titleStrOld);
		assertTrue(recordStrNew.contains("430 Quartette$mVl 1 2$sKl 4hdg.$v11"));

		// 430 RSWK (irgendein Kommentar)
		titleStrOld =
			"130 aa\n" + "430 Quartett, Violine 1,2$v11" + "\n913 $Sswd$ipt$a";
		recordStrNew = transformMachine(titleStrOld);
		assertTrue(recordStrNew.contains("430 Quartett$mVioline 1,2$v11"));

	}

	// Bleiben nicht behandelte Felder erhalten?
	@Test
	public final void testTransformUnhandled() {
		titleStrOld = "375 m"; // irgendwas
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains(titleStrOld));
	}

	@Test
	public final void testTransformOldRAK() {
		try {
			titleStrOld = null;
			recordStrNew = transformIntellect(titleStrOld);
			fail();
		} catch (IllegalArgumentException e) {
			//OK
		}

		// kein Kommentar!
		// // - Teil - Fassung
		titleStrOld = "130 aa" + "$v" + Constants.KOM_VOR_2003;
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains(titleStrOld));

		// in der Migration nicht erkannte Fassung
		titleStrOld = "130 aa. Vl" + "$v" + Constants.KOM_VOR_2003;
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("aa$sVl"));

		// + Teil + Fassung
		titleStrOld = "130 aa$pbb$sVl" + "$v" + Constants.KOM_VOR_2003;
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("aa$pbb$sVl"));

		// + Teil - Fassung
		titleStrOld = "130 aa$pbb" + "$v" + Constants.KOM_VOR_2003;
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("aa$pbb"));

		// - Teil + Fassung
		titleStrOld = "130 aa$sVl" + "$v" + Constants.KOM_VOR_2003;
		recordStrNew = transformIntellect(titleStrOld);
		assertTrue(recordStrNew.contains("aa$sVl"));
	}

}
