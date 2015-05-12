package cp125.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.cli.*;

import static cp125.chat.Constants.*;

/**
 * @author Stuart Maclean
 *
 * The server component of our networked chat program.  The server
 * simply connects to chat client programs together.  All chat traffic
 * goes through the server.  Clients do NOT make socket connections to
 * each other.
 *
 * The first chat client connecting to the server must be the
 * 'listening' client, i.e. a chat user who is willing to start a chat
 * with some other user who is yet to connect.  Until a listener is
 * identified, client connections are politely refused by the server,
 * with a FAILURE message.  The id of the 'listening chat client' is
 * recorded, call it ID1.
 *
 * Once a chat listener has connected to us, we accept a connection
 * from a second chat client, but only if they want to chat with ID1.
 * If they want to talk to some other user, i.e. ID2, they are
 * politely refused with a FAILURE message.
 *
 * Once two clients have satisfied the rules above, they are permitted
 * to exchange data.  The client which connected second is the FIRST
 * to speak.  The server must funnel all chat data to and from the two
 * participants.
 */

public class ChatServer {

	static public void main( String[] args ) {
		
		String USAGE = ChatServer.class.getName() + " [-p PORT]";

		Options os = new Options();
		os.addOption( "p", true, "listening port" );

		CommandLineParser clp = new PosixParser();
		CommandLine cl = null;
		try {
			cl = clp.parse( os, args );
		} catch( Exception e ) {
			HelpFormatter hf = new HelpFormatter();
			hf.setWidth( 80 );
			final String HEADER = "";
			final String FOOTER = "";
			hf.printHelp( USAGE, HEADER, os, FOOTER );
			System.exit(1);
		}

		int port = DEFAULTPORT;
		if( cl.hasOption( "p" ) ) {
			try {
				port = Integer.parseInt( cl.getOptionValue( "p" ) );
			} catch( NumberFormatException nfe ) {

			}
		}

		try {
			ChatServer main = new ChatServer( port );
			main.start();
		} catch( IOException ioe ) {
			System.err.println( ioe );
		}
	}
	
	public ChatServer( int listeningPort )
		throws IOException {
		ss = new ServerSocket( listeningPort );
		System.out.println( "Listening on: " + ss );
	} 

	public void start() {
		try {
			while( true ) {
				getListener();
				getConnector();
				System.out.println( id1 + " <--> " + id2 );
				chat();
				cleanUp();
			}
		} catch( IOException ioe ) {
			System.err.println( ioe );
		}
	}

	/*
	  This method loops until a chat client connects saying it is
	  willing to wait for some other chat user to connect to it.  The
	  intro text a client sends once connected to this server are two
	  lines, either:

	  ID someName
	  LISTEN

	  or

	  ID someName 
	  CHAT someOtherName

	  The ID line identifies the chat user connecting.  The name in
	  the CHAT sentence identifies the peer this user wants to chat
	  with.

	  In getListener, we accept ONLY the first intro, i.e. ID, LISTEN.
	  We refuse ID, CHAT.
	*/
	private void getListener() throws IOException {
		
		s1 = ss.accept();

		br1 = Utils.getReader( s1 );
		
		String line1 = br1.readLine();
		String line2 = br1.readLine();
		
		if (line2.equals("LISTEN")){
			isListening = true;
			id1 = line1.substring(3);
			pw1 = Utils.getWriter( s1 );
		}
	}
	
	/*
	  This method loops until a chat client connects saying it wants
	  to chat to a named other user.  The intro text a client sends
	  once connected to this server are two lines, either:

	  ID someName
	  LISTEN

	  or

	  ID someName 
	  CHAT someOtherName

	  The ID line identifies the chat user connecting.  The name in
	  the CHAT sentence identifies the peer this user wants to chat
	  with.

	  In getConnector, we accept ONLY the second intro, i.e. ID, CHAT.
	  We refuse ID, LISTEN.  We also refuse ID, CHAT if the supplied
	  peer name does not match the listener identified by
	  getListener().
	*/
	private void getConnector() throws IOException {
		
		while (isListening){
			s2 = ss.accept();

			br2 = Utils.getReader( s2 );
			
			String line1 = br2.readLine();
			String line2 = br2.readLine();
		
			id2 = line1.substring(3);
			
			String testForChat = line2.substring(0, 4);
			String testForID1 = line2.substring(5);
			if (testForChat.equals("CHAT")){
				if(testForID1.equals(id1)){
					pw2 = Utils.getWriter( s2 );
					isListening = false;
				}
	
			}
			
		}
	}

	/*
	  At this point, we have our two connected chat clients.  Now
	  read lines of text from each one and forward it to the other.
	  The SECOND client to successfully connect, i.e. the one identified
	  in getConnector(), speaks first.
	*/
	private void chat() throws IOException {
		isChatting = true;
		String line1;
		String line2;
		pw2.println( "SUCCESS: Connected to " + id1 + ". You talk first" );
		pw1.println( "SUCCESS: Connected to " + id2 + ". You talk second" );
		while (isChatting) {
			line2 = br2.readLine();
			if (line2.equals("EXIT")){isChatting=false;}
			line2 = id2 +" : "+line2;
			System.out.println( line2);
			pw1.println(line2);
			line1 = br1.readLine();
			if (line1.equals("EXIT")){isChatting=false;}
			line1 = id1 +" : "+line2;
			System.out.println(line1);
			pw2.println(line1);
		}
	}

	private void cleanUp() throws IOException {
		br1.close();
		pw1.close();
		s1.close();
		br2.close();
		pw2.close();
		s2.close();
	}
	
	private final ServerSocket ss;
	
	private Boolean isListening = false;
	private Boolean isChatting = false;

	private Socket s1;
	private BufferedReader br1;
	private PrintWriter pw1;
	private String id1;

	private Socket s2;
	private BufferedReader br2;
	private PrintWriter pw2;
	private String id2;

}

// eof
