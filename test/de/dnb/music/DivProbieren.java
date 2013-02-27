package de.dnb.music;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.JOptionPane;

import de.dnb.music.genre.Genre;
import de.dnb.music.genre.GenreList;
import de.dnb.music.genre.ParseGenre;
import de.dnb.music.publicInterface.MusikfileLesen;
import de.dnb.music.publicInterface.Transform130;
import de.dnb.music.title.MusicTitle;

public class DivProbieren {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		TextArea ar = new TextArea("Blbla");
		JOptionPane.showMessageDialog(null, ar);

	}

}
