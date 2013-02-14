package de.dnb.music.publicInterface;

import static org.junit.Assert.*;

import org.junit.Test;

import de.dnb.music.publicInterface.Transform130;

public class Transform130Test {

	@Test
	public final void testTransform() {
		// SWD
		String t = Transform130.transform2DNB("Te Deum <1711>");
		assertTrue(t.contains("130 Te Deum$f1711"));
		//              kein $v
		assertFalse(t.contains("130 Te Deum$f1711" + "$v"
			+ TransformRecord.KOM_NACH_2003));
		assertFalse(t.contains("430 Te Deum, 1711" + "$v"
			+ TransformRecord.KOM_PORTAL_430));
		assertTrue(t.endsWith("\n"));

		// DMA
		t =
				Transform130.transform2DNB("Te Deum, 1711" + "$v"
				+ TransformRecord.KOM_NACH_2003);
		assertTrue(t.contains("130 Te Deum$f1711" + "$v"
			+ TransformRecord.KOM_MASCHINELL_NACH_2003));
		assertFalse(t.contains("430 Te Deum, 1711" + "$v"
			+ TransformRecord.KOM_PORTAL_430));
		assertTrue(t.endsWith("\n"));

		// Gattung
		t =
				Transform130.transform2DNB("Motetten, Buch 5" + "$v"
				+ TransformRecord.KOM_NACH_2003);
		assertTrue(t.contains("\n380 "));
		assertTrue(t.endsWith("\n"));

		// Gattung + Instrument
		t =
				Transform130.transform2DNB("Sonaten, Vl Bc (1716)" + "$v"
				+ TransformRecord.KOM_NACH_2003);
		assertTrue(t.contains("\n380 "));
		assertTrue(t.contains("\n382 "));
		assertTrue(t.endsWith("\n"));

		// Werkteil
		t =
				Transform130.transform2DNB("Motetten, Buch 5$pExsultate Deo" + "$v"
				+ TransformRecord.KOM_NACH_2003);
		// Umsetzung nach 430
		assertTrue(t.contains("\n430 " + "Motetten$nBuch 5$gExsultate Deo"
			+ "$v" + TransformRecord.KOM_PORTAL_430));
		assertTrue(t.contains("\n430 "));
		assertTrue(t.contains("\n380 "));
		assertTrue(t.endsWith("\n"));

		// Fassung 1
		t =
			Transform130.transform2DNB("Stücke, Tb Kl, 1966. Fassung 2008"
				+ "$v" + TransformRecord.KOM_NACH_2003);
		if (TransformRecord.RELATED_WORK_AS_STRING)
			assertTrue(t.contains("\n530 "));
		assertTrue(t.contains("\n382 "));
		assertTrue(t.contains("\n380 "));
		assertTrue(t.endsWith("\n"));

		// Fassung 2
		String t2 =
				Transform130.transform2DNB("Stücke, Tb Kl, 1966$sFassung 2008"
				+ "$v" + TransformRecord.KOM_NACH_2003);
		if (TransformRecord.RELATED_WORK_AS_STRING)
			assertTrue(t2.contains("\n530 "));
		assertTrue(t2.contains("\n382 "));
		assertTrue(t2.contains("\n380 "));
		assertTrue(t.endsWith("\n"));
		// Funktioniert nicht, da 430 nicht übereinstimmen kann 
		//(mal ". ", mal $s
		//		assertEquals(t, t2);

		t = Transform130.transform2DNB("aaa");
		assertEquals("130 aaa$v" + TransformRecord.KOM_MASCHINELL + "\n", t);
		assertTrue(t.endsWith("\n"));

		try {
			t = Transform130.transform2DNB("");
			fail();
		} catch (IllegalArgumentException e1) {
			// OK
		}

		t = Transform130.transform2DNB("a$v" + TransformRecord.KOM_VOR_2003);
		assertTrue(t.contains("130 a$v" + TransformRecord.KOM_MASCHINELL));
		assertFalse(t.contains("430 a$v" + TransformRecord.KOM_PORTAL_430));
		assertTrue(t.endsWith("\n"));

		try {
			t = Transform130.transform2DNB(null);
			fail();
		} catch (IllegalArgumentException e) {
			// OK
		}

	}

}
