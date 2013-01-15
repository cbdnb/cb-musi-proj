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

	public static boolean isNormIdentical(String record1, String record2) {
		return normalize(record1).equals(normalize(record2));
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
		
		norm =
			norm.replaceAll("\\$v" + TransformRecord.KOM_MASCHINELL_NACH_2003,
					"");
		norm =
			norm.replaceAll("\\$v" + TransformRecord.KOM_MASCHINELL_VOR_2003,
					"");
		norm = norm.replaceAll("\\$v" + TransformRecord.KOM_MASCHINELL, "");
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
			"005 Tu1"
				+ "\n006 http://d-nb.info/gnd/301034354"
				+ "\n008 wim"
				+ "\n011 m"
				+ "\n012 m"
				+ "\n035 gnd/301034354"
				+ "\n039 dma/301034354$vzg"
				+ "\n130 Praxis pietatis melica ...$vR:Umsetzung GND aus RAK-M 2003"
				+ "\n500 !118834967!$4kom1"
				+ "\n680 - MGG 2 +, kein Nachweis in RISM. Ist die 5. veränd. Aufl. 1653 von \"Newes vollkömliches Gesangbuch ...\". Kirchenliederbuch.; Ansetzung nach den RAK-M 2003."
				+ "\n903 $eDE-101c"
				+ "\n903 $rDE-101c"
				+ "\n913 $Sest$ipt$aCrüger, Johann: Praxis pietatis melica ...$0301034354";

		String newR =
			"005 Tu1"
				+ "\n006 http://d-nb.info/gnd/301034354"
				+ "\n008 wim"
				+ "\n011 m"
				+ "\n012 m"
				+ "\n035 gnd/301034354"
				+ "\n039 dma/301034354$vzg"
				+ "\n130 Praxis pietatis melica ...$vR:Maschinelle Umsetzung GND aus RAK-M 2003"
				+ "\n500 !118834967!$4kom1"
				+ "\n680 - MGG 2 +, kein Nachweis in RISM. Ist die 5. veränd. Aufl. 1653 von \"Newes vollkömliches Gesangbuch ...\". Kirchenliederbuch.; Ansetzung nach den RAK-M 2003."
				
				+ "\n903 $eDE-101c"
				+ "\n903 $rDE-101c"
				+ "\n913 $Sest$ipt$aCrüger, Johann: Praxis pietatis melica ...$0301034354";

		System.err.println(isNormIdentical(oldR, newR));
		System.err.println(normalize(newR));
		System.err.println();
		System.err.println(normalize(oldR));

	}
}
