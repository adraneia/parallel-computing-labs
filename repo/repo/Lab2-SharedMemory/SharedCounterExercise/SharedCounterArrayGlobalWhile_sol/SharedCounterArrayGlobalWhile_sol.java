public class SharedCounterArrayGlobalWhile_sol {
    
    public static void main(String[] args) {
        int end = 10000;
        int[] array = new int[end];
        int numThreads = 4;
        Counter counter = new Counter();   // (1) object wrapper -> shared by reference

        CounterThread threads[] = new CounterThread[numThreads];
	
		for (int i = 0; i < numThreads; i++) {
			threads[i] = new CounterThread(end, counter, array);   // (2) περνάμε το ΙΔΙΟ αντικείμενο σε όλα
			threads[i].start();
		}
	
		for (int i = 0; i < numThreads; i++) {
			try {
				threads[i].join();
			}
			catch (InterruptedException e) {}
		} 
        check_array (end, array);
    }
     
    static void check_array (int end,  int[] array)  {
		int i, errors = 0;

		System.out.println ("Checking...");

        for (i = 0; i < end; i++) {
			if (array[i] != 1) {
				errors++;
				System.out.printf("%d: %d should be 1\n", i, array[i]);
			}         
		}
        System.out.println (errors+" errors.");
    }

    /* wrapper -- ο μόνος τρόπος να μοιράσουμε έναν int που τροποποιείται */
    static class Counter {
        int value = 0;
    }

    static class CounterThread extends Thread {
        int end;
         Counter counter;   // (3) reference στο ΙΔΙΟ αντικείμενο σε όλα τα threads
        int[] array; 
  	
        public CounterThread(int end, Counter counter, int[] array) {
            this.end = end;
            this.counter = counter;
            this.array = array;
        }
  	
       public void run() {
            while (true) {
                if (counter.value >= end)
                    break;
                array[counter.value]++;
                counter.value++;
            }
		}            	
    }
}
