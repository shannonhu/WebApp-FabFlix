package parser;


public class Main {
	
	private String title;
	private int year;
	private String directors;
	private String genres;
	private int movieID;
	private int genreID;

	public Main() {
		movieID = -1;
		genreID = -1;
	}

	public Main(String title, int year, String directors) {
		this.movieID = -1;
		this.genreID = -1;
		this.title = title;
		this.year = year;
		this.directors = directors;
	}

	public Main(String genres) {
		this.genres = genres;
		this.movieID = -1;
		this.genreID = -1;
		this.year = -1;
	}

	public Main(int movieID, int genreID) {
		this.movieID = movieID;
		this.genreID = genreID;
		this.year = -1;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDirectors() {
		return directors;
	}

	public void setDirectors(String directors) {
		this.directors = directors;
	}

	public String getGenres() {
		return genres;
	}

	public void setGenres(String genres) {
		this.genres = genres;
	}

	public int getMovieID() {
		return movieID;
	}

	public void setMovieID(int movieID) {
		this.movieID = movieID;
	}

	public int getGenreID() {
		return genreID;
	}

	public void setGenreID(int genreID) {
		this.genreID = genreID;
	}

	@Override
	public int hashCode() {

		int hash = 7;
		hash = 31 * hash + (null == this.title ? 0 : this.title.hashCode());
		hash = 31 * hash + (null == this.directors ? 0 : this.directors.hashCode());
		hash = 31 * hash + (null == this.genres ? 0 : this.genres.hashCode());
		hash = 31 * hash + this.year;
		hash = 31 * hash + this.genreID;
		hash = 31 * hash + this.movieID;
		return hash;
	}

	@Override
	public boolean equals(Object o) {
		Main comparison = (Main) o;

		if (this.title != null && comparison.getTitle() != null && !this.title.equals(comparison.getTitle()))
			return false;
		if (this.year != comparison.getYear())
			return false;
		if (this.directors != null && comparison.getDirectors() != null
				&& !this.directors.equals(comparison.getDirectors()))
			return false;
		if (this.genres != null && comparison.getGenres() != null && !this.genres.equals(comparison.getGenres()))
			return false;
		if (this.genreID != comparison.getGenreID())
			return false;
		if (this.movieID != comparison.getMovieID())
			return false;
		//
		return true;
	}
}
