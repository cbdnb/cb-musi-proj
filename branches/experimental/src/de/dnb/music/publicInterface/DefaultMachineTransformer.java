package de.dnb.music.publicInterface;

import utils.TitleUtils;
import de.dnb.gnd.utils.WorkUtils;
import de.dnb.music.publicInterface.Constants.SetOfRules;

public class DefaultMachineTransformer extends DefaultRecordTransformer {

	public DefaultMachineTransformer() {
		forceTotalCount = false;
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
		// TODO Auto-generated method stub

	}

}
