package de.dnb.music.publicInterface;

import static org.junit.Assert.*;
import static de.dnb.music.publicInterface.Constants.*;

import org.junit.Before;
import org.junit.Test;

import utils.StringUtils;

public class TransformRecordTest {

	private String titleStrOld;
	private String newRecord;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testTransform130KomAltNeu() {
		// RAK 130 intellektuell alt
		titleStrOld =
			"130 Quartette Vl 1 2. Kl 4hdg$vR:Umsetzung GND aus RAK-M vor 2003"
				+ "\n913 $Sest$ipt$a Quartette Vl 1 2. Kl 4hd$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord.contains("130 Quartette$mVl 1 2$sKl 4hdg\n"));

		// RAK 130 intellektuell neu
		titleStrOld =
			"130 Quartette, Vl 1 2 <aa>" + "$vR:Umsetzung GND aus RAK-M 2003"
				+ "\n913 $Sest$ipt$aQuartette, Vl 1 2 <aa>$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord.contains("130 Quartette$mVl 1 2$paa"));
		assertTrue(newRecord.contains("430 Quartette, Vl 1 2 <aa>" + "$v"
			+ KOM_PORTAL_430));

		titleStrOld =
			"130 Quartette, Vl 1 2. Fassung Kl 4hdg"
				+ "$vR:Umsetzung GND aus RAK-M 2003"
				+ "\n913 $Sest$it$aQuartette, Vl 1 2. Fassung Kl 4hdg$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord
				.contains("130 Quartette$mVl 1 2$sFassung Kl 4hdg\n"));

		assertFalse(newRecord.contains("430 Quartette, Vl 1 2. Fassung Kl 4hdg"
			+ "$v" + KOM_PORTAL_430));

		// RAK 130 maschinell alt
		titleStrOld =
			"130 Quartette Vl 1 2. Kl 4hdg$vR:Umsetzung GND aus RAK-M vor 2003"
				+ "\n913 $Sest$ipt$aQuartette Vl 1 2. Kl 4hdg$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord.contains("130 Quartette Vl 1 2$sKl 4hdg$v"
			+ KOM_MASCHINELL_VOR_2003));

		// 130 maschinell RSWK 
		titleStrOld =
			"130 Quartett, Violine 1,2"
				+ "\n913 $Sswd$ipt$aQuartett, Violine 1,2$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord.contains("130 Quartette$mVl 1 2$v"
			+ KOM_MASCHINELL));
		//		assertTrue(newRecord.contains("430 Quartette Vl 1 2. Kl 4hdg$4nswd"));

		// 130 intellektuell RSWK 
		titleStrOld =
			"130 Quartett, Violine 1,2"
				+ "\n913 $Sest$ipt$aQuartett, Violine 1,2$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord.contains("130 Quartett, Violine 1,2\n"));
		//		assertTrue(newRecord.contains("430 Quartette Vl 1 2. Kl 4hdg$4nswd"));

		// 130 maschinell RAK neu
		titleStrOld =
			"130 Quartette, Vl 1 2 <aa>" + "$vR:Umsetzung GND aus RAK-M 2003"
				+ "\n913 $Sest$ipt$aQuartette, Vl 1 2 <aa>$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord.contains("130 Quartette$mVl 1 2$paa$v"
			+ KOM_MASCHINELL_NACH_2003));
		assertTrue(newRecord.contains("430 Quartette, Vl 1 2 <aa>" + "$v"
			+ KOM_PORTAL_430));

		titleStrOld =
			"130 Quartette, Vl 1 2. Fassung Kl 4hdg"
				+ "$vR:Umsetzung GND aus RAK-M 2003"
				+ "\n913 $Sest$ipt$aQuartette, Vl 1 2. Fassung Kl 4hd$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord
				.contains("130 Quartette$mVl 1 2$sFassung Kl 4hdg$v"
					+ KOM_MASCHINELL_NACH_2003));
		assertFalse(newRecord.contains("430 Quartette, Vl 1 2. Fassung Kl 4hdg"
			+ "$v" + KOM_PORTAL_430));

		// Pica+, Script, 130
		titleStrOld =
			"022A $aQuartette, Vl 1 2$vR:Umsetzung GND aus RAK-M 2003"
				+ "\n913 $Sest$ipt$aQuartette, Vl 1 2$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		newRecord = StringUtils.gnd2Pica(newRecord);
		assertTrue(newRecord.contains("022A $aQuartette$mVl 1 2$v"
			+ KOM_MASCHINELL_NACH_2003));

	}

	/**
	 * Werkverzeichnisse.
	 */
	@Test
	public final void testTHI() {
		// + INTELLECT + Komponist
		titleStrOld = "500 !11862119X!Telemann, Georg Philipp$4kom1";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord.contains("670 TWV"));

		// + INTELLECT - Komponist
		titleStrOld = "500 !11862119!Telemann, Georg Philipp$4kom1";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertFalse(newRecord.contains("670 "));

		// - INTELLECT + Komponist
		titleStrOld = "500 !11862119X!Telemann, Georg Philipp$4kom1";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertFalse(newRecord.contains("670 TWV"));

		// - INTELLECT - Komponist
		titleStrOld = "500 !11862119!Telemann, Georg Philipp$4kom1";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertFalse(newRecord.contains("670 "));
	}

	/**
	 * Ländercode.
	 */
	@Test
	public final void testCC() {
		// + INTELLECT + Komponist
		titleStrOld = "500 !11862119X!Telemann, Georg Philipp$4kom1";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord.contains("043 XA-DE"));

		// + INTELLECT - Komponist
		titleStrOld = "500 !11862119!Telemann, Georg Philipp$4kom1";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertFalse(newRecord.contains("043 XA-DE"));

		// - INTELLECT + Komponist
		titleStrOld = "500 !11862119X!Telemann, Georg Philipp$4kom1";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord.contains("043 XA-DE"));

		// - INTELLECT - Komponist
		titleStrOld = "500 !11862119!Telemann, Georg Philipp$4kom1";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertFalse(newRecord.contains("043 XA-DE"));
	}

	@Test
	public final void testTransform430() {
		// INTELLEKTUELL ----------------
		// 430 alt 
		titleStrOld =
			"430 Quartette Vl 1 2. Kl 4hdg$vR:EST vor RAK-M 2003"
				+ "\n913 $Sest$ipt$aQuartette Vl 1 2. Kl 4hd$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord.contains("430 Quartette Vl 1 2$sKl 4hdg"
			+ "$vR:EST vor RAK-M 2003"));

		// 430 neu 
		titleStrOld =
			"430 Quartette Vl 1 2. Fassung Kl 4hdg"
				+ "\n913 $Sest$ipt$a130 Quartette$mVl 1 2$sKl 4hd$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord.contains("430 Quartette$mVl 1 2$sFassung Kl 4hdg"));

		// 430 RAK neu (irgendein Kommentar)
		titleStrOld = "430 Quartette Vl 1 2. Kl 4hdg$v11" + "\n913 $Sest$ipt$a";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord.contains("430 Quartette$mVl 1 2$sKl 4hdg$v11"));

		// 430 RSWK (irgendein Kommentar)
		titleStrOld =
			"430 Quartett, Violine 1,2$v11"
				+ "\n913 $Sswd$ipt$a130 Quartette$mVl 1 2$sKl 4hd$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.INTELLECT);
		assertTrue(newRecord.contains("430 Quartett$mVioline 1,2$v11"));

		// MASCHINELL -----------------------
		// 430 alt 
		titleStrOld =
			"430 Quartette Vl 1 2. Kl 4hdg$vR:EST vor RAK-M 2003"
				+ "\n913 $Sest$ipt$a130 Quartette$mVl 1 2$sKl 4hd$0";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord.contains("430 Quartette Vl 1 2$sKl 4hdg$v"
			+ KOM_VOR_2003_430));

		// 430 RAK, kein Komm 
		titleStrOld = "430 Quartette Vl 1 2. Kl 4hdg" + "\n913 $Sest$ipt$a";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord.contains("430 Quartette$mVl 1 2$sKl 4hdg"));

		// 430 RAK, bel. Komm.
		titleStrOld = "430 Quartette Vl 1 2. Kl 4hdg$v11" + "\n913 $Sest$ipt$a";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord.contains("430 Quartette$mVl 1 2$sKl 4hdg$v11"));

		// 430 RSWK (irgendein Kommentar)
		titleStrOld = "430 Quartett, Violine 1,2$v11" + "\n913 $Sswd$ipt$a";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord.contains("430 Quartett$mVioline 1,2$v11"));

		// Pica+, Script, 430
		titleStrOld = "022@ $aQuartette, Vl 1 2" + "\n913 $Sest$ipt$a";
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		newRecord = StringUtils.gnd2Pica(newRecord);
		assertTrue(newRecord.contains("022@ $aQuartette$mVl 1 2"));
	}

	// Bleiben nicht behandelte Felder erhalten?
	@Test
	public final void testTransformUnhandled() {
		titleStrOld = "375 m"; // irgendwas
		newRecord =
			TransformRecord.transform(titleStrOld, TransformMode.MACHINE);
		assertTrue(newRecord.contains(titleStrOld));
	}

	@Test
	public final void testTransformOldRAK() {
		try {
			titleStrOld = null;
			newRecord = TransformRecord.transformOldRAK(titleStrOld);
			fail();
		} catch (IllegalArgumentException e) {
			//OK
		}
		try {
			titleStrOld = "";
			newRecord = TransformRecord.transformOldRAK(titleStrOld);
			fail();
		} catch (IllegalArgumentException e) {
			//OK
		}
		try {
			titleStrOld = "  ";
			newRecord = TransformRecord.transformOldRAK(titleStrOld);
			fail();
		} catch (IllegalArgumentException e) {
			//OK
		}
		try {
			titleStrOld = "aa";
			newRecord = TransformRecord.transformOldRAK(titleStrOld);
			fail();
		} catch (IllegalArgumentException e) {
			//OK
		}
		// kein Kommentar!
		// // - Teil - Fassung
		titleStrOld = "aa$v11";
		newRecord = TransformRecord.transformOldRAK(titleStrOld);
		assertEquals("aa", newRecord);

		// in der Migration nicht erkannte Fassung
		titleStrOld = "aa. Vl$v11";
		newRecord = TransformRecord.transformOldRAK(titleStrOld);
		assertEquals("aa$sVl", newRecord);

		// + Teil + Fassung
		titleStrOld = "aa$pbb$sVl$v11";
		newRecord = TransformRecord.transformOldRAK(titleStrOld);
		assertEquals("aa$pbb$sVl", newRecord);

		// + Teil - Fassung
		titleStrOld = "aa$pbb$v11";
		newRecord = TransformRecord.transformOldRAK(titleStrOld);
		assertEquals("aa$pbb", newRecord);

		// - Teil + Fassung
		titleStrOld = "aa$sVl$v11";
		newRecord = TransformRecord.transformOldRAK(titleStrOld);
		assertEquals("aa$sVl", newRecord);
	}

}
