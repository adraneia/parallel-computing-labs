import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * SharedCounterArrayGlobalWhile_Object_Mutex.java
 *
 * STEP 2 solution for the OBJECT (1.2) "While" version.
 * Fully object-oriented: the shared counter, the array, and the Lock
 * all live inside one SharedData object. Threads only call
 * data.takeNext() and let the object handle its own locking
 * internally -- the check, the write, and the increment are all
 * inside ONE lock/unlock pair (same single-critical-section
 * requirement as the other "While" mutex versions).
 *
 * Expected output: "0 errors." every single time.
 */
public class SharedCounterArrayGlobalWhile_Object_Mutex {

    public static void main(String[] args) {

        int numThreads = 4;
        SharedData data = new SharedData(10000);

        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(data);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(data);
    }

    static void check_array(SharedData data) {
        int errors = 0;
        System.out.println("Checking...");
        for (int i = 0; i < data.end; i++) {
            if (data.array[i] != 1) {
                errors++;
                System.out.printf("%d: %d should be 1\n", i, data.array[i]);
            }
        }
        System.out.println(errors + " errors.");
    }

    static class SharedData {

        int end;
        int counter = 0;
        int[] array;
        Lock lock = new ReentrantLock();

        public SharedData(int end) {
            this.end = end;
            this.array = new int[end];
        }

        /*
         * Returns true and does the work if there is still work to
         * grab, false if we've run out of indices. The check, the
         * write, and the increment are all inside ONE lock/unlock
         * pair.
         */
        public boolean takeNext() {
            lock.lock();
            try {
                if (counter >= end) {
                    return false;
                }
                array[counter]++;
                counter++;
                return true;
            } finally {
                lock.unlock();
            }
        }
    }

    static class CounterThread extends Thread {

        SharedData data;

        public CounterThread(SharedData data) {
            this.data = data;
        }

        public void run() {
            while (data.takeNext()) {
                // work already done inside takeNext(); nothing else to do here
            }
        }
    }
}
