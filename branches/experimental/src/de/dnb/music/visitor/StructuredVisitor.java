package de.dnb.music.visitor;

import de.dnb.basics.applicationComponents.StringUtils;
import de.dnb.music.additionalInformation.DateOfComposition;
import de.dnb.music.additionalInformation.Key;
import de.dnb.music.additionalInformation.OpusNumber;
import de.dnb.music.additionalInformation.Qualifier;
import de.dnb.music.additionalInformation.SerialNumber;
import de.dnb.music.additionalInformation.ThematicIndexNumber;
import de.dnb.music.genre.Genre;
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

public class StructuredVisitor extends Visitor {

	private String structured = "";

	@Override
	public String toString() {
		return structured;
	}

	private int tabs = 0;
	private String indentation = "";

	private void increaseIndentation() {
		tabs++;
		if (indentation.length() == 0)
			indentation = "\n\t";
		else
			indentation = indentation + '\t';
	}

	private void decreaseIndentation() {
		tabs--;
		indentation = "\n" + StringUtils.padding(tabs, '\t');
	}

	public boolean visit(IndividualTitle individualTitle) {
		structured +=
			indentation + "Individualtitel: "
				+ individualTitle.getIndividualTitle();
		increaseIndentation();
		return true;
	}

	public void leave(IndividualTitle individualTitle) {
		decreaseIndentation();
	}

	public boolean visit(FormalTitle formalTitle) {
		structured += indentation + "Formalsachtitel:";
		increaseIndentation();
		return true;
	}

	public void leave(FormalTitle formalTitle) {
		decreaseIndentation();
	}

	public boolean visit(Version version) {

		structured += '\n' + indentation;

		switch (version.getFallgruppeParagraphM511()) {
		case 'a':
			structured += "Neufassung als";
			break;

		case 'b':
			structured += "Fassung als";
			break;

		case 'c':
			switch (version.getUntergruppe()) {
			case 1:
				structured += version.getRakPhrase() + " aus dem";
				break;
			case 2:
				structured += version.getRakPhrase() + " für die";
				break;
			case 3:
				structured += version.getRakPhrase() + version.getRest();
				break;
			case 4:
				structured += version.getRakPhrase() + " für die";
				break;
			case 5:
				structured += version.getRakPhrase() + version.getRest();
				break;
			default:
				break;
			}
			break;

		case 'e':
			switch (version.getUntergruppe()) {
			case 1:
				structured += "Fassung (altes Regelwerk)  aus dem";
				break;
			case 2:
				structured += "Fassung (altes Regelwerk) für die";
				break;
			default:
				break;
			} // case 'e'
			break;

		case '$':
			structured += "Fassung aus $s: " + version.getMatch();
			break;
		default:
			break;
		}
		increaseIndentation();
		return true;
	}

	public void leave(Version version) {
		decreaseIndentation();
	}

	public void visit(DateOfComposition dateOfComposition) {
		structured +=
			indentation + "Entstehungsjahr: " + dateOfComposition.getDate();
	}

	public void visit(Key key) {
		structured += indentation + "Tonart: " + key;
	}

	public void visit(OpusNumber opusNumber) {
		structured +=
			indentation + "Op.-Zählung: " + opusNumber.getOp() + " "
				+ opusNumber.getOpusNumber();
	}

	public void visit(SerialNumber serialNumber) {
		structured +=
			indentation + "Sonst. Z: " + serialNumber.getPhrase()
				+ serialNumber.getSerialNumber();
	}

	public void visit(Qualifier qualifier) {
		structured += indentation + "Homonymzusatz: " + qualifier.toString();
	}

	public void visit(ThematicIndexNumber thematicIndexNumber) {
		structured +=
			indentation + "Werkverz: " + thematicIndexNumber.getAbbreviation()
				+ " " + thematicIndexNumber.getNumber();
	}

	public boolean visit(GenreList genreList) {
		structured += indentation + "Gattungen:";
		increaseIndentation();
		return true;
	}

	public void leave(GenreList genreList) {
		decreaseIndentation();
	}

	public void visit(Genre genre) {
		structured += indentation + genre.getSwd();
	}

	public boolean visit(InstrumentationList instrumentationList) {
		structured += indentation + "Besetzung:";
		increaseIndentation();
		return true;
	}

	public void leave(InstrumentationList instrumentationList) {
		decreaseIndentation();
	}

	public void visit(Instrument instrument) {
		structured +=
			indentation + instrument.getCount() + "-mal " + instrument.getSwd();
	}

	public void visit(Arrangement arrangement) {
		structured +=
			'\n' + indentation + "Bearbeitungsvermerk: "
				+ arrangement.toString();
	}

	public boolean visit(PartOfWork partOfWork) {
		structured += '\n' + indentation + "Werkteile:";
		increaseIndentation();
		return true;
	}

	public void leave(PartOfWork partOfWork) {
		decreaseIndentation();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parse(null,
					"Adagio und Fuge, Vl, 1234 <Adagio Vl, aa, dio>. "
						+ "Fuge, 1234" + " / Arr.");
		StructuredVisitor vis = new StructuredVisitor();
		mt.accept(vis);

		System.out.println(vis);

	}

}
