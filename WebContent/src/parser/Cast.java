package parser;


public class Cast {

	private int movieID;
	private int starID;
	
	
	public void setmovidID(int mid) {
		this.movieID=mid;
	}
	public void setstarID(int sid) {
		this.starID=sid;
	}
	public int getmovieID(){ return movieID;}
	public int getstarID() {return starID;}
	
	@Override
	public int hashCode() {

		int hash = 7;
		
		hash = 31 * hash + this.starID;
		hash = 31 * hash + this.movieID;
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		Cast comparison = (Cast) o;

		if (this.starID != comparison.getstarID())
			return false;
		if (this.movieID != comparison.getmovieID())
			return false;
		//
		return true;
	}
}
