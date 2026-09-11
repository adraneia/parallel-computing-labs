import java.io.*;
import java.net.*;

class ServerThread extends Thread
{
	private Socket myDataSocket;

	private InputStream is;
   	private BufferedReader in;
	private OutputStream os;
   	private PrintWriter out;
	private static final String EXIT = "CLOSE";

   	public ServerThread(Socket socket)
   	{
		myDataSocket = socket;
		
		try {
			is = myDataSocket.getInputStream();
			in = new BufferedReader(new InputStreamReader(is));
			os = myDataSocket.getOutputStream();
			out = new PrintWriter(os,true);

			ConnectionManager.addClient(out);  // [REGISTER] καταχωρούμε τον εαυτό μας στη λίστα
		}
		catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}

	public void run()
	{
   		String inmsg, outmsg;
		
		try {
			inmsg = in.readLine();
			ServerProtocol app = new ServerProtocol();
			outmsg = app.processRequest(inmsg);
			while(!outmsg.equals(EXIT)) {
				
				ConnectionManager.broadcast(outmsg);
				inmsg = in.readLine();
				outmsg = app.processRequest(inmsg);
			}	
			
			ConnectionManager.removeClient(out);  // [UNREGISTER]
			myDataSocket.close();
			System.out.println("Data socket closed");

		} catch (IOException e)	{		
	 		System.out.println("I/O Error " + e);
		}
	}	
}	
			
		
