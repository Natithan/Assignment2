package client;

import java.io.*;
import java.net.*;
import org.jsoup.*;

/* A class that implements a HTTP client, implementing
 * - GET
 * - HEAD
 * - PUT
 * - POST
 * methods
*/
public class MyHTTPClient {
	
	// Main function, initiated by command line or console MyHTTPClient
	public static void main(String argv[]) throws Exception
	{
	
		 // Create a connection with the user
	    BufferedReader inFromUser = new BufferedReader( new
		InputStreamReader(System.in));
	    
	    // Parse the user input
	    
	    String HTTPCommand;
	    String requestURI;
	    int Port;
	    
		// Get arguments from user.
	    // Command line arguments
		if ((argv != null)&& (argv.length != 0)){
			MyParse(argv);
		} else { //Otherwise, terminal input arguments
			String requestLineString = inFromUser.readLine();
			MyParse(requestLineString.split("\\s+"));// Split around whitespaces
			
		}
		
		
		// Get domain from uri
		URI uri = new URI(requestURI);
	    String domain = uri.getHost();
		
	 
	   
	    
		// Create a connection to the server
		Socket clientSocket = new Socket(domain, Port);
		DataOutputStream outToServer = new DataOutputStream(clientSocket .getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		
		// Pass the argument on to the server in correct format
		String request = HTTPCommand + " " + requestURI + " " + "HTTP/1.1 " + "\r\n" + "Host:" + " " + domain;
		outToServer.writeBytes(request + "\r\n\r\n");
		
		System.out.println("Out to server: " + "\r\n" + request);
		
		//Display the servers response
		String response = inFromServer.readLine();
		System.out.println("FROM SERVER: " + response);
		
		
		
		// Close the connection
		clientSocket.close();
		
		
		//Based on HTTP command, go to the correct functionality
		if (HTTPCommand == "GET") {
			myGet(HTTPCommand, requestURI, Port);
		}
		if (HTTPCommand == "HEAD") {
			myHead(HTTPCommand, requestURI, Port);
		}
		if (HTTPCommand == "PUT") {
			myPost(HTTPCommand, requestURI, Port);
		}
		if (HTTPCommand == "POST") {
			myPut(HTTPCommand, requestURI, Port);
		}
		
		// 
	
	}
	
	// Method that parses the input, and passes it on to the appropriate handler.
	// Throws errors if the input is incorrect, meaning not of 	
	// the form: command uri port
	private static void MyParse(String[] argv) throws IOException{
		if (argv == null || (argv.length != 3)|| argv[0] ||  ) {
			throw new IOException("Incorrect input");
		} else if ()
		HTTPCommand = argv[0];
		requestURI = argv[1]; 
		Port = Integer.parseInt(argv[2]);
		
	}

	private static void myPut(String hTTPCommand, String uRI, int port) {
		// TODO Auto-generated method stub
		
	}

	private static void myPost(String hTTPCommand, String uRI, int port) {
		// TODO Auto-generated method stub
		
	}

	private static void myHead(String hTTPCommand, String uRI, int port) {
		// TODO Auto-generated method stub
		
	}

	// Method that sends a GET-request, retrieves the entity identified by the Request-uri
	private static void myGet(String hTTPCommand, String uRI, int port) {
		// TODO Auto-generated method stub
		
	}
	

}
