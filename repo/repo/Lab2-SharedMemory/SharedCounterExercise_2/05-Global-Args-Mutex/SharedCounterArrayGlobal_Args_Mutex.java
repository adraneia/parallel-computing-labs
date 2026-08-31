import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * SharedCounterArrayGlobal_Args_Mutex.java
 *
 * STEP 2 solution for the ARGUMENTS (1.1) version.
 * Same as 02-Global-Args, but now also carries a Lock, passed into
 * each thread's constructor alongside end/numThreads/array,
 * protecting array[i]++.
 *
 * Expected output: "0 errors." every single time.
 */
public class SharedCounterArrayGlobal_Args_Mutex {

    public static void main(String[] args) {

        int end = 1000;
        int numThreads = 4;
        int[] array = new int[end];
        Lock lock = new ReentrantLock();

        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(end, numThreads, array, lock);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(end, numThreads, array);
    }

    static void check_array(int end, int numThreads, int[] array) {
        int errors = 0;
        System.out.println("Checking...");
        for (int i = 0; i < end; i++) {
            if (array[i] != numThreads * i) {
                errors++;
                System.out.printf("%d: %d should be %d\n", i, array[i], numThreads * i);
            }
        }
        System.out.println(errors + " errors.");
    }

    static class CounterThread extends Thread {

        int end, numThreads;
        int[] array;
        Lock lock;

        public CounterThread(int end, int numThreads, int[] array, Lock lock) {
            this.end = end;
            this.numThreads = numThreads;
            this.array = array;
            this.lock = lock;   // same shared Lock object in every thread
        }

        public void run() {
            for (int i = 0; i < end; i++) {
                for (int j = 0; j < i; j++) {
                    lock.lock();
                    try {
                        array[i]++;
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }
}
