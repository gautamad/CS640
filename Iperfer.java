
	/* Main Class */

public class Iperfer {
    public static void main(String args[]) {
        String[] cmdline = new String[10];
	boolean server, client;
  	int argslength = args.length;
        if(argslength == 0) {
	    System.out.println("Error: missing additional arguments");
	    System.exit(0);
	}
	System.out.println("largs[0] = "+ args[0]);
  	client = args[0].equals("-c"); //Iperfer as Client
	server = args[0].equals("-s"); //Iperfer as Server
	cmdline[0] = args[0];
	if(client) System.out.println("Success ");
	if(client && argslength != 7) {
	    System.out.println("Error: missing additional arguments");
	    ystem.exit(0);
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
	}
	else {
	//Server code here
	}
    }
}

// Server Class

public class IperferServer {
    public void server(int serverPort) throws IOException {
	ServerSocket serverSocket = null;
	try {
	    serverSocket = new ServerSocket(serverPort);
	}
	catch (IOException e) {
	    System.err.println("Could not listen on port :" +serverPort);
	}

	Socket clientSocket = null;
	System.out.println("Waiting for connection");
	


    }
}

