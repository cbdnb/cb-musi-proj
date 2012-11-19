/**
 * 
 */
package de.dnb.music.publicInterface;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import utils.Pair;
import utils.StringUtils;

/**
 * 
 * @author baumann
 *
 */
public class MusicRecord {

	private TreeMap<String, LinkedHashSet<String>> data =
		new TreeMap<String, LinkedHashSet<String>>();

	public MusicRecord(final String recordS) {
		List<String> linesList = makeList(recordS);

		for (String line : linesList) {
			add(line);
		}
	}

	/**
	 * @param recordS
	 * @return
	 */
	public List<String> makeList(final String recordS) {
		String[] linesArr = recordS.split("\n");
		List<String> linesList = new LinkedList<String>();

		// in Zeilen-Liste umwandeln:
		for (String line : linesArr) {
			line = line.trim();
			if (line.length() != 0)
				linesList.add(line);
		}
		return linesList;
	}

	public MusicRecord() {
	}

	public final void addAll(List<String> lineList) {
		for (String line : lineList) {
			add(line);
		}
	}

	public final void addAll(String lineList) {
		addAll(makeList(lineList));
	}

	public final void add(final String tag, final String content) {
		LinkedHashSet<String> tagLines = data.get(tag);
		if (tagLines == null) {
			tagLines = new LinkedHashSet<String>();
			data.put(tag, tagLines);
		}
		tagLines.add(content);
	}

	public final void add(final String line) {
		Pair<String, String> pair = StringUtils.getTagAndcontent(line);
		if (pair == null) {
			// Ist wohl irgendwie eine Zeile hineingekommen
			return;
		}
		String tag = pair.first;
		String content = pair.second;
		add(tag, content);
	}

	@Override
	public final String toString() {
		// ausgeben:
		String out = "";
		for (Entry<String, LinkedHashSet<String>> entry : data.entrySet()) {
			LinkedHashSet<String> lines = entry.getValue();
			for (String line : lines) {
				out += entry.getKey() + " " + line + '\n';
			}
		}
		if (out.length() > 0)
			out = out.substring(0, out.length() - 1);
		return out;
	}

	public static String normalize(String record) {
		if (record == null || record.trim() == "")
			throw new IllegalArgumentException();
		return new MusicRecord(record).normalized();
	}

	public static boolean isNormIdentical(String record1, String reocord2) {
		return normalize(record1).equals(normalize(reocord2));
	}

	/**
	 * Normalisiert Musikstrings:
	 * - entfernt die standardisierten Kommentare
	 * - entfernt 667 <Aufarbeitung>\n
	 * - trimmt.
	 * @return	Normalisierten Datensatz als String.
	 */
	public String normalized() {
		String norm = this.toString();
		norm = norm.replaceAll("\\$v" + TransformRecord.KOM_NACH_2003, "");
		norm = norm.replaceAll("\\$v" + TransformRecord.KOM_VOR_2003, "");
		norm = norm.replaceAll("\\$v" + TransformRecord.KOM_MASCHINELL, "");
		norm =
			norm.replaceAll("\\$v" + TransformRecord.KOM_MASCHINELL_NACH_2003,
					"");
		norm =
			norm.replaceAll("\\$v" + TransformRecord.KOM_MASCHINELL_VOR_2003,
					"");
		norm =
			norm.replaceAll("667 " + TransformRecord.VORZ_BEN_AUFGEARB + "\n",
					"");
		norm = norm.replaceAll("667 " + TransformRecord.SATZ_AUFG + "\n", "");
		norm = norm.replaceAll("667 " + TransformRecord.MASCH_AUFG + "\n", "");
		norm = norm.replaceAll("667 " + TransformRecord.GEW_INTELL + "\n", "");
		norm = norm.replaceAll("667 " + TransformRecord.GEW_MASCH + "\n", "");
		norm = norm.replaceAll("667 " + TransformRecord.VERL_MASCH + "\n", "");
		norm = norm.trim();

		return norm;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String oldR =
			"\n008 wim" + "\n065 14.4p"
				+ "\n130 aaa$vR:Umsetzung GND aus RAK-M 2003" + "\n913 qwer";

		String newR =
			"008 wim" + "\n065 14.4p"
				+ "\n130 aaa$vR:Maschinelle Umsetzung GND" + "\n667 MmV"
				+ "\n913 qwer";

		System.err.println(isNormIdentical(oldR, newR));

	}
}
