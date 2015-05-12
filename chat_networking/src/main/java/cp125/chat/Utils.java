package cp125.chat;

import java.io.*;
import java.net.Socket;

public class Utils {
	
	static BufferedReader getReader( Socket s ) throws IOException {
		InputStream is = s.getInputStream();
		return getReader( is );
	}

	static BufferedReader getReader( InputStream is ) throws IOException {
		InputStreamReader isr = new InputStreamReader( is );
		BufferedReader br = new BufferedReader( isr );
		return br;
	}

	static PrintWriter getWriter( Socket s ) throws IOException {
		OutputStream os = s.getOutputStream();
		return getWriter( os );
	}

	static PrintWriter getWriter( OutputStream os ) throws IOException {
		PrintWriter pw = new PrintWriter( os, true );
		return pw;
	}
}

// eof
