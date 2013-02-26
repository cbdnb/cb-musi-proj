package de.dnb.music.publicInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import utils.StringUtils;

public final class SortFields {

	private SortFields() {
		super();
	}

	/**
	 * @param args nix.
	 */
	public static void main(final String[] args) {

		String titleStrOld = "";

		// von stdin einlesen:
		try {
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(System.in));

			String read;

			while ((read = reader.readLine()) != null) {
				titleStrOld += read + "\n";
			}

		} catch (IOException e) {
			//
		}

		String out = StringUtils.sortFields(titleStrOld);
		System.out.println(out);

	}

}
