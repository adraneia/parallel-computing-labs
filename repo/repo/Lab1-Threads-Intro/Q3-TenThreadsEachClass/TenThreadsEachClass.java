/*
 * TenThreadsEachClass.java
 *
 * Extension of Question 2: main() creates 10 threads from
 * GreeterThread and 10 threads from CounterThread (20 total).
 * Each thread array is used so that join() can be called on
 * every thread later (see Question 1 discussion).
 */
public class TenThreadsEachClass {

    public static void main(String[] args) {

        int numPerClass = 10;

        /* one array per class, so we keep a reference to every thread */
        MyBaseThread[] greeters = new MyBaseThread[numPerClass];
        MyBaseThread[] counters = new MyBaseThread[numPerClass];

        System.out.println("In main: creating " + numPerClass +
                            " GreeterThread and " + numPerClass +
                            " CounterThread objects");

        /* create and start 10 GreeterThread objects */
        for (int i = 0; i < numPerClass; ++i) {
            greeters[i] = new GreeterThread("Person" + i);
            greeters[i].start();
        }

        /* create and start 10 CounterThread objects */
        for (int i = 0; i < numPerClass; ++i) {
            counters[i] = new CounterThread(i + 1);   // counts up to i+1
            counters[i].start();
        }

        /* wait for all GreeterThread objects to finish */
        for (int i = 0; i < numPerClass; ++i) {
            try {
                greeters[i].join();
            } catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }

        /* wait for all CounterThread objects to finish */
        for (int i = 0; i < numPerClass; ++i) {
            try {
                counters[i].join();
            } catch (InterruptedException e) {
                System.err.println("this should not happen");
            }
        }

        System.out.println("In main: all " + (2 * numPerClass) + " threads finished");
    }
}

/* common abstract base class: shared by both kinds of threads */
abstract class MyBaseThread extends Thread {

    /* each subclass provides its own implementation (polymorphism) */
    public abstract void run();
}

/* first subclass: takes a name and greets */
class GreeterThread extends MyBaseThread {

    private String name;

    /* its own constructor, different from the other class */
    public GreeterThread(String name) {
        this.name = name;
    }

    /* its own run() */
    @Override
    public void run() {
        System.out.println("Hello " + name + " from thread " +
                            Thread.currentThread().getName());
    }
}

/* second subclass: takes a number and counts up to it */
class CounterThread extends MyBaseThread {

    private int upTo;

    /* different constructor: takes an int instead of a String */
    public CounterThread(int upTo) {
        this.upTo = upTo;
    }

    /* different run(): does entirely different work */
    @Override
    public void run() {
        for (int i = 1; i <= upTo; ++i) {
            System.out.println("Thread " + Thread.currentThread().getName() +
                                " counting: " + i);
        }
    }
}
