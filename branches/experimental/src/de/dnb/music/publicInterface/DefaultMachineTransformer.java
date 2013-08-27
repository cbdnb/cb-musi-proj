package de.dnb.music.publicInterface;

import utils.TitleUtils;
import de.dnb.music.publicInterface.Constants.SetOfRules;

public class DefaultMachineTransformer extends DefaultRecordTransformer {

	public DefaultMachineTransformer() {
		forceTotalCount = false;
	}

	@Override
	protected void makeNew130Comment() {
		SetOfRules rules = getRules();
		if (rules == SetOfRules.RAK)
			if (Constants.KOM_NACH_2003.equals(actualCommentStr))
				actualCommentStr = Constants.KOM_MASCHINELL_NACH_2003;
			else
				actualCommentStr = Constants.KOM_MASCHINELL_VOR_2003;
		else {
			actualCommentStr = Constants.KOM_MASCHINELL;
		}
		keepOldComments = false;
	}

	@Override
	protected void make130titleSubs() {
		String rak913 = getTitleFrom913();
		String rakSynth = TitleUtils.getRAK(actualMusicTitle);
		boolean coincidentWithRAK913 = rak913.equals(rakSynth);
		if (coincidentWithRAK913)
			super.make130titleSubs();
		else {
			titleSubs = actualLine.getSubfields();
			unusedSubs.clear();
			actualCommentStr = null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
