
   
public class MainArguments {

    public static void main(String[] args) {

        int numThreads = 10;  
        
		int n = 5;
	    int[] a = new int[numThreads];
	    for (int i = 0; i < numThreads; i++)
			a[i] = i; 
			
		
		CounterThread counterThreads[] = new CounterThread[numThreads];
		for (int i = 0; i < numThreads; i++) {
		    counterThreads[i] = new CounterThread(i, n, a);
		    counterThreads[i].start();
		}

        int sum = 0;
        for (int i = 0; i < numThreads; i++) {
            try {
				counterThreads[i].join();
				sum = sum + counterThreads[i].n;
            }
            catch (InterruptedException e) {}
		}
		System.out.println("sum = "+ sum);
		
		for (int i = 0; i < numThreads; i++)
			System.out.println("a["+i+"] = "+a[i]); 	
    }

}

class CounterThread extends Thread {
  	
    int threadID;
    int n;
    int[] a;
    
    public CounterThread(int tid, int n, int[] a) {
        this.threadID = tid;
        this.n = n;
        this.a = a;
    }
  	
    public void run() {
        
        n = n + threadID;
        a[threadID] = a[threadID] + n;
        System.out.println("Thread "+threadID+ " my n = "+n+ " my a = "+a[threadID]); 
    }
}
