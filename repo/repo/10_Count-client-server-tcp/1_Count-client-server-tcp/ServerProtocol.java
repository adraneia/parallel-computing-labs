import java.io.IOException;

public class ServerProtocol {

	 

    public String processRequest(String theInput) throws IOException{
        System.out.println("Received message from client: " + theInput);
		
		if (theInput.equals("-1")) {        // [EXIT CHECK] αναγνωρίζουμε την εντολή τερματισμού
        return "-1";
    	}

        long numSteps = Long.parseLong(theInput);


        // [CALC] ο γνωστός κώδικας NumInt
		double step = 1.0 / (double) numSteps;
		double sum = 0.0;
		for (long i = 0; i < numSteps; ++i) {
			double x = ((double) i + 0.5) * step;
			sum += 4.0 / (1.0 + x * x);
		}
		double pi = sum * step;


        String theOutput = "" + pi;   // [RETURN] το αποτέλεσμα ως String

		System.out.println("Send message to client: " + theOutput);

		return theOutput;
    }

    
}
