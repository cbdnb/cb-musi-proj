package de.dnb.music.title;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import utils.TitleUtils;

import de.dnb.music.title.FormalTitle;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public class ParseMusicTitleTest {

	private MusicTitle mt;

	private String titleStr;

	@Test
	public void testParseDollarG() {
		titleStr = "aa$gbb";
		mt = ParseMusicTitle.parseGND(null, titleStr);
		assertEquals(titleStr, TitleUtils.getX30ContentAsString(mt));		
		mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		assertEquals(titleStr, TitleUtils.getX30ContentAsString(mt));
		
		titleStr = "aa$gNr. 1";
		mt = ParseMusicTitle.parseGND(null, titleStr);
		assertEquals("aa$nNr. 1", TitleUtils.getX30ContentAsString(mt));		
		mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		assertEquals("aa$nNr. 1", TitleUtils.getX30ContentAsString(mt));
		
		titleStr = "aa$g1234";
		mt = ParseMusicTitle.parseGND(null, titleStr);
		assertEquals("aa$f1234", TitleUtils.getX30ContentAsString(mt));
		mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		assertEquals("aa$f1234", TitleUtils.getX30ContentAsString(mt));
		
		titleStr = "Adagio, Violine$g1234";
		mt = ParseMusicTitle.parseGND(null, titleStr);
		assertEquals("Adagio$mVl$f1234", TitleUtils.getX30ContentAsString(mt));
		mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		assertEquals("Adagio$mVl$f1234", TitleUtils.getX30ContentAsString(mt));
		
		titleStr = "Adagio$g1234";
		mt = ParseMusicTitle.parseGND(null, titleStr);
		assertEquals("Adagio$f1234", TitleUtils.getX30ContentAsString(mt));
		mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		assertEquals("Adagio$f1234", TitleUtils.getX30ContentAsString(mt));
		
		titleStr = "Adagio$g1";
		mt = ParseMusicTitle.parseGND(null, titleStr);
		assertEquals("Adagio$n1", TitleUtils.getX30ContentAsString(mt));
		mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		assertEquals("Adagio$n1", TitleUtils.getX30ContentAsString(mt));
		mt = ParseFormalTitle.parse(null, titleStr);
		assertEquals("Adagio$n1", TitleUtils.getX30ContentAsString(mt));
		
		titleStr = "Adagio$gNr. 1";
		mt = ParseMusicTitle.parseGND(null, titleStr);
		assertEquals("Adagio$nNr. 1", TitleUtils.getX30ContentAsString(mt));
		mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		assertEquals("Adagio$nNr. 1", TitleUtils.getX30ContentAsString(mt));
		mt = ParseFormalTitle.parse(null, titleStr);
		assertEquals("Adagio$nNr. 1", TitleUtils.getX30ContentAsString(mt));
		
		titleStr = "aa$g1";
		mt = ParseMusicTitle.parseGND(null, titleStr);
		assertEquals("aa$n1", TitleUtils.getX30ContentAsString(mt));
		mt = ParseMusicTitle.parseFullRAK(null, titleStr);
		assertEquals("aa$n1", TitleUtils.getX30ContentAsString(mt));
	}

	@Test
	public void testParseMitFassung() {
		mt = ParseMusicTitle.parseTitlePlusVersion(null, "aaa op. 1788");
		assertEquals("130 aaa$nop. 1788", TitleUtils.getGND1XXPlusTag(mt));
	}

	@Test
	public void testParseGND() {
		mt =
			ParseMusicTitle
					.parseGND(null, "Stücke$mVl$mKl$nop. 50$pBauerntanz");
		assertEquals("130 Stücke$mVl$mKl$nop. 50$pBauerntanz",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					"Das @befreite Deutschland$pOuvertüre");
		assertEquals("130 Das @befreite Deutschland$pOuvertüre",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					"Sonaten$mVl$mBc$f1716$pSonate$nNr. 9$p1. Satz");
		assertEquals("130 Sonaten$mVl$mBc$f1716$pSonate$nNr. 9$p1. Satz",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					"Sonaten$mKl$nHob 16,35 - 39$pSonate$nHob 16,35");
		assertEquals("130 Sonaten$mKl$nHob 16,35 - 39$pSonate$nHob 16,35",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					"Das @wohltemperierte Klavier$nTeil 1"
						+ "$pPräludium und Fuge$nBWV 861$pFuge");
		assertEquals(
				"130 Das @wohltemperierte Klavier$nTeil 1$pPräludium und Fuge"
					+ "$nBWV 861$pFuge", TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					"Sonaten$mOrg$nBWV 525 - 530$pSonate$nBWV 528$p2. Satz");
		assertEquals(
				"130 Sonaten$mOrg$nBWV 525 - 530$pSonate$nBWV 528$p2. Satz",
				TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseGND(null, "Kantaten$nBWV 35$pSinfonia 1");
		assertEquals("130 Kantaten$nBWV 35$pSinfonia 1",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					"Kwasi & Kwame$pYour eyes, so big and sparkling");
		assertEquals("130 Kwasi & Kwame$pYour eyes, so big and sparkling",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					"Walzer$mKl 4hdg.$nop. 39$oAusw. Arr.");
		assertEquals("130 Walzer$mKl 4hdg.$nop. 39$oAusw. Arr.",
				TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseGND(null, "Elegien$mBassetthr$mKl$f1965");
		assertEquals("130 Elegien$mBassetthorn$mKl$f1965",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					"Konzertante Trios$mVl$mVc$mKl$nconatum 14");
		assertEquals("130 Konzertante Trios$mVl$mVc$mKl$nconatum 14",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					" East Eleventh Street New York 10003");
		assertEquals("130 East Eleventh Street New York 10003",
				TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseGND(null, "Etüden$mAkk linke Hand$f1979");
		assertEquals("130 Etüden$mAkk linke Hand$f1979",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle
					.parseGND(null, "Konzerte$mVa d'amore$mOrch$nRV 397");
		assertEquals("130 Konzerte$mVa d'amore$mOrch$nRV 397",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseGND(null,
					"Fantasien$mSopr-Instr.$mTen-Instr.$mBass-Instr.");
		assertEquals("130 Fantasien$mSopr-Instr.$mTen-Instr.$mBass-Instr.",
				TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseGND(null, "Werke$mKl");
		assertEquals("130 Werke$mKl", TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseGND(null, "aaa$nop. 1788");
		assertEquals("130 aaa$nop. 1788", TitleUtils.getGND1XXPlusTag(mt));
	}

	@Test
	public void testParseMitOrdnungshilfeUndGruppe() {
		mt = ParseMusicTitle.parseFullRAK(null, "Stücke, Vl Kl, op. 50 <Bauerntanz>");
		assertEquals("130 Stücke$mVl$mKl$nop. 50$pBauerntanz",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Stücke$mVl$mKl$nop. 50$4obpa",
				TitleUtils.getGND530(mt, true));

		mt =
			ParseMusicTitle
					.parseFullRAK(null, "Das @befreite Deutschland <Ouvertüre>");
		assertEquals("130 Das @befreite Deutschland$pOuvertüre",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Das @befreite Deutschland$4obpa",
				TitleUtils.getGND530(mt, true));

		mt =
			ParseMusicTitle.parseFullRAK(null,
					"Sonaten, Vl Bc, 1716 <Sonate Nr. 9, 1. Satz>");
		assertEquals("130 Sonaten$mVl$mBc$f1716$pSonate$nNr. 9$p1. Satz",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Sonaten$mVl$mBc$f1716$pSonate$nNr. 9$4obpa",
				TitleUtils.getGND530(mt, true));

		mt =
			ParseMusicTitle.parseFullRAK(null,
					"Sonaten, Kl, Hob 16,35 - 39 <Sonate Hob 16,35>");
		assertEquals("130 Sonaten$mKl$nHob 16,35 - 39$pSonate$nHob 16,35",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Sonaten$mKl$nHob 16,35 - 39$4obpa",
				TitleUtils.getGND530(mt, true));

		mt =
			ParseMusicTitle.parseFullRAK(null, "Das @wohltemperierte Klavier, Teil 1 "
				+ "<Präludium und Fuge BWV 861, Fuge>");
		assertEquals("130 Das @wohltemperierte Klavier$nTeil 1"
			+ "$pPräludium und Fuge$nBWV 861$pFuge",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Das @wohltemperierte Klavier$nTeil 1"
			+ "$pPräludium und Fuge$nBWV 861$4obpa",
				TitleUtils.getGND530(mt, true));

		mt =
			ParseMusicTitle.parseFullRAK(null,
					"Sonaten, Org, BWV 525 - 530 <Sonate BWV 528, 2. Satz>");
		assertEquals(
				"130 Sonaten$mOrg$nBWV 525 - 530$pSonate$nBWV 528$p2. Satz",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Sonaten$mOrg$nBWV 525 - 530$pSonate$nBWV 528$4obpa",
				TitleUtils.getGND530(mt, true));

		mt = ParseMusicTitle.parseFullRAK(null, "Kantaten, BWV 35 <Sinfonia 1>");
		assertEquals("130 Kantaten$nBWV 35$pSinfonia 1",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Kantaten$nBWV 35$4obpa",
				TitleUtils.getGND530(mt, true));

		mt =
			ParseMusicTitle.parseFullRAK(null,
					"Kwasi & Kwame <Your eyes, so big and sparkling>");

		assertEquals("130 Kwasi & Kwame$pYour eyes, so big and sparkling",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Kwasi & Kwame$4obpa", TitleUtils.getGND530(mt, true));

		mt =
			ParseMusicTitle
					.parseFullRAK(null, "Walzer, Kl 4hdg., op. 39 / Ausw. Arr.");

		assertEquals("130 Walzer$mKl 4hdg.$nop. 39$oAusw. Arr.",
				TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("530 Walzer$mKl 4hdg.$nop. 39$4werk",
				TitleUtils.getGND530(mt, true));
	}

	@Test
	public void testParseOhneFassung() {
		mt =
			ParseMusicTitle.parseSimpleTitle(null,
					"Elegien, Bassetthr Kl (1965)");
		assertEquals("130 Elegien$mBassetthorn$mKl$f1965",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseSimpleTitle(null,
					"Konzertante Trios, Vl Vc Kl conatum 14");
		assertEquals("130 Konzertante Trios$mVl$mVc$mKl$nconatum 14",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseSimpleTitle(null,
					"Konzertante Trios, Vl Vc Kl conatum 14");
		assertEquals("130 Konzertante Trios$mVl$mVc$mKl$nconatum 14",
				TitleUtils.getGND1XXPlusTag(mt));

		assertTrue(TitleUtils.getGND3XX(mt).contains("383 conatum 14"));

		mt =
			ParseMusicTitle.parseSimpleTitle(null,
					"East Eleventh Street New York 10003");
		assertEquals("130 East Eleventh Street New York 10003",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseSimpleTitle(null,
					"Etüden, Akk linke Hd (1979)");
		// "linke Hand" ist die Vorzugsbenennung in RAK-M 2003
		assertEquals("130 Etüden$mAkk linke Hand$f1979",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseSimpleTitle(null,
					"Konzerte, Va d'amore Orch, RV 397");
		assertEquals("130 Konzerte$mVa d'amore$mOrch$nRV 397",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseSimpleTitle(null,
					"Fantasien, Sopr-Instr. Ten-Instr. Bass-Instr.");
		assertEquals("130 Fantasien$mSopr-Instr.$mTen-Instr.$mBass-Instr.",
				TitleUtils.getGND1XXPlusTag(mt));

		mt =
			ParseMusicTitle.parseSimpleTitle(null,
					"Fantasien, Sopr-Instr. Ten-Instr. Bass-Instr.");
		assertEquals("130 Fantasien$mSopr-Instr.$mTen-Instr.$mBass-Instr.",
				TitleUtils.getGND1XXPlusTag(mt));

		mt = ParseMusicTitle.parseSimpleTitle(null, "Werke, Kl");
		assertEquals("130 Werke$mKl", TitleUtils.getGND1XXPlusTag(mt));
		assertEquals("Werke, Kl", TitleUtils.getRAK(mt));
		assertTrue(mt instanceof FormalTitle);

	}

}
