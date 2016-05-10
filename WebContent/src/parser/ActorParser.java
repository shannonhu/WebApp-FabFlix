package parser;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.sql.*;
import java.util.HashSet;

public class ActorParser extends DefaultHandler {

	private int count=1;
	private int bcon=128;
	private boolean bActor = false;
	private boolean bFN = false;
	private boolean bLN = false;
	private boolean bDOB = false;
	private HashSet<Actor> actor;
	Connection conn;
	PreparedStatement insertStatement;
	private Actor tempActor;
	
	public ActorParser(){
		
		actor = new HashSet<Actor>();
		tempActor = new Actor();
		conn = DBconnect.get(conn);
		initializeHashSet();
		String insert = "INSERT INTO stars(first_name,last_name,dob) VALUES (?, ?, ?); ";
		try {
			insertStatement = conn.prepareStatement(insert);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void startParsing() {
		try {
			
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();
			System.out.println("Start parsing actor63.xml.");
			sp.parse("./source_XML_parsing/actors63.xml", this);
			insertStatement.executeBatch();
			conn.commit();
			System.out.println("Parsing actor63.xml finished.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("actor")) {
			bActor = true;
		} else if (qName.equalsIgnoreCase("familyname") && bActor)
			bLN = true;
		else if (qName.equalsIgnoreCase("firstname") && bActor)
			bFN = true;
		else if (qName.equalsIgnoreCase("dob") && bActor)
			bDOB = true;

	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		if (qName.equalsIgnoreCase("actor")) {
			try {

				insert();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			bActor = false;
		} else if (qName.equalsIgnoreCase("familyname") && bActor)
			bLN = false;
		else if (qName.equalsIgnoreCase("firstname") && bActor)
			bFN = false;
		else if (qName.equalsIgnoreCase("dob") && bActor)
			bDOB = false;
	}

	public void characters(char[] ch, int start, int length) throws SAXException {

		if (bFN) {
			tempActor.setfirstName(new String(ch, start, length).trim());
		}

		else if (bLN) {
			tempActor.setlastName(new String(ch, start, length).trim());
		}

		else if (bDOB) {
			String dobstring = new String(ch, start, length).trim();
			if (isYear(dobstring))
				tempActor.setdob(dobstring + "-01-01");
			else
				tempActor.setdob(null);
		}
	}

	private void initializeHashSet() {
		try {
			Statement statement = conn.createStatement();
			String getAllActors = "SELECT * FROM stars; ";
			ResultSet rs = statement.executeQuery(getAllActors);

			while (rs.next()) {

				tempActor.setdob(rs.getString("dob"));
				tempActor.setfirstName(rs.getString("first_name"));
				tempActor.setlastName(rs.getString("last_name"));
				actor.add(tempActor);
			}
			rs.close();
			statement.close();
		} catch (SQLException e) {
			e.getStackTrace();
		}
	}

	private void insert() {

		if (tempActor.getdob() == null || tempActor.getdob().equals("") || tempActor.getfirstName() == null
				|| tempActor.getfirstName().equals("") || tempActor.getlastName() == null
				|| tempActor.getlastName().equals(""))
			return;

		if (actor.add(tempActor)) {
			try {

				insertStatement.setString(1, tempActor.getfirstName());
				insertStatement.setString(2, tempActor.getlastName());
				insertStatement.setDate(3, Date.valueOf(tempActor.getdob()));
				insertStatement.addBatch();
				
				if(count++%bcon==0){
					insertStatement.executeBatch();
					conn.commit();
				}			
				
			} catch (SQLException e) {
				e.getStackTrace();
			}
		}
	}

	private boolean isYear(String year) {
		if (year.length() != 4)
			return false;
		for (int i = 0; i < year.length(); i++) {
			if (!Character.isDigit(year.charAt(i)))
				return false;
		}
		return true;

	}
}
