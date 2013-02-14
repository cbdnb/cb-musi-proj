package de.dnb.music.title;

import de.dnb.music.visitor.TitleElement;
import de.dnb.music.visitor.Visitor;

public class Comment implements TitleElement{
	
	private String comment;

	public Comment(String comment) {
		
		if(comment == null)
			throw new IllegalArgumentException("kein Kommentar");
		this.comment = comment;
	}

	@Override
	public String toString() {		
		return comment;
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);		
	}

	

}
