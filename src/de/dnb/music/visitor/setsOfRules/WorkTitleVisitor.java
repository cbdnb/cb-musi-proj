package de.dnb.music.visitor.setsOfRules;

import utils.TitleUtils;
import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.OpusNumber;
import de.dnb.music.additionalInformation.Qualifier;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.additionalInformation.ThematicIndexNumber;
import de.dnb.music.genre.Genre.Numeri;
import de.dnb.music.genre.GenreList;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.title.Arrangement;
import de.dnb.music.title.FormalTitle;
import de.dnb.music.title.IndividualTitle;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.title.PartOfWork;
import de.dnb.music.version.ParseVersion;
import de.dnb.music.version.Version;
import de.dnb.music.version.VersionDB;
import de.dnb.music.visitor.Visitor;

public class WorkTitleVisitor extends Visitor {

	public WorkTitleVisitor(AbstractParticleFactory factory) {
		this.factory = factory;
	}

	protected AbstractParticleFactory factory;

	/**
	 * 
	 * Beim Durchwandern des Baumes treten mehrere Zustände auf, die 
	 * Auswirkung auf die von der Factory zu besorgenden Partikel haben.
	 * Ausserdem können diese Informationen in anderen Zusammenhängen
	 * hilfreich werden (relationierte Werke). Sollte die Zahl der Zustände
	 * zu gross werden, müsste man auf das State-Pattern umsteigen.
	 * 
	 * Die Zustansübergänge sind in der Regel:
	 * 
	 * 
	 * INITIAL	--> TITLE --> FIRST_PART --> FOLLOWING_PARTS --> VERSION --
	 * 					|		|_________________________________î  î		|
	 * 					|____________________________________________|		|
	 * 																		|
	 * 				  										ARRANGEMENT <---
	 *
	 *	Der Zustandswechsel erfolgt immer bei Eintritt in Title, also beim
	 *	Aufruf von visit(IndividualTitle) oder visit(FormalTitle), oder
	 *	beim Aufruf von visit(Version).
	 *
	 *	Bei Version wird berücksichtig, ob das 1., 2. oder 3. (oder höher)
	 *	Element vorliegt. Vor dem ersten Element ist kein Präfix, vor dem
	 *	2. Element ist ein " " und vor allen weiteren Elementen ist ein ", ".
	 *
	 */
	public enum States {
		IITIAL, TITLE, FIRST_PART, FOLLOWING_PARTS, VERSION, ARRANGEMENT
	}

	protected int numberOfVersionElement = 0;

	protected String getVersionElementPrefix() {
		if (numberOfVersionElement <= 0)
			throw new IllegalStateException("numberOfVersionElement falsch");
		if (numberOfVersionElement == 1)
			return "";
		else if (numberOfVersionElement == 2)
			return " ";
		else
			return ", ";
	}

	protected States state = States.IITIAL;

	/**
	 * Für weitere Auswertung bei relationierten Werken.
	 * @return	Endzustand nach Traversierung.
	 */
	public States getState() {
		return state;
	}

	/**
	 * Weiterer Zustand der festgehalten werden muss (für preGenre...).
	 */
	protected boolean isIndividualTitle = false;

	/**
	 * Erster bis vorletzter Teil bei Werkteilen und Fassungen. Wird benutzt, 
	 * um relationierte Werke zu generieren.
	 */
	protected String firstComponents = "";

	protected String lastComponent = "";

	@Override
	public String toString() {
		return firstComponents + lastComponent;
	}

	public String relatedWork() {
		return firstComponents;
	}

	protected void enterTitle() {
		firstComponents += lastComponent;
		lastComponent = "";

		switch (state) {
		case IITIAL:
			state = States.TITLE;
			lastComponent += factory.getPreTitle();
			break;
		case TITLE:
			state = States.FIRST_PART;
			lastComponent += factory.getPreTitleFirstPart();
			break;
		case FIRST_PART:
		case FOLLOWING_PARTS:
			state = States.FOLLOWING_PARTS;
			lastComponent += factory.getPreTitleFollowingPts();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean visit(FormalTitle formalTitle) {
		isIndividualTitle = false;
		enterTitle();
		return true;
	}

	@Override
	public boolean visit(IndividualTitle individualTitle) {
		isIndividualTitle = true;
		enterTitle();
		lastComponent += individualTitle.getIndividualTitle();
		return true;
	}

	@Override
	public boolean visit(GenreList genreList) {
		if (isIndividualTitle)
			lastComponent +=
				factory.getPreGenreIndiv()
					+ genreList.toString(factory.getPreferredNumerusIndiv());
		else { // Formalsachtitel
			lastComponent += factory.getPreGenreFormal();
			switch (state) {
			case TITLE:
			case IITIAL:
				lastComponent +=
					genreList.toString(factory.getPreferredNumerus());
				break;
			case FIRST_PART:
			case FOLLOWING_PARTS:
				lastComponent +=
					genreList.toString(factory.getPreferredNumerusParts());
				break;
			case VERSION:
				numberOfVersionElement++;
				lastComponent +=
					getVersionElementPrefix()
						+ genreList.toString(Numeri.SINGULAR);
				break;
			default:
				break;
			}
		}

		// die Kinder nicht besuchen:
		return false;
	}

	@Override
	public boolean visit(InstrumentationList instrumentationList) {
		if (state == States.VERSION) {
			numberOfVersionElement++;
			lastComponent += getVersionElementPrefix();
		} else {

			lastComponent += factory.getPreInstrList();
		}
		return true;
	}

	boolean isfirstInstrumentOfVersion = true;

	@Override
	public void visit(Instrument instrument) {
		String inStr = instrument.toString(factory.getAbbreviationMode());
		if (inStr.contains("hdg") || inStr.contains("linke")
			|| inStr.contains("händig"))
			lastComponent += " ";
		else {
			if (state == States.VERSION) {
				if (isfirstInstrumentOfVersion)
					isfirstInstrumentOfVersion = false;
				else
					lastComponent += " ";
			} else
				lastComponent += factory.getPreInstrumnt();
		}
		lastComponent += inStr;
		int count = instrument.getCount();
		if (count > 1) {
			lastComponent += factory.getPreNumberList();
			for (int i = 1; i < count; i++) {
				lastComponent += i + factory.getBtwNumber();
			}
			lastComponent += count;
		}

	}

	@Override
	public void visit(DateOfComposition dateOfComposition) {
		if (state == States.VERSION) {
			numberOfVersionElement++;
			lastComponent += getVersionElementPrefix() + dateOfComposition;
		} else
			lastComponent +=
				factory.getPreYear() + dateOfComposition
					+ factory.getPostYear();
	}

	@Override
	public void visit(Qualifier qualifier) {
		lastComponent +=
			factory.getPreQualifier() + qualifier + factory.getPostQualifier();
	}

	@Override
	public void visit(Key key) {
		switch (state) {
		case FIRST_PART:
		case FOLLOWING_PARTS:
			lastComponent += factory.getPreKeyParts() + key;
			break;
		case IITIAL:
		case TITLE:
			lastComponent += factory.getPreKey() + key;
			break;
		case VERSION:
			numberOfVersionElement++;
			lastComponent += getVersionElementPrefix() + key;
			break;

		default:
			break;
		}

	}

	@Override
	public void visit(OpusNumber opusNumber) {
		switch (state) {
		case FIRST_PART:
		case FOLLOWING_PARTS:
			lastComponent += factory.getPreOpusParts() + opusNumber;
			break;
		case IITIAL:
		case TITLE:
			lastComponent += factory.getPreOpus() + opusNumber;
			break;
		case VERSION:
			numberOfVersionElement++;
			lastComponent += getVersionElementPrefix() + opusNumber;
			break;
		default:
			break;
		}

	}

	@Override
	public void visit(SerialNumber serialNumber) {
		switch (state) {
		case FIRST_PART:
		case FOLLOWING_PARTS:
			lastComponent += factory.getPreSerNumParts() + serialNumber;
			break;
		case IITIAL:
		case TITLE:
			lastComponent += factory.getPreSerNum() + serialNumber;
			break;
		case VERSION:
			numberOfVersionElement++;
			lastComponent += getVersionElementPrefix() + serialNumber;
			break;
		default:
			break;
		}

	}

	@Override
	public void visit(ThematicIndexNumber thematicIndexNumber) {
		switch (state) {
		case FIRST_PART:
		case FOLLOWING_PARTS:
			lastComponent += factory.getPreThIdxParts() + thematicIndexNumber;
			break;
		case IITIAL:
		case TITLE:
			lastComponent += factory.getPreThIdx() + thematicIndexNumber;
			break;
		case VERSION:
			numberOfVersionElement++;
			lastComponent += getVersionElementPrefix() + thematicIndexNumber;
			break;
		default:
			break;
		}

	}

	@Override
	public boolean visit(PartOfWork partOfWork) {
		lastComponent += factory.getPreParts();
		// vor FIRST_PART setzen:
		state = States.TITLE;
		return true;
	}

	@Override
	public void leave(PartOfWork partOfWork) {
		lastComponent += factory.getPostParts();
	}

	@Override
	public boolean visit(Version version) {
		
		state = States.VERSION;
		isIndividualTitle = false;
		firstComponents += lastComponent;
		lastComponent = factory.getPreVersion();
		//		if (version.getMatch() != null) {
		char fallgr = version.getFallgruppeParagraphM511();
		int untergr = version.getUntergruppe();
		if (fallgr == '$' || (fallgr == 'c' && untergr == 3)) {
			lastComponent += version.getMatch();
			return false;
		}
		if (version.getRakPhrase() != null) {
			numberOfVersionElement++;
			lastComponent += version.getRakPhrase();
		}
		return true;
	}

	@Override
	public void visit(Arrangement arrangement) {
		state = States.ARRANGEMENT;
		firstComponents += lastComponent;
		lastComponent = factory.getPreArrgmt() + arrangement;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		Version ver = ParseVersion.parse(null, "Entwurf");

		System.out.println(TitleUtils.getRAK(ver));

	}

}
