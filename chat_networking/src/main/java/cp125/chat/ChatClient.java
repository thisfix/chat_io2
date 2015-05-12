package cp125.chat;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.cli.*;

import static cp125.chat.Constants.*;

/**
 * @author Stuart Maclean
 *
 * The client part of a networked chat application.  There is a
 * central server to which clients connect.  So to get a chat working,
 * you need a server running, and then two instances of this client.
 *
 * By default, the server listens on port 52108 (tcp).  If it is
 * running on some different port, say 50000, start this program with
 * a -p option:
 *
 * java cp125.chat.ChatClient -p 50000
 *
 * If the server is running on a different machine to the client,
 * identify the server host with a -h option:
 *
 * java cp125.chat.ChatClient -h 192.168.1.10
 *
 * The client announces the name of its user to the server.  By
 * default, this is taken from System.getProperty( "user.name" ),
 * i.e. the user name of the person running the client.  To announce a
 * different name to the server, start this program with a -i option:
 *
 * java cp125.chat.ChatClient -i Jim
 *
 * Any combination of the -h, -i and -p options can be used.
 *
 * The client can connect to the server in two ways.  If you want to
 * wait for some other chat user to talk to you, start the client with
 * no other arguments. You will then get a chat session with the first
 * chat user that connects (currently, you have to talk to anyone). So
 * run like this (plus any options above):
 *
 * java cp125.chat.ChatClient 
 *
 * Rather than wait for someone else to initiate a chat session, you
 * can be the initiator, by supplying the name of the peer you want to
 * chat with, i.e.  you want to chat with 'James', supply that name as
 * the final argument to the program (plus any options above), i.e:
 *
 * java cp125.chat.ChatClient James
 *
 * Once the server has identified the two chat users (it first wants
 * to hear from the 'listening' user, THEN the 'connecting user), a
 * chat session is established.  If your chat client was the
 * connecting user, you have to speak FIRST.  What you say goes to the
 * chat peer, via the server.  If your chat client was the listening
 * user, you speak SECOND.
 *
 * There is VERY primitive error handling and notification in the
 * server.  If a client successfully joins a chat session, the server
 * notifies this via a "SUCCESS message as first line back to client.
 * If a client is unsuccessful, the server notifies this via a
 * "FAILURE" message and closes your socket connection to the server.
 *
 * @see ChatServer
 */

public class ChatClient {

	static public void main( String[] args ) {

		/*
		  By default, we expect the server to be running on the same
		  machine as the client.  Of course, NOT likely in general
		*/
		String DEFAULTHOST = "127.0.0.1";

		/*
		  By default, identify ourselves as logged-in user name
		*/
		String DEFAULTID = System.getProperty( "user.name" );
		
		String USAGE = ChatClient.class.getName() +
			" [-h SERVERHOST [-i USERID] [-p SERVERPORT] chatPeerID?";

		/*
		  commons-cli is a nice package for parsing a command line.
		  See the pom for its artifact details
		*/
		Options os = new Options();
		os.addOption( "h", true, "serverHost, defaults to " +
					  DEFAULTHOST );
		os.addOption( "i", true, "current chat user's id, defaults to " +
					  DEFAULTID );
		os.addOption( "p", true, "server's listening port, defaults to " +
					  DEFAULTPORT );

		CommandLineParser clp = new PosixParser();
		CommandLine cl = null;
		try {
			cl = clp.parse( os, args );
		} catch( Exception e ) {
			printUsage( USAGE, os );
			System.exit(-1);
		}

		String serverHost = DEFAULTHOST;
		if( cl.hasOption( "h" ) ) {
			serverHost = cl.getOptionValue( "h" );
		}

		String id = DEFAULTID;
		if( cl.hasOption( "i" ) ) {
			id = cl.getOptionValue( "i" );
		}
		
		int serverPort = DEFAULTPORT;
		if( cl.hasOption( "p" ) ) {
			try {
				serverPort = Integer.parseInt( cl.getOptionValue( "p" ) );
			} catch( NumberFormatException nfe ) {

			}
		}

		/*
		  Once all the options have been removed, we are left with
		  our own arguments to process
		*/
		args = cl.getArgs();

		// No chatPeer specified means we use a 'null' for this value
		String chatPeer = args.length > 0 ? args[0] : null;

		ChatClient main = new ChatClient( serverHost, serverPort,
										  id, chatPeer );
		main.start();
	}

	/**
	 * @param id - how we identify ourselves to the server.
	 *
	 * @param chatPeer - a chat user name (from Users.ALL) to chat with,
	 * or null if willing to listen for peers to chat to me
	 */
	public ChatClient( String serverHost, int serverPort, String id,
					   String chatPeer ) {
		this.serverHost = serverHost;
		this.serverPort = serverPort;
		this.id = id;
		this.chatPeer = chatPeer;
	}

	public void start() {
		try {
			Socket s = new Socket( serverHost, serverPort );
			System.out.println( "Connected to: " + s );
			BufferedReader br = Utils.getReader( s );
			PrintWriter pw = Utils.getWriter( s );

			// First line to server announces our identity
			pw.println( "ID " + id );

			/*
			  Second line to server says whether we want to listen
			  for someone to chat to us, or whether we actively want
			  to chat with some named peer

			  NB: The term 'listen' here is NOT the same as a
			  serverSocket listening on a port.  We are a client
			  program, making active SOCKET connections to a server.
			  We may listen in a CHAT sense though.
			*/
			if( chatPeer == null )
				pw.println( "LISTEN" );
			else
				pw.println( "CHAT " + chatPeer );

			/*
			  Next, read a response from the server.  It will
			  start with SUCCESS or FAILURE
			*/
			String line = br.readLine();
			System.out.println( "Server: " + line );
			if( line.startsWith( "FAILURE" ) )
				return;

			/*
			  OK, we got a successful chat going.
			  Get our chat content from keyboard+screen 
			*/
			BufferedReader brLocal = Utils.getReader( System.in );
			PrintWriter pwLocal = Utils.getWriter( System.out );

			line = null;
			/*
			  If we were the initiator of the chat, i.e. we NAMED some
			  peer we wanted to talk to, we speak first.  After that,
			  each user speaks in turn.  If we ever see a null, either
			  from socket or keyboard, we bail.
			*/
			if( chatPeer != null ) {
				line = brLocal.readLine();
				if( line == null )
					return;
				pw.println( line );
			}
			while( true ) {
				line = br.readLine();
				if( line == null )
					break;
				pwLocal.println( line );
				line = brLocal.readLine();
				if( line == null )
					break;
				pw.println( line );
			}
		} catch( IOException ioe ) {
			System.err.println( ioe );
		}
	}
	
	static void printUsage( String usage, Options os ) {
		HelpFormatter hf = new HelpFormatter();
		hf.setWidth( 80 );
		final String HEADER = "";
		final String FOOTER = "";
		hf.printHelp( usage, HEADER, os, FOOTER );
	}

	private final String serverHost, id, chatPeer;
	private final int serverPort;
}

// eof
