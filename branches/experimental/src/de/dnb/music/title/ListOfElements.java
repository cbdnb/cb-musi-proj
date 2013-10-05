package de.dnb.music.title;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import applikationsbausteine.ListUtils;
import applikationsbausteine.RangeCheckUtils;
import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public abstract class ListOfElements<T extends TitleElement> {

	protected ArrayList<T> children = new ArrayList<T>();

	public ListOfElements() {
		super();
	}

	public List<T> getChildren() {
		return Collections.unmodifiableList(children);
	}

	/**
	 * Fügt der Liste einen weiteren, hierarchisch untergeordneten Teil
	 * hinzu.
	 * 
	 * @param elem	nicht null.
	 */
	public void add(final T elem) {
		RangeCheckUtils.assertReferenceParamNotNull("elem", elem);
		children.add(elem);
	}

	/**
	 * Fügt der Liste einen weitere, hierarchisch untergeordneten Teile,
	 * die in einer anderen Liste vorliegen, hinzu.
	 * 
	 * @param other	nicht null.
	 */
	public void addAll(final ListOfElements<T> other) {
		RangeCheckUtils.assertReferenceParamNotNull("other", other);
		List<T> otherTitles = other.getChildren();
		for (T musicTitle : otherTitles) {
			add(musicTitle);
		}
	}

	public T getLast() {
		if (children.isEmpty())
			throw new IllegalStateException("Liste ist leer");
		return ListUtils.getLast(children);
	}

	protected void visitChildren(Visitor visitor) {
		for (T elem : getChildren()) {
			elem.accept(visitor);
		}
	}

	public void addToTitle(MusicTitle title) {
		RangeCheckUtils.assertReferenceParamNotNull("title", title);
		for (T child : children) {
			child.addToTitle(title);
		}
	}

}