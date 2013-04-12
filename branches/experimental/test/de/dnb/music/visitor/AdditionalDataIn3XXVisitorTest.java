package de.dnb.music.visitor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.dnb.music.additionalInformation.AdditionalInformation;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public class AdditionalDataIn3XXVisitorTest {

	private MusicTitle mt;
	private AdditionalDataIn3XXVisitor vis;

	@Before
	public void setUp() throws Exception {
		vis = new AdditionalDataIn3XXVisitor();
	}

	@Test
	public final void testAll() {
		mt =
			ParseMusicTitle.parseFullRAK(null,
					"Adagio und Fuge, Vl 1 2 Va 1 2, C-Dur"
						+ "<aa Nr. 2>. Op. 2");
		mt.accept(vis);
		assertTrue(vis.toString().contains("383 $bOp. 2"));
		assertTrue(vis.toString().contains("383 Nr. 2"));
		assertTrue(vis.toString().contains("384 C-Dur"));

	}

	@Test
	public final void testVisitDoubleDateOfComposition() {
		mt =
			ParseMusicTitle.parseFullRAK(null,
					"Stücke, Tb Kl, 1966. Fassung 2008");
		mt.accept(vis);
		assertFalse(vis.toString().contains("548 $c1966$4dats"));
		assertTrue(vis.toString().contains("548 $c2008$4dats"));
	}

	@Test
	public final void testVisitKey() {
		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Sonaten, Fl 1 2 Bc, B-Dur");
		mt.accept(vis);
		assertTrue(vis.toString().contains("384 B-Dur"));
	}

	@Test
	public final void testVisitDoubleKey() {
		mt =
			ParseMusicTitle.parseFullRAK(null,
					"Sonaten, Fl 1 2 Bc, B-Dur <aa, C-Dur>");
		mt.accept(vis);
		assertTrue(vis.toString().contains("384 C-Dur"));
		assertFalse(vis.toString().contains("384 B-Dur"));
	}

	@Test
	public final void testVisitOpusNumber() {
		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Ave Maria, op. 24");
		mt.accept(vis);
		assertTrue(vis.toString().contains("383 $bop. 24"));
	}

	@Test
	public final void testVisitDoubleOpusNumber() {
		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Ave Maria, op. 23. Op. 24");
		mt.accept(vis);
		assertTrue(vis.toString().contains("383 $bOp. 24"));
		assertFalse(vis.toString().contains("383 $bop. 23"));
	}

	@Test
	public final void testVisitSerialNumber() {
		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Sinfonien, Nr. 4");
		mt.accept(vis);
		assertTrue(vis.toString().contains("383 Nr. 4"));
	}

	@Test
	public final void testVisitPureNumber() {
		mt = ParseMusicTitle.parseTitlePlusVersion(null, "Cantica, 1");
		mt.accept(vis);
		assertTrue(vis.toString().contains("383 1"));
	}

	@Test
	public final void testVisitThematicIndexNumber() {
		mt =
			ParseMusicTitle.parseTitlePlusVersion(null,
					"Quintette, Ob 1 2 Hr 1 2 Fg, ED 5 B flat 3");
		mt.accept(vis);
		assertTrue(vis.toString().contains("383 $cED 5 B flat 3"));
	}

}
