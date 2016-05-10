package parser;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.*;
import java.util.HashSet;

public class CastParser extends DefaultHandler {

	private boolean bfilmc = false;
	private boolean bdirfilms = false;
	private boolean bis = false;
	private boolean ba = false;
	private boolean bt = false;

	private int count=1;
	private int bcon=16;
	PreparedStatement insertStatement;
	
	private String fn;
	private String ln;
	private String dir;
	private String title;

	
	Cast tempcast;
	HashSet<Cast> ainm;
	Connection conn;

	public void startParsing() {
		try {
			tempcast = new Cast();
			ainm = new HashSet<Cast>();
			conn = DBconnect.get(conn);
			initializeHashTable();

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			String insert = "INSERT INTO stars_in_movies(movie_id,star_id) VALUES (?, ?); ";
			insertStatement = conn.prepareStatement(insert);
			System.out.println("Start parsing casts124.xml.");
			sp.parse("./source_XML_parsing/casts124.xml", this);
			insertStatement.executeBatch();
			conn.commit();
			System.out.println("Parsing casts124.xml finished.");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void initializeHashTable() {

		try {
			Statement statement = conn.createStatement();
			String getAllActors = "SELECT * FROM stars_in_movies; ";
			ResultSet rs = statement.executeQuery(getAllActors);

			while (rs.next()) {
				tempcast.setmovidID(rs.getInt("movie_id"));
				tempcast.setstarID(rs.getInt("star_id"));
				ainm.add(tempcast);
			}

			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.getStackTrace();
		}

	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("dirfilms"))
			bdirfilms = true;
		else if (qName.equalsIgnoreCase("is") && bdirfilms)
			bis = true;
		else if (qName.equalsIgnoreCase("filmc") && bdirfilms)
			bfilmc = true;
		else if (qName.equalsIgnoreCase("t") && bfilmc)
			bt = true;
		else if (qName.equalsIgnoreCase("a") && bfilmc)
			ba = true;
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (qName.equalsIgnoreCase("dirfilms"))
			bdirfilms = false;
		else if (qName.equalsIgnoreCase("is"))
			bis = false;
		else if (qName.equalsIgnoreCase("filmc")) {
			bfilmc = false;
			getid_insert(fn, ln, dir, title);

		} else if (qName.equalsIgnoreCase("a") && !bt) {
			ba = false;
			bfilmc = false;
		} else if (qName.equalsIgnoreCase("t"))
			bt = false;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {

		if (bis) {
			dir = new String(ch, start, length).trim();
		} else if (bt) {
			title = new String(ch, start, length).trim();
		} else if (ba) {

			String name = new String(ch, start, length).trim();
			
			if (!name.equals("sa")) {
				String fl[] = name.split(" ");
				if (fl.length == 2) {
					fn = fl[0];
					ln = fl[1];
				}

			}
		}
	}

	private void getid_insert(String fn, String ln, String dir, String title) {
		try {
			String selectMID = "Select ID from movies where title=? and director=?;";
			PreparedStatement pstate = conn.prepareStatement(selectMID);
			pstate.setString(1, title);
			pstate.setString(2, dir);
			ResultSet rs = pstate.executeQuery();
			
			if (rs.next())
				tempcast.setmovidID(rs.getInt(1));
			else
				return;

			String selectAID = "Select ID from stars where first_name=? and last_name=?;";
			pstate = conn.prepareStatement(selectAID);
			pstate.setString(1, fn);
			pstate.setString(2, ln);
			rs = pstate.executeQuery();

			if (rs.next())
				tempcast.setstarID(rs.getInt(1));
			else
				return;
			
			pstate.close();

			if (ainm.add(tempcast)) {

				try {
					
					insertStatement.setInt(1, tempcast.getmovieID());
					insertStatement.setInt(2, tempcast.getstarID());
					insertStatement.addBatch();
					
					if(count++%bcon==0){
						insertStatement.executeBatch();
						conn.commit();
					}	
					} catch (SQLException e) {
					e.getStackTrace();
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
