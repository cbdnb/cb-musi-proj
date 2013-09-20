package de.dnb.music.visitor;

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
import de.dnb.music.title.ListOfElements;
import de.dnb.music.title.MusicTitle;
import de.dnb.music.title.ParseMusicTitle;
import de.dnb.music.version.Version;

public class ExampleVisitor extends Visitor {

	public boolean visit(IndividualTitle individualTitle) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(IndividualTitle)");
		return true;
	}

	public void leave(IndividualTitle individualTitle) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.leave(IndividualTitle)");
	}

	public boolean visit(FormalTitle formalTitle) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(FormalTitle)");
		return true;
	}

	public void leave(FormalTitle formalTitle) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.leave(FormalTitle)");
	}

	public boolean visit(Version version) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(Version)");
		return true;
	}

	public void leave(Version version) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.leave(Version)");
	}

	public void visit(DateOfComposition dateOfComposition) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(DateOfComposition)");
	}

	public void visit(Key key) {
		System.out.println("de.dnb.music.visitor.ExampleVisitor.visit(Key)");
	}

	public void visit(OpusNumber opusNumber) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(OpusNumber)");
	}

	public void visit(SerialNumber serialNumber) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(SerialNumber)");
	}

	public void visit(ThematicIndexNumber thematicIndexNumber) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(ThematicIndexNumber)");
	}

	@Override
	public void visit(Qualifier qualifier) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(Qualifier)");
	}

	public boolean visit(GenreList genreList) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(GenreList)");
		return true;
	}

	public void leave(GenreList genreList) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.leave(GenreList)");
	}

	public void visit(Genre genre) {
		System.out.println("de.dnb.music.visitor.ExampleVisitor.visit(Genre)");
	}

	public void visit(Instrument instrument) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(Instrument)");
	}

	public boolean visit(InstrumentationList instrumentationList) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(InstrumentationList)");
		return true;
	}

	public void leave(InstrumentationList instrumentationList) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.leave(InstrumentationList)");
	}

	public void visit(Arrangement arrangement) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(Arrangement)");
	}

	public boolean visit(ListOfElements partOfWork) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.visit(PartOfWork)");
		return true;
	}

	public void leave(ListOfElements partOfWork) {
		System.out
				.println("de.dnb.music.visitor.ExampleVisitor.leave(PartOfWork)");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MusicTitle mt =
			ParseMusicTitle.parse(null,
					"Adaagio und Fuge, Vl 1 2 Va 1 2"
						+ "<Fuge KV 5a, Durchführung 1>. Fassung Vl, Kl");
		ExampleVisitor vis = new ExampleVisitor();
		mt.accept(vis);

	}

}
