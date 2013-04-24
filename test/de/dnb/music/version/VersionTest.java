package de.dnb.music.version;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import utils.TestUtils;
import utils.TitleUtils;

import de.dnb.music.version.ParseVersion;
import de.dnb.music.version.Version;


public class VersionTest {

	@Test
	public final void testGetFallgruppeParagraphM511() {
		Version f = ParseVersion.parse(null, "KV 23");
		assertEquals('a', f.getFallgruppeParagraphM511());
		assertEquals(1, f.getUntergruppe());

		f = ParseVersion.parse(null, "Op. 134");
		assertEquals('a', f.getFallgruppeParagraphM511());
		assertEquals(2, f.getUntergruppe());

		f = ParseVersion.parse(null, "Suite");
		assertEquals('b', f.getFallgruppeParagraphM511());
		assertEquals(1, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "Suite Vl");
		assertEquals('b', f.getFallgruppeParagraphM511());
		assertEquals(2, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "Suite a-Moll");
		assertEquals('b', f.getFallgruppeParagraphM511());
		assertEquals(3, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "Suite 1777");
		assertEquals('b', f.getFallgruppeParagraphM511());
		assertEquals(4, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "Suite Nr. 3");
		assertEquals('b', f.getFallgruppeParagraphM511());
		assertEquals(5, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "Fassung 1777");
		assertEquals('c', f.getFallgruppeParagraphM511());
		assertEquals(1, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "Fassung Vl");
		assertEquals('c', f.getFallgruppeParagraphM511());
		assertEquals(2, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "Fassung für Bläser");
		assertEquals('c', f.getFallgruppeParagraphM511());
		assertEquals(3, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "Fassung Vl 1777");
		assertEquals('c', f.getFallgruppeParagraphM511());
		assertEquals(4, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "1777");
		assertEquals('e', f.getFallgruppeParagraphM511());
		assertEquals(1, f.getUntergruppe());
		
		f = ParseVersion.parse(null, "Vl");
		assertEquals('e', f.getFallgruppeParagraphM511());
		assertEquals(2, f.getUntergruppe());
		
		f = new Version("blabla");
		assertEquals('$', f.getFallgruppeParagraphM511());
		assertEquals(1, f.getUntergruppe());

	}

	@Test
	public final void testGetGND1XX() {
		Version f = ParseVersion.parse(null, "KV 23");
		assertEquals("$sKV 23", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Op. 134");
		assertEquals("$sOp. 134", TitleUtils.getX30ContentAsString(f));
		

		f = ParseVersion.parse(null, "Suite");
		assertEquals("$sSuite", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Suite Vl");
		assertEquals("$sSuite Vl", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Suite a-Moll");
		assertEquals("$sSuite a-Moll", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Suite 1777");
		assertEquals("$sSuite 1777", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Suite Nr. 3");
		assertEquals("$sSuite Nr. 3", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Fassung 1777");
		assertEquals("$sFassung 1777", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Fassung Vl");
		assertEquals("$sFassung Vl", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Fassung für Bläser");
		assertEquals("$sFassung für Bläser", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Fassung Vl 1777");
		assertEquals("$sFassung Vl 1777", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Fassung Vl, 1777");
		assertEquals("$sFassung Vl, 1777", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "1777");
		assertEquals("$s1777", TitleUtils.getX30ContentAsString(f));
		
		f = ParseVersion.parse(null, "Vl");
		assertEquals("$sVl", TitleUtils.getX30ContentAsString(f));
		
		f = new Version("blabla");
		assertEquals("$sblabla", TitleUtils.getX30ContentAsString(f));
	}
	
	@Test
	public final void testAsRAK() {
		Version f = ParseVersion.parse(null, "KV 23");
		assertEquals(". KV 23", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Op. 134");
		assertEquals(". Op. 134", TitleUtils.getRAK(f));
		

		f = ParseVersion.parse(null, "Suite");
		assertEquals(". Suite", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Suite Vl");
		assertEquals(". Suite Vl", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Suite a-Moll");
		assertEquals(". Suite a-Moll", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Suite 1777");
		assertEquals(". Suite 1777", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Suite Nr. 3");
		assertEquals(". Suite Nr. 3", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Fassung 1777");
		assertEquals(". Fassung 1777", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Fassung Vl");
		assertEquals(". Fassung Vl", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Fassung für Bläser");
		assertEquals(". Fassung für Bläser", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Fassung Vl 1777");
		assertEquals(". Fassung Vl 1777", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Fassung Vl, 1777");
		assertEquals(". Fassung Vl, 1777", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "1777");
		assertEquals(". 1777", TitleUtils.getRAK(f));
		
		f = ParseVersion.parse(null, "Vl");
		assertEquals(". Vl", TitleUtils.getRAK(f));
		
		f = new Version("blabla");
		assertEquals(". blabla", TitleUtils.getRAK(f));
	}

}
