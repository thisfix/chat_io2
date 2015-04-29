package cp125.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author Stuart Maclean
 *
 */

public class ConversationsReader {

	/**
	 * @param dir - the directory containing the Conversation files
	 *
	 * @param users - a lookup table of Users by userID string
	 *
	 * @return The Conversation with the longest duration, from all
	 * the conversations found under dir
	 *
	 * @see Conversation.getDuration()
	 */
	static public Conversation loadAll( File dir, Map<String,User> users )
		throws IOException, IllegalArgumentException {

		/*
		  TO EXPAND:

		  check dir is a directory, if no then IllegalArgumentException

		  locate all .cnv files in the directory

		  load each one, using Conversation.load

		  keep a record of the longest Conversation, using
		  Conversation.getDuration
		*/

		// A bogus result used to test Main, NOT the real answer
		/*
		  return new Conversation( new User( "f1", "l1", "u1" ),
								 new User( "f2", "l2", "u2" ),
								 new Date(), new Date() );
		*/
		return null;
	}
}

// eof
 