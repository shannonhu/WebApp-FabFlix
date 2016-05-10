package parser;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MainParser extends DefaultHandler {

	private boolean bFilm = false;
	private boolean bTitle = false;
	private boolean bYear = false;
	private boolean bdirk=false;
	private boolean bDirector = false;
	private boolean bGenre = false;
	
	private Main tempMovies_genres;
	private MainQuery query;
	
	public MainParser(){
		query = new MainQuery();			 
	}
	public void startParsing() {		
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp= spf.newSAXParser();			
			query.initializeHashMaps();		
			System.out.println("Star parsing main243.xml.");
			sp.parse("./source_XML_parsing/mains243.xml", this);
			System.out.println("Parsing main243.xml finished.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("film")) {

			tempMovies_genres = new Main();
			bFilm = true;

		} else if (qName.equalsIgnoreCase("t")&& bFilm)
			bTitle = true;
		else if (qName.equalsIgnoreCase("year")&& bFilm)
			bYear = true;
		else if (qName.equalsIgnoreCase("dirn")&& bFilm)
			bDirector = true;
		else if (qName.equalsIgnoreCase("cat")&& bFilm)
			bGenre = true;
		else if (qName.equalsIgnoreCase("dirk")&& bFilm)
			bdirk = true;
		
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (qName.equalsIgnoreCase("film")) {

			try {

				query.insertIntoTable(tempMovies_genres);
				query.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
			tempMovies_genres = null;
			bFilm = false;
		}

	}

	public void characters(char[] ch, int start, int length) throws SAXException {

		if (bTitle) {

			tempMovies_genres.setTitle(new String(ch, start, length).trim());
			bTitle = false;

		}

		else if (bYear) {

			if (isYear(new String(ch, start, length)))
				tempMovies_genres.setYear(Integer.parseInt(new String(ch, start, length).trim()));
			else
				tempMovies_genres.setYear(-1);
			bYear = false;

		}

		else if (bDirector) {
			tempMovies_genres.setDirectors(new String(ch, start, length).trim());
			bDirector = false;
		}

		else if (bGenre) {
			tempMovies_genres.setGenres(new String(ch, start, length).trim());
			bGenre = false;
		}
		else if (bdirk) {
			if(new String(ch, start, length).trim().equals("R"))
				bDirector =false;
		}
		bdirk=false;
	}

	private boolean isYear(String year) {
		for (int i = 0; i < year.length(); i++) {
			if (!Character.isDigit(year.charAt(i)))
				return false;
		}
		return true;

	}
}
