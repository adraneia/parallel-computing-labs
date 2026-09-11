import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ServerProtocol {

	 private static HashMap<Long, Double> cache = new HashMap<>();
	 private static ReentrantLock lock = new ReentrantLock();

    public String processRequest(String theInput) throws IOException{
        System.out.println("Received message from client: " + theInput);
		
		if (theInput.equals("-1")) {        // [EXIT CHECK] αναγνωρίζουμε την εντολή τερματισμού
        return "-1";
    	}

        long numSteps = Long.parseLong(theInput);
		Double pi;

		lock.lock();
		try{
			if (cache.containsKey(numSteps)){
				// cache hit 
				pi = cache.get(numSteps);
				System.out.println("foudn in cache! ");
			} else {
				// cache miss ipologizw mesa sto kleidwma
				// [CALC] ο γνωστός κώδικας NumInt
				double step = 1.0 / (double) numSteps;
				double sum = 0.0;
				for (long i = 0; i < numSteps; ++i) {
					double x = ((double) i + 0.5) * step;
					sum += 4.0 / (1.0 + x * x);
				}
				pi = sum * step;
				cache.put(numSteps, pi);
			}
		}finally {
			lock.unlock();	
		}

        String theOutput = "" + pi;   // [RETURN] το αποτέλεσμα ως String
		System.out.println("Send message to client: " + theOutput);

		return theOutput;
    }

    
}
