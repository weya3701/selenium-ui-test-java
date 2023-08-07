package org.example;

public class Main {
    public static void main(String[] args) {
        TaskParser tp = new TaskParser(args[0]);
        TestJob tj = tp.getTestJob();
        new TaskRunner(tj);
    }
}
