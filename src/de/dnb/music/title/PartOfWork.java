package de.dnb.music.title;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import applikationsbausteine.ListUtils;
import applikationsbausteine.RangeCheckUtils;

import utils.StringUtils;
import utils.TitleUtils;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

/**
 * Eine Liste von Musiktiteln, die eine hierarchisch gestufte Folge
 * (Teil eines Teils eines Teils ...) von Musiktiteln enthält.
 * 
 * @author baumann
 *
 */
public class PartOfWork implements TitleElement {

	private final List<MusicTitle> partsOfWork;
	private LinkedList<String> assumedParts;

	public final List<MusicTitle> getPartsOfWork() {
		return Collections.unmodifiableList(partsOfWork);
	}

	/**
	 * Fügt der Liste einen weiteren, hierarchisch untergeordneten Teil
	 * hinzu.
	 * 
	 * @param mt	nicht null.
	 */
	public final void addPartOfWork(final MusicTitle mt) {
		RangeCheckUtils.assertReferenceParamNotNull("mt", mt);
		partsOfWork.add(mt);
	}

	/**
	 * Fügt der Liste einen weitere, hierarchisch untergeordneten Teile,
	 * die in einer anderen Liste vorliegen, hinzu.
	 * 
	 * @param otherParts	nicht null.
	 */
	public final void addPartsOfWork(final PartOfWork otherParts) {
		RangeCheckUtils.assertReferenceParamNotNull("otherParts", otherParts);
		List<MusicTitle> otherTitles = otherParts.getPartsOfWork();
		for (MusicTitle musicTitle : otherTitles) {
			addPartOfWork(musicTitle);
		}
	}

	/**
	 * @param titleList Liste der Werkteile.
	 */
	public PartOfWork(final List<MusicTitle> titleList) {
		RangeCheckUtils.assertCollectionParamNotNullOrEmpty("titleList",
				titleList);
		this.partsOfWork = titleList;
	}

	//@formatter:off
	/**
	 * @param titles Liste der Werkteile.
	 */
	public PartOfWork(final MusicTitle... titles) {
		this(Arrays.asList(titles));
	}
	//@formatter:on

	public MusicTitle getLastPart() {
		if (partsOfWork.isEmpty())
			throw new IllegalStateException("Liste der Werkteile ist leer");
		return ListUtils.getLast(partsOfWork);
	}

	/**
	 * Findet in der Liste vermuteteTeile den längsten auf den Anfang 
	 * passenden Formalsachtitel und verkürzt die Liste entsprechend.
	 * 
	 * @return	längsten passenden Formalsachtitel oder null.
	 */
	private FormalTitle findLongestMatchingFT() {
		FormalTitle fTitle = null;
		String ft = "";
		for (Iterator<String> iterator = assumedParts.iterator(); iterator
				.hasNext();) {
			String possiblePart = iterator.next();
			ft += possiblePart;
			FormalTitle tryTitle = ParseFormalTitle.parse(null, ft);
			if (tryTitle != null) {
				iterator.remove();
				fTitle = tryTitle;
				/*
				 * Die nachfolgende Operation ist ein wenig problematisch und
				 * führt nicht in allen Fällen zum gewünschten Ergebnis:
				 * In Werkverzeichnis- und Opusangaben wurde vor 2003 ein
				 * Blank nach dem Komma gesetzt, heute nicht mehr. Demzufolge
				 * wird bei älteren Titeln der String 
				 * durch StringUtils.splitPartsOfWork(String)
				 * in mehrere Teile zerbrochen. Diese werden in der Schleife
				 * hier solange wieder zusammengefügt, wie ein FST erkannt
				 * wird. Da aber 
				 * ParseAdditionalInformation.parse(String, String, boolean)
				 * nach einer WV- oder Opus-Angabe alle beliebigen folgenden
				 * Zeichen akzeptiert, wird im Falle einer WV- oder Opus-Angabe
				 * die gesamte vermuteteTeile-Liste als Formalsachtitel
				 * erkannt. Um das zu vermeiden, wird nach dem ersten 
				 * Teilstring, der zu einer erkannten Zusatzinformation
				 * führt, abgebrochen. Titel vor 2003 werden damit nicht mehr
				 * korrekt erkannt.
				 * 
				 */
				if (fTitle.containsAdditionalInformation())
					break;
				ft += ", ";
			} else
				break;
		}
		return fTitle;
	}

	/**
	 * 
	 * Zerlegt werkteilString in eine Liste von Teilen von Teilen.
	 * 
	 * @param werkteilString
	 * 
	 * Oft ist eine  Zerlegung von werkteilString möglich: ein Werkteil wie
	 * 		$pQuartett Hob 3,77, 2. Satz
	 * ist eigentlich ein Teil eines Teils eines Werkes. 
	 * Zunächst habe ich angenommen, eine einfache die Zerlegung nach
	 *  ", " aus StringUtils.splitWerkteile(String) könnte
	 * diese Informationen wieder liefern. Jedoch gibt es viele
	 * Arien mit Kommata: 
	 * 		$pIch ruf' zu dir, Herr Jesu Christ
	 * Bei denen das nicht funktioniert. Hier hilft nur ein
	 * genaueres Parsing nach Formalsachtiteln. Das erldigt der
	 * Konstruktor von Werkteil.
	 */
	public PartOfWork(final String werkteilString) {
		if (werkteilString == null)
			throw new IllegalArgumentException("Leerer Werkteilstring");
		assumedParts = StringUtils.splitPartsOfWork(werkteilString);
		partsOfWork = new LinkedList<MusicTitle>();
		FormalTitle formalT = findLongestMatchingFT();
		while (formalT != null) {
			partsOfWork.add(formalT);
			formalT = findLongestMatchingFT();
		}
		// Rest als Individualtitel auffassen
		String indivString = "";
		for (Iterator<String> iterator = assumedParts.iterator(); iterator
				.hasNext();) {
			indivString += iterator.next();
			if (iterator.hasNext()) {
				indivString += ", ";
			}
		}
		if (indivString.length() != 0)
			partsOfWork.add(ParseIndividualTitle.parse(null, indivString));

	}

	/**
	 * Wenn mehrere $p-Teilstrings vorliegen, so müssen diese nicht mehr auf-
	 * gebrochen werden. 
	 * 
	 * @param titleStringList
	 */
	public PartOfWork(final ArrayList<String> titleStringList) {
		if (titleStringList == null || titleStringList.size() == 0)
			throw new IllegalArgumentException(
					"Liste muss mind. ein Element enthalten");
		LinkedList<MusicTitle> titleList = new LinkedList<MusicTitle>();
		for (String titleString : titleStringList) {
			titleList.add(ParseMusicTitle.parseSimpleTitle(null, titleString));
		}
		this.partsOfWork = titleList;
	}

	@Override
	public final void accept(Visitor visitor) {
		boolean visitChildren = visitor.visit(this);
		if (visitChildren)
			for (MusicTitle musicTitle : partsOfWork) {
				musicTitle.accept(visitor);
			}
		visitor.leave(this);
	}

	@Override
	public final void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		if (title.containsParts()) {
			PartOfWork titlesParts = title.getPartOfWork();
			titlesParts.addPartsOfWork(this);
		} else {
			title.setPartOfWork(this);
		}
	}

	public static void main(final String[] args) {
		PartOfWork pw = new PartOfWork("Adagio KV 1, oo");
		MusicTitle mt = ParseMusicTitle.parseSimpleTitle(null, "aa");
		pw.addToTitle(mt);
		System.out.println(TitleUtils.getGND1XXPlusTag(mt));
	}

}
