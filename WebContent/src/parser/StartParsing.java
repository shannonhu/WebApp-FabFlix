package parser;
public class StartParsing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long castst;
		long castet;
		long actorst;
		long actoret;
		long mainst;
		long mainet;

		
		
		
		mainst= System.currentTimeMillis();
		MainParser mainparser = new MainParser();
		mainparser.startParsing();
		mainet= System.currentTimeMillis();
		
		actorst= System.currentTimeMillis();
		ActorParser actorparser = new ActorParser();
		actorparser.startParsing();
		actoret= System.currentTimeMillis();
		
		castst= System.currentTimeMillis();
		CastParser castparser = new CastParser();
		castparser.startParsing();
		castet= System.currentTimeMillis();
	
		System.out.println("Time in Seconds for Main Parser: " + ((mainet - mainst) / 1000.0));
		System.out.println("Time in Seconds for Actor Parser: " + ((actoret - actorst) / 1000.0));
		System.out.println("Time in Seconds for Cast Parser: " + ((castet - castst) / 1000.0));
	}

	
}
