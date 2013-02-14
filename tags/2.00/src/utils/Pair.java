package utils;

public class Pair<A, B> {
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}
	

	public final A first;

	public final B second;
	
	@Override
	public String toString() {
		
		return "<"+first + ", " + second + ">";
	}
}