package de.dnb.music.visitor.setsOfRules;

import utils.TitleUtils;
import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.OpusNumber;
import de.dnb.music.additionalInformation.Qualifier;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.additionalInformation.ThematicIndexNumber;
import de.dnb.music.genre.GenreList;
import de.dnb.music.mediumOfPerformance.Instrument;
import de.dnb.music.mediumOfPerformance.InstrumentationList;
import de.dnb.music.title.Arrangement;
import de.dnb.music.title.FormalTitle;
import de.dnb.music.title.IndividualTitle;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.title.PartOfWork;
import de.dnb.music.version.Version;
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
	 * INITIAL	--> TITLE --> FIRST_PART --> FOLLOWING_PARTS --> VERSION
	 * 					|		|_________________________________î  î
	 * 					|____________________________________________|
	 * 				  
	 *
	 *	Der Zustandswechsel erfolgt immer bei Eintritt in Title, also beim
	 *	Aufruf von visit(IndividualTitle) oder visit(FormalTitle), oder
	 *	beim Aufruf von visit(Version).
	 *
	 */
	public enum States {
		IITIAL, TITLE, FIRST_PART, FOLLOWING_PARTS, VERSION, ARRANGEMENT
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
	public void visit(FormalTitle formalTitle) {
		isIndividualTitle = false;
		enterTitle();

	}

	@Override
	public void visit(IndividualTitle individualTitle) {
		isIndividualTitle = true;
		enterTitle();
		lastComponent += individualTitle.getIndividualTitle();

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
			default:
				break;
			}
		}

		// die Kinder nicht besuchen:
		return false;
	}

	@Override
	public boolean visit(InstrumentationList instrumentationList) {
		lastComponent += factory.getPreInstrList();
		return true;
	}

	@Override
	public void visit(Instrument instrument) {
		String inStr = instrument.toString(factory.getAbbreviationMode());
		if (inStr.contains("hdg") || inStr.contains("linke")
			|| inStr.contains("händig"))
			lastComponent += " ";
		else
			lastComponent += factory.getPreInstrumnt();
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
		lastComponent +=
			factory.getPreYear() + dateOfComposition + factory.getPostYear();
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
		firstComponents += lastComponent;

		//--------
		String versionStr = "";
		switch (version.getFallgruppeParagraphM511()) {
		case 'a':
			versionStr = version.getAdditionalInformation().toString();
			break;

		case 'b':
			switch (version.getUntergruppe()) {
			case 1:
				versionStr = TitleUtils.getRAK(version.getGenreList());
				break;
			case 2:
				versionStr =
					TitleUtils.getRAK(version.getInstrumentationList());
				break;
			case 3:
			case 4:
			case 5:
				versionStr =
					TitleUtils.getRAK(version.getAdditionalInformation());
				break;
			default:
				throw new IllegalStateException(
						"Fallgruppe oder Untergruppe unbekannt");
			}
			break;

		case 'c':
			switch (version.getUntergruppe()) {
			case 1:
				versionStr += version.getRakPhrase() + " aus dem";
				break;
			case 2:
				versionStr += version.getRakPhrase() + " für die";
				break;
			case 3:
				versionStr += version.getRakPhrase() + version.getRest();
				break;
			case 4:
				versionStr += version.getRakPhrase() + " für die";
				break;
			default:
				break;
			}
			break;

		case 'e':
			switch (version.getUntergruppe()) {
			case 1:
				versionStr += "Fassung (altes Regelwerk)  aus dem";
				break;
			case 2:
				versionStr += "Fassung (altes Regelwerk) für die";
				break;
			default:
				break;
			} // case 'e'
			break;

		case '$':
			versionStr = version.getMatch();
			break;
		default:
			break;
		}
		lastComponent = factory.getPreVersion() + versionStr;
		System.err.println(lastComponent);
		//--------
		lastComponent = factory.getPreVersion() + version.getMatch();
		return false;
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
		MusicTitle mt =
			ParseMusicTitle.parseFullRAK(null, "Quartette <Quartett 1234, aa>");
		WorkTitleVisitor vis = new WorkTitleVisitor(new RAKParticleFactory());
		mt.accept(vis);

		System.out.println(vis);
		System.out.println(vis.relatedWork());
		System.out.println(vis.state);

	}

}
