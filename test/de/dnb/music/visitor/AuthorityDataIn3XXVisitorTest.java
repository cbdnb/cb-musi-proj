package de.dnb.music.visitor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.mediumOfPerformance.ParseInstrumentation;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public class AuthorityDataIn3XXVisitorTest {

	InstrumentationList iL;
	AuthorityDataVisitor vis;
	MusicTitle mt;

	@Before
	public void setUp() throws Exception {
		vis = new AuthorityDataVisitor();
		vis.setExpansion(true);
	}

	@Test
	public final void test1Instrument() {
		iL = ParseInstrumentation.parse("Vl");
		iL.accept(vis);
		assertTrue(vis.toString().contains("Violine"));
		// keine Gesamtzahl, auch kein $n1 bei Einzelinstrumenten
		assertFalse(vis.toString().contains("$s"));
		assertFalse(vis.toString().contains("$n"));
	}
	
	@Test
	public final void test2Instrument() {
		iL = ParseInstrumentation.parse("Vl 1 2");
		iL.accept(vis);
		assertTrue(vis.toString().contains("$s2"));
		assertTrue(vis.toString().contains("$n2"));
	}
	
	@Test
	public final void test2Plus1Instrument() {
		iL = ParseInstrumentation.parse("Vl 1 2 Va");
		iL.accept(vis);
		assertFalse(vis.toString().contains("$s"));
		assertFalse(vis.toString().contains("Viola$n"));
	}
	
	@Test
	public final void test2Plus2Instrument() {
		iL = ParseInstrumentation.parse("Vl 1 2 Va 1 2");
		iL.accept(vis);
		assertTrue(vis.toString().contains("$s4"));
		assertTrue(vis.toString().contains("Violine$n2"));
		assertTrue(vis.toString().contains("Viola$n2"));
	}
	
	@Test
	public final void test2Plus1InstrumentForce() {
		iL = ParseInstrumentation.parse("Vl 1 2 Va");
		vis.setForceTotalCount(true);
		iL.accept(vis);
		assertTrue(vis.toString().contains("$s3"));
		assertFalse(vis.toString().contains("Viola$n"));
	}
	
	@Test
	public final void testGenre() {
		mt = ParseMusicTitle.parseFullRAK(null, "Adagio und Fuge, Vl Va 1 2 3"
				+ "<Fuge KV 5a, Durchführung 1>. Fassung 1234");
		mt.accept(vis);
		assertFalse(vis.toString().contains("Adagio"));
		assertTrue(vis.toString().contains("Fuge"));
	}

}
