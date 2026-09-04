public class SharedCounterArrayGlobal_sol_mutex {
    public static void main(String[] args) {
        
        
        int numThreads = 4;
        SharedData data = new SharedData(1000);   // (1) ένα αντικείμενο μαζεύει end + array int end = 1000;   int[] array = new int[end];
		CounterThread threads[] = new CounterThread[numThreads];
		
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread(numThreads, data);   // (2) περνάμε ΜΟΝΟ το data + numThreads
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
		check_array(numThreads, data); // ---------kai edw 

    }
     
    static void check_array(int numThreads, SharedData data) { //------kai edw
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < data.end; i++) { // kai edw 
			if (data.array[i] != numThreads*i) {
				errors++;
				System.out.printf("%d: %d should be %d\n", i, data.array[i], numThreads*i);
			}         
        }
        System.out.println (errors+" errors.");
    }


    static class CounterThread extends Thread {

        int numThreads;      // primitive -> by value, δεν χρειάζεται να είναι shared
        SharedData data;     // αντικείμενο -> by reference, shared
  	
        //olos o kataskevastis allazei 
       public CounterThread(int numThreads, SharedData data) {
            this.numThreads = numThreads;
            this.data = data;
        }
  	
       public void run() {
  
            for (int i = 0; i < data.end; i++) {
				for (int j = 0; j < i; j++)
					data.array[i]++;   // προσπελαύνουμε απευθείας το field, χωρίς μέθοδο	
            } 
		}            	
    }

   
}
 /* Η μοιραζόμενη δομή: μόνο δεδομένα, καμία μέθοδος (structure-style,
   όπως το struct στη C) — αντίθετα από την πλήρως OOP εκδοχή του
   Βήματος 2 που θα έχει ενσωματωμένη τη μέθοδο increment. */
    class SharedData {

        int end;
        int[] array;

        public SharedData(int end) {
            this.end = end;
            this.array = new int[end];
        }
    }