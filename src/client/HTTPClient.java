package client;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HTTPClient {
	
	/*
	 * 
	 */
	public static void main(String argv[]) throws Exception{
		if ((argv != null) &&(argv.length != 0)) {
			String command = argv[1];
			String uri = argv[2];
			String temp = argv[3];
			int port = Integer.parseInt(temp);
			parser(command, uri, port);
		}
		BufferedReader userInput = new BufferedReader( new InputStreamReader(System.in));
		while(true){
			// Input is read.
			String userSentence = userInput.readLine();
			System.out.println("usersentence: " + userSentence);
			// Input is handled.
			parser(userSentence);
		}
	}
	
	/*
	 * This function decides what to do depending on the given input.
	 */
	public  static String parser(String input) throws IOException{
		if (input == null)
			return "Null input!";
		
		else if (input.startsWith("HTTPClient ")){
			input = input.replace("HTTPClient ", ""); // Cut HTTPClient of the string so we can handle the command more easily.
						
			if("exit".equals(input))
				return EXIT();
			
			else {
				
				String[] splitted = input.split("\\s+"); // Split around spaces
				String command = splitted[0];
				String uri = splitted[1];
				int port = Integer.parseInt(splitted[2]);
				System.out.println(uri);
				System.out.println(port);
				
				
				if (command.startsWith("HEAD")){
					return HEAD(uri, port);
				}
			
				else if (command.startsWith("GET")){
					return GET(uri, port);
				}
				
				else if (command.startsWith("PUT")){
					return PUT(uri, port);
				}
			
				else if (command.startsWith("POST")){ 
					return POST(uri, port);
				}
			}
		}
		
		return "";
	}
	
	/*
	 * This function decides what to do depending on the given input.
	 */
	public  static String parser(String command, String uri, int port) throws IOException{
		if ((command == null) | (uri == null)){
			return "No input!";
		}
			
		else if (command.startsWith("HEAD")){
			return HEAD(uri, port);
		}
			
		else if (command.startsWith("GET")){
			return GET(uri, port);
		}
		
		else if (command.startsWith("PUT")){
			return PUT(uri, port);
		}
		
		else if (command.startsWith("POST")){
			return POST(uri, port);
		}
		
		return "";
	}
	
	/*
	*
	 * This function performs the HEAD command.
	 */
	public static String EXIT(){
		System.out.println("Shutting down client...");
		System.exit(0);
		return "";
		
	}
	
	/*
	 * This function performs the HEAD command.
	 */
	public static String HEAD(String uri, int port) throws IOException {
		
		
		String domain = uri.split("/",2)[0];
		String filename = uri.split("/",2)[1];
		System.out.println("domain: " + domain);
		System.out.println("filename: " + filename);
		Socket socket = new Socket(domain, port);
		
		
		// send the command to the server.
		System.out.println(socket.isConnected());
		DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String request = "HEAD " +"/"+ filename + " HTTP/1.1 "+"\r\n"+"Host: " + domain + "\r\n\r\n";
		System.out.println(request);
		outToServer.writeBytes(request);
		
		//create a file to write in.
		File file = new File(domain+".txt");
		// if file doesn't exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
		
		boolean isHeader=true;
		while(isHeader == true){
			String serverSentence = inFromServer.readLine();
			if (serverSentence == null) {
				isHeader = false;
//				break;
			}
			
			//write in the file
			else {
				FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(serverSentence+"\r\n");
				System.out.println(serverSentence);
				bw.close();
			}
		}
		
//		HTTPClient HEAD www.themountaingoats.net/contact.html 80
//		HTTPClient HEAD www.google.com/index.html 80
		
		return null;
	}
	
	/*
	 * This function performs the GET command.
	 */
	public static String GET(String uri, int port) throws IOException {
		
		String domain = uri.split("/",2)[0];
		String filename = uri.split("/",2)[1];
		System.out.println("domain: " + domain);
		System.out.println("filename: " + filename);
		Socket socket = new Socket(domain, port);
		
		
		// send the command to the server.
		System.out.println(socket.isConnected());
		DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String request = "GET " +"/"+ filename + " HTTP/1.1 "+"\r\n"+"Host: " + domain + "\r\n\r\n";
		System.out.println(request);
		outToServer.writeBytes(request);
		
		//create a file to write in.
		File file = new File(domain+".txt");
		// if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
		
		int characterCounter=100;
		while(characterCounter >= 0){
			String serverSentence = inFromServer.readLine();
			System.out.println(serverSentence);
			if (serverSentence.startsWith("Content-Length:")){
				characterCounter = Integer.parseInt(serverSentence.replace("Content-Length: ",""));
			}
			if ( !serverSentence.startsWith("Cache-Control: ") && !serverSentence.startsWith("Content-Type: ") && !serverSentence.startsWith("Date: ") && !serverSentence.startsWith("Etag: ")
					&& !serverSentence.startsWith("Expires: ") && !serverSentence.startsWith("Last-Modified: ") && !serverSentence.startsWith("Server: ") && !serverSentence.startsWith("Vary: ")
					&& !serverSentence.startsWith("X-Cache: ") && !serverSentence.startsWith("Content-Length: ") ){
				characterCounter = characterCounter - serverSentence.length()-1;
			}
			
			//write in the file
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(serverSentence+"\r\n");
	        bw.close();
		}
		

	    Document doc = Jsoup.parse(file, "UTF-8");
		Elements imgs = doc.getElementsByTag("img");
		
		System.out.println(imgs);
	    
	    
	    for (Element link : imgs) {
	    	String source = link.attr("src");
	    	
	    	source = source.replace("http://"+domain+"", "");
	    	
	    	System.out.println(source);
	    	
	    	//create a file to write in.
			File image = new File(source);
			// if file doesnt exists, then create it
	        if (!image.exists()) {
	        	image.createNewFile();
	        }
	        PrintWriter imageWriter = new PrintWriter(image);
	        imageWriter.print("");
	        imageWriter.close();
			
			characterCounter=100;
			while(characterCounter >= 0){
				String serverSentence = inFromServer.readLine();
				System.out.println(serverSentence);
				if (serverSentence.startsWith("Content-Length:")){
					characterCounter = Integer.parseInt(serverSentence.replace("Content-Length: ",""));
				}
				if ( !serverSentence.startsWith("Cache-Control: ") && !serverSentence.startsWith("Content-Type: ") && !serverSentence.startsWith("Date: ") && !serverSentence.startsWith("Etag: ")
						&& !serverSentence.startsWith("Expires: ") && !serverSentence.startsWith("Last-Modified: ") && !serverSentence.startsWith("Server: ") && !serverSentence.startsWith("Vary: ")
						&& !serverSentence.startsWith("X-Cache: ") && !serverSentence.startsWith("Content-Length: ") ){
					characterCounter = characterCounter - serverSentence.length()-1;
				}
				
				//write in the file
				FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
		        BufferedWriter bw = new BufferedWriter(fw);
		        bw.write(serverSentence+"\r\n");
		        bw.close();
			}
	    }
	    return null;
	}
	
	/*
	 * This function performs the PUT command.
	 */
	public static String PUT(String uri, int port){
		return null;
		
	}
	
	/*
	 * This function performs the POST command.
	 */
	public static String POST(String uri, int port){
		
		String domain = uri.split("/",2)[0];
		String filename = uri.split("/",2)[1];
		Socket socket;
		
		Map<String,String> arguments = new HashMap<>();
	    arguments.put("key1", "value");
	    arguments.put("key2", "value");
	    StringBuilder sj = new StringBuilder();
	    for(Map.Entry<String,String> entry : arguments.entrySet()) {
	        try {
	        	if (sj.length() == 0) {
					sj.append(URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));
	        	}
	        	else {
					sj.append("&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8"));

	        	}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
//	    byte[] out = sj.toString().getBytes();
		
		try {
			socket = new Socket(domain, port);
	
			// send the command to the server.
			DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String request = "POST " +"/"+ filename + " HTTP/1.1 "+"\r\n"+"Host: " + domain + "\r\n" 
					+ "Content-Type: application/x-www-form-urlencoded" + "\r\n" + "Content-Length: " + sj.length()
					+ "\r\n\r\n" + sj;
			System.out.println(request);
			outToServer.writeBytes(request);
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
		
	}
}
