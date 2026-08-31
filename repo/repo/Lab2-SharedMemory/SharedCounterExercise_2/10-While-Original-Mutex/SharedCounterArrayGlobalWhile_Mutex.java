import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * SharedCounterArrayGlobalWhile_Mutex.java
 *
 * STEP 2 solution for the ORIGINAL (static/global) "While" version.
 *
 * IMPORTANT: the check "counter >= end", the write "array[counter]++"
 * and the increment "counter++" must ALL be inside the SAME critical
 * section (same lock/unlock pair). If we locked them separately,
 * another thread could sneak in between and read/modify "counter" at
 * the wrong moment, and we'd be back to a race condition (e.g. two
 * threads reading the same counter value and writing to the same
 * array index, or the counter "skipping" a value so some array slot
 * is never touched).
 *
 * Expected output: "0 errors." every single time.
 */
public class SharedCounterArrayGlobalWhile_Mutex {

    static int end = 10000;
    static int counter = 0;
    static int[] array = new int[end];
    static int numThreads = 4;
    static Lock lock = new ReentrantLock();

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
            if (array[i] != 1) {
                errors++;
                System.out.printf("%d: %d should be 1\n", i, array[i]);
            }
        }
        System.out.println(errors + " errors.");
    }

    static class CounterThread extends Thread {

        public CounterThread() {
        }

        public void run() {
            while (true) {
                lock.lock();
                try {
                    if (counter >= end) {
                        break;              // ok to break INSIDE the try; finally still runs
                    }
                    array[counter]++;
                    counter++;
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
