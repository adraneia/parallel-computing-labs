import java.net.*;
import java.io.*;

public class ServerProtocol {

	public String processRequest(String theInput) {
		System.out.println("Received message from client: " + theInput);

		String[] parts = theInput.split(" ", 2);  
		//parts[0] = "3" 				το πρώτο "κομμάτι" (μέχρι το πρώτο κενό)
		//parts[1] = "5 hello world"  	ΟΛΟ το υπόλοιπο, μαζί, χωρίς να ξανακοπεί
		
		String option = parts[0];
		String rest = parts[1];
		String theOutput; 
		
		
		if (option.equals("1")) {
			theOutput = rest.toLowerCase();
		} else if (option.equals("2")){
			theOutput = rest.toUpperCase();
		} else if (option.equals("3") || option.equals("4")){
			String[] offsetAndMessage = rest.split(" ", 2);
			int offset = Integer.parseInt(offsetAndMessage[0]);
			String pureMessage = offsetAndMessage[1];

			if (option.equals("4")) offset = 26- offset; // DECODE = αντίστροφη μετατόπιση
			theOutput = caesar(pureMessage,offset);
		} else { 
			theOutput = "UNKNOWN COMMAND";
		}

		System.out.println("Send message to client: " + theOutput);
		return theOutput;


		// String theOutput = theInput;
		// System.out.println("Send message to client: " + theOutput);
		// return theOutput;

		
		
	}


	private String caesar(String message, int offset) {
		StringBuilder result = new StringBuilder();
		for (char character : message.toCharArray()) {
			if (character != ' ') {
				int pos = character - 'a';
				int newPos = ((pos + offset) % 26 + 26) % 26;
				result.append((char) ('a' + newPos));
			} else {
				result.append(character);
			}
		}
		return result.toString();
    }
}