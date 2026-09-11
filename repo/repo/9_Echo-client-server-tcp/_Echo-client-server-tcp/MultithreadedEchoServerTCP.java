import java.net.*;
import java.io.*;

public class MultithreadedEchoServerTCP {
	private static final int PORT = 1234;

	
	public static void main(String args[]) throws IOException {

		ServerSocket connectionSocket = new ServerSocket(PORT);
		
		while (true) {	

			System.out.println("Server is listening to port: " + PORT);
			Socket dataSocket = connectionSocket.accept(); //accept
			System.out.println("Received request from " + dataSocket.getInetAddress());

			ServerThread sthread = new ServerThread(dataSocket); //create
			sthread.start(); //start
		}
	}
}


// "Ποια τμήματα κώδικα υλοποιούν την ταυτόχρονη εξυπηρέτηση πολλών πελατών;" ????????????????????????????????
// "Το κύριο νήμα, μέσα στο accept-loop, μετά από κάθε accept() καλεί create/start Thread 
// για να αναθέσει τον νέο πελάτη σε ξεχωριστό νήμα — και επειδή το create/start δεν μπλοκάρει, 
// το main γυρνάει αμέσως στην κορυφή του βρόχου για το επόμενο accept()."
