package com.homework;

import java.util.Scanner;
import java.util.concurrent.Semaphore;


public class Test {
    private static final int LICENSE_LIMIT = 5;
    private static int maxThreads;
    private static Semaphore semaphore;

    //Assume its a third party library
    static class OptimizationSolver {
        public void solve() throws InterruptedException {
            Thread.sleep(10000); //Do any stuff
        }
    }

    static class Task extends Thread {
        private String threadName;

        public Task( String name) {
            this.threadName = name;
        }

        public void run() {
            try {
                System.out.println(threadName + " is acquiring lock.");
                System.out.println("Available permits now: " + semaphore.availablePermits());
                semaphore.acquire();

                //Calling library function
                OptimizationSolver optimizationSolver = new OptimizationSolver();
                optimizationSolver.solve();

                System.out.println(threadName + " is performing operation " + " so available permits: " + semaphore.availablePermits());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                System.out.println(threadName + " is releasing lock.");
                semaphore.release();
                System.out.println("Available permits now: " + semaphore.availablePermits());
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  //Create a Scanner object to read license count from user
        System.out.println("Enter no of licenses: ");
        int licenseCount = scanner.nextInt();

        maxThreads = licenseCount * LICENSE_LIMIT; //Total no of threads i.e license count into license limit

        semaphore = new Semaphore(maxThreads); //Max parallel execution is equal to no of semaphores

        //Ten threads created but parallel execution depends on max threads
        for(int i=0; i<10; i++) {
            Task thread = new Task("Thread-" + i);
            thread.start();
        }
    }
}
