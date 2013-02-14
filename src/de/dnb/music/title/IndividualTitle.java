package de.dnb.music.title;

import de.dnb.music.visitor.Visitor;

public class IndividualTitle extends MusicTitle {
	
	public IndividualTitle(IndividualTitle it) {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public IndividualTitle() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit(this);
		visitChildren(visitor);
		visitor.leave(this);		
	}

	String individualTitle = "";


	public final String getIndividualTitle() {
		return individualTitle;
	}

	public static void main(final String[] args) {
	}

	@Override
	public MusicTitle clone() {
		// TODO Auto-generated method stub
		return new IndividualTitle(this);
	}

}
