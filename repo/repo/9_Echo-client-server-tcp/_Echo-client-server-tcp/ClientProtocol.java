import java.net.*;
import java.io.*;

public class ClientProtocol {

	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	
	public String prepareRequest() throws IOException {
	
		System.out.print("Enter message to send to server:");
		String message = user.readLine();



		System.out.println("choose option: 1=lower 2 =upper 3=encode 4=decode");
		String option = user.readLine(); // 1 2 3 4

		if (option.equals("3") || option.equals("4")) {
			System.out.println("enter offset");
			String offset = user.readLine();
			return option + " " + offset + " " + message;
		}else {
			return option + " "+ message; 
		}
	}

		
	// return message;

	public void processReply(String theInput) throws IOException {
		System.out.println("Message received from server: " + theInput);
	}
}
