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
public class Record {

	private TreeMap<String, LinkedHashSet<String>> data =
		new TreeMap<String, LinkedHashSet<String>>();

	public Record(final String recordS) {
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

	public Record() {
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
		return new Record(record).normalized();
	}
	
	public static boolean isNormIdentical(String record1, String reocord2) {
		return normalize(record1).equals(normalize(reocord2));
	}

	/**
	 * Normalisiert Musikstrings:
	 * - entfernt "$vR:Maschinelle Umsetzung GND"
	 * - entfernt 667 MmV-LF
	 * - trimt.
	 * @return	Normalisierten Datensatz als String.
	 */
	public String normalized() {
		String norm = this.toString();
		norm = norm.replaceAll("\\$vR:Maschinelle Umsetzung GND", "");
		norm = norm.replaceAll("667 MmV\n", "");
		norm = norm.trim();

		return norm;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String old = args[0];
		String news = normalize(old);
		
		System.err.println(isNormIdentical(old, news));
		
		System.err.println(news);

	}

}
