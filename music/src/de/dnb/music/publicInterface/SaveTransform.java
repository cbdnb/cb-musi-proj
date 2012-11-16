package de.dnb.music.publicInterface;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public final class SaveTransform {

	private SaveTransform() {
		super();
	}

	public static void main(final String[] args) throws IOException {

		String titleStrOld = "";

		if (args == null || args.length != 1) {

			try {
				BufferedReader reader =
					new BufferedReader(new InputStreamReader(System.in));

				String read;

				while ((read = reader.readLine()) != null) {
					titleStrOld += read + "\n";
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
			titleStrOld = args[0];

		FileWriter fw = new FileWriter("D:\\FuerIT.txt", true);

		fw.write(titleStrOld);

		fw.close();

	}
}
