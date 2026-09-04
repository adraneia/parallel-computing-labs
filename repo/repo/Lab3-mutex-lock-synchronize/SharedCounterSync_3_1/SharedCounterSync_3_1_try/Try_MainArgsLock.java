import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;


public class Try_MainArgsLock {

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
    Lock lock = new ReentrantLock(); 

    public SharedCounter (){
		this.n = 0;
    }
    
    public int get() {
        
            lock.lock();
		try {
		    return n;
		} finally {    
		   lock.unlock();
		}   
       
           
        }
        
    
    public void inc() {
        lock.lock();
        try {
            n = n + 1;
        } finally {
            lock.unlock();
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
