package de.dnb.music.publicInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import utils.TitleUtils;

import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;

public final class MusikfileLesen {

	private MusikfileLesen() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(final String[] args) throws IOException {
		File file = new File("musikTitel.txt");
		BufferedReader br;
//		MusicTitle.setGND3XXExplicit(true);

		br =
			new BufferedReader(new InputStreamReader(new FileInputStream(
					file)));
		PrintStream psgnd = new PrintStream("D:/analysen/temp/mus_gnd1.txt");
		PrintStream psstr =
			new PrintStream("D:/analysen/temp/mus_strukt1.txt");
		String s = br.readLine();
		while (s != null) {
			MusicTitle tit = ParseMusicTitle.parse("", s);

			String gnd = Transform130.transform2DNB(s);
			// überzählige Leerzeilen entfernen
			gnd = gnd.replaceAll("\n\n+", "\n");
			psgnd.println("ALT:\n" + s);
			psgnd.println("NEU:");
			psgnd.println(gnd);

			String str = TitleUtils.getStructured(tit);
			str = str.replaceAll("\n\n+", "\n");
			psstr.println("ALT:\n" + s);
			psstr.println("STRUKTURIERT:");
			psstr.println(str);
			psstr.println();
			s = br.readLine();
		}

	}

}
