package cp125.chat;

import java.io.File;
import java.io.IOException;

/**
 * The entry point into this week's chat conversation analysis
 * program.
 *
 * Some prior conversations have been saved to disk, each in a '.cnv'
 * file, all in a single directory D.  To complicate things, some .cnv
 * file are bogus (empty), and have to be discarded.  Also, there are
 * some non-cnv files in D.  The goal of the assignment is to load all
 * the conversations and locate the one with the longest duration.

 * Recall that each User is a triple: first name, last name, id.  In
 * the recorded conversation files, only the ids are recorded (acting
 * like primary keys in a relational database).  To locate the full
 * User given just an id, we thus supply a canned database, see class
 * Users.
 
 * The task is to complete the implementations for
 *
 * 1 Conversation.load
 *
 * 2 ConversationsReader.loadAll
 *
 * and finally
 *
 * 3 print out details of the longest conversation.  User names suffice
 *
 * @see Conversation.getDuration
 * @see Users
 */
public class Main {

	static public void main( String[] args ) {

		File dir = new File( "data" );

		try {
			Conversation c = ConversationsReader.loadAll( dir, Users.ALL );
			if( c != null ) {
				System.out.println( "User1: " + c.getUser1().getId() );
				System.out.println( "User2: " + c.getUser2().getId() );
				System.out.println( "User3: " + c.getDuration() );
			}
		} catch( IOException ioe ) {
			System.err.println( ioe );
		}
	}
}

// eof
