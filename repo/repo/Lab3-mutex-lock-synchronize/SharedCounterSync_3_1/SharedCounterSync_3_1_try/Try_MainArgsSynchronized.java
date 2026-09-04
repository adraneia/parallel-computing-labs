

public class Try_MainArgsSynchronized {

    public static void main(String[] args) {

        int numThreads = 4;  

        SharedCounter counter = new SharedCounter();
        	
		CounterThread counterThreads[] = new CounterThread[numThreads];
		for (int i = 0; i < numThreads; i++) {
		    counterThreads[i] = new CounterThread(i, counter);
		    counterThreads[i].start();
		}
	
	    for (int i = 0; i < numThreads; i++) {
	        try {
				counterThreads[i].join();
	        }
	        catch (InterruptedException e) {}
		}
		 
        System.out.println("n = "+counter.get());
    }

}

class SharedCounter {

    int n;
    
    public SharedCounter (){
		this.n = 0;
    }
    
    public int get() {
        return n;
    }    

     /*
     * "synchronized" εδώ σημαίνει: πριν εκτελεστεί αυτή η μέθοδος,
     * το τρέχον thread πρέπει να πάρει το "κρυφό" ενσωματωμένο
     * κλείδωμα (intrinsic lock) του ΤΡΕΧΟΝΤΟΣ αντικειμένου (του
     * "this" -- δηλαδή του συγκεκριμένου SharedCounter instance).
     * Μόνο ένα thread τη φορά μπορεί να εκτελεί αυτή τη μέθοδο πάνω
     * στο ίδιο αντικείμενο. Το lock/unlock γίνεται ΑΥΤΟΜΑΤΑ από τη
     * JVM -- δεν χρειάζεται try/finally, ούτε ξεχωριστό Lock πεδίο.
     */
    
    public synchronized void inc() { 
        n = n + 1;
    } 
    
       
}

class CounterThread extends Thread {
  	
    int threadID;
    SharedCounter count;
    
    public CounterThread(int tid, SharedCounter counter) {
        this.threadID = tid;
        this.count = counter;
    }
  	
    public void run() {

        for (int i = 0; i < 10000; i++) {
           	    count.inc();
		}
		
		System.out.println("Thread "+threadID+ " n = "+count.get());
    }
}
