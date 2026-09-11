import java.net.*;
import java.io.*;

public class ClientProtocol {

	BufferedReader user = new BufferedReader(new InputStreamReader(System.in));
	
	public String prepareRequest() throws IOException {
	
		System.out.print("Enter PRAKSI to send to server (ex 5 * 4)" );
		String praksi = user.readLine();
		
		// String[] praksi3 = praksi.split(" ",3);
		// String numberOne = praksi3[0];
		// String numberTwo = praksi3[2];
		// String symbol = praksi3[1];

		// double number1 = Double.parseDouble(numberOne);
		// double number2 = Double.parseDouble(numberTwo);

		// double result = 0; 

		// if (symbol.equals("*")) {
		// 	result = number1 * number2;
		// }else if (symbol.equals("/")) {
		// 	result = number1 / number2;
		// } else if (symbol.equals("+")) {
		// 	result = number1 + number2;
		// } else if (symbol.equals("-")) {
		// 	result = number1 - number2;
		// } else {
		// 	System.out.println("wrong input!!!!");}		

		// String resultString = "" + result;
		return praksi;
	}


	public void processReply(String praksi) throws IOException {
	
		System.out.println("Message received from server: " + praksi);
	}
}
