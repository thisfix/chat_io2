package cp125.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * A Conversation is between two Users and has a known duration (end
 * time minus start time).
 */

public class Conversation {

	public Conversation( User u1, User u2, Date startTime, Date endTime ) {
		this.u1 = u1;
		this.u2 = u2;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public User getUser1() {
		return u1;
	}

	public User getUser2() {
		return u2;
	}
	
	public long getDuration() {
		return endTime.getTime() - startTime.getTime();
	}
	
	public void save( File f ) throws IOException {
		FileWriter fw = new FileWriter( f );
		PrintWriter pw = new PrintWriter( fw );
		pw.println( u1.getId() + DELIMITER + u2.getId() + DELIMITER +
					startTime.getTime() + DELIMITER + endTime.getTime() );
		pw.close();
	}

	/**
	 * Load a Conversation from its saved representation in a text file
	 *
	 * @param f - the file supposedly containing the conversation details
	 * May not be well-formed: could be empty of not contain expected fields
	 *
	 * @param users - user table lookup, for deriving a user given
	 * just the user id as stored in the file
	 *
	 * @return the new Conversation
	 *
	 * @see save( java.io.File ) showing how the files were created
	 */
	public static Conversation load( File f, Map<String,User> users )
		throws IOException, IllegalArgumentException {

		// EXPAND
		return null;
	}
	
	private final User u1, u2;
	private final Date startTime, endTime;
	
	static private final String DELIMITER = ",";
}

// eof
