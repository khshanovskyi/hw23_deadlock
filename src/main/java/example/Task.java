package example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Task {

    private static final Lock lock1 = new ReentrantLock(true);
    private static final Lock lock2 = new ReentrantLock(true);
    private static String status = "EMPTY";

    public static void main(String[] args) {
        Thread t1 = new Thread(Task::actionOne, "T1");
        Thread t2 = new Thread(Task::actionTwo, "T2");
        checkState(t1);
        checkState(t2);
        System.out.println();
        t1.start();
        t2.start();

        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < 20) {
            checkState(t1);
            checkState(t2);
        }

        System.out.println();

    }

    private static void actionOne() {
        sleep(10);
        lock1.lock();
        changeStatus();
        lock2.lock();
        changeStatus();
        lock2.unlock();
        lock1.unlock();
    }

    private static void actionTwo() {
        sleep(10);
        lock2.lock();
        changeStatus();
        lock1.lock();
        changeStatus();
        lock1.unlock();
        lock2.unlock();
    }

    private static void checkState(Thread thread) {
        System.out.println(thread.getName() + ", state: " + thread.getState());
    }

    private static void changeStatus(){
        status = "status changed by " + Thread.currentThread().getName();
        System.out.println(status);
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
