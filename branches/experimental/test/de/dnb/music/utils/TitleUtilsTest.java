package de.dnb.music.utils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.utils.TitleUtils;

public class TitleUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testIsRSWK() {
		assertTrue(TitleUtils.isRSWK("Fuge, Violine 1,2 Viola KV 5"));
		assertTrue(TitleUtils.isRSWK("$aFuge, Violine 1,2 Viola KV 5"));
		assertFalse(TitleUtils.isRSWK("Fugen, Vl 1 2 Va, KV 5"));
		assertFalse(TitleUtils.isRSWK("Fugen$mVl 1 2$mVa$nKV 5"));
	}

	@Test
	public final void testIsRAK() {
		assertTrue(TitleUtils.isRAK("Fugen, Vl 1 2 Va, KV 5"));
		assertTrue(TitleUtils.isRAK("$aFugen, Vl 1 2 Va, KV 5"));
		assertFalse(TitleUtils.isRAK("Fuge, Violine 1,2 Viola KV 5"));
		assertFalse(TitleUtils.isRAK("Fugen$mVl 1 2$mVa$nKV 5"));
	}

	@Test
	public final void testIsGND() {
		assertTrue(TitleUtils.isGND("Fugen$mVl 1 2$mVa$nKV 5"));
		assertTrue(TitleUtils.isGND("$aFugen$mVl 1 2$mVa$nKV 5"));
		assertFalse(TitleUtils.isGND("Fuge, Violine 1,2 Viola KV 5"));
		assertFalse(TitleUtils.isGND("Fugen, Vl 1 2 Va, KV 5"));
	}

}
