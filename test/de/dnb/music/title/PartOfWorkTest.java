package de.dnb.music.title;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import utils.TestUtils;
import utils.TitleUtils;

import de.dnb.music.title.PartOfWork;

public class PartOfWorkTest {

	PartOfWork p1, p2, p3, p4, p5;
	String s1, s2, s3, s4, s5;

	@Before
	public void setUp() throws Exception {
		s1 = "A, Dio";
		s2 = "Sonate KV 1, Adagio";
		s3 = "Sonate KV 1, Adagio, aa";
		s4 = "Sonate KV 1, Adagio, aa, bb";
		s5 = "Messe KV 1,2 Anh";
		p1 = new PartOfWork(s1);
		p2 = new PartOfWork(s2);
		p3 = new PartOfWork(s3);
		p4 = new PartOfWork(s4);
		p5 = new PartOfWork(s5);
	}

	@Test
	public final void testGetGND1XX() {
		assertEquals("$p" + s1, TitleUtils.getGND130Or430(p1));
		assertEquals("$pSonate$nKV 1$pAdagio", TitleUtils.getGND130Or430(p2));
		assertEquals("$pSonate$nKV 1$pAdagio$paa", TitleUtils.getGND130Or430(p3));
		assertEquals("$pSonate$nKV 1$pAdagio$paa, bb", TitleUtils.getGND130Or430(p4));
		assertEquals("$pMesse$nKV 1,2 Anh", TitleUtils.getGND130Or430(p5));		
	}
	
	@Test
	public final void testRAK() {
		assertEquals(" <" + s1 + ">", TitleUtils.getRAK(p1));
		assertEquals(" <" + s2 + ">", TitleUtils.getRAK(p2));
		assertEquals(" <" + s3 + ">", TitleUtils.getRAK(p3));
		assertEquals(" <" + s4 + ">", TitleUtils.getRAK(p4));
		assertEquals(" <" + s5 + ">", TitleUtils.getRAK(p5));		
	}

	

	@Test
	public final void testGetGND530() {
		//		System.out.println("\nPartOfWorkTest");
		//		System.out.println("testGetGND530");
		//		System.out.println(p1.getGND530());
		//		System.out.println(p2.getGND530());
		//		System.out.println(p3.getGND530());
		//		System.out.println(p4.getGND530());

	}

	
}
