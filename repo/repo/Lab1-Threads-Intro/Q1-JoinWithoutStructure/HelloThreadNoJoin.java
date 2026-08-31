/*
 * HelloThreadNoJoin.java
 *
 * Companion demo for Question 1.
 * Same as HelloThread.java, but threads are created and started
 * WITHOUT being stored in any data structure (no array), and
 * therefore main CANNOT call join() on them.
 *
 * Run this and compare the output/timing with the original
 * HelloThread.java (which uses an array + join loop).
 *
 * Notice: main's "all done" message is no longer guaranteed to
 * print after every thread has finished -- there is no
 * synchronization point anymore. The threads WILL still finish
 * eventually (JVM stays alive until all non-daemon threads exit),
 * but we lost the ability to deterministically wait for them.
 */
public class HelloThreadNoJoin {

    public static void main(String[] args) {

        int numThreads = 20;

        System.out.println("In main: creating and starting threads (no array, no join)");

        for (int i = 0; i < numThreads; ++i) {
            System.out.println("In main: create and start thread " + i);
            new MyThread().start();   // no reference kept anywhere!
        }

        // There is nothing to join() here -- we have no references
        // to the threads we just created.

        System.out.println("In main: reached end of main (threads may still be running!)");
    }
}

class MyThread extends Thread {
    public void run() {
        System.out.println("Hello from thread " + Thread.currentThread().getName());
    }
}
