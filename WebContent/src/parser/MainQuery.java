package parser;


import java.util.HashMap;
import java.util.HashSet;
import java.sql.*;

public class MainQuery {

	// To connect with current DB, need to initialize and keep tracking the
	// updated data
	private HashMap<Main, Integer> movieMap;
	private HashMap<Main, Integer> genreMap;
	private HashSet<Main> genresInMoviesSet;
	private Connection conn=null;
	
	public MainQuery(){

		movieMap = new HashMap<Main, Integer>();
		new HashMap<Main, Integer>();
		genreMap = new HashMap<Main, Integer>();
		genresInMoviesSet = new HashSet<Main>();

		conn=DBconnect.get(conn);
	}

	public void initializeHashMaps()
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Statement statement = conn.createStatement();
		String getAllMovies = "SELECT * FROM movies; ";
		ResultSet allMovies = statement.executeQuery(getAllMovies);
		while (allMovies.next())
			movieMap.put(new Main(allMovies.getString("title").toLowerCase().trim(), allMovies.getInt("year"),
					allMovies.getString("director").toLowerCase().trim()), allMovies.getInt("id"));

		allMovies.close();

		String getAllGenres = "SELECT * FROM genres; ";
		ResultSet allGenres = statement.executeQuery(getAllGenres);
		while (allGenres.next())
			genreMap.put(new Main(allGenres.getString("name").toLowerCase().trim()), allGenres.getInt("id"));

		allGenres.close();

		String getAllMovies_genress = "SELECT * FROM genres_in_movies; ";
		ResultSet allGenresInMovies = statement.executeQuery(getAllMovies_genress);
		while (allGenresInMovies.next())
			genresInMoviesSet
					.add(new Main(allGenresInMovies.getInt("genre_id"), allGenresInMovies.getInt("movie_id")));
		
		allGenresInMovies.close();
		statement.close();

	}

	public void insertIntoTable(Main MovieInput) throws SQLException {

		if (MovieInput.getTitle() == null || MovieInput.getTitle().equals("") ||
				 MovieInput.getYear() == -1 || MovieInput.getDirectors() == null
				|| MovieInput.getDirectors().equals("")) {
			return;
		}

		int insertMovieStatus = 0;
		if (!movieMap.containsKey(new Main(MovieInput.getTitle().toLowerCase().trim(), MovieInput.getYear(),
				MovieInput.getDirectors().toLowerCase().trim()))) {

			String insertMovie = "INSERT INTO movies (title, year, director) VALUES (?, ?, ?); ";
			PreparedStatement insertMovieStatement = conn.prepareStatement(insertMovie);
			insertMovieStatement.setString(1, MovieInput.getTitle());
			insertMovieStatement.setInt(2, MovieInput.getYear());
			insertMovieStatement.setString(3, MovieInput.getDirectors());
			insertMovieStatus = insertMovieStatement.executeUpdate();
			insertMovieStatement.close();
		}
		if (insertMovieStatus > 0) {

			String getLastKey = "SELECT LAST_INSERT_ID(); ";
			PreparedStatement getLastKeyStatement = conn.prepareStatement(getLastKey);
			ResultSet primaryKey = getLastKeyStatement.executeQuery();
			primaryKey.next();
			movieMap.put(new Main(MovieInput.getTitle().toLowerCase().trim(), MovieInput.getYear(),
					MovieInput.getDirectors().toLowerCase().trim()), primaryKey.getInt(1));
			
			primaryKey.close();
			getLastKeyStatement.close();
		}
		if (MovieInput.getGenres() == null || MovieInput.getGenres().equals("")) {

			return;
		}
		int insertGenreStatus = 0;
		if (!genreMap.containsKey(new Main(MovieInput.getGenres().toLowerCase().trim()))) {

			String insertGenre = "INSERT INTO genres (name) VALUES (?); ";
			PreparedStatement insertGenreStatement = conn.prepareStatement(insertGenre);
			insertGenreStatement.setString(1, MovieInput.getGenres());
			insertGenreStatus = insertGenreStatement.executeUpdate();
			insertGenreStatement.close();
		}

		if (insertGenreStatus > 0) {

			String getLastKey = "SELECT LAST_INSERT_ID(); ";
			PreparedStatement getLastKeyStatement = conn.prepareStatement(getLastKey);
			ResultSet primaryKey = getLastKeyStatement.executeQuery();
			primaryKey.next();
			genreMap.put(new Main(MovieInput.getGenres().toLowerCase().trim()), primaryKey.getInt(1));
			primaryKey.close();
			getLastKeyStatement.close();
		}

		int genreID = genreMap.get(new Main(MovieInput.getGenres().toLowerCase().trim()));
		int movieID = movieMap.get(new Main(MovieInput.getTitle().toLowerCase().trim(), MovieInput.getYear(),
				MovieInput.getDirectors().toLowerCase().trim()));

		int insertGenresInMoviesStatus = 0;
		if (!genresInMoviesSet.contains(new Main(genreID, movieID))) {

			String insertMovies_genres = "INSERT INTO genres_in_movies (genre_id, movie_id) VALUES (?, ?); ";
			PreparedStatement insertMovies_genresStatement = conn.prepareStatement(insertMovies_genres);
			insertMovies_genresStatement.setInt(1, genreID);
			insertMovies_genresStatement.setInt(2, movieID);
			insertGenresInMoviesStatus = insertMovies_genresStatement.executeUpdate();
			insertMovies_genresStatement.close();
		}

		if (insertGenresInMoviesStatus > 0) {

			genresInMoviesSet.add(new Main(genreID, movieID));
		}
	}

	public void commit() throws SQLException {

		conn.commit();
	}

}