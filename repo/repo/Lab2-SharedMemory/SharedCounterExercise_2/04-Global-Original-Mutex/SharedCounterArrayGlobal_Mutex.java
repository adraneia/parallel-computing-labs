import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * SharedCounterArrayGlobal_Mutex.java
 *
 * STEP 2 solution for the ORIGINAL (static/global) version.
 * Same as 01-Global-Original, but array[i]++ is now protected with
 * a Lock -- see reference-code/SharedCounterMutex/GlobalVarsLock.java
 * for the pattern.
 *
 * Expected output: "0 errors." every single time.
 */
public class SharedCounterArrayGlobal_Mutex {

    static int end = 1000;
    static int[] array = new int[end];
    static int numThreads = 4;
    static Lock lock = new ReentrantLock();   // ONE lock protecting the whole array

    public static void main(String[] args) {

        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread();
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array();
    }

    static void check_array() {
        int i, errors = 0;
        System.out.println("Checking...");
        for (i = 0; i < end; i++) {
            if (array[i] != numThreads * i) {
                errors++;
                System.out.printf("%d: %d should be %d\n", i, array[i], numThreads * i);
            }
        }
        System.out.println(errors + " errors.");
    }

    static class CounterThread extends Thread {

        public CounterThread() {
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
