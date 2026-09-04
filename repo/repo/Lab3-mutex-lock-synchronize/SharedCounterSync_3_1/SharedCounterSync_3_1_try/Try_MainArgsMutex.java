import java.util.concurrent.Semaphore;

public class Try_MainArgsMutex {

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
    Semaphore mutex = new Semaphore(1);   // (1) binary semaphore -- 1 = "ξεκλείδωτο"

    
    public SharedCounter (){
		this.n = 0;
    }
    
    public int get() {
        return n;
    }    
    
    public void inc() {
       try {
            mutex.acquire();      // (2) ζήτα το κλειδί -- P()
        } catch (InterruptedException e) {}

        try {
            n = n + 1;
        } finally {
            mutex.release();      // (3) άφησέ το -- V(), ΠΑΝΤΑ εκτελείται
        }
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
