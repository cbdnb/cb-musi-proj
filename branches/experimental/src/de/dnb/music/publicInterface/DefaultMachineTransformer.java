package de.dnb.music.publicInterface;

import utils.TitleUtils;
import de.dnb.gnd.parser.Record;
import de.dnb.gnd.parser.RecordParser;
import de.dnb.gnd.parser.line.Line;
import de.dnb.gnd.parser.tag.GNDTagDB;
import de.dnb.gnd.utils.GNDUtils;
import de.dnb.gnd.utils.RecordUtils;
import de.dnb.gnd.utils.WorkUtils;
import de.dnb.music.publicInterface.Constants.SetOfRules;

public class DefaultMachineTransformer extends DefaultRecordTransformer {

	public DefaultMachineTransformer() {
		forceTotalCount = false;
	}

	@Override
	protected boolean isPermitted(Record record) {
		Line heading;
		try {
			heading = GNDUtils.getHeading(record);
		} catch (IllegalStateException e) {
			return false;
		}
		String comment = GNDUtils.getFirstComment(heading);
		// muss nicht zweimal maschinell transformiert werden:
		if (Constants.COMMENTS_MACHINE.contains(comment))
			return false;

		return super.isPermitted(record);
	}

	@Override
	protected void addComposerData() {
//		if (getRules() != SetOfRules.RAK)
//			super.addComposerData();
	}

	@Override
	public void addGeneralNote(Record record) {
		// keine Aktion.
	}

	@Override
	public void addGNDClassification(Record record) {
		if (getRules() != SetOfRules.RAK)
			super.addGNDClassification(record);
	}

	@Override
	protected void makeNew130Comment() {
		if (getRules() == SetOfRules.RAK)
			if (Constants.KOM_NACH_2003.equals(actualCommentStr))
				actualCommentStr = Constants.KOM_MASCHINELL_NACH_2003;
			else
				actualCommentStr = Constants.KOM_MASCHINELL_VOR_2003;
		else {
			actualCommentStr = Constants.KOM_MASCHINELL;
		}
		keepOldComments = false;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void make130titleSubs() {
		if (getRules() == SetOfRules.RAK) {
			if (isOldRAK(actualLine)) {
				// nur $a, $p und $s
				setGlobalsOldRAK();
				makeOldRakTitleSubs();
				return;
			}
			// also nach 2003
			if (!isCoincidentWithRAK913()) {
				// noch vorsichtiger: alles belassen
				titleSubs.clear();
				titleSubs.addAll(actualLine.getSubfields());
				return;
			}
		}
		// sonst default:
		super.make130titleSubs();
	}

	/**
	 * Gibt den alten Titel, wenn nur einer vorhanden ist. Sonst wird
	 * GND angenommen und null zurückgegeben.
	 * 
	 * @return Titel oder null.
	 * 
	 */
	protected final String getTitleFrom913() {
		SetOfRules rules = getRules();
		if (rules != SetOfRules.GND)
			return WorkUtils.getOriginalTitles(oldRecord).get(0);
		else
			return null;
	}

	boolean isCoincidentWithRAK913() {
		String rak913 = getTitleFrom913();
		String rakSynth = TitleUtils.getRAK(actualMusicTitle);
		return rak913.equals(rakSynth);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DefaultRecordTransformer transformer = new DefaultMachineTransformer();
		RecordParser parser = new RecordParser();
		parser.setTagDB(GNDTagDB.getDB());
		String old =
				"130 aa\n" + "500 !11862119X!Telemann, Georg Philipp$4kom1";
		Record record = parser.parse(old);
		Record newR = transformer.transform(record);
		System.out.println(RecordUtils.toPica(newR));

	}

}
