
public class MainArgsObj {

    public static void main(String[] args) {

        int numThreads = 10;  	
        SharedData sharedData = new SharedData(numThreads);
        
		CounterThread counterThreads[] = new CounterThread[numThreads];
		for (int i = 0; i < numThreads; i++) {
		    counterThreads[i] = new CounterThread(i, sharedData);
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
			System.out.println("a["+i+"] = "+sharedData.a[i]); 	
    }

}

class SharedData {

    int n;
    int[] a;
    
    public SharedData (int numThreads){
		this.n = 5;
		this.a = new int[numThreads];
		for (int i = 0; i < numThreads; i++)
		    a[i] = i;
    }
}

class CounterThread extends Thread {
  	
    int threadID;
    int n;
    SharedData data;
    
    public CounterThread(int tid, SharedData sharedData) {
        this.threadID = tid;
        this.data = sharedData;
        this.n = data.n;
    }
  	
    public void run() {
        n = n + threadID;
        data.a[threadID] = data.a[threadID] + n;
        System.out.println("Thread "+threadID+ " my n = "+n+ " my a = "+data.a[threadID]); 
    }
}
