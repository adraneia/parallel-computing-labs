/*
 * TwoThreadClasses.java
 *
 * main() creates two threads, each belonging to a different class.
 * The two classes (GreeterThread, CounterThread) inherit from a
 * common abstract class (MyBaseThread) that extends Thread.
 * Each has its own constructor and its own run() (polymorphism).
 */
public class TwoThreadClasses {

    public static void main(String[] args) {

        System.out.println("In main: creating two threads of different classes");

        /* first thread: from class GreeterThread, with a String argument */
        MyBaseThread t1 = new GreeterThread("Nick");

        /* second thread: from class CounterThread, with an int argument */
        MyBaseThread t2 = new CounterThread(5);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.err.println("this should not happen");
        }

        System.out.println("In main: both threads finished");
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
