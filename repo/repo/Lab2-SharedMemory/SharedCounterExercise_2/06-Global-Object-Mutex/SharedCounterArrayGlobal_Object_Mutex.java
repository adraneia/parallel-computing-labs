import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * SharedCounterArrayGlobal_Object_Mutex.java
 *
 * STEP 2 solution for the OBJECT (1.2) version.
 * Fully object-oriented: the shared array AND the Lock both live
 * inside a single SharedData object. The increment logic is a
 * method (incAt) on that object, so the locking is fully
 * encapsulated -- callers never see the Lock directly.
 *
 * Expected output: "0 errors." every single time.
 */
public class SharedCounterArrayGlobal_Object_Mutex {

    public static void main(String[] args) {

        int numThreads = 4;
        SharedData data = new SharedData(1000);

        CounterThread threads[] = new CounterThread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new CounterThread(numThreads, data);
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            }
            catch (InterruptedException e) {}
        }
        check_array(numThreads, data);
    }

    static void check_array(int numThreads, SharedData data) {
        int errors = 0;
        System.out.println("Checking...");
        for (int i = 0; i < data.end; i++) {
            if (data.get(i) != numThreads * i) {
                errors++;
                System.out.printf("%d: %d should be %d\n", i, data.get(i), numThreads * i);
            }
        }
        System.out.println(errors + " errors.");
    }

    /* encapsulates the array + its lock + the increment logic */
    static class SharedData {

        int end;
        int[] array;
        Lock lock = new ReentrantLock();

        public SharedData(int end) {
            this.end = end;
            this.array = new int[end];
        }

        public void incAt(int i) {
            lock.lock();
            try {
                array[i]++;
            } finally {
                lock.unlock();
            }
        }

        public int get(int i) {
            return array[i];   // read-only check, called after join() -> no race here
        }
    }

    static class CounterThread extends Thread {

        int numThreads;
        SharedData data;

        public CounterThread(int numThreads, SharedData data) {
            this.numThreads = numThreads;
            this.data = data;
        }

        public void run() {
            for (int i = 0; i < data.end; i++) {
                for (int j = 0; j < i; j++) {
                    data.incAt(i);
                }
            }
        }
    }
}
