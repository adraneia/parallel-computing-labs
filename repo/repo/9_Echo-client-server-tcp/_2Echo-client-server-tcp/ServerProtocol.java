import java.net.*;
import java.io.*;

public class ServerProtocol {

	public String processRequest(String theInput) {
		System.out.println("Received message from client: " + theInput);
		String theOutput = theInput;




		String[] praksi3 = theInput.split(" ",3);
		String numberOne = praksi3[0];
		String numberTwo = praksi3[2];
		String symbol = praksi3[1];

		double number1 = Double.parseDouble(numberOne);
		double number2 = Double.parseDouble(numberTwo);

		double result = 0; 

		if (symbol.equals("*")) {
			result = number1 * number2;
		}else if (symbol.equals("/")) {
			result = number1 / number2;
		} else if (symbol.equals("+")) {
			result = number1 + number2;
		} else if (symbol.equals("-")) {
			result = number1 - number2;
		} else {
			System.out.println("wrong input!!!!");}		

		String resultString = "" + result;
		System.out.println("Send message to client: " + resultString);
		return resultString;
		
	}
}