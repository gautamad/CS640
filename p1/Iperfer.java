import java.net.*;
import java.io.*;
import java.util.Timer;
	/* Main Class */

public class Iperfer {
    public static void main(String args[]) {
        IperferServer serverobj = new IperferServer();
	IperferClient clientobj = new IperferClient();
	String[] cmdline = new String[10];
	boolean server, client;
  	int argslength = args.length;
	if(argslength == 0) {
	    System.out.println("Error: missing additional arguments");
	    System.exit(0);
	}
  	client = args[0].equals("-c"); //Iperfer as Client
	server = args[0].equals("-s"); //Iperfer as Server
	cmdline[0] = args[0];
	if(client && argslength != 7) {
	    System.out.println("Error: missing additional arguments");
	    System.exit(0);
	}
	else if(server && argslength !=3) {
	    System.out.println("Error: missing additional arguments");
	    System.exit(0);
	}
	else if(!client && !server) {
	    System.out.println("Error: missing additional arguments");
	    System.exit(0);
	}
	if(client) {
	    // Client code here
	    int clientserverport = Integer.parseInt(args[4]);
	    double time = Integer.parseInt(args[6]);
	    try {
		clientobj.iperferClient(args[2],clientserverport,time);
	    }
	    catch (IOException e) { System.exit(1); }
	}
	else {
	    //Server code here
	    int serverport = Integer.parseInt(args[2]);
	    try {
		serverobj.iperferServer(serverport);
	    }
	    catch(IOException e) { System.exit(1); }
	}
    }
}

// Server Class

class IperferServer {
    public void iperferServer(int serverPort) throws IOException {
	ServerSocket serverSocket = null;
	long count = 0;
	try {
	    serverSocket = new ServerSocket(serverPort);
	}
	catch (IOException e) {
	    System.err.println("Could not listen on port :" +serverPort);
	}
	Socket clientSocket = null;
	System.out.println("Waiting for connection");
	try {
	    clientSocket = serverSocket.accept();
	}
	catch (IOException e) {
	    System.err.println("Cannot accept client");
	    System.exit(1);
	}
	System.out.println("Connection Successful");
	System.out.println("");
	BufferedReader in = new BufferedReader (
						new InputStreamReader( clientSocket.getInputStream())); 
	String inputLine;
	double startTime = System.nanoTime();
	while((inputLine = in.readLine()) != null) {
	    count++;
	   if (inputLine.equals("Bye."))
	       break;
	}
	double timeelapsed = System.nanoTime() - startTime;
	double bandwidth = (count*8/1000)/(timeelapsed/1000000000);
	System.out.println("Received data = "+count+" KB ," +"Bandwidth = "+ bandwidth+" Mbps");
	
	in.close(); 
	clientSocket.close(); 
	serverSocket.close(); 
    }
}

// Iperfer Client Class

class IperferClient {
    public void iperferClient(String serverHostname, int serverPort, double time) throws IOException {
	char[] data;
	data = new char[1000];
	int i;
	for(i=0;i<1000;i++) { data[i] = '0'; }
        System.out.println ("Attemping to connect to host " +
			    serverHostname + " on port"+ serverPort);
 		
        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;
	
        try {
	    echoSocket = new Socket(serverHostname, serverPort);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
							  echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + serverHostname);
            System.exit(1);
        }
	
	
	double startTime = System.nanoTime();
	double packetssent = 0;
        while((System.nanoTime()-startTime) < time*1000000000) {
	   packetssent++;
	   out.println(data);
	}
	//System.out.println("packets sent: "+packetssent);
	double bandwidth = (packetssent*8/1000)/time;
	System.out.println("Sent Data = "+packetssent+" KB ," +"Bandwidth = "+ bandwidth+" Mbps");
	in.close();	
	out.close();
	echoSocket.close();
    }
}
