package cp125.chat;

import java.util.HashMap;
import java.util.Map;

/**
 * A fixed database of users of our chat applicaton.  Names inspired
 * in part by http://en.wikipedia.org/wiki/Alice_and_Bob
 *
 * @see User
 */

public class Users {

	static public final Map<String,User> ALL = new HashMap<String,User>();

	// private so outside callers may not add to our user group...
	static private void add( String id, String first, String last ) {
		ALL.put( id, new User( first, last, id ) );
	}
	
	static {
		add( "alice", "Alice", "Good" );
		add( "bob", "Robert", "Good" );
		add( "carol", "CarolAnn", "GoodToo" );
		add( "dan", "Dare", "GoodAlso" );
		add( "eve", "Eves", "Dropper" );
		add( "faythe", "Trusted", "Advisor" );
		add( "gerry", "Gerry", "Terry" );
		add( "hamble", "Hamble", "Doll" );
		add( "ian", "Ian", "Curtis" );
		add( "jemima", "Jemima", "Doll" );
		add( "klark", "Klark", "Kent" );
		add( "laurie", "Laurie", "Driver" );
		add( "mallory", "Mallory", "EvilAttacker" );
	}
}

// eof
